package org.slave.minecraft.retweak.client.screens;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;

/**
 * Created by Master on 5/15/2016 at 10:05 AM.
 *
 * @author Master
 */
@SideOnly(Side.CLIENT)
@SuppressWarnings({ "unchecked", "RedundantArrayCreation" })
public final class GuiScreenReTweakMods extends GuiScreen {

    private final GuiScreen parentScreen;

    public GuiScreenReTweakMods(final GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void drawScreen(final int p_73863_1_, final int p_73863_2_, final float p_73863_3_) {
        //TODO DRAW BACKGROUND
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
    }

    @Override
    public void initGui() {
        GuiButton done = new GuiButton(
                0,
                0,
                0,
                I18n.format("gui.done")
        );
        done.xPosition = (width / 2) - (done.width / 2);
        done.yPosition = (height / 2);//TODO FIX Y POSITION
        buttonList.add(done);
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
