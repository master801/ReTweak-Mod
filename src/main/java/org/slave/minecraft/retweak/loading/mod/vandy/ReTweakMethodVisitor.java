package org.slave.minecraft.retweak.loading.mod.vandy;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.FieldEntryBuilder.FieldEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.MethodEntryBuilder.MethodEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

import java.util.Arrays;

/**
 * Created by Master on 6/3/2017 at 9:53 AM.
 *
 * @author Master
 */
public final class ReTweakMethodVisitor extends MethodVisitor {

    private final GameVersion gameVersion;
    private final ClassEntry classEntry;

    public ReTweakMethodVisitor(final int api, final GameVersion gameVersion, final ClassEntry classEntry, final MethodVisitor mv) {
        super(
            api,
            mv
        );
        this.gameVersion = gameVersion;
        this.classEntry = classEntry;
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

        //<editor-fold desc="Desc">
        ClassInfo descClassInfo = gameVersion.getClassInfo(typeDesc.getClassName());
        if (descClassInfo != null) {
            Class<?> overrideClass = descClassInfo.getClassEntry().getTo();
            if (overrideClass != null) newTypeDesc = Type.getType(overrideClass);
        }
        //</editor-fold>

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

        //<editor-fold desc="Local">
        if (local != null) {
            for(int i = 0; i < local.length; i++) {
                Object localItem = local[i];
                Object newLocalItem = null;

                if (localItem instanceof String) {
                    ClassInfo localClassInfo = gameVersion.getClassInfo((String)localItem);
                    if (localClassInfo != null) {
                        Class<?> overrideClass = localClassInfo.getClassEntry().getTo();
                        if (overrideClass != null) newLocalItem = Type.getInternalName(overrideClass);
                    }
                }

                if (newLocal == null) newLocal = new Object[local.length];
                if (newLocalItem != null) {
                    newLocal[i] = newLocalItem;
                } else {
                    newLocal[i] = localItem;
                }
            }
        }
        //</editor-fold>
        
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

        String newName = null;

        Type descType = Type.getType(desc);
        Type newDescType = null;

        //<editor-fold desc="Owner">
        ClassInfo ownerClassInfo = gameVersion.getClassInfo(owner);
        Class<?> overrideOwnerClass = null;
        if (ownerClassInfo != null) {
            overrideOwnerClass = ownerClassInfo.getClassEntry().getTo();
            if (overrideOwnerClass != null) newOwner = Type.getInternalName(overrideOwnerClass);
        }
        //</editor-fold>

        //<editor-fold desc="Name">
        if (ownerClassInfo != null) {
            FieldEntry fieldEntry = ownerClassInfo.getClassEntry().getField(name, desc);
            if (fieldEntry != null) {
                newName = fieldEntry.getDeobfuscatedName();
                if (fieldEntry.getToDescType() != null) newDescType = fieldEntry.getToDescType();
            }
        }
        //</editor-fold>

        //<editor-fold desc="Desc">
        if (newDescType == null) {
            ClassInfo descClassInfo = gameVersion.getClassInfo(descType.getClassName());
            if (descClassInfo != null) {
                Class<?> overrideDescClass = descClassInfo.getClassEntry().getTo();
                if (overrideDescClass != null) newDescType = Type.getType(overrideDescClass);
            }

            if (descType.getSort() == Type.ARRAY) {
                Type elementDescType = descType.getElementType();

                ClassInfo elementClassInfo = gameVersion.getClassInfo(elementDescType.getClassName());

                if (elementClassInfo != null) overrideOwnerClass = elementClassInfo.getClassEntry().getTo();
                Object[] dimensions = new Object[descType.getDimensions()];
                Arrays.fill(
                    dimensions,
                    '['
                );
                newDescType = Type.getType(
                    Joiner.on("").join(dimensions) + (overrideOwnerClass != null ? Type.getDescriptor(overrideOwnerClass) : elementDescType.getDescriptor())
                );
            }
        }
        //</editor-fold>

        super.visitFieldInsn(
            opcode,
            newOwner != null ? newOwner : owner,
            newName != null ? newName : name,
            newDescType != null ? newDescType.getDescriptor() : desc
        );
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        String newOwner = null;
        String newName = null;

        Type[] argumentDescTypes = Type.getArgumentTypes(desc);
        Type[] newArgumentDescTypes = null;

        //<editor-fold desc="Owner">
        ClassInfo ownerClassInfo = gameVersion.getClassInfo(owner);
        if (ownerClassInfo != null) {
            Class<?> overrideOwnerClass = ownerClassInfo.getClassEntry().getTo();
            if (overrideOwnerClass != null) newOwner = Type.getInternalName(overrideOwnerClass);
        }
        //</editor-fold>

        //<editor-fold desc="Name">
        if (ownerClassInfo != null) {
            MethodEntry methodEntry = ownerClassInfo.getClassEntry().getMethod(name, desc);
            if (methodEntry != null) newName = methodEntry.getDeobfuscatedName();
        }
        //</editor-fold>

        //<editor-fold desc="Desc">
        //<editor-fold desc="Args">
        for(int i = 0; i < argumentDescTypes.length; ++i) {
            Type argumentDescType = argumentDescTypes[i];
            Type newArgumentDescType = null;

            ClassInfo argumentClassInfo = gameVersion.getClassInfo(argumentDescType.getClassName());
            Class<?> argumentDescOverrideClass = null;
            if (argumentClassInfo != null) {
                argumentDescOverrideClass = argumentClassInfo.getClassEntry().getTo();
                if (argumentDescOverrideClass != null) newArgumentDescType = Type.getType(argumentDescOverrideClass);
            }

            //<editor-fold desc="Array">
            if (argumentDescType.getSort() == Type.ARRAY) {
                Type elementArgumentDescType = argumentDescType.getElementType();

                ClassInfo elementClassInfo = gameVersion.getClassInfo(elementArgumentDescType.getClassName());
                if (elementClassInfo != null) argumentDescOverrideClass = elementClassInfo.getClassEntry().getTo();

                Object[] dimensions = new Object[argumentDescType.getDimensions()];
                Arrays.fill(
                    dimensions,
                    '['
                );

                newArgumentDescType = Type.getType(
                    Joiner.on("").join(dimensions) + (argumentDescOverrideClass != null ? Type.getDescriptor(argumentDescOverrideClass) : elementArgumentDescType.getDescriptor())
                );
            }
            //</editor-fold>

            if (newArgumentDescTypes == null) newArgumentDescTypes = new Type[argumentDescTypes.length];
            newArgumentDescTypes[i] = newArgumentDescType != null ? newArgumentDescType : argumentDescType;
        }
        //</editor-fold>

        //<editor-fold desc="Return">
        Type returnDescType = Type.getReturnType(desc);
        Type newReturnDescType = null;

        ClassInfo returnClassInfo = gameVersion.getClassInfo(returnDescType.getClassName());
        Class<?> returnDescOverrideClass = null;
        if (returnClassInfo != null) {
            returnDescOverrideClass = returnClassInfo.getClassEntry().getTo();
            if (returnDescOverrideClass != null) newReturnDescType = Type.getType(returnDescOverrideClass);
        }

        //<editor-fold desc="Array">
        if (returnDescType.getSort() == Type.ARRAY) {
            Type elementReturnDescType = returnDescType.getElementType();

            ClassInfo elementClassInfo = gameVersion.getClassInfo(elementReturnDescType.getClassName());

            if (elementClassInfo != null) returnDescOverrideClass = elementClassInfo.getClassEntry().getTo();

            Object[] dimensions = new Object[returnDescType.getDimensions()];
            Arrays.fill(
                dimensions,
                '['
            );

            newReturnDescType = Type.getType(
                Joiner.on("").join(dimensions) + (returnDescOverrideClass != null ? Type.getDescriptor(returnDescOverrideClass) : elementReturnDescType.getDescriptor())
            );
        }
        //</editor-fold>
        //</editor-fold>

        if (newArgumentDescTypes == null) newArgumentDescTypes = argumentDescTypes;
        if (newReturnDescType == null) newReturnDescType = returnDescType;
        //</editor-fold>

        super.visitMethodInsn(
            opcode,
            newOwner != null ? newOwner : owner,
            newName != null ? newName : name,
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

        //<editor-fold desc="Desc">
        ClassInfo descClassInfo = gameVersion.getClassInfo(descType.getClassName());
        if (descClassInfo != null) {
            Class<?> descOverrideClass = descClassInfo.getClassEntry().getTo();
            if (descOverrideClass != null) newDescType = Type.getType(descOverrideClass);
        }
        //</editor-fold>

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

        ClassInfo typeClassInfo = gameVersion.getClassInfo(type);

        if (typeClassInfo != null) {
            Class<?> overrideClass = typeClassInfo.getClassEntry().getTo();
            if (overrideClass != null) newType = Type.getInternalName(overrideClass);
        }

        super.visitTypeInsn(
            opcode,
            newType != null ? newType : type
        );
    }

}
