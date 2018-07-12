package org.slave.minecraft.retweak.asm.transformers;

import cpw.mods.fml.common.LoadController;
import jdk.internal.org.objectweb.asm.Opcodes;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.asm.ReTweakASM;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper.Injection;

/**
 * Created by Master on 7/11/2018 at 5:26 AM.
 *
 * @author Master
 */
public final class TransformerLoadController extends BasicTransformer implements IClassTransformer {

    private static final String CLASS_NAME = LoadController.class.getName();

    public TransformerLoadController() {
        super(ReTweakASM.LOGGER_RETWEAK_ASM);
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        return true;
    }

    @Override
    protected ClassNode getClassNode() {
        return new ClassNode() {

            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                return ReTweakASMHelper.visitMethod(
                        super.api,
                        super.visitMethod(
                                access,
                                name,
                                desc,
                                signature,
                                exceptions
                        ),
                        _Injection.values(),
                        TransformerLoadController.this.getLogger(),

                        access,
                        name,
                        desc,
                        signature,
                        exceptions
                );
            }

        };
    }

    @Override
    protected String getClassName(final boolean b) {
        return TransformerLoadController.CLASS_NAME.replace('/', '.');
    }

    @RequiredArgsConstructor
    private enum _Injection implements Injection {

        DISTRIBUTE_STATE_MESSAGE(
                new String[] {
                        "distributeStateMessage"
                },
                new String[] {
                        "(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V"
                },
                MethodVisitorDistributeStateMessage.class
        ),

        TRANSITION(
                new String[] {
                        "transition"
                },
                new String[] {
                        "(Lcpw/mods/fml/common/LoaderState;Z)V"
                },
                MethodVisitorTransition.class
        ),

        DISTRIBUTE_STATE_EVENT(
                new String[] {
                        "distributeStateMessage"
                },
                new String[] {
                        "(Ljava/lang/Class;)V"
                },
                MethodVisitorDistributeStateEvent.class
        );

        @Getter
        private final String[] name;

        @Getter
        private final String[] desc;

        @Getter
        private final Class<? extends MethodVisitor> methodVisitorClass;

    }

    public static final class MethodVisitorDistributeStateMessage extends MethodVisitor {

        public MethodVisitorDistributeStateMessage(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            super.visitMethodInsn(
                    opcode,
                    owner,
                    name,
                    desc,
                    itf
            );

            if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("com/google/common/eventbus/EventBus") && name.equals("post") && desc.equals("(Ljava/lang/Object;)V")) {
                super.visitLabel(
                        new Label()
                );
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "org/slave/minecraft/retweak/load/ReTweakLoader",
                        "instance",
                        "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;",
                        false
                );
                super.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "org/slave/minecraft/retweak/load/ReTweakLoader",
                        "getReTweakLoadController",
                        "()Lorg/slave/minecraft/retweak/load/ReTweakController;",
                        false
                );
                super.visitVarInsn(
                        Opcodes.ALOAD,
                        1
                );
                super.visitVarInsn(
                        Opcodes.ALOAD,
                        2
                );
                super.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "org/slave/minecraft/retweak/ReTweakController",
                        "distributeStateMessage",
                        "(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V",
                        false
                );
            }
        }

    }

    public static final class MethodVisitorTransition extends MethodVisitor {

        public MethodVisitorTransition(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitFieldInsn(
                    opcode,
                    owner,
                    name,
                    desc
            );

            if (opcode == Opcodes.PUTFIELD && owner.equals("cpw/mods/fml/common/LoadController") && name.equals("state") && desc.equals("Lcpw/mods/fml/common/LoaderState;")) {
                super.visitLabel(
                        new Label()
                );
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "org/slave/minecraft/retweak/load/ReTweakLoader",
                        "instance",
                        "Lorg/slave/minecraft/retweak/load/ReTweakLoader;",
                        false
                );
                super.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "org/slave/minecraft/retweak/load/ReTweakLoader",
                        "getReTweakLoadController",
                        "()Lorg/slave/minecraft/retweak/load/ReTweakLoadController;",
                        false
                );
                super.visitVarInsn(
                        Opcodes.ALOAD,
                        1
                );
                super.visitVarInsn(
                        Opcodes.ALOAD,
                        2
                );
                super.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "org/slave/minecraft/retweak/load/ReTweakLoadController",
                        "transition",
                        "(Lcpw/mods/fml/common/LoaderState;Z)V",
                        false
                );
            }
        }

    }

    public static class MethodVisitorDistributeStateEvent extends MethodVisitor {

        public MethodVisitorDistributeStateEvent(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            super.visitMethodInsn(
                    opcode,
                    owner,
                    name,
                    desc,
                    itf
            );

            if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("com/google/common/eventbus/EventBus") && name.equals("post") && desc.equals("(Ljava/lang/Object;)V")) {
                super.visitLabel(
                        new Label()
                );
                super.visitMethodInsn(
                        Opcodes.INVOKESTATIC,
                        "org/slave/minecraft/retweak/load/ReTweakLoadController",
                        "instance",
                        "()Lorg/slave/minecraft/retweak/load/ReTweakLoadController;",
                        false
                );
                super.visitVarInsn(
                        Opcodes.ALOAD,
                        1
                );
                super.visitMethodInsn(
                        Opcodes.INVOKEVIRTUAL,
                        "org/slave/minecraft/retweak/load/ReTweakLoadController",
                        "distributeStateMessage",
                        "(Ljava/lang/Class;)V",
                        false
                );
            }
        }

    }

}
