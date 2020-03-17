package org.slave.minecraft.retweak.load.mapping.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 3/17/2020 at 9:56 AM
 *
 * @author Master
 */
public final class SrgMethodVisitor extends MethodVisitor {

    private final GameVersion gameVersion;

    public SrgMethodVisitor(final int api, final MethodVisitor mv, final GameVersion gameVersion) {
        super(api, mv);
        this.gameVersion = gameVersion;
    }

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

    @Override
    public void visitLdcInsn(final Object cst) {
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

}
