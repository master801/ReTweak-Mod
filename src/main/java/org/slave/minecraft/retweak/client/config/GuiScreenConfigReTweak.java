package org.slave.minecraft.retweak.client.config;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import org.slave.lib.helpers.IterableHelper;
import org.slave.minecraft.retweak.client.config.elements.GuiScreenConfigReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

/**
 * Created by Master801 on 3/21/2016 at 9:29 AM.
 *
 * @author Master801
 */
public final class GuiScreenConfigReTweak extends GuiConfig {

    public GuiScreenConfigReTweak(GuiScreen parentScreen) {
        //noinspection ArraysAsListWithZeroOrOneArgument
        super(
                parentScreen,
                IterableHelper.easyArrayList(new IConfigElement[] {
                        new GuiScreenConfigReTweakConfig()
                }),
                ReTweakStrings.RETWEAK_MOD,
                ReTweakStrings.RETWEAK_GUI_CONFIG_ID,
                false,
                false,
                ReTweakStrings.RETWEAK_MOD
        );
    }

}
