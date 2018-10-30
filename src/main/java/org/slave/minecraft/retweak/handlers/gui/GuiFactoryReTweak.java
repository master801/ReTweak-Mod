package org.slave.minecraft.retweak.handlers.gui;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.slave.minecraft.retweak.handlers.gui.screen.GuiConfigReTweak;

import java.util.Set;

/**
 * Created by Master on 7/25/2018 at 4:21 PM.
 *
 * @author Master
 */
public final class GuiFactoryReTweak implements IModGuiFactory {

    @Override
    public void initialize(final Minecraft minecraftInstance) {
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiConfigReTweak.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(final RuntimeOptionCategoryElement element) {
        return null;
    }

}
