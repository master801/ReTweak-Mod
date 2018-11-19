package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;

import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationMethod.MigrationMethod;
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

    private final Map<String, MigrationClass> classesToMigrate = Maps.newHashMap();

    @Getter
    private final List<String> classesToTweak = Lists.newArrayList();

    protected TweakClass(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    protected final void addMigrationClass(final MigrationClass migrationClass) {
        if (migrationClass == null || classesToMigrate.containsValue(migrationClass)) return;
        classesToMigrate.put(migrationClass.getFrom().replace('/', '.'), migrationClass);
    }

    public final boolean hasMigrationClass(final String name) {
        return classesToMigrate.containsKey(name.replace('/', '.'));
    }

    public final MigrationClass getMigrationClass(final String name) {
        return classesToMigrate.get(name.replace('/', '.'));
    }

    public final MigrationMethod getMigrationMethod(final String name, final String desc) {
        for(MigrationClass migrationClass : classesToMigrate.values()) {
            MigrationMethod methodEntry = migrationClass.getMethod(name, desc);
            if (methodEntry != null) return methodEntry;
        }
        return null;
    }

    public final MigrationField getMigrationField(final String name, final String desc) {
        for(MigrationClass migrationClass : classesToMigrate.values()) {
            MigrationField fieldEntry = migrationClass.getField(name, desc);
            if (fieldEntry != null) return fieldEntry;
        }
        return null;
    }

}
