package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import org.slave.minecraft.retweak.handlers.TextureHandler;
import org.slave.minecraft.retweak.handlers.TextureHandler.TextureUser;
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
public class up extends Item implements TextureUser {

    @Obfuscated(name = "cl")
    @Deobfuscated(name = "iconIndex")
    public int cl;

    public up(final int itemID) {
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        TextureHandler.registerIcons(this, iconRegister);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTextureFile() {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setIconIndex(final int iconIndex) {
        cl = iconIndex;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getIconIndex() {
        return cl;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void setIconCoord(final int x, final int y) {
        setIconIndex(x + y);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int[] getIconCoords() {
        int x = 0, y = 0;

        int tmp = getIconIndex();
        while(tmp > 15) {
            y++;
            tmp -= 15;
        }

        return new int[] {
                x,
                y
        };
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

    @Obfuscated(name = "b", parameters = { int.class, int.class })
    @Deobfuscated(name = "setIconCoord")
    public up b(final int iconX, final int iconY) {
        setIconCoord(iconX, iconY);
        return this;
    }

    @Obfuscated(name = "c")
    @Deobfuscated(name = "setIconIndex")
    public up c(final int iconIndex) {
        setIconIndex(iconIndex);
        return this;
    }

}
