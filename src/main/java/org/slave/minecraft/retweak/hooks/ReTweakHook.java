package org.slave.minecraft.retweak.hooks;

import lombok.experimental.UtilityClass;
import org.slave.minecraft.retweak.ReTweak;

import java.io.IOException;

/**
 * Created by Master on 7/26/2018 at 2:45 PM.
 *
 * @author Master
 */
@UtilityClass
public final class ReTweakHook {

    /**
     * {@link cpw.mods.fml.common.Loader#loadingComplete()}
     */
    public static void onLoadingComplete() {
        try {
            ReTweak.getConfiguration().read();
        } catch (IOException e) {
            ReTweak.LOGGER_RETWEAK.warn("Failed to read the configuration file!", e);
        }
    }

}
