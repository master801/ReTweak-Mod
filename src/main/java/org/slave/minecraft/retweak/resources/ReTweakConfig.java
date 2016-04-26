package org.slave.minecraft.retweak.resources;

import com.google.common.base.Joiner;
import org.slave.lib.api.CommentProperties;
import org.slave.lib.helpers.ConfigHelper;
import org.slave.lib.helpers.FileHelper;
import org.slave.minecraft.retweak.loading.CompilationMode;

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

    private CompilationMode compilationMode;

    private ReTweakConfig() {
        config.put(
                ReTweakStrings.RETWEAK_CONFIG_KEY_COMPILATION_MODE,
                CompilationMode.JIT.name(),
                Joiner.on(", ").join(CompilationMode.values())
        );
    }

    public void update(final boolean load) throws IOException {
        if (load && ReTweakConfig.CONFIG_FILE.exists()) {
            FileInputStream fileInputStream = new FileInputStream(ReTweakConfig.CONFIG_FILE);
            config.read(fileInputStream);
            fileInputStream.close();
        }

        //TODO
        compilationMode = CompilationMode.valueOf(
                (String)config.get(ReTweakStrings.RETWEAK_CONFIG_KEY_COMPILATION_MODE)
        );

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

    public CompilationMode getCompilationMode() {
        return compilationMode;
    }

}
