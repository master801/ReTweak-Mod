package org.slave.minecraft.retweak;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import org.slave.minecraft.library.api.network.proxy.Proxy;
import org.slave.minecraft.library.helpers.ModHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

/**
 * Created by Master801 on 3/18/2016 at 9:09 PM.
 *
 * @author Master801
 */
@Mod(
        modid = ReTweakStrings.RETWEAK_MOD,
        name = ReTweakStrings.RETWEAK_MOD,
        version = ReTweakStrings.RETWEAK_VERSION,
        guiFactory = "org.slave.minecraft.retweak.client.config.ReTweakGUIFactory"
)
public final class ReTweakMod {

    @Instance(ReTweakStrings.RETWEAK_MOD)
    public static ReTweakMod instance;

    @SidedProxy(
            clientSide = ReTweakStrings.RETWEAK_PROXY_CLIENT,
            serverSide = ReTweakStrings.RETWEAK_PROXY_SERVER,
            modId = ReTweakStrings.RETWEAK_MOD
    )
    public static Proxy proxy;

    @Metadata(ReTweakStrings.RETWEAK_MOD)
    public static ModMetadata modMetadata;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModHelper.injectMetadata(ReTweakMod.modMetadata, ReTweakResources.RETWEAK_METADATA);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

    @SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if (event.modID.equals(ReTweakStrings.RETWEAK_MOD) && event.configID.equals(ReTweakStrings.RETWEAK_GUI_CONFIG_ID)) {
            ReTweakResources.RETWEAK_LOGGER.info("Dope!");
        }
    }

}
