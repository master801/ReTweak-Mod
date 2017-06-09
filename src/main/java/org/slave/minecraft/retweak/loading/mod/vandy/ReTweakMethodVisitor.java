package org.slave.minecraft.retweak.loading.mod.vandy;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

import java.util.Arrays;

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
        Type typeDesc = Type.getType(desc);
        Type newTypeDesc = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(typeDesc.getClassName());
        if (overrideClass != null) newTypeDesc = Type.getType(overrideClass);

        return new ReTweakAnnotationVisitor(
            super.api,
            gameVersion,
            super.visitAnnotation(
                newTypeDesc != null ? newTypeDesc.getDescriptor() : desc,
                visible
            )
        );
    }

    @Override
    public void visitFrame(final int type, final int nLocal, final Object[] local, final int nStack, final Object[] stack) {
        Object[] newLocal = null;

        if (local != null) {
            for(int i = 0; i < local.length; i++) {
                Object localItem = local[i];
                Object newLocalItem = null;

                if (localItem instanceof String) {
                    Class<?> overrideClass = gameVersion.getOverrideClass((String)localItem);
                    if (overrideClass != null) newLocalItem = Type.getInternalName(overrideClass);
                }

                if (newLocal == null) newLocal = new Object[local.length];
                if (newLocalItem != null) {
                    newLocal[i] = newLocalItem;
                } else {
                    newLocal[i] = localItem;
                }
            }
        }
        
        super.visitFrame(
            type,
            nLocal,
            newLocal != null ? newLocal : local,
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
            Type elementDescType = descType.getElementType();

            overrideOwnerClass = gameVersion.getOverrideClass(elementDescType.getClassName());
            Object[] dimensions = new Object[descType.getDimensions()];
            Arrays.fill(
                dimensions,
                '['
            );
            newDescType = Type.getType(
                Joiner.on("").join(dimensions) + (overrideOwnerClass != null ? Type.getDescriptor(overrideOwnerClass) : elementDescType.getDescriptor())
            );
        }

        Class<?> overrideDescClass = gameVersion.getOverrideClass(descType.getClassName());
        if (overrideDescClass != null) newDescType = Type.getType(overrideDescClass);

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

        Class<?> overrideOwnerClass = gameVersion.getOverrideClass(owner);
        if (overrideOwnerClass != null) newOwner = Type.getInternalName(overrideOwnerClass);

        Type[] argumentDescTypes = Type.getArgumentTypes(desc);
        Type[] newArgumentDescTypes = null;

        for(int i = 0; i < argumentDescTypes.length; ++i) {
            Type argumentDescType = argumentDescTypes[i];
            Type newArgumentDescType = null;

            Class<?> argumentDescOverrideClass = gameVersion.getOverrideClass(argumentDescType.getClassName());
            if (argumentDescOverrideClass != null) newArgumentDescType = Type.getType(argumentDescOverrideClass);

            if (argumentDescType.getSort() == Type.ARRAY) {
                Type elementArgumentDescType = argumentDescType.getElementType();

                argumentDescOverrideClass = gameVersion.getOverrideClass(elementArgumentDescType.getClassName());

                Object[] dimensions = new Object[argumentDescType.getDimensions()];
                Arrays.fill(
                    dimensions,
                    '['
                );

                newArgumentDescType = Type.getType(
                    Joiner.on("").join(dimensions) + (argumentDescOverrideClass != null ? Type.getDescriptor(argumentDescOverrideClass) : elementArgumentDescType.getDescriptor())
                );
            }

            if (newArgumentDescTypes == null) newArgumentDescTypes = new Type[argumentDescTypes.length];
            newArgumentDescTypes[i] = newArgumentDescType != null ? newArgumentDescType : argumentDescType;

        }

        Type returnDescType = Type.getReturnType(desc);
        Type newReturnDescType = null;

        Class<?> returnDescOverrideClass = gameVersion.getOverrideClass(returnDescType.getClassName());
        if (returnDescOverrideClass != null) newReturnDescType = Type.getType(returnDescOverrideClass);

        if (returnDescType.getSort() == Type.ARRAY) {
            Type elementReturnDescType = returnDescType.getElementType();

            returnDescOverrideClass = gameVersion.getOverrideClass(elementReturnDescType.getClassName());

            Object[] dimensions = new Object[returnDescType.getSort()];
            Arrays.fill(
                dimensions,
                '['
            );

            newReturnDescType = Type.getType(
                Joiner.on("").join(dimensions) + (returnDescOverrideClass != null ? Type.getDescriptor(returnDescOverrideClass) : elementReturnDescType.getDescriptor())
            );
        }

        if (newArgumentDescTypes == null) newArgumentDescTypes = argumentDescTypes;
        if (newReturnDescType == null) newReturnDescType = returnDescType;

        super.visitMethodInsn(
            opcode,
            newOwner != null ? newOwner : owner,
            name,
            Type.getMethodDescriptor(newReturnDescType, newArgumentDescTypes),
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
        Type descType = Type.getType(desc);
        Type newDescType = null;

        Class<?> descOverrideClass = gameVersion.getOverrideClass(descType.getClassName());
        if (descOverrideClass != null) newDescType = Type.getType(descOverrideClass);

        super.visitLocalVariable(
            name,
            newDescType != null ? newDescType.getDescriptor() : desc,
            signature,
            start,
            end,
            index
        );
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        String newType = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(type);

        if (overrideClass != null) newType = Type.getInternalName(overrideClass);

        super.visitTypeInsn(
            opcode,
            newType != null ? newType : type
        );
    }

}
