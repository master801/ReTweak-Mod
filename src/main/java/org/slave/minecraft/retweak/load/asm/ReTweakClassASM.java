package org.slave.minecraft.retweak.load.asm;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.discovery.ModCandidate;
import jdk.internal.org.objectweb.asm.Type;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slave.lib.helpers.ASMHelper;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakLoader;
import org.slave.minecraft.retweak.load.asm.tweak.visitors.TweakClassVisitor;
import org.slave.minecraft.retweak.load.mapping.asm.SrgClassVisitor;
import org.slave.minecraft.retweak.load.mod.ReTweakModCandidate;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Master on 7/12/2018 at 12:14 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class  ReTweakClassASM {

    private static final EnumMap<GameVersion, ReTweakClassASM> INSTANCES = Maps.newEnumMap(GameVersion.class);

    private final GameVersion gameVersion;

    public byte[] tweak(final InputStream inputStream) {
        ClassReader classReader;
        try {
            classReader = new ClassReader(inputStream);
        } catch(IOException e) {
            ReTweak.LOGGER_RETWEAK.warn("Failed get the input stream of the class to tweak?!", e);
            return null;
        }

        ClassNode classNode = new ClassNode();
        ASMTable asmTable = getASMTable(classReader.getClassName());

        //Tweak
        TweakClassVisitor tweakClassVisitor = new TweakClassVisitor(Opcodes.ASM5, classNode, gameVersion);

        //Deobfuscation
        SrgClassVisitor srgClassVisitor = new SrgClassVisitor(Opcodes.ASM5, tweakClassVisitor, gameVersion);

        classReader.accept(srgClassVisitor, 0);//srg is loaded first, then tweak

        //wtf
        fixBadProgrammingGarbage(classNode);

        backendTweak(classNode);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public ASMTable getASMTable(final String className) {
        ASMTable asmTable = null;
        for(ModCandidate modCandidate : ReTweakLoader.instance().getReTweakModDiscoverer(gameVersion).getCandidates()) {
            ReTweakModCandidate reTweakModCandidate = (ReTweakModCandidate)modCandidate;
            for(TableClass iteratingTableClass : reTweakModCandidate.getASMTable().getTableClasses()) {
                if (iteratingTableClass.getName().equals(className)) {
                    asmTable = reTweakModCandidate.getASMTable();
                    break;
                }
            }
        }
        return asmTable;
    }

    private void fixBadProgrammingGarbage(final ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            Map<Integer, AbstractInsnNode> injection = Maps.newHashMap();

            for(int i = 0; i < methodNode.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;

                    //Fixes bad creative tab
                    if (methodInsnNode.name.equals("setCreativeTab") && (methodInsnNode.desc.equals("(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/block/Block;") || methodInsnNode.desc.equals("(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/item/Item;"))) {
                        if (methodNode.instructions.get(i - 1).getOpcode() == Opcodes.ACONST_NULL) {
                            injection.put(
                                    i - 1,
                                    new FieldInsnNode(Opcodes.GETSTATIC, "org/slave/minecraft/retweak/resources/ReTweakResources", "CREATIVE_TAB_RETWEAK", Type.getType(CreativeTabs.class).getDescriptor())
                            );
                        }
                    }
                }
            }

            for(Entry<Integer, AbstractInsnNode> entry : injection.entrySet()) {
                methodNode.instructions.set(methodNode.instructions.get(entry.getKey()), entry.getValue());
            }
        }
    }

    private void backendTweak(final ClassNode classNode) {
        for(int i = 0; i < classNode.methods.size(); ++i) {
            MethodNode methodNode = classNode.methods.get(i);

            List<MethodInsnNode> listAddSmeltingHook = Lists.newArrayList(), listConstructorItemStackIII = Lists.newArrayList();
            for(int j = 0; j < methodNode.instructions.size(); ++j) {
                AbstractInsnNode abstractInsnNode = methodNode.instructions.get(j);
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKESTATIC && methodInsnNode.owner.equals("cpw/mods/fml/common/registry/GameRegistry") && methodInsnNode.name.equals("addSmelting") && methodInsnNode.desc.equals("(ILnet/minecraft/item/ItemStack;F)V")) {
                        listAddSmeltingHook.add(methodInsnNode);
                    }
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKESPECIAL && methodInsnNode.owner.equals("net/minecraft/item/ItemStack") && methodInsnNode.name.equals("<init>") && methodInsnNode.desc.equals("(III)V")) {
                        listConstructorItemStackIII.add(methodInsnNode);
                    }
                }
            }

            if (!listAddSmeltingHook.isEmpty()) {
                for(MethodInsnNode addSmeltingHook : listAddSmeltingHook) {
                    FieldInsnNode itemID = null;
                    AbstractInsnNode iterating = addSmeltingHook.getPrevious();
                    while((iterating = iterating.getPrevious()) != null) {
                        if (iterating instanceof FieldInsnNode) {
                            FieldInsnNode fieldInsnNode = (FieldInsnNode)iterating;
                            if (fieldInsnNode.getOpcode() == Opcodes.GETFIELD && fieldInsnNode.name.equals("itemID") && fieldInsnNode.desc.equals("I")) {
                                itemID = fieldInsnNode;
                                break;
                            }
                        }
                    }

                    if (itemID != null) {
                        methodNode.instructions.remove(itemID);
                        addSmeltingHook.owner = "org/slave/minecraft/retweak/hooks/ReTweakHook";
                        addSmeltingHook.desc = "(Ljava/lang/Object;Lnet/minecraft/item/ItemStack;F)V";
                        continue;
                    }
                }
            }
            if (!listConstructorItemStackIII.isEmpty()) {
                for(MethodInsnNode constructorItemStackIII : listConstructorItemStackIII) {
                    AbstractInsnNode remove = null;
                    AbstractInsnNode prev = constructorItemStackIII.getPrevious();
                    while(prev != null) {
                        if (prev instanceof TypeInsnNode) {
                            TypeInsnNode typeInsnNode = (TypeInsnNode)prev;
                            if (typeInsnNode.getOpcode() == Opcodes.NEW && typeInsnNode.desc.equals("net/minecraft/item/ItemStack") && typeInsnNode.getNext().getOpcode() == Opcodes.DUP) {
                                remove = typeInsnNode;
                                break;
                            }
                        }
                        prev = prev.getPrevious();
                    }
                    if (remove != null) {
                        methodNode.instructions.set(constructorItemStackIII, new MethodInsnNode(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/hooks/ReTweakHook", "createItemStack", "(III)Lnet/minecraft/item/ItemStack;", false));
                        methodNode.instructions.remove(remove.getNext());//Remove DUP
                        methodNode.instructions.remove(remove);//remove NEW
                    }
                }
            }
        }

        MethodNode methodNodeInitI = ASMHelper.findMethodNode(classNode.methods, new String[] {"<init>"}, new String[] {"(I)V"});
        MethodNode methodNodeInitBlock = ASMHelper.findMethodNode(classNode.methods, new String[] {"<init>"}, new String[] {"(Lnet/minecraft/block/Block;)V"});
        if (methodNodeInitI != null && methodNodeInitBlock == null) {
            MethodInsnNode init = null;
            for(int i = 0; i < methodNodeInitI.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = methodNodeInitI.instructions.get(i);
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                    if (methodInsnNode.name.equals("<init>") && methodInsnNode.desc.equals("(I)V")) {
                        init = methodInsnNode;
                        break;
                    }
                }
            }

            if (init != null) {
                LocalVariableNode localVariableNode = methodNodeInitI.localVariables.get(1);
                if (localVariableNode != null && localVariableNode.desc.equals("I")) {
                    methodNodeInitI.desc = "(Lnet/minecraft/block/Block;)V";
                    init.desc = "(Lnet/minecraft/block/Block;)V";
                    localVariableNode.desc = Type.getDescriptor(Block.class);

                    VarInsnNode varInsnNode = (VarInsnNode)init.getPrevious();
                    if (varInsnNode.getOpcode() == Opcodes.ILOAD && varInsnNode.var == 1) {
                        varInsnNode.setOpcode(Opcodes.ALOAD);
                    }
                }
            }
        }
    }

    public static ReTweakClassASM instance(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        if (!ReTweakClassASM.INSTANCES.containsKey(gameVersion)) ReTweakClassASM.INSTANCES.put(gameVersion, new ReTweakClassASM(gameVersion));
        return ReTweakClassASM.INSTANCES.get(gameVersion);
    }

}
