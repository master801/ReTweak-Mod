package org.slave.minecraft.retweak.loading.tweaker;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.FreeTransformer;
import org.slave.minecraft.retweak.loading.SupportedGameVersion;

/**
 * Created by Master801 on 4/12/2016 at 3:41 PM.
 *
 * @author Master801
 */
public final class DeobfuscationTweaker extends FreeTransformer implements Tweak {

    private final SupportedGameVersion supportedGameVersion;

    public DeobfuscationTweaker(SupportedGameVersion supportedGameVersion) throws NullPointerException {
        super(null);
        if (supportedGameVersion == null) throw new NullPointerException();
        this.supportedGameVersion = supportedGameVersion;
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        return true;
    }

    @Override
    protected boolean enableLogging() {
        return false;
    }

    @Override
    protected ClassVisitor getClassVisitor() {
        return new ClassVisitor(Opcodes.ASM5) {

            @Override
            public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces);
            }

            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM5, super.visitMethod(
                        access,
                        name,
                        desc,
                        signature,
                        exceptions
                )) {

                    @Override
                    public void visitInsn(final int opcode) {
                        super.visitInsn(opcode);
                    }

                    @Override
                    public void visitIntInsn(final int opcode, final int operand) {
                        super.visitIntInsn(opcode, operand);
                    }

                    @Override
                    public void visitVarInsn(final int opcode, final int var) {
                        super.visitVarInsn(opcode, var);
                    }

                    @Override
                    public void visitTypeInsn(final int opcode, final String type) {
                        super.visitTypeInsn(opcode, type);
                    }

                    @Override
                    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                        super.visitFieldInsn(opcode, owner, name, desc);
                    }

                    @Override
                    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                        super.visitMethodInsn(opcode, owner, name, desc, itf);
                    }

                };
            }

        };
    }

}
