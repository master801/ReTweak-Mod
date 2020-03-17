package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_5_2;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 1/18/2020 at 12:55 AM
 *
 * @author Master
 */
@Obfuscated(
        name = "ju"
)
@Deobfuscated(
        _package = @Package("net.minecraft.stats"),
        name = "Achievement"
)
public class ju extends Achievement {

    public ju(final String p_i45300_1_, final String p_i45300_2_, final int p_i45300_3_, final int p_i45300_4_, final Item p_i45300_5_, final Achievement p_i45300_6_) {
        super(p_i45300_1_, p_i45300_2_, p_i45300_3_, p_i45300_4_, p_i45300_5_, p_i45300_6_);
    }

    public ju(final String p_i45301_1_, final String p_i45301_2_, final int p_i45301_3_, final int p_i45301_4_, final Block p_i45301_5_, final Achievement p_i45301_6_) {
        super(p_i45301_1_, p_i45301_2_, p_i45301_3_, p_i45301_4_, p_i45301_5_, p_i45301_6_);
    }

    public ju(final String p_i45302_1_, final String p_i45302_2_, final int p_i45302_3_, final int p_i45302_4_, final ItemStack p_i45302_5_, final Achievement p_i45302_6_) {
        super(p_i45302_1_, p_i45302_2_, p_i45302_3_, p_i45302_4_, p_i45302_5_, p_i45302_6_);
    }

}
