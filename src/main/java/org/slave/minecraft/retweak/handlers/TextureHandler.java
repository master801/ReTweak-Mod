package org.slave.minecraft.retweak.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureMap;

/**
 * Created by Master on 7/13/2018 at 11:44 AM.
 *
 * @author Master
 */
@UtilityClass
@SideOnly(Side.CLIENT)
public final class TextureHandler {

    @SideOnly(Side.CLIENT)
    public static void registerIcons(final TextureUser textureUser, final IIconRegister iconRegister) {
        if (iconRegister instanceof TextureMap) {
            TextureMap textureMap = (TextureMap)iconRegister;
            //TODO
        }
    }

    @SideOnly(Side.CLIENT)
    public interface TextureUser {

        @SideOnly(Side.CLIENT)
        String getTextureFile();

        @SideOnly(Side.CLIENT)
        void setIconIndex(final int iconIndex);

        @SideOnly(Side.CLIENT)
        int getIconIndex();

        @SideOnly(Side.CLIENT)
        void setIconCoord(final int x, final int y);

        @SideOnly(Side.CLIENT)
        int[] getIconCoords();

    }

}
