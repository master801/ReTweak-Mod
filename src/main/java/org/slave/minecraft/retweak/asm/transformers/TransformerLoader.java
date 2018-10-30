package org.slave.minecraft.retweak.asm.transformers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.asm.ReTweakASM;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper.Injection;
import org.slave.minecraft.retweak.hooks.ReTweakHook;

/**
 * Created by master on 2/25/18 at 9:33 PM
 *
 * @author master
 */
public final class TransformerLoader extends BasicTransformer implements IClassTransformer  {

    public TransformerLoader() {
        super(ReTweakASM.LOGGER_RETWEAK_ASM);
    }

    @Override
    protected void transform(final ClassNode classNode) {
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "cpw.mods.fml.common.Loader";
    }

    @Override
    protected ClassVisitor getClassVisitor(final ClassNode classNode) {
        return new ClassVisitor(Opcodes.ASM5, classNode) {

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
                        ReTweakASM.LOGGER_RETWEAK_ASM,

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
    protected boolean writeClassFile() {
        return true;
    }

    @Override
    protected boolean writeASMFile() {
        return true;
    }

    @RequiredArgsConstructor
    private enum _Injection implements Injection {

        LOAD_MODS(
                new String[] {
                        "loadMods"
                },
                new String[] {
                        "()V"
                },
                MethodVisitorLoadMods.class
        ),

        LOADING_COMPLETE(
                new String[] {
                        "loadingComplete"
                },
                new String[] {
                        "()V"
                },
                MethodVisitorLoadingComplete.class
        );

        @Getter
        private final String[] name;

        @Getter
        private final String[] desc;

        @Getter
        private final Class<? extends MethodVisitor> methodVisitorClass;

    }

    public static final class MethodVisitorLoadMods extends MethodVisitor {

        public MethodVisitorLoadMods(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitFieldInsn(opcode, owner, name, desc);

            if (owner.equals("cpw/mods/fml/common/Loader")) {
                switch (opcode) {
                    case Opcodes.PUTFIELD:
                        if (name.equals("discoverer") && desc.equals("Lcpw/mods/fml/common/discovery/ModDiscoverer;")) {
                            super.visitLabel(new Label());
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "identifyMods", "()V", false);
                        }
                        break;
                }
            }
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);

            if (owner.equals("cpw/mods/fml/common/Loader")) {
                switch (opcode) {
                    case Opcodes.INVOKESPECIAL:
                        if (name.equals("initializeLoader") && desc.equals("()V")) {
                            super.visitLabel(new Label());
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "initialize", "()V", false);
                            break;
                        }
                        break;
                }
            }
        }

    }

    public static class MethodVisitorLoadingComplete extends MethodVisitor {

        public MethodVisitorLoadingComplete(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitFieldInsn(opcode, owner, name, desc);

            if (opcode == Opcodes.PUTFIELD && owner.equals("cpw/mods/fml/common/Loader") && name.equals("progressBar") && desc.equals("Lcpw/mods/fml/common/ProgressManager$ProgressBar;")) {
                super.visitLabel(new Label());
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/hooks/ReTweakHook", "onLoadingComplete", "()V", false);
            }
        }

        private static final class x {

            public void xx() {
                ReTweakHook.onLoadingComplete();
            }

        }

    }

}
