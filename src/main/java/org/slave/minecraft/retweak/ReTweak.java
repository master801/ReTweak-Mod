package org.slave.minecraft.retweak;

import org.slave.lib.api.Configuration;
import org.slave.lib.api.Configuration.Category;
import org.slave.lib.api.Configuration.Item;
import org.slave.minecraft.retweak.load.util.GameVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.function.Function;

import cpw.mods.fml.common.Loader;
import lombok.experimental.UtilityClass;

/**
 * Created by master on 2/25/18 at 9:25 PM
 *
 * @author master
 */
@UtilityClass
public final class ReTweak {

    public static final String INFO_MOD_SYSTEM = "ReTweak";

    public static final Logger LOGGER_RETWEAK = LoggerFactory.getLogger("ReTweak");

    public static final File FILE_DIRECTORY_RETWEAK = new File("retweak");
    public static final File FILE_DIRECTORY_RETWEAK_ASM = new File(ReTweak.FILE_DIRECTORY_RETWEAK, "asm");
    public static final File FILE_DIRECTORY_RETWEAK_MODS = new File(ReTweak.FILE_DIRECTORY_RETWEAK, "mods");
    public static final File FILE_DIRECTORY_RETWEAK_CONFIG = new File(ReTweak.FILE_DIRECTORY_RETWEAK, "config");
    public static final File FILE_DIRECTORY_RETWEAK_MAPPINGS = new File(ReTweak.FILE_DIRECTORY_RETWEAK, "mappings");

    public static final boolean DEBUG = Boolean.parseBoolean(
            System.getProperty(
                    "org.slave.minecraft.retweak.debug",
                    Boolean.FALSE.toString()
            ).toLowerCase()
    );

    private static File fileReTweakConfig = null;
    private static Configuration configuration;

    public static Configuration getConfiguration() {
        if (ReTweak.configuration == null) {
            ReTweak.configuration = Configuration.Builder.getInstance()
                    .setName("ReTweak")
                    .setFile(ReTweak.getFileReTweakConfig())
                    .setLogger(ReTweak.LOGGER_RETWEAK)
                    .setCreateDefault(
                            new Function<Configuration, Configuration>() {

                                @Override
                                public Configuration apply(final Configuration configuration) {
                                    Category categoryGameVersion = new Category("Game Versions");
                                    Category categoryGameVersionEnable = new Category("Enable");
                                    for(GameVersion gameVersion : GameVersion.values()) {
                                        categoryGameVersionEnable.add(new Item<>(gameVersion.getVersion(), true));
                                    }
                                    categoryGameVersion.addSubCategory(categoryGameVersionEnable);

                                    configuration.addCategory(categoryGameVersion);
                                    return configuration;
                                }

                            }
                    )
                    .build();
            ReTweak.configuration.getSettings().setPrettyPrint();
        }
        return ReTweak.configuration;
    }

    private static File getFileReTweakConfig() {
        if (ReTweak.fileReTweakConfig == null) ReTweak.fileReTweakConfig = new File(Loader.instance().getConfigDir(), "ReTweak.cfg");
        return ReTweak.fileReTweakConfig;
    }

}
