package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 7/13/2018 at 11:01 AM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package(""),
        name = "up"
)
@Deobfuscated(
        _package = @Package("net.minecraft.item"),
        name = "Item"
)
public class up extends Item {

    public up(final int itemID) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "setCreativeTab")
    public up a(final tf creativeTab) {
        super.setCreativeTab(creativeTab);
        return this;
    }

    @Obfuscated(name = "a", parameters = { ur.class, yc.class, qx.class })
    @Deobfuscated(name = "onItemRightClick")
    public ur a(ur itemstack, yc world, qx entityplayer) {
//        return super.onItemRightClick(itemstack, world, entityplayer);
        return null;
    }

    @SideOnly(Side.CLIENT)
    public String getTextureFile() {
        return null;
    }

}
