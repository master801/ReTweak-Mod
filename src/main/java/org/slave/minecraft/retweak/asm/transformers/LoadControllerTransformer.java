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
import org.objectweb.asm.tree.VarInsnNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 4/10/2016 at 9:30 PM.
 *
 * @author Master801
 */
public final class LoadControllerTransformer extends BasicTransformer implements IClassTransformer {

    public LoadControllerTransformer() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        MethodNode transition = null;

        for(MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("transition") && methodNode.desc.equals("(Lcpw/mods/fml/common/LoaderState;Z)V")) transition = methodNode;
        }

        if (transition != null) {
            AbstractInsnNode injectionNode = null;
            for(int i = 0; i < transition.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = transition.instructions.get(i);

                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                    if (fieldInsnNode.getOpcode() == Opcodes.PUTFIELD &&
                            fieldInsnNode.owner.equals("cpw/mods/fml/common/LoadController") &&
                            fieldInsnNode.name.equals("state") &&
                            fieldInsnNode.desc.equals("Lcpw/mods/fml/common/LoaderState;")) {
                        injectionNode = abstractInsnNode;
                    }
                }
            }

            if (injectionNode != null) {
                InsnList instructionsToInject = new InsnList();
                instructionsToInject.add(new LabelNode(new Label()));
                instructionsToInject.add(new VarInsnNode(
                        Opcodes.ALOAD,
                        0
                ));
                instructionsToInject.add(new VarInsnNode(
                        Opcodes.ALOAD,
                        0
                ));
                instructionsToInject.add(new FieldInsnNode(
                        Opcodes.GETFIELD,
                        "cpw/mods/fml/common/LoadController",
                        "state",
                        "Lcpw/mods/fml/common/LoaderState;"
                ));
                instructionsToInject.add(new VarInsnNode(
                        Opcodes.ALOAD,
                        1
                ));

                instructionsToInject.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "org/slave/minecraft/retweak/loading/ReTweakStep",
                        "step",
                        "(Lcpw/mods/fml/common/LoadController;Lcpw/mods/fml/common/LoaderState;Lcpw/mods/fml/common/LoaderState;)V",
                        false
                ));

                transition.instructions.insert(
                        injectionNode,
                        instructionsToInject
                );
            }
        }
        return true;
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "cpw.mods.fml.common.LoadController";
    }

    @Override
    protected boolean writeClassFile() {
        return true;
    }

    @Override
    protected boolean writeASMFile() {
        return true;
    }

}
