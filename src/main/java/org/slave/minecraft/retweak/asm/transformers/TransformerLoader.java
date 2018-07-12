package org.slave.minecraft.retweak.asm.transformers;

import cpw.mods.fml.common.Loader;
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
 * Created by master on 2/25/18 at 9:33 PM
 *
 * @author master
 */
public final class TransformerLoader extends BasicTransformer implements IClassTransformer  {

    private static final String CLASS_NAME = Loader.class.getName();

    public TransformerLoader() {
        super(ReTweakASM.LOGGER_RETWEAK_ASM);
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        return true;
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return TransformerLoader.CLASS_NAME;
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
                        TransformerLoader.this.getLogger(),

                        access,
                        name,
                        desc,
                        signature,
                        exceptions
                );
            }

        };
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
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            super.visitMethodInsn(
                    opcode,
                    owner,
                    name,
                    desc,
                    itf
            );

            if (opcode == Opcodes.INVOKESPECIAL && owner.equals("cpw/mods/fml/common/Loader") && name.equals("initializeLoader") && desc.equals("()V")) {
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
                        "initialize",
                        "()V",
                        false
                );
            } else if (opcode == Opcodes.INVOKESPECIAL && owner.equals("cpw/mods/fml/common/Loader") && name.equals("identifyMods") && desc.equals("()V")) {
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
                        "identifyMods",
                        "()V",
                        false
                );
            }
        }

    }

}
