package org.slave.minecraft.retweak.loading;

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

    static void preInitialization() {
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting pre-initialization for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling pre-initialization",
                            reTweakModContainer.getModid()
                    );
                    continue;
                }
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending pre-initialization for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void initialization() {
        //TODO
    }

    static void postInitialization() {
        //TODO
    }

}
