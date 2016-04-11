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
    }

}
