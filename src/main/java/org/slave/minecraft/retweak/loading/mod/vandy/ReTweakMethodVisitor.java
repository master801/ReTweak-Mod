package org.slave.minecraft.retweak.loading.mod.vandy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 6/3/2017 at 9:53 AM.
 *
 * @author Master
 */
public final class ReTweakMethodVisitor extends MethodVisitor {

    private final GameVersion gameVersion;

    public ReTweakMethodVisitor(final int api, final GameVersion gameVersion, final MethodVisitor mv) {
        super(api, mv);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visitParameter(final String name, final int access) {
        super.visitParameter(
            name,
            access
        );
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return new ReTweakAnnotationVisitor(
            super.api,
            gameVersion,
            super.visitAnnotation(
                desc,
                visible
            )
        );
    }

    @Override
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        super.visitFrame(
            type,
            nLocal,
            local,
            nStack,
            stack
        );
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        super.visitFieldInsn(
            opcode,
            owner,
            name,
            desc
        );
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
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        super.visitLdcInsn(
            cst
        );
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        super.visitLocalVariable(
            name,
            desc,
            signature,
            start,
            end,
            index
        );
    }

}
