package org.slave.minecraft.retweak.loading.tweaker.v_1_4_7;

import org.slave.minecraft.retweak.loading.SupportedGameVersion;
import org.slave.minecraft.retweak.loading.tweaker.DeobfuscationTweaker;
import org.slave.minecraft.retweak.loading.tweaker.TweakLoader;
import org.slave.minecraft.retweak.loading.tweaker.Tweak;

import java.util.ArrayList;

/**
 * Created by Master801 on 4/11/2016 at 8:50 PM.
 *
 * @author Master801
 */
public final class V_1_4_7_TweakLoader implements TweakLoader {

    private final ArrayList<Tweak> tweaks;

    public V_1_4_7_TweakLoader() {
        tweaks = new ArrayList<>();
        tweaks.add(new DeobfuscationTweaker(SupportedGameVersion.V_1_4_7));
    }

    @Override
    public SupportedGameVersion getGameVersion() {
        return SupportedGameVersion.V_1_4_7;
    }

    @Override
    public Iterable<Tweak> getTweaks() {
        return tweaks;
    }

}
