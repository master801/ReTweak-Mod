package org.slave.minecraft.retweak.load.mapping.asm;

import org.objectweb.asm.FieldVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 3/17/2020 at 9:58 AM
 *
 * @author Master
 */
public final class SrgFieldVisitor extends FieldVisitor {

    private final GameVersion gameVersion;

    public SrgFieldVisitor(final int api, final FieldVisitor fv, final GameVersion gameVersion) {
        super(api, fv);
        this.gameVersion = gameVersion;
    }

}
