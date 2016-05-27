package org.slave.minecraft.retweak.resources;

import com.google.common.base.Joiner;
import org.slave.lib.api.CommentProperties;
import org.slave.lib.helpers.ConfigHelper;
import org.slave.lib.helpers.FileHelper;
import org.slave.minecraft.retweak.loading.capsule.CompilationMode;

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
            ReTweakResources.RETWEAK_MAIN_CONFIG_DIRECTORY,
            "ReTweak.cfg"
    );
    private static final String COMMENT = ReTweakStrings.RETWEAK_MOD;

    private final CommentProperties config = ConfigHelper.createCommentProperties();

    private CompilationMode compilationMode = null;

    private ReTweakConfig() {
        config.put(
                ReTweakStrings.RETWEAK_CONFIG_KEY_COMPILATION_MODE,
                ReTweakResources.DEBUG ? CompilationMode.JIT.name() : CompilationMode.INTERPRETER.name(),
                "[" + Joiner.on(", ").join(CompilationMode.values()) + "]"
        );
    }

    public void update(final boolean load) throws IOException {
        if (load && ReTweakConfig.CONFIG_FILE.exists()) {
            FileInputStream fileInputStream = new FileInputStream(ReTweakConfig.CONFIG_FILE);
            config.read(fileInputStream);
            fileInputStream.close();
        }

        //TODO
        if (config.hasKey(ReTweakStrings.RETWEAK_CONFIG_KEY_COMPILATION_MODE)) {
            try {
                compilationMode = CompilationMode.valueOf((String)config.get(ReTweakStrings.RETWEAK_CONFIG_KEY_COMPILATION_MODE));
            } catch(IllegalArgumentException e) {
                compilationMode = CompilationMode.INTERPRETER;
            }
        } else {
            compilationMode = CompilationMode.INTERPRETER;
        }

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

    public void setCompilationMode(CompilationMode compilationMode) {
        if (compilationMode == null) return;
        this.compilationMode = compilationMode;
    }

}
