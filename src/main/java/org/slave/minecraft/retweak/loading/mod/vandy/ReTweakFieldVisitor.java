package org.slave.minecraft.retweak.loading.mod.vandy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 6/3/2017 at 9:52 AM.
 *
 * @author Master
 */
public final class ReTweakFieldVisitor extends FieldVisitor {

    private final GameVersion gameVersion;

    public ReTweakFieldVisitor(final int api, final GameVersion gameVersion, final FieldVisitor fv) {
        super(api, fv);
        this.gameVersion = gameVersion;
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

}
