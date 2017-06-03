package org.slave.minecraft.retweak.loading.mod.vandy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

/**
 * Created by Master on 6/3/2017 at 9:42 AM.
 *
 * @author Master
 */
public final class ReTweakClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

    public ReTweakClassVisitor(final int api, final GameVersion gameVersion, final ClassVisitor cv) {
        super(api, cv);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(
            version,
            access,
            name,
            signature,
            superName,
            interfaces
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
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        Type descType = Type.getType(desc);
        Type newDescType = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(descType.getClassName());
        if (overrideClass != null) newDescType = Type.getType(overrideClass);

        if (descType.getSort() == Type.ARRAY) {
            //TODO
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to get the desc type of field \"{} {}\"! Array is not supported!",
                name,
                desc
            );
        }

        if (newDescType == null) newDescType = descType;

        return new ReTweakFieldVisitor(
            super.api,
            gameVersion,
            super.visitField(
                access,
                name,
                newDescType.getDescriptor(),
                signature,
                value
            )
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        Type[] argDescTypes = Type.getArgumentTypes(desc);
        Type[] newArgDescTypes = null;

        Type returnDescType = Type.getReturnType(desc);
        Type newReturnDescType = null;

        newArgDescTypes = new Type[argDescTypes.length];
        for(int i = 0; i < argDescTypes.length; i++) {
            Type argDescType = argDescTypes[i];
            Type newArgDescType = null;

            if (argDescType.getSort() == Type.ARRAY) {
                //TODO
                ReTweakResources.RETWEAK_LOGGER.error(
                    "Failed to get the desc type of method arg [{}] \"{} {}\"! Array is not supported!",
                    i,
                    name,
                    desc
                );
            } else {
                Class<?> overrideClass = gameVersion.getOverrideClass(name);
            }

            if (newArgDescType == null) newArgDescType = argDescType;

            newArgDescTypes[i] = newArgDescType;
        }

        return new ReTweakMethodVisitor(
            super.api,
            gameVersion,
            super.visitMethod(
                access,
                name,
                desc,
                signature,
                exceptions
            )
        );
    }

}
