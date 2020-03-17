package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_5_2;

import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.world.World;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 1/18/2020 at 3:06 AM
 *
 * @author Master
 */
@Obfuscated(
        name = "bem"
)
@Deobfuscated(
        _package = @Package("net.minecraft.client.particle"),
        name = "EntityFlameFX"
)
public class bem extends EntityFlameFX {

    public bem(final World p_i1209_1_, final double p_i1209_2_, final double p_i1209_4_, final double p_i1209_6_, final double p_i1209_8_, final double p_i1209_10_, final double p_i1209_12_) {
        super(p_i1209_1_, p_i1209_2_, p_i1209_4_, p_i1209_6_, p_i1209_8_, p_i1209_10_, p_i1209_12_);
    }

}
