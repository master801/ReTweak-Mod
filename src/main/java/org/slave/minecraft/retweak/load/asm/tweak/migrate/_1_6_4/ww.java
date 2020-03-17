package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by master on 11/30/18 at 3:23 PM
 *
 * @author master
 */
public class ww extends CreativeTabs {

    public ww(final String label) {
        super(label);
    }

    public ww(final int p_i1853_1_, final String p_i1853_2_) {
        super(p_i1853_1_, p_i1853_2_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getIconItemStack() {
        return super.getIconItemStack();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return null;
    }

}
