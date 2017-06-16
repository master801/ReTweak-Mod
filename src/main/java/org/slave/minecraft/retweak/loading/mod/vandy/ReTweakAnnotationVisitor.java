package org.slave.minecraft.retweak.loading.mod.vandy;

import org.objectweb.asm.AnnotationVisitor;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 6/3/2017 at 9:51 AM.
 *
 * @author Master
 */
public final class ReTweakAnnotationVisitor extends AnnotationVisitor {

    private final GameVersion gameVersion;

    public ReTweakAnnotationVisitor(final int api, final GameVersion gameVersion, final AnnotationVisitor av) {
        super(
            api,
            av
        );
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final String name, final Object value) {
        super.visit(
            name,
            value
        );
    }

    @Override
    public void visitEnum(final String name, final String desc, final String value) {
        super.visitEnum(
            name,
            desc,
            value
        );
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String name, final String desc) {
        return super.visitAnnotation(
            name,
            desc
        );
    }

    @Override
    public AnnotationVisitor visitArray(final String name) {
        return super.visitArray(
            name
        );
    }

}
