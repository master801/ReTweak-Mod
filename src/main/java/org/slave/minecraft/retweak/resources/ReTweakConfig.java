package org.slave.minecraft.retweak.resources;

import org.slave.lib.api.CommentProperties;
import org.slave.lib.helpers.ConfigHelper;
import org.slave.lib.helpers.FileHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Master on 4/24/2016 at 11:32 PM.
 *
 * @author Master
 */
public final class ReTweakConfig {

    public static final ReTweakConfig INSTANCE = new ReTweakConfig();

    private static final File CONFIG_FILE = new File(
            ReTweakResources.RETWEAK_CONFIG_DIRECTORY,
            "ReTweak.cfg"
    );
    private static final String COMMENT = ReTweakStrings.RETWEAK_MOD;

    private final CommentProperties config = ConfigHelper.createCommentProperties();

    private ReTweakConfig() {
    }

    public void update(final boolean load) throws IOException {
        if (load && ReTweakConfig.CONFIG_FILE.exists()) {
            FileInputStream fileInputStream = new FileInputStream(ReTweakConfig.CONFIG_FILE);
            config.read(fileInputStream);
            fileInputStream.close();
        }

        //TODO

        if (!load || !ReTweakConfig.CONFIG_FILE.exists()) {
            if (!ReTweakConfig.CONFIG_FILE.getParentFile().exists()) FileHelper.createDirectory(ReTweakConfig.CONFIG_FILE.getParentFile());
            FileOutputStream fileOutputStream = new FileOutputStream(ReTweakConfig.CONFIG_FILE);
            config.write(
                    fileOutputStream,
                    ReTweakConfig.COMMENT
            );
            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }

}
