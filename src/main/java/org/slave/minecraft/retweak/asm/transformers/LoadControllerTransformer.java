package org.slave.minecraft.retweak.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
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
        MethodNode distributeStateMessage = null;

        for(MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("transition") && methodNode.desc.equals("(Lcpw/mods/fml/common/LoaderState;Z)V")) transition = methodNode;
            if (methodNode.name.equals("distributeStateMessage") && methodNode.desc.equals("(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V")) distributeStateMessage = methodNode;

            if (transition != null && distributeStateMessage != null) break;
        }

        if (transition != null) {
            AbstractInsnNode injectionNode = null;
            for(int i = 0; i < transition.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = transition.instructions.get(i);
                if (abstractInsnNode instanceof InsnNode) {
                    InsnNode insnNode = (InsnNode)abstractInsnNode;
                    if (insnNode.getOpcode() == Opcodes.RETURN) {
                        injectionNode = transition.instructions.get(i - 3);
                        break;
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
                        "org/slave/minecraft/retweak/loading/ReTweakStateHandler",
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
        if (distributeStateMessage != null) {
            AbstractInsnNode injectionNode = null;
            for(int i = 0; i < distributeStateMessage.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = distributeStateMessage.instructions.get(i);

                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && methodInsnNode.owner.equals("com/google/common/eventbus/EventBus") && methodInsnNode.name.equals("post") && methodInsnNode.desc.equals("(Ljava/lang/Object;)V")) {
                        injectionNode = abstractInsnNode;
                        break;
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
                        1
                ));
                instructionsToInject.add(new VarInsnNode(
                        Opcodes.ALOAD,
                        2
                ));
                instructionsToInject.add(new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        "cpw/mods/fml/common/LoaderState",
                        "getEvent",
                        "([Ljava/lang/Object;)Lcpw/mods/fml/common/event/FMLStateEvent;",
                        false
                ));
                instructionsToInject.add(new MethodInsnNode(
                        Opcodes.INVOKESTATIC,
                        "org/slave/minecraft/retweak/loading/ReTweakStateHandler",
                        "sendStateEvent",
                        "(Lcpw/mods/fml/common/LoadController;Lcpw/mods/fml/common/event/FMLStateEvent;)V",
                        false
                ));

                distributeStateMessage.instructions.insert(
                        injectionNode,
                        instructionsToInject
                );
            }
        }
        return transition != null && distributeStateMessage != null;
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "cpw.mods.fml.common.LoadController";
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
