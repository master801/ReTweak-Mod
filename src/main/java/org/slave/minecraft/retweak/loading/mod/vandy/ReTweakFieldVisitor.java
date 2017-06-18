package org.slave.minecraft.retweak.loading.mod.vandy;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 6/3/2017 at 9:52 AM.
 *
 * @author Master
 */
public final class ReTweakFieldVisitor extends FieldVisitor {

    private final GameVersion gameVersion;
    private final ClassEntry classEntry;

    public ReTweakFieldVisitor(final int api, final GameVersion gameVersion, final ClassEntry classEntry, final FieldVisitor fv) {
        super(
            api,
            fv
        );
        this.gameVersion = gameVersion;
        this.classEntry = classEntry;
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

}
