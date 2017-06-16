package org.slave.minecraft.retweak.loading.mod.vandy;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

import java.util.Arrays;

/**
 * Created by Master on 6/3/2017 at 9:42 AM.
 *
 * @author Master
 */
public final class ReTweakClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

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

        Class<?> overrideSuperNameClass = gameVersion.getOverrideClass(superName);
        if (overrideSuperNameClass != null) newSuperName = Type.getInternalName(overrideSuperNameClass);

        newInterfaces = new String[interfaces.length];
        for(int i = 0; i < newInterfaces.length; i++) {
            String _interface = interfaces[i];
            String newInterface = null;

            Class<?> overrideInterfaceClass = gameVersion.getOverrideClass(_interface);
            if (overrideInterfaceClass != null) newInterface = Type.getInternalName(overrideInterfaceClass);

            if (newInterface == null) newInterface = _interface;

            newInterfaces[i] = newInterface;
        }

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

        Class<?> overrideClass = gameVersion.getOverrideClass(descType.getClassName());
        if (overrideClass != null) newDescType = Type.getType(overrideClass);

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
        Type descType = Type.getType(desc);
        Type newDescType = null;

        Class<?> descOverrideClass = gameVersion.getOverrideClass(descType.getClassName());
        if (descOverrideClass != null) newDescType = Type.getType(descOverrideClass);

        if (descType.getSort() == Type.ARRAY) {
            Type elementDescType = descType.getElementType();

            descOverrideClass = gameVersion.getOverrideClass(elementDescType.getClassName());

            Object[] dimensions = new Object[descType.getDimensions()];
            Arrays.fill(
                dimensions,
                '['
            );

            newDescType = Type.getType(
                Joiner.on("").join(dimensions) + (descOverrideClass != null ? Type.getDescriptor(descOverrideClass) : elementDescType.getDescriptor())
            );
        }

        return new ReTweakFieldVisitor(
            super.api,
            gameVersion,
            super.visitField(
                access,
                name,
                newDescType != null ? newDescType.getDescriptor() : desc,
                signature,
                value
            )
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        Type[] argDescTypes = Type.getArgumentTypes(desc);
        Type[] newArgDescTypes;

        Type returnDescType = Type.getReturnType(desc);
        Type newReturnDescType = null;

        newArgDescTypes = new Type[argDescTypes.length];
        for(int i = 0; i < argDescTypes.length; i++) {
            Type argumentDescType = argDescTypes[i];
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

            if (newArgumentDescType == null) newArgumentDescType = argumentDescType;
            newArgDescTypes[i] = newArgumentDescType;
        }

        Class<?> returnDescOverrideClass = gameVersion.getOverrideClass(returnDescType.getClassName());
        if (returnDescOverrideClass != null) newReturnDescType = Type.getType(returnDescOverrideClass);

        if (returnDescType.getSort() == Type.ARRAY) {
            Type elementReturnDescType = returnDescType.getElementType();

            returnDescOverrideClass = gameVersion.getOverrideClass(elementReturnDescType.getClassName());

            Object[] dimensions = new Object[returnDescType.getDimensions()];
            Arrays.fill(
                dimensions,
                '['
            );

            newReturnDescType = Type.getType(
                Joiner.on("").join(dimensions) + (returnDescOverrideClass != null ? Type.getDescriptor(returnDescOverrideClass) : elementReturnDescType.getDescriptor())
            );
        }

        return new ReTweakMethodVisitor(
            super.api,
            gameVersion,
            super.visitMethod(
                access,
                name,
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
