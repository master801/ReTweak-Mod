package org.slave.minecraft.retweak.loading.mod.vandy;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.FieldEntryBuilder.FieldEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.MethodEntryBuilder.MethodEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

import java.util.Arrays;

/**
 * Created by Master on 6/3/2017 at 9:42 AM.
 *
 * @author Master
 */
public final class ReTweakClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

    private ClassEntry classEntry;

    public ReTweakClassVisitor(final int api, final GameVersion gameVersion, final ClassVisitor cv) {
        super(
            api,
            cv
        );
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        String newSuperName = null;
        String[] newInterfaces;

        //<editor-fold name="Name">
        ClassInfo classInfo = gameVersion.getClassInfo(name);
        if (classInfo != null) classEntry = classInfo.getClassEntry();
        //</editor-fold>

        //<editor-fold desc="Super">
        ClassInfo superNameClassInfo = gameVersion.getClassInfo(superName);

        if (superNameClassInfo != null) {
            Class<?> overrideSuperNameClass = superNameClassInfo.getClassEntry().getTo();
            if (overrideSuperNameClass != null) newSuperName = Type.getInternalName(overrideSuperNameClass);
        }
        //</editor-fold>

        //<editor-fold desc="Interfaces">
        newInterfaces = new String[interfaces.length];
        for(int i = 0; i < newInterfaces.length; i++) {
            String _interface = interfaces[i];
            String newInterface = null;

            ClassInfo interfaceClassInfo = gameVersion.getClassInfo(_interface);

            if (interfaceClassInfo != null) {
                Class<?> overrideInterfaceClass = interfaceClassInfo.getClassEntry().getTo();
                if (overrideInterfaceClass != null) newInterface = Type.getInternalName(overrideInterfaceClass);
            }

            if (newInterface == null) newInterface = _interface;

            newInterfaces[i] = newInterface;
        }
        //</editor-fold>

        super.visit(
            version,
            access,
            name,
            signature,
            newSuperName != null ? newSuperName : superName,
            newInterfaces
        );
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        super.visitInnerClass(
            name,
            outerName,
            innerName,
            access
        );
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        Type descType = Type.getType(desc);
        Type newDescType = null;

        //<editor-fold desc="Desc">
        ClassInfo descClassInfo = gameVersion.getClassInfo(descType.getClassName());

        if (descClassInfo != null) {
            Class<?> overrideClass = descClassInfo.getClassEntry().getTo();
            if (overrideClass != null) newDescType = Type.getType(overrideClass);
        }
        //</editor-fold>

        return new ReTweakAnnotationVisitor(
            super.api,
            gameVersion,
            super.visitAnnotation(
                newDescType != null ? newDescType.getDescriptor() : desc,
                visible
            )
        );
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        String newName = null;

        Type descType = Type.getType(desc);
        Type newDescType = null;

        //<editor-fold desc="Name">
        if (classEntry != null) {
            FieldEntry fieldEntry = classEntry.getField(name, desc);
            if (fieldEntry != null) newName = fieldEntry.getDeobfuscatedName();
        }
        //</editor-fold>

        //<editor-fold desc="Desc">
        ClassInfo descClassInfo = gameVersion.getClassInfo(descType.getClassName());
        Class<?> descOverrideClass = descClassInfo != null ? descClassInfo.getClassEntry().getTo() : null;
        if (descOverrideClass != null) {
            newDescType = Type.getType(descOverrideClass);
        }

        //<editor-fold desc="Array">
        if (descType.getSort() == Type.ARRAY) {
            Type elementDescType = descType.getElementType();

            ClassInfo elementClassInfo = gameVersion.getClassInfo(elementDescType.getClassName());
            if (elementClassInfo != null) descOverrideClass = elementClassInfo.getClassEntry().getTo();

            Object[] dimensions = new Object[descType.getDimensions()];
            Arrays.fill(
                dimensions,
                '['
            );

            newDescType = Type.getType(
                Joiner.on("").join(dimensions) + (descOverrideClass != null ? Type.getDescriptor(descOverrideClass) : elementDescType.getDescriptor())
            );
        }
        //</editor-fold>
        //</editor-fold>

        return new ReTweakFieldVisitor(
            super.api,
            gameVersion,
            classEntry,
            super.visitField(
                access,
                newName != null ? newName : name,
                newDescType != null ? newDescType.getDescriptor() : desc,
                signature,
                value
            )
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        String newName = null;

        //<editor-fold desc="Name">
        if (classEntry != null) {
            MethodEntry methodEntry = classEntry.getMethod(name, desc);
            if (methodEntry != null) newName = methodEntry.getDeobfuscatedName();
        }
        //</editor-fold>

        //<editor-fold desc="Desc">
        Type[] argDescTypes = Type.getArgumentTypes(desc);
        Type[] newArgDescTypes;

        Type returnDescType = Type.getReturnType(desc);
        Type newReturnDescType = null;

        //<editor-fold desc="Args">
        newArgDescTypes = new Type[argDescTypes.length];
        for(int i = 0; i < argDescTypes.length; i++) {
            Type argumentDescType = argDescTypes[i];
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

            if (newArgumentDescType == null) newArgumentDescType = argumentDescType;
            newArgDescTypes[i] = newArgumentDescType;
        }
        //</editor-fold>

        //<editor-fold desc="Return">
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
            //</editor-fold>
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        return new ReTweakMethodVisitor(
            super.api,
            gameVersion,
            classEntry,
            super.visitMethod(
                access,
                newName != null ? newName : name,
                Type.getMethodDescriptor(
                    newReturnDescType != null ? newReturnDescType : returnDescType,
                    newArgDescTypes
                ),
                signature,
                exceptions
            )
        );
    }

}
