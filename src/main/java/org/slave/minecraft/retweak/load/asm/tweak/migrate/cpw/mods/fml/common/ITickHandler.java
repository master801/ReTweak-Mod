package org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common;

import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

import java.util.EnumSet;

/**
 * Created by Master on 7/19/2018 at 12:03 PM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package("cpw.mods.fml.common"),
        name = "ITickHandler"
)
@Deobfuscated(
        _package = @Package("cpw.mods.fml.common"),
        name = "ITickHandler"
)
public interface ITickHandler {

    void tickStart();

    void tickEnd();

    EnumSet<?> ticks();

    String getLabel();

}
