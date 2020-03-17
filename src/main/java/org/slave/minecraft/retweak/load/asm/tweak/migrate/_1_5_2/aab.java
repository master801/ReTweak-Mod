package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_5_2;

import net.minecraft.entity.Entity;
import net.minecraft.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.ISaveHandler;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 1/18/2020 at 3:09 AM
 *
 * @author Master
 */
@Obfuscated(
        name = "aab"
)
@Deobfuscated(
        _package = @Package("net.minecraft.world"),
        name = "World"
)
public class aab extends World {

    public aab(final ISaveHandler p_i45368_1_, final String p_i45368_2_, final WorldProvider p_i45368_3_, final WorldSettings p_i45368_4_, final Profiler p_i45368_5_) {
        super(p_i45368_1_, p_i45368_2_, p_i45368_3_, p_i45368_4_, p_i45368_5_);
    }

    public aab(final ISaveHandler p_i45369_1_, final String p_i45369_2_, final WorldSettings p_i45369_3_, final WorldProvider p_i45369_4_, final Profiler p_i45369_5_) {
        super(p_i45369_1_, p_i45369_2_, p_i45369_3_, p_i45369_4_, p_i45369_5_);
    }

    @Override
    protected IChunkProvider createChunkProvider() {
        return null;
    }

    @Override
    protected int func_152379_p() {
        return 0;
    }

    @Override
    public Entity getEntityByID(final int p_73045_1_) {
        return null;
    }

}
