package org.slave.minecraft.retweak.client.screens;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
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

    private static final Color BACKGROUND_COLOR = new Color(0xFDD2D9);
    private static final Color BORDER_COLOR = new Color(0xF795A6);
    private static final int BORDER_THICKNESS = 5;

    private final GuiScreen parentScreen;
    private AbstractTexture backgroundTexture;

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

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        drawBackground();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        drawForeground();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
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
        backgroundTexture = new ColoredTexture(
                width,
                height,
                GuiScreenReTweakMods.BACKGROUND_COLOR
        );
        //TODO Add mods
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

    private void drawBackground() {
        //Bind texture
        TextureHelper.bindTexture(backgroundTexture);

        //Render texture
        super.drawTexturedModalRect(
                0,
                0,
                0,
                0,
                width,
                height
        );
    }

    private void drawForeground() {
        Tessellator.instance.startDrawing(GL11.GL_LINE_LOOP);
        GL11.glLineWidth(GuiScreenReTweakMods.BORDER_THICKNESS);
        Tessellator.instance.setColorRGBA(
                GuiScreenReTweakMods.BORDER_COLOR.getRed(),
                GuiScreenReTweakMods.BORDER_COLOR.getGreen(),
                GuiScreenReTweakMods.BORDER_COLOR.getBlue(),
                GuiScreenReTweakMods.BORDER_COLOR.getAlpha()
        );
        Tessellator.instance.addVertex(
                0.0D,
                0.0D,
                0.0D
        );
        Tessellator.instance.addVertex(
                width,
                0.0D,
                0.0D
        );
        Tessellator.instance.addVertex(
                width,
                height,
                0.0D
        );
        Tessellator.instance.addVertex(
                0.0D,
                height,
                0.0D
        );
        Tessellator.instance.draw();
    }

}
