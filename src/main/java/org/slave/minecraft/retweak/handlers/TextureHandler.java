package org.slave.minecraft.retweak.handlers;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.library.client.texture.BufferedTexture;
import org.slave.minecraft.library.helpers.TextureHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Master on 6/16/2016 at 9:16 AM.
 *
 * @author Master
 */
@SideOnly(Side.CLIENT)
public final class TextureHandler {

    private static final Map<String, AbstractTexture> TEXTURE_CACHE = new HashMap<>();

    private TextureHandler() {
        throw new IllegalStateException();
    }

    public static AbstractTexture getTextureOnClassPath(final String texturePath, final Class<?> clazzPath) {
        if (StringHelper.isNullOrEmpty(texturePath)) return null;
        if (TextureHandler.TEXTURE_CACHE.containsKey(texturePath)) return TextureHandler.TEXTURE_CACHE.get(texturePath);
        AbstractTexture abstractTexture = null;
        BufferedImage bufferedImage = null;
        InputStream inputStream = clazzPath.getResourceAsStream(texturePath);
        if (inputStream != null) {
            try {
                bufferedImage = ImageIO.read(inputStream);
                inputStream.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        if (bufferedImage != null) {
            abstractTexture = new BufferedTexture(
                    bufferedImage.getWidth(),
                    bufferedImage.getHeight(),
                    bufferedImage
            );
            try {
                abstractTexture.loadTexture(
                        Minecraft.getMinecraft().getResourceManager()
                );
            } catch(IOException e) {
                //Ignore
            }
            TextureHandler.TEXTURE_CACHE.put(
                    texturePath,
                    abstractTexture
            );
        }
        return abstractTexture;
    }

    public static void bindTexture(final Class<?> clazz, final String texturePath) {
        TextureHelper.bindTexture(
                TextureHandler.getTextureOnClassPath(
                        texturePath,
                        clazz
                )
        );
    }

}
