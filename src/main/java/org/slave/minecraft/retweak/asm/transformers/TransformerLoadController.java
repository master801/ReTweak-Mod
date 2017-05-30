package org.slave.minecraft.retweak.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slave.lib.asm.transformers.MethodsTransformer;
import org.slave.lib.resources.MethodStub;
import org.slave.minecraft.retweak.util.ReTweakResources;

/**
 * Created by Master801 on 4/10/2016 at 9:30 PM.
 *
 * @author Master801
 */
public final class TransformerLoadController extends MethodsTransformer implements IClassTransformer {

    public TransformerLoadController() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected void transformMethod(final MethodStub methodStub, final MethodNode methodNode, final boolean isObfuscated) throws Exception {
        switch(methodStub.getName()) {
            case "transition":
                transformTransition(
                    methodNode,
                    isObfuscated
                );
                break;
            case "distributeStateMessage":
                transformDistributeMessage(
                    methodNode,
                    isObfuscated
                );
                break;
        }
    }

    private void transformTransition(final MethodNode methodNode, final boolean isObfuscated) {
        AbstractInsnNode injectionNode = null;
        for(int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
            if (abstractInsnNode instanceof InsnNode) {
                InsnNode insnNode = (InsnNode)abstractInsnNode;
                if (insnNode.getOpcode() == Opcodes.RETURN) {
                    injectionNode = methodNode.instructions.get(i - 3);
                    break;
                }
            }
        }

        if (injectionNode != null) {
            InsnList instructionsToInject = new InsnList();
            instructionsToInject.add(
                new LabelNode(
                    new Label()
                )
            );
            instructionsToInject.add(
                new VarInsnNode(
                    Opcodes.ALOAD,
                    0
                )
            );
            instructionsToInject.add(
                new VarInsnNode(
                    Opcodes.ALOAD,
                    0
                )
            );
            instructionsToInject.add(
                new FieldInsnNode(
                    Opcodes.GETFIELD,
                    "cpw/mods/fml/common/LoadController",
                    "state",
                    "Lcpw/mods/fml/common/LoaderState;"
                )
            );
            instructionsToInject.add(
                new VarInsnNode(
                    Opcodes.ALOAD,
                    1
                )
            );

            instructionsToInject.add(
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "org/slave/minecraft/retweak/loading/mod/ReTweakStateHandler",
                    "step",
                    "(Lcpw/mods/fml/common/LoadController;Lcpw/mods/fml/common/LoaderState;Lcpw/mods/fml/common/LoaderState;)V",
                    false
                )
            );

            methodNode.instructions.insert(
                injectionNode,
                instructionsToInject
            );
        }
    }

    private void transformDistributeMessage(final MethodNode methodNode, final boolean isObfuscated) {
        AbstractInsnNode injectionNode = null;
        for(int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
            if (abstractInsnNode instanceof VarInsnNode) {
                VarInsnNode varInsnNode = (VarInsnNode)abstractInsnNode;
                if (varInsnNode.getOpcode() == Opcodes.ALOAD && varInsnNode.var == 0) {
                    //LABEL <-- What we want
                    //LINE NUMBER
                    //ALOAD, 0 <-- What we're checking for
                    injectionNode = varInsnNode.getPrevious().getPrevious();
                    break;
                }
            }
        }

        if (injectionNode != null) {
            InsnList instructionsToInject = new InsnList();
            instructionsToInject.add(
                new LabelNode(new Label())
            );
            instructionsToInject.add(
                new VarInsnNode(
                    Opcodes.ALOAD,
                    1
                )
            );
            instructionsToInject.add(
                new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "org/slave/minecraft/retweak/loading/mod/ReTweakLoadHandler",
                    "distributeStateMessage",
                    "(Ljava/lang/Class;)V",
                    false
                )
            );

            methodNode.instructions.insertBefore(
                injectionNode,
                instructionsToInject
            );
        }
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "cpw.mods.fml.common.LoadController";
    }

    @Override
    protected MethodStub[] getMethods() {
        return new MethodStub[] {
            new MethodStub(
                "transition",
                "(Lcpw/mods/fml/common/LoaderState;Z)V"
            ),
            new MethodStub(
                "distributeStateMessage",
                "(Ljava/lang/Class;)V"
            )
        };
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
