package org.slave.minecraft.retweak.load.asm.tweak.migrate.net.minecraftforge.common;

import net.minecraftforge.common.config.Property;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

import java.io.File;

/**
 * Created by Master on 7/12/2018 at 9:24 PM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package("net.minecraftforge.common"),
        name = "Configuration"
)
@Deobfuscated(
        _package = @Package("net.minecraftforge.common.config"),
        name = "Configuration"
)
public class Configuration extends net.minecraftforge.common.config.Configuration {

    private static final String CATEGORY_BLOCKS = "blocks", CATEGORY_ITEMS = "items";

    public Configuration() {
        super();
    }

    public Configuration(final File file) {
        super(file);
    }

    public Configuration(final File file, final String configVersion) {
        super(file, configVersion);
    }

    public Configuration(final File file, final String configVersion, final boolean caseSensitiveCustomCategories) {
        super(file, configVersion, caseSensitiveCustomCategories);
    }

    public Configuration(final File file, final boolean caseSensitiveCustomCategories) {
        super(file, caseSensitiveCustomCategories);
    }

    public Property getBlock(final String category, final String key, final int defaultValue) {
        return super.get(category, key, defaultValue);
    }

    public Property getItem(final String category, final String key, final int defaultValue) {
        return super.get(category, key, defaultValue);
    }

    public Property getBlock(final String key, final int defaultValue) {
        return super.get(Configuration.CATEGORY_BLOCKS, key, defaultValue);
    }

    public Property getItem(final String key, final int defaultValue) {
        return super.get(Configuration.CATEGORY_ITEMS, key, defaultValue);
    }

}
