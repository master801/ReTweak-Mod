package org.slave.minecraft.retweak.load.asm.tweak.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/12/2018 at 8:37 PM.
 *
 * @author Master
 */
public final class TweakAnnotationVisitor extends AnnotationVisitor {

    private final GameVersion gameVersion;

    TweakAnnotationVisitor(final int api, final GameVersion gameVersion, final AnnotationVisitor av) {
        super(api, av);
        this.gameVersion = gameVersion;
    }

}
