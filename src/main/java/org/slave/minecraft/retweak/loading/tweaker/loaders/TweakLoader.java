package org.slave.minecraft.retweak.loading.tweaker.loaders;

import org.slave.minecraft.retweak.loading.SupportedGameVersion;
import org.slave.minecraft.retweak.loading.tweaker.tweaks.Tweak;

/**
 * Created by Master801 on 4/11/2016 at 6:19 AM.
 *
 * @author Master801
 */
public interface TweakLoader {

    SupportedGameVersion getGameVersion();

    Iterable<Tweak> getTweaks();

}
