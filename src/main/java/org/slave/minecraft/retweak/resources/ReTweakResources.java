package org.slave.minecraft.retweak.resources;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.experimental.UtilityClass;

/**
 * Created by master on 11/18/18 at 11:20 PM
 *
 * @author master
 */
@UtilityClass
public final class ReTweakResources {

    /**
     * {@link org.slave.minecraft.retweak.load.asm.ReTweakClassASM#fixBadProgrammingGarbage(org.objectweb.asm.tree.ClassNode)}
     */
    public static final CreativeTabs CREATIVE_TAB_RETWEAK = new CreativeTabs("retweak") {

        @Override
        @SideOnly(Side.CLIENT)
        public Item getTabIconItem() {
            return Items.diamond;
        }

    };

}
