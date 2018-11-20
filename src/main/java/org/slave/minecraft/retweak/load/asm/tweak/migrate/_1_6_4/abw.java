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

    public abw(final ISaveHandler p_i45368_1_, final String p_i45368_2_, final WorldProvider p_i45368_3_, final WorldSettings p_i45368_4_, final Profiler p_i45368_5_) {
        super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
    }

    public abw(final ISaveHandler p_i45369_1_, final String p_i45369_2_, final WorldSettings p_i45369_3_, final WorldProvider p_i45369_4_, final Profiler p_i45369_5_) {
        super(p_i45369_1_, p_i45369_2_, p_i45369_3_, p_i45369_4_, p_i45369_5_);
    }

}
