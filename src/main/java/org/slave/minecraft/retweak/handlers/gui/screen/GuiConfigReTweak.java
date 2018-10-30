package org.slave.minecraft.retweak.handlers.gui.screen;

import com.google.common.collect.Lists;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import org.slave.minecraft.retweak.asm.ReTweakASM.ReTweakInternalModContainer;
import org.slave.minecraft.retweak.handlers.gui.screen.elements.category.CategoryElementGameVersion;

/**
 * Created by Master on 7/25/2018 at 4:36 PM.
 *
 * @author Master
 */
public final class GuiConfigReTweak extends GuiConfig {

    public GuiConfigReTweak(final GuiScreen parentScreen) {
        super(
                parentScreen,
                Lists.newArrayList(
                        new CategoryElementGameVersion().init()
                ),
                ReTweakInternalModContainer.MODID,
                "ReTweak-CFG",
                false,
                false,
                "ReTweak Config"
        );
    }

}
