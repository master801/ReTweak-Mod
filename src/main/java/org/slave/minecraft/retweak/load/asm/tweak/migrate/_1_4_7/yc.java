package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 7/13/2018 at 11:04 AM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package(""),
        name = "yc"
)
@Deobfuscated(
        _package = @Package("net.minecraft.world"),
        name = "World"
)
public abstract class yc extends World {

    public yc(final ISaveHandler p_i45368_1_, final String p_i45368_2_, final WorldProvider p_i45368_3_, final WorldSettings p_i45368_4_, final Profiler p_i45368_5_) {
        super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
    }

    public yc(final ISaveHandler p_i45369_1_, final String p_i45369_2_, final WorldSettings p_i45369_3_, final WorldProvider p_i45369_4_, final Profiler p_i45369_5_) {
        super(p_i45369_1_, p_i45369_2_, p_i45369_3_, p_i45369_4_, p_i45369_5_);
    }

}
