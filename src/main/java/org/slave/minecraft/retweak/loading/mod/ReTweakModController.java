package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Master on 4/26/2016 at 3:26 PM.
 *
 * @author Master
 */
public final class ReTweakModController {

    private ReTweakModController() {
        throw new IllegalStateException();
    }

    static void preInitialization() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting pre-initialization state for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"pre-initialization\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLPreInitializationEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending pre-initialization state for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void initialization() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state initialization for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"initialization\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLInitializationEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state initialization state for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void postInitialization() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state post-initialization for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"post-initialization\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLPostInitializationEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state post-initialization for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void serverAboutToStart() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state \"server about to start\" for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"server about to start\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLServerAboutToStartEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state \"server about to start\" for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void serverStarting() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state \"server starting\" for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"server starting\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLServerStartingEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state \"server starting\" for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void serverStarted() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state \"server started\" for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"server started\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLServerStartedEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state \"server started\" for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void serverStopping() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state \"server stopping\" for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"server stopping\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLServerStoppingEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state \"server stopping\" for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

    static void serverStopped() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Starting state \"server stopped\" for game version {}",
                    gameVersion.getVersion()
            );

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion)) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not calling state \"server stopped\"",
                            reTweakModContainer.getModId()
                    );
                    continue;
                }
                reTweakModContainer.callState(FMLServerStoppedEvent.class);
            }

            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Ending state \"server stopped\" for game version {}",
                    gameVersion.getVersion()
            );
        }
    }

}
