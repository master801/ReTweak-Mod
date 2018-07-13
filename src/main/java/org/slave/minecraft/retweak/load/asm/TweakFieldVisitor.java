package org.slave.minecraft.retweak.load.asm;

import org.objectweb.asm.FieldVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/12/2018 at 8:35 PM.
 *
 * @author Master
 */
public final class TweakFieldVisitor extends FieldVisitor {

    private final GameVersion gameVersion;

    TweakFieldVisitor(final int api, final GameVersion gameVersion, final FieldVisitor fv) {
        super(api, fv);
        this.gameVersion = gameVersion;
    }

}
