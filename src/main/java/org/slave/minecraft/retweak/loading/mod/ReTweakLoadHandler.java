package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 5/30/2017 at 10:18 AM.
 *
 * @author Master
 */
@UtilityClass
public final class ReTweakLoadHandler {

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController#transformDistributeMessage_Event(org.objectweb.asm.tree.MethodNode, boolean)}
     *
     * {@link cpw.mods.fml.common.LoadController#distributeStateMessage(java.lang.Class)}
     *
     * @param loadController Instance of {@link cpw.mods.fml.common.LoadController}
     * @param stateClass State to distribute
     */
    public static void distributeStateMessage(final LoadController loadController, final Class<?> stateClass) {
        if (stateClass == null) return;
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakLoader.INSTANCE.getReTweakLoadController(
                gameVersion
            ).distributeStateMessage(
                stateClass
            );
        }
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController#transformDistributeStateMessage(org.objectweb.asm.tree.MethodNode, boolean)}
     *
     * {@link cpw.mods.fml.common.LoadController#distributeStateMessage(cpw.mods.fml.common.LoaderState, java.lang.Object...)}
     *
     * @param loadController Instance of {@link cpw.mods.fml.common.LoadController}
     * @param loaderState {@link cpw.mods.fml.common.LoaderState} to distribute
     * @param eventData Data of {@link cpw.mods.fml.common.LoaderState}
     */
    public static void distributeStateMessage(final LoadController loadController, @NonNull final LoaderState loaderState, final Object... eventData) {
        if (loaderState == null) return;
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakLoader.INSTANCE.getReTweakLoadController(
                gameVersion
            ).distributeStateMessage(
                loaderState,
                eventData
            );
        }
    }

}
