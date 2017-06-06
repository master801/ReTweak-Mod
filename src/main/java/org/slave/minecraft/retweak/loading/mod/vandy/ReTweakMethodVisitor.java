package org.slave.minecraft.retweak.loading.mod.vandy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

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
        String newOwner = null;
        Type descType = Type.getType(desc);
        Type newDescType = null;

        Class<?> overrideOwnerClass = gameVersion.getOverrideClass(owner);
        if (overrideOwnerClass != null) newOwner = Type.getInternalName(overrideOwnerClass);

        if (descType.getSort() == Type.ARRAY) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                "Could not transform desc of field insn \"{} {} {}\"! Arrays not yet supported!",
                owner,
                name,
                desc
            );
        }

        Class<?> overrideDescClass = gameVersion.getOverrideClass(descType.getClassName());
        if (overrideDescClass != null) {
            newDescType = Type.getType(
                Type.getInternalName(overrideDescClass)
            );
        }

        super.visitFieldInsn(
            opcode,
            newOwner != null ? newOwner : owner,
            name,
            newDescType != null ? newDescType.getDescriptor() : desc
        );
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        String newOwner = null;
        Type descType = Type.getType(desc);
        Type newDescType = null;

        Class<?> overrideOwnerClass = gameVersion.getOverrideClass(owner);
        if (overrideOwnerClass != null) newOwner = Type.getInternalName(overrideOwnerClass);

        if (descType.getSort() == Type.ARRAY) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                "Could not transform desc of method insn \"{} {}{}\"! Arrays not yet supported!",
                owner,
                name,
                desc
            );
        }

        Class<?> overrideDescClass = gameVersion.getOverrideClass(desc);
        if (overrideDescClass != null) {
            newDescType = Type.getType(
                descType.getClassName()
            );
        }

        super.visitMethodInsn(
            opcode,
            newOwner != null ? newOwner : owner,
            name,
            newDescType != null ? newDescType.getDescriptor() : desc,
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
