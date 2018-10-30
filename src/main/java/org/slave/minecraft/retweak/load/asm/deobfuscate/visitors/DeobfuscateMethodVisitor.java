package org.slave.minecraft.retweak.load.asm.deobfuscate.visitors;

import org.objectweb.asm.MethodVisitor;

/**
 * Created by master on 10/27/18 at 3:03 PM
 *
 * @author master
 */
public final class DeobfuscateMethodVisitor extends MethodVisitor {

    public DeobfuscateMethodVisitor(final int api, final MethodVisitor mv) {
        super(api, mv);
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
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        super.visitFrame(type, nLocal, local, nStack, stack);
    }

}
