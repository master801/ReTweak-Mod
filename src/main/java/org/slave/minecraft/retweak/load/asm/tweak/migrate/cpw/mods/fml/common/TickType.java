package org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common;

import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

import java.util.EnumSet;

/**
 * Created by Master on 7/19/2018 at 12:05 PM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package("cpw.mods.fml.common"),
        name = "TickType"
)
@Deobfuscated(
        _package = @Package("cpw.mods.fml.common"),
        name = "TickType"
)
public enum TickType {

    WORLD,

    RENDER,

    GUI,

    CLIENTGUI,

    WORLDLOAD,

    CLIENT,

    PLAYER,

    SERVER;

    public EnumSet<TickType> partnerTicks() {
        if (this == CLIENT) return EnumSet.of(RENDER);
        if (this == RENDER) return EnumSet.of(CLIENT);

        if (this == GUI) return EnumSet.of(CLIENTGUI);
        if (this == CLIENTGUI) return EnumSet.of(GUI);
        return EnumSet.noneOf(TickType.class);
    }

}
