package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;

import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by master on 11/20/18 at 4:05 PM
 *
 * @author master
 */
@Obfuscated(name = "abw")
@Deobfuscated(
        _package = @Package("net.minecraft.world"),
        name = "World"
)
public abstract class abw extends World {

    public abw(final ISaveHandler saveHandler, final String name, final WorldProvider provider, final WorldSettings settings, final Profiler profiler) {
        super(saveHandler, name, provider, settings, profiler);
    }

    public abw(final ISaveHandler saveHandler, final String name, final WorldSettings settings, final WorldProvider provider, final Profiler profiler) {
        super(saveHandler, name, settings, provider, profiler);
    }

}
