package org.slave.minecraft.retweak.loading.tweaker;

import org.slave.minecraft.retweak.loading.SupportedGameVersion;
import org.slave.minecraft.retweak.loading.tweaker.v_1_4_7.V_1_4_7_TweakLoader;

import java.util.HashMap;

/**
 * Created by Master801 on 4/11/2016 at 8:54 PM.
 *
 * @author Master801
 */
public final class ReTweakTweakLoaderIndexer {

    public static final ReTweakTweakLoaderIndexer INSTANCE = new ReTweakTweakLoaderIndexer();

    private final HashMap<SupportedGameVersion, TweakLoader> tweakLoaders = new HashMap<>();

    private ReTweakTweakLoaderIndexer() {
        tweakLoaders.put(
                SupportedGameVersion.V_1_4_7,
                new V_1_4_7_TweakLoader()
        );
    }

    public TweakLoader getTweakLoader(SupportedGameVersion supportedGameVersion) {
        if (tweakLoaders.containsKey(supportedGameVersion)) return tweakLoaders.get(supportedGameVersion);
        return null;
    }

}
