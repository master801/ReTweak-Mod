package org.slave.minecraft.retweak.client.screens;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.slave.lib.helpers.ColorHelper;
import org.slave.lib.resources.Colors;
import org.slave.minecraft.retweak.util.ReTweakStrings;

/**
 * Created by Master on 5/15/2016 at 10:05 AM.
 *
 * @author Master
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings({ "unchecked", "RedundantArrayCreation" })
public final class GuiScreenReTweakMods extends GuiScreen {

    private static final ResourceLocation RESOURCE_LOCATION_RETWEAK_MODS_BACKGROUND = new ResourceLocation(
            ReTweakStrings.RETWEAK_MOD.toLowerCase(),
            ReTweakStrings.RETWEAK_TEXTURES_GUI_RETWEAK_MODS + "Background.png"
    );
    private static final ResourceLocation RESOURCE_LOCATION_RETWEAK_MODS_BORDER = new ResourceLocation(
            ReTweakStrings.RETWEAK_MOD.toLowerCase(),
            ReTweakStrings.RETWEAK_TEXTURES_GUI_RETWEAK_MODS + "Border.png"
    );

    private final GuiScreen parentScreen;

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
        mc.getTextureManager().bindTexture(GuiScreenReTweakMods.RESOURCE_LOCATION_RETWEAK_MODS_BACKGROUND);
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
        int color = Colors.COLOR_RGB_PINK;
        float r = ColorHelper.toGL(
            ColorHelper.getR(color)
        );
        float g = ColorHelper.toGL(
            ColorHelper.getG(color)
        );
        float b = ColorHelper.toGL(
            ColorHelper.getB(color)
        );
        GL11.glColor4f(
            r,
            g,
            b,
            1.0F
        );

        final int thickness = 3;

        //Top
        super.drawTexturedModalRect(
            0,
            0,
            0,
            0,
            width,
            thickness
        );
        //Right
        super.drawTexturedModalRect(
            width - thickness,
            0,
            0,
            0,
            thickness,
            height
        );
        //Bottom
        super.drawTexturedModalRect(
            0,
            height - thickness,
            0,
            0,
            width,
            thickness
        );
        //Left
        super.drawTexturedModalRect(
            0,
            0,
            0,
            0,
            thickness,
            height
        );
    }

}
