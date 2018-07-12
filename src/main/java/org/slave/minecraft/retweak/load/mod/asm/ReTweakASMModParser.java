package org.slave.minecraft.retweak.load.mod.asm;

import cpw.mods.fml.common.discovery.asm.ASMModParser;
import lombok.AccessLevel;
import lombok.Getter;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.asm.ASMModParser}
 * </p>
 *
 *
 * Created by Master on 7/11/2018 at 11:59 PM.
 *
 * @author Master
 */
public final class ReTweakASMModParser extends ASMModParser {

    @Getter(value = AccessLevel.PROTECTED)
    private final GameVersion gameVersion;

    public ReTweakASMModParser(final GameVersion gameVersion, final InputStream stream) throws IOException {
        super(stream);
        this.gameVersion = gameVersion;
    }

}
