package org.slave.retweak.test;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

/**
 * Created by Master on 6/16/2016 at 12:38 PM.
 *
 * @author Master
 */
public final class ItemTest__1_4_7 extends Item {

    private int iconIndex;
    private int iconIndex_x, iconIndex_y;

    private ItemTest__1_4_7() {
        super();
    }

    public final void func_77665_c(final int iconIndex) {
        this.iconIndex = iconIndex;
    }

    public final void func_77652_b(final int iconIndex_x, final int iconIndex_y) {
        this.iconIndex_x = iconIndex_x;
        this.iconIndex_y = iconIndex_y;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
    }

}
