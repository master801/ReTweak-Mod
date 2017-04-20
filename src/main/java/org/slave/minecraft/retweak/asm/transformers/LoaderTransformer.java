package org.slave.minecraft.retweak.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 3/19/2016 at 1:49 PM.
 *
 * @author Master801
 */
public final class LoaderTransformer extends BasicTransformer implements IClassTransformer {

    public LoaderTransformer() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected boolean transformClass(ClassNode classNode) throws Exception {
        MethodNode loadModsMethodNode = null;

        for(MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("loadMods") && methodNode.desc.equals("()V")) loadModsMethodNode = methodNode;

            if (loadModsMethodNode != null) break;
        }

        if (loadModsMethodNode != null) {
            for(int i = 0; i < loadModsMethodNode.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = loadModsMethodNode.instructions.get(i);

                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;

                    //Injects our code after:
                    //    discoverer = identifyMods();
                    if (fieldInsnNode.getOpcode() == Opcodes.PUTFIELD && fieldInsnNode.owner.equals("cpw/mods/fml/common/Loader") && fieldInsnNode.name.equals("discoverer") && fieldInsnNode.desc.equals("Lcpw/mods/fml/common/discovery/ModDiscoverer;")) {
                        InsnList injectionInstructions = new InsnList();
                        injectionInstructions.add(
                                new LabelNode(new Label())
                        );
                        injectionInstructions.add(
                                new FieldInsnNode(
                                        Opcodes.GETSTATIC,
                                        "org/slave/minecraft/retweak/loading/mod/ReTweakLoader",
                                        "INSTANCE",
                                        "Lorg/slave/minecraft/retweak/loading/mod/ReTweakLoader;"
                                )
                        );
                        injectionInstructions.add(
                                new MethodInsnNode(
                                        Opcodes.INVOKEVIRTUAL,
                                        "org/slave/minecraft/retweak/loading/mod/ReTweakLoader",
                                        "loadMods",
                                        "()V",
                                        false
                                )
                        );
                        loadModsMethodNode.instructions.insert(fieldInsnNode, injectionInstructions);
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected String getClassName(boolean isNameTransformed) {
        return "cpw.mods.fml.common.Loader";
    }

    @Override
    protected boolean writeClassFile() {
        return ReTweakResources.DEBUG;
    }

    @Override
    protected boolean writeASMFile() {
        return ReTweakResources.DEBUG;
    }

}
