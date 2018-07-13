package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.MigrationClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.List;
import java.util.Map;

/**
 * Created by Master on 7/12/2018 at 8:48 PM.
 *
 * @author Master
 */
public abstract class TweakClass {

    @Getter
    private final GameVersion gameVersion;

    @Getter
    private final Map<String, MigrationClass> classesToMigrate = Maps.newHashMap();

    @Getter
    private final List<String> classesToTweak = Lists.newArrayList();

    protected TweakClass(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    protected final void addMigrationClass(final MigrationClass migrationClass) {
        if (migrationClass == null || classesToMigrate.containsValue(migrationClass)) return;
        classesToMigrate.put(migrationClass.getFrom().replace('.', '/'), migrationClass);
    }

}
