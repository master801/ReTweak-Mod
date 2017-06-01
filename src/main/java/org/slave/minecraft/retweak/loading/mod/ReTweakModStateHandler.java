package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.ArrayListMultimap;
import cpw.mods.fml.common.LoaderState;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

/**
 * Created by Master on 4/26/2016 at 3:26 PM.
 *
 * @author Master
 */
public final class ReTweakModStateHandler {

    private ReTweakModStateHandler() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        throw new IllegalStateException();
    }

    static void preInitialization() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;

        ReTweakModStateHandler.callState(
            LoaderState.PREINITIALIZATION,

            ReTweakLoader.INSTANCE.getReTweakModDiscoverer(GameVersion.V_1_4_7).getASMTable(),
            ReTweakResources.RETWEAK_CONFIG_DIRECTORY
        );
    }

    static void initialization() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.INITIALIZATION
        );
    }

    static void postInitialization() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.POSTINITIALIZATION
        );
    }

    static void serverAboutToStart() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.SERVER_ABOUT_TO_START
        );
    }

    static void serverStarting() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.SERVER_STARTING
        );
    }

    static void serverStarted() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.SERVER_STARTED
        );
    }

    static void serverStopping() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.SERVER_STOPPING
        );
    }

    static void serverStopped() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        ReTweakModStateHandler.callState(
            LoaderState.SERVER_STOPPED
        );
    }

    private static void callState(final LoaderState loaderState, @SuppressWarnings("unused") final Object... objects) {
        @SuppressWarnings("unused") final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;

        for(GameVersion gameVersion : GameVersion.values()) {
            Object[] newObjects = new Object[] {
                ReTweakClassLoader.getReTweakClassLoader(gameVersion),//Class loader
                ReTweakLoader.INSTANCE.getReTweakModDiscoverer(//ASM Table
                    gameVersion
                ).getASMTable(),
                ArrayListMultimap.create()//Create empty map for reversed dependencies -- TODO?
            };

            ReTweakLoader.INSTANCE.getReTweakLoadController(
                gameVersion
            ).distributeStateMessage(
                loaderState,
                newObjects
            );

            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Called state \"{}\" for game version \"{}\"",
                    loaderState.name(),
                    gameVersion.getVersion()
                );
            }
        }
    }

}
