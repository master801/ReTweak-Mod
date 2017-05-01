package org.slave.minecraft.retweak.util;

import org.slave.minecraft.library.resources.LibraryResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Master801 on 3/18/2016 at 9:11 PM.
 *
 * @author Master801
 */
public final class ReTweakResources {

    public static final File RETWEAK_DIRECTORY = new File(
            "retweak"
    );
    public static final File RETWEAK_MODS_DIRECTORY = new File(
            ReTweakResources.RETWEAK_DIRECTORY,
            "mods"
    );
    public static final File RETWEAK_PLAY_DIRECTORY = new File(
            ReTweakResources.RETWEAK_DIRECTORY,
            "play"
    );
    public static final File RETWEAK_CONFIG_DIRECTORY = new File(
            ReTweakResources.RETWEAK_DIRECTORY,
            "config"
    );
    public static final File RETWEAK_MAIN_CONFIG_DIRECTORY = new File(
            LibraryResources.CONFIG_DIRECTORY,
            "retweak"
    );

    public static final Logger RETWEAK_LOGGER = LoggerFactory.getLogger("ReTweak-Mod");

    public static final Boolean DEBUG = Boolean.valueOf(
            System.getProperty(
                    "org.slave.minecraft.retweak.debug",
                    Boolean.FALSE.toString()
            )
    );

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerGuiMainMenu}
     */
    public static final List<String> RETWEAK_SPLASH_TEXT = Arrays.asList(
            "Don't count on it!",
            "Tweak all you want, you'll crash anyway!",
            "Bugs? Who said games didn't have any bugs?",
            "Crash-crash, go-away, I don't like you, so please don't stay.");

    private ReTweakResources() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        throw new IllegalStateException();
    }

}