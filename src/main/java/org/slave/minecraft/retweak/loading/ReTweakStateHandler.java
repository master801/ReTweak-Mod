package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 4/10/2016 at 9:43 PM.
 *
 * @author Master801
 */
public final class ReTweakStateHandler {

    private ReTweakStateHandler() {
        throw new IllegalStateException();
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoadControllerTransformer}
     *
     * @param currentState Such a fragile existence...
     */
    public static void step(LoadController loadController, LoaderState currentState, LoaderState wantedState) {
        if (loadController == null || currentState == null || wantedState == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "An error occurred while stepping [all three variables for {} are null]! The ASM hacks may have failed?",
                    ReTweakStateHandler.class.getName()
            );
            return;
        }
        switch(currentState) {
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
            case SERVER_STARTING:
            case SERVER_STARTED:
            case SERVER_STOPPING:
            case SERVER_STOPPED:
                //Unsupported -- NOOP
                break;
            case LOADING:
            case AVAILABLE:
                //NOOP
                break;
            case NOINIT:
            case ERRORED:
                //NOOP
                break;
        }
    }

    private static void constructing(LoadController loadController) {
        for(GameVersion gameVersion : GameVersion.values()) {
            for(ReTweakModCandidate reTweakModCandidate : ReTweakLoader.INSTANCE.getReTweakModDiscoverer().getModCandidates(gameVersion)) {
                ReTweakClassLoader.getInstance().addFile(reTweakModCandidate.getFile());

                for(String modClassName : reTweakModCandidate.getModClasses()) {
                    modClassName = modClassName.replace(
                            '/',
                            '.'
                    ).substring(
                            0,
                            modClassName.indexOf(".class")
                    );

                    try {
                        Class.forName(
                                modClassName,
                                true,
                                ReTweakClassLoader.getInstance()
                        );
                    } catch(ClassNotFoundException e) {
                        ReTweakResources.RETWEAK_LOGGER.error(
                                "Failed to load a mod class!",
                                e
                        );
                    }
                }
            }
        }
    }

}
