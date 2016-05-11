package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master on 4/26/2016 at 3:26 PM.
 *
 * @author Master
 */
public final class ReTweakModController {

    private ReTweakModController() {
        throw new IllegalStateException();
    }

    static void preInitialization(FMLPreInitializationEvent fmlPreInitializationEvent) {
        Object _INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting pre-initialization state for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling pre-initialization state",
                            reTweakModContainer.getModid()
                    );
                    continue;
                }
                reTweakModContainer.callState(fmlPreInitializationEvent);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending pre-initialization state for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void initialization(FMLInitializationEvent fmlInitializationEvent) {
        Object _INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting initialization state for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling initialization state",
                            reTweakModContainer.getModid()
                    );
                    continue;
                }
                reTweakModContainer.callState(fmlInitializationEvent);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending initialization state for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void postInitialization(FMLPostInitializationEvent fmlPostInitializationEvent) {
        Object _INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting post-initialization for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling post-initialization",
                            reTweakModContainer.getModid()
                    );
                    continue;
                }
                reTweakModContainer.callState(fmlPostInitializationEvent);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending post-initialization for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

}
