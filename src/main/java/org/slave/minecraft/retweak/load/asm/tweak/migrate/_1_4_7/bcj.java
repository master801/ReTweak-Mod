package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 7/19/2018 at 2:00 PM.
 *
 * @author Master
 */
@Obfuscated(name = "bcj")
@Deobfuscated(
        _package = @Package("net.minecraft.client.renderer.entity"),
        name = "RenderLiving"
)
public abstract class bcj extends RenderLiving {

    public bcj(final ModelBase p_i1262_1_, final float p_i1262_2_) {
        super(p_i1262_1_, p_i1262_2_);
    }

}
