package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_5_2;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 1/18/2020 at 3:03 AM
 *
 * @author Master
 */
@Obfuscated(
        name = "bet"
)
@Deobfuscated(
        _package = @Package("net.minecraft.client.particle"),
        name = "EntityFX"
)
public class bet extends EntityFX {

    protected bet(final World p_i1218_1_, final double p_i1218_2_, final double p_i1218_4_, final double p_i1218_6_) {
        super(p_i1218_1_, p_i1218_2_, p_i1218_4_, p_i1218_6_);
    }

    public bet(final World p_i1219_1_, final double p_i1219_2_, final double p_i1219_4_, final double p_i1219_6_, final double p_i1219_8_, final double p_i1219_10_, final double p_i1219_12_) {
        super(p_i1219_1_, p_i1219_2_, p_i1219_4_, p_i1219_6_, p_i1219_8_, p_i1219_10_, p_i1219_12_);
    }

}
