package org.slave.minecraft.retweak.load.asm._super.visitors;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.load.mapping._super.SuperMap;

/**
 * Created by master on 11/18/18 at 11:02 AM
 *
 * @author master
 */
public final class SuperMethodVisitor extends MethodVisitor {

    private final SuperMap superMap;

    public SuperMethodVisitor(final int api, final MethodVisitor mv, final SuperMap superMap) {
        super(api, mv);
        this.superMap = superMap;
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
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

}
