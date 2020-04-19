package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.Migrate;

/**
 * Created by Master on 3/20/2020 at 1:18 PM
 *
 * @author Master
 */
public final class BuilderSuperMigration {

    private static BuilderSuperMigration instance;

    private String from;
    private Class<? extends Migrate> classTo;

    public BuilderSuperMigration from(final Class<?> clazz) {
        if (clazz == null) return this;
        return from(clazz.getName().replace('.', '/'));
    }

    public BuilderSuperMigration from(final String from) {
        if (from == null) return this;
        this.from = from;
        return this;
    }

    public BuilderSuperMigration to(final Class<? extends Migrate> classMigrate) {
        this.classTo = classMigrate;
        return this;
    }

    public SuperMigration build() {
        if (from == null || classTo == null) return null;
        return new SuperMigration(from, classTo);
    }

    public static BuilderSuperMigration instance() {
        if (BuilderSuperMigration.instance == null) BuilderSuperMigration.instance = new BuilderSuperMigration();
        return BuilderSuperMigration.instance;
    }

    @RequiredArgsConstructor
    public static final class SuperMigration {

        @Getter
        private final String from;

        @Getter
        private final Class<? extends Migrate> classTo;

    }

}
