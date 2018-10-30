package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import net.minecraft.world.biome.BiomeGenBase;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 7/19/2018 at 12:38 PM.
 *
 * @author Master
 */
@Obfuscated(name = "yy")
@Deobfuscated(
        _package = @Package("net.minecraft.world.biome"),
        name = "BiomeGenBase"
)
public abstract class yy extends BiomeGenBase {

    public yy(final int p_i1971_1_) {
        super(p_i1971_1_);
    }

    public yy(final int p_i1971_1_, final boolean register) {
        super(p_i1971_1_, register);
    }

}
