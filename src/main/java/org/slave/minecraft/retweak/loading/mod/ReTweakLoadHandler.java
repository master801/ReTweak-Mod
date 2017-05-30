package org.slave.minecraft.retweak.loading.mod;

import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 5/30/2017 at 10:18 AM.
 *
 * @author Master
 */
public final class ReTweakLoadHandler {

    private ReTweakLoadHandler() {
        final Object _INTERNAL_USAGE_ONLY_ = null;
        throw new IllegalStateException();
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController#transformDistributeMessage(org.objectweb.asm.tree.MethodNode, boolean)}
     */
    public static void distributeStateMessage(final Class<?> stateClass) {
        if (stateClass == null) return;
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakLoader.INSTANCE.getReTweakLoadController(
                gameVersion
            ).distributeStateMessage(
                stateClass
            );
        }
    }

}
