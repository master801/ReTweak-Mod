package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Master801 on 4/10/2016 at 9:43 PM.
 *
 * @author Master801
 */
public final class ReTweakStateHandler {

    private ReTweakStateHandler() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        throw new IllegalStateException();
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController}
     * {@link cpw.mods.fml.common.LoadController#transition(cpw.mods.fml.common.LoaderState, boolean)}
     *
     * @param currentState Such a fragile existence...
     */
    public static void step(final LoadController loadController, final LoaderState currentState, final LoaderState wantedState) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        if (loadController == null || currentState == null || wantedState == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "An error occurred while stepping (variables for {} are null)! The ASM hacks may have failed?",
                    ReTweakStateHandler.class.getName()
            );
            return;
        }
        switch(currentState) {
            case LOADING:
                ReTweakLoader.INSTANCE.loadMods();
                break;
            case CONSTRUCTING:
                ReTweakStateHandler.constructing(loadController);
                break;
            case PREINITIALIZATION:
                ReTweakModController.preInitialization();
                break;
            case INITIALIZATION:
                ReTweakModController.initialization();
                break;
            case POSTINITIALIZATION:
                ReTweakModController.postInitialization();
                break;
            case SERVER_ABOUT_TO_START:
                ReTweakModController.serverAboutToStart();
                break;
            case SERVER_STARTING:
                ReTweakModController.serverStarting();
                break;
            case SERVER_STARTED:
                ReTweakModController.serverStarted();
                break;
            case SERVER_STOPPING:
                ReTweakModController.serverStopping();
                break;
            case SERVER_STOPPED:
                ReTweakModController.serverStopped();
                break;
            case AVAILABLE:
                //NOOP
                break;
            case NOINIT:
            case ERRORED:
                //NOOP
                break;
        }
    }

    private static void constructing(final LoadController loadController) {
        //TODO
    }

}