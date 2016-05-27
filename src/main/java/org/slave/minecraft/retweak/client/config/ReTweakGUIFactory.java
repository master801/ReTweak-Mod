package org.slave.minecraft.retweak.client.config;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

/**
 * <p>
 *     {@link org.slave.minecraft.retweak.ReTweakMod}
 *     {@link org.slave.minecraft.retweak.resources.ReTweakStrings#RETWEAK_GUI_FACTORY}
 * </p>
 *
 * Created by Master801 on 3/21/2016 at 9:23 AM.
 *
 * @author Master801
 */
public final class ReTweakGUIFactory implements IModGuiFactory {

    @Override
    public void initialize(Minecraft minecraftInstance) {
        //NOOP
    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiScreenConfigReTweak.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }

}
