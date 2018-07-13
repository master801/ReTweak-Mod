package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
        _package = @Package("ItemStack"),
        name = "net.minecraft.item"
)
public class ur extends ItemStack {

    public ur(final Block p_i1876_1_) {
        super(p_i1876_1_);
    }

    public ur(final Block p_i1877_1_, final int p_i1877_2_) {
        super(p_i1877_1_, p_i1877_2_);
    }

    public ur(final Block p_i1878_1_, final int p_i1878_2_, final int p_i1878_3_) {
        super(p_i1878_1_, p_i1878_2_, p_i1878_3_);
    }

    public ur(final Item p_i1879_1_) {
        super(p_i1879_1_);
    }

    public ur(final Item p_i1880_1_, final int p_i1880_2_) {
        super(p_i1880_1_, p_i1880_2_);
    }

    public ur(final Item p_i1881_1_, final int p_i1881_2_, final int p_i1881_3_) {
        super(p_i1881_1_, p_i1881_2_, p_i1881_3_);
    }

}
