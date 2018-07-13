package org.slave.minecraft.retweak.load.asm;

import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/12/2018 at 8:35 PM.
 *
 * @author Master
 */
public final class TweakMethodVisitor extends MethodVisitor {

    private final GameVersion gameVersion;

    TweakMethodVisitor(final int api, final GameVersion gameVersion, final MethodVisitor mv) {
        super(api, mv);
        this.gameVersion = gameVersion;
    }

}
