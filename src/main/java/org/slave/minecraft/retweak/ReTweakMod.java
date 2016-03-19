package org.slave.minecraft.retweak;

import corelibrary.api.network.proxy.Proxy;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 3/18/2016 at 9:09 PM.
 *
 * @author Master801
 */
@Mod(modid = ReTweakResources.RETWEAK_MOD, name = ReTweakResources.RETWEAK_MOD, version = ReTweakResources.RETWEAK_VERSION)
public final class ReTweakMod {

    @SidedProxy(clientSide = ReTweakResources.RETWEAK_PROXY_CLIENT, serverSide = ReTweakResources.RETWEAK_PROXY_SERVER, modId = ReTweakResources.RETWEAK_MOD)
    private Proxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    }

}
