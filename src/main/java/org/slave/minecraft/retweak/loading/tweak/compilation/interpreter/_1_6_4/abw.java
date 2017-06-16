package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4;

import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.storage.ISaveHandler;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Obfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

/**
 * Created by Master on 6/15/2017 at 1:26 PM.
 *
 * @author Master
 */
@Obfuscated(
    name = "abw",
    _package = @Package("")
)
@Deobfuscated(
    name = "World",
    _package = @Package("net.minecraft.world")
)
public abstract class abw extends World {

    public abw(final ISaveHandler p_i45368_1_, final String p_i45368_2_, final WorldProvider p_i45368_3_, final WorldSettings p_i45368_4_, final Profiler p_i45368_5_) {
        super(
            p_i45368_1_,
            p_i45368_2_,
            p_i45368_3_,
            p_i45368_4_,
            p_i45368_5_
        );
    }

    public abw(final ISaveHandler p_i45369_1_, final String p_i45369_2_, final WorldSettings p_i45369_3_, final WorldProvider p_i45369_4_, final Profiler p_i45369_5_) {
        super(
            p_i45369_1_,
            p_i45369_2_,
            p_i45369_3_,
            p_i45369_4_,
            p_i45369_5_
        );
    }

}
