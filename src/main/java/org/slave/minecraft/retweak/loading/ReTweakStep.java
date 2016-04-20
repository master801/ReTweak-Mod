package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 4/10/2016 at 9:43 PM.
 *
 * @author Master801
 */
public final class ReTweakStep {

    private ReTweakStep() {
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
                    ReTweakStep.class.getName()
            );
            return;
        }
        switch(currentState) {
            case CONSTRUCTING:
                ReTweakStep.constructing(loadController);
                break;
            case PREINITIALIZATION:
                //TODO Send pre-init message to all re-tweak mods
                break;
            case INITIALIZATION:
                //TODO Send init message to all re-tweak mods
                break;
            case POSTINITIALIZATION:
                //TODO Send post-init message to all re-tweak mods
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
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            for(ReTweakModCandidate reTweakModCandidate : ReTweakLoader.INSTANCE.getModCandidates(supportedGameVersion)) {
                //TODO
            }
        }
    }

}
