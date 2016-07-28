package org.slave.minecraft.retweak;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import org.slave.minecraft.library.helpers.ModHelper;
import org.slave.minecraft.retweak.client.screens.GuiScreenReTweakMods;
import org.slave.minecraft.retweak.resources.ReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakMetadata;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

import java.io.IOException;
import java.util.List;

/**
 * Created by Master801 on 3/18/2016 at 9:09 PM.
 *
 * @author Master801
 */
@Mod(
        modid = ReTweakStrings.RETWEAK_MOD,
        name = ReTweakStrings.RETWEAK_MOD,
        version = ReTweakStrings.RETWEAK_VERSION,
        guiFactory = ReTweakStrings.RETWEAK_GUI_FACTORY
)
public final class ReTweakMod {

    @Instance(ReTweakStrings.RETWEAK_MOD)
    public static ReTweakMod instance;

    @Metadata(ReTweakStrings.RETWEAK_MOD)
    public static ModMetadata modMetadata;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModHelper.injectMetadata(
                ReTweakMod.modMetadata,
                ReTweakMetadata.INSTANCE
        );
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (event.modID.equals(ReTweakStrings.RETWEAK_MOD) && event.configID.equals(ReTweakStrings.RETWEAK_GUI_CONFIG_ID)) {
            //TODO
            try {
                ReTweakConfig.INSTANCE.update(false);
            } catch(IOException e) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Caught an IO exception while updating the config file!",
                        e
                );
            }
        }
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiInitPost(InitGuiEvent.Post event) {
        if (event.gui instanceof GuiMainMenu) {
            GuiButton multiPlayerButton = null;

            GuiButton modsButton = null;
            for(GuiButton guiButton : (List<GuiButton>)event.buttonList) {
                //01 = Single-player
                //02 = Multi-player
                //06 = Mods
                //14 = Realms
                if (guiButton.id == 1 || guiButton.id == 2 || guiButton.id == 14) guiButton.yPosition -= 12;//Move buttons up to make room for more
                if (guiButton.id == 6) modsButton = guiButton;
                if (guiButton.id == 2) multiPlayerButton = guiButton;
                if (guiButton.id == 14 && multiPlayerButton != null) guiButton.width = multiPlayerButton.width;
            }

            if (modsButton != null) {
                GuiButton button = new GuiButton(
                        13,
                        (event.gui.width / 2) + 24,
                        0,
                        "ReTweak-Mods"
                );
                modsButton.yPosition += 12;
                button.width = modsButton.width;
                button.xPosition = modsButton.xPosition;
                button.yPosition = modsButton.yPosition;
                modsButton.xPosition -= (modsButton.width + 4);
                event.buttonList.add(button);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onGuiButtonClicked(ActionPerformedEvent event) {
        if (event.gui instanceof GuiMainMenu) {
            switch(event.button.id) {
                case 13:
                    event.gui.mc.displayGuiScreen(new GuiScreenReTweakMods(event.gui));
                    break;
            }
        }
    }

}
