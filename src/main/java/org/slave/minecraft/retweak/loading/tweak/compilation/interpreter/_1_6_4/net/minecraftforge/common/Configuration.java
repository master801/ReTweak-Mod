package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.net.minecraftforge.common;

import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

import java.io.File;

/**
 * Created by Master on 4/20/2017 at 8:32 AM.
 *
 * @author Master
 */
@Deobfuscated(
    name = "Configuration",
    _package = @Package("net.minecraftforge.common.config")
)
public class Configuration extends net.minecraftforge.common.config.Configuration {

    public Configuration() {
        super();
    }

    public Configuration(final File file) {
        super(file);
    }

    public Configuration(final File file, final String configVersion) {
        super(
                file,
                configVersion
        );
    }

    public Configuration(final File file, final String configVersion, final boolean caseSensitiveCustomCategories) {
        super(
                file,
                configVersion,
                caseSensitiveCustomCategories
        );
    }

    public Configuration(final File file, final boolean caseSensitiveCustomCategories) {
        super(
                file,
                caseSensitiveCustomCategories
        );
    }

}
