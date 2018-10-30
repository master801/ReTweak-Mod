package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;

/**
 * Created by Master on 8/30/2018 at 1:32 AM.
 *
 * @author Master
 */
public abstract class tj extends CreativeTabs {

    public tj(final int p_i1853_1_, final String p_i1853_2_) {
        super(p_i1853_1_, p_i1853_2_);
    }

    public tj(final String label) {
        super(label);
    }

    @Obfuscated(name = "c")
    @Deobfuscated(name = "getTranslatedTabLabel")
    public String c() {
        return super.getTranslatedTabLabel();
    }

    @SideOnly(Side.CLIENT)
    @Obfuscated(name = "e")
    @Deobfuscated(name = "getTabIconItemIndex")
    public int e() {
        return -1;//TODO
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return null;
    }

    @Override
    public String getTranslatedTabLabel() {
        return c();
    }

}
