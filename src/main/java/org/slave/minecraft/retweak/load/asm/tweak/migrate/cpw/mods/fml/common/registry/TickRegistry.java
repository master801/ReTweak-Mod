package org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.registry;

import cpw.mods.fml.relauncher.Side;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.IScheduledTickHandler;

/**
 * Created by Master on 1/18/2020 at 1:07 AM
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package("cpw.mods.fml.common.registry"),
        name = "TickRegistry"
)
@Deobfuscated(
        _package = @Package("cpw.mods.fml.common.registry"),
        name = "TickRegistry"
)
public final class TickRegistry {

    public static void registerScheduledTickHandler(IScheduledTickHandler handler, Side side) {
        //TODO
    }

}
