package org.slave.minecraft.retweak.load.asm;

import com.google.common.collect.Maps;

import net.minecraft.creativetab.CreativeTabs;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakLoader;
import org.slave.minecraft.retweak.load.asm._super.visitors.SuperClassVisitor;
import org.slave.minecraft.retweak.load.asm.tweak.visitors.TweakClassVisitor;
import org.slave.minecraft.retweak.load.mod.ReTweakModCandidate;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;

import cpw.mods.fml.common.discovery.ModCandidate;
import jdk.internal.org.objectweb.asm.Type;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Created by Master on 7/12/2018 at 12:14 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakClassASM {

    private static final EnumMap<GameVersion, ReTweakClassASM> INSTANCES = Maps.newEnumMap(GameVersion.class);

    private final GameVersion gameVersion;

    public byte[] tweak(final InputStream inputStream) {
        ClassReader classReader;
        try {
            classReader = new ClassReader(inputStream);
        } catch(IOException e) {
            ReTweak.LOGGER_RETWEAK.warn(
                    "Failed get the input stream of the class to tweak?!",
                    e
            );
            return null;
        }

        ClassNode classNode = new ClassNode();
        ASMTable asmTable = getASMTable(classReader.getClassName());

        //Read super
        SuperClassVisitor superClassVisitor = new SuperClassVisitor(Opcodes.ASM5, gameVersion, asmTable);
        classReader.accept(superClassVisitor, 0);

        //Tweak
        TweakClassVisitor tweakClassVisitor = new TweakClassVisitor(Opcodes.ASM5, classNode, gameVersion, asmTable);
        classReader.accept(tweakClassVisitor, 0);

        //wtf
        fixBadProgrammingGarbage(classNode);

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

                    //Fix bad creative tab
                    if (methodInsnNode.name.equals("setCreativeTab") && (methodInsnNode.desc.equals("(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/block/Block;") || methodInsnNode.desc.equals("(Lnet/minecraft/creativetab/CreativeTabs;)Lnet/minecraft/item/Item;"))) {
                        if (methodNode.instructions.get(i - 1).getOpcode() == Opcodes.ACONST_NULL) {
                            FieldInsnNode creativeTabReTweak = new FieldInsnNode(
                                    Opcodes.GETSTATIC,
                                    "org/slave/minecraft/retweak/resources/ReTweakResources",
                                    "CREATIVE_TAB_RETWEAK",
                                    Type.getType(CreativeTabs.class).getDescriptor()
                            );
                            injection.put(i - 1, creativeTabReTweak);
                        }
                    }
                }
            }

            for(Entry<Integer, AbstractInsnNode> entry : injection.entrySet()) {
                methodNode.instructions.set(
                        methodNode.instructions.get(entry.getKey()),
                        entry.getValue()
                );
            }
        }
    }

    public static ReTweakClassASM instance(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        if (!ReTweakClassASM.INSTANCES.containsKey(gameVersion)) {
            ReTweakClassASM.INSTANCES.put(
                    gameVersion,
                    new ReTweakClassASM(gameVersion)
            );
        }
        return ReTweakClassASM.INSTANCES.get(gameVersion);
    }

}
