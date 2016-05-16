package org.slave.minecraft.retweak.client.screens;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;
import org.slave.minecraft.library.client.texture.ColoredTexture;
import org.slave.minecraft.library.helpers.TextureHelper;

import java.awt.Color;

/**
 * Created by Master on 5/15/2016 at 10:05 AM.
 *
 * @author Master
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings({ "unchecked", "RedundantArrayCreation" })
public final class GuiScreenReTweakMods extends GuiScreen {

    private final GuiScreen parentScreen;
    private AbstractTexture background;

    public GuiScreenReTweakMods(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void drawScreen(final int mousePosX, final int mousePosY, final float renderPartialTicks) {
        //Reset color state
        GL11.glColor4d(
                1.0D,
                1.0D,
                1.0D,
                1.0D
        );

        //Bind texture
        TextureHelper.bindTexture(background.getGlTextureId());

        //Render texture
        super.drawTexturedModalRect(
                0,
                0,
                0,
                0,
                width,
                height
        );
        super.drawScreen(mousePosX, mousePosY, renderPartialTicks);
    }

    @Override
    public void initGui() {
        GuiButton done = new GuiButton(
                0,
                0,//Dummy x pos
                0,//Dummy y pos
                I18n.format("gui.done")
        );
        done.xPosition = (width / 2) - (done.width / 2);
        done.yPosition = height - (20 + 12);
        buttonList.add(done);
        background = new ColoredTexture(
                width,
                height,
                Color.BLACK
        );
        super.initGui();
    }

    @Override
    protected void actionPerformed(final GuiButton guiButton) {
        if (guiButton == null) return;
        switch(guiButton.id) {
            case 0:
                mc.displayGuiScreen(parentScreen);
                break;
        }
        super.actionPerformed(guiButton);
    }

}
