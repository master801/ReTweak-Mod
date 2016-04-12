package org.slave.minecraft.retweak.loading.tweaks;

import org.slave.minecraft.retweak.loading.SupportedGameVersion;

/**
 * Created by Master801 on 4/11/2016 at 6:19 AM.
 *
 * @author Master801
 */
public interface TweakLoader {

    SupportedGameVersion getGameVersion();

    void loadTweak();

}
