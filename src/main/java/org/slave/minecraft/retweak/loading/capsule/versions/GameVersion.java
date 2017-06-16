package org.slave.minecraft.retweak.loading.capsule.versions;

import cpw.mods.fml.common.Mod;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.util.Bulk;
import org.slave.minecraft.retweak.loading.capsule.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;

import java.util.Map.Entry;

/**
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public enum GameVersion {

    V_1_4_7(
        "1.4.7",
        new ClassHolderWrapper_1_4_7(),
        Boolean.TRUE,
        new Bulk<>(
            Type.ANNOTATION,
            Mod.class
        )
    ),

    V_1_5_2(
        "1.5.2",
        new ClassHolderWrapper_1_5_2(),
        Boolean.TRUE,
        new Bulk<>(
            Type.ANNOTATION,
            Mod.class
        )
    ),

    V_1_6_4(
        "1.6.4",
        new ClassHolderWrapper_1_6_4(),
        Boolean.FALSE,
        new Bulk<>(
            Type.ANNOTATION,
            Mod.class
        )
    );

    private static final String INTERPRETER_PACKAGE_PREFIX = "org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.";

    private final String version;
    private final ClassHolderWrapper classHolderWrapper;
    private final String interpreterPackagePrefix;
    private final boolean hasResources;
    private final Entry<Type, ?> modType;

    GameVersion(final String version, final ClassHolderWrapper classHolderWrapper, final boolean hasResources, final Bulk<Type, ?> modType) {
        this.version = version;
        this.classHolderWrapper = classHolderWrapper;
        interpreterPackagePrefix = GameVersion.INTERPRETER_PACKAGE_PREFIX + "_" + version.replace('.', '_') + ".";
        this.hasResources = hasResources;
        this.modType = modType;
    }


    public String getVersion() {
        return version;
    }

    /*
    public List<String> getClasses() {
        return classHolder.getClasses();
    }

    public List<Class<?>> getOverrideClassList() {
        return classHolder.getOverrideClassList();
    }
    */

    public Class<?> getOverrideClass(final String className) {
        for(ClassEntry overrideClass : classHolderWrapper.getClassHolder().getOverrideClasses()) {
            if (overrideClass.getFrom().equals(className.replace('/', '.'))) {
                return overrideClass.getTo();
            }
        }
        /*
        for(Class<?> interpreterClass : getOverrideClassList()) {
            String name;
            name = interpreterClass.getCanonicalName();
            name = name.substring(
                interpreterPackagePrefix.length(),
                name.length()
            );
            if (name.equals(className.replace('/', '.'))) return interpreterClass;
        }
        */
        return null;
    }

    public String getInterpreterPackagePrefix() {
        return interpreterPackagePrefix;
    }

    public boolean hasResources() {
        return hasResources;
    }

    public Entry<Type, ?> getModType() {
        return modType;
    }

    public static GameVersion getFromVersion(final String version) {
        if (StringHelper.isNullOrEmpty(version)) return null;
        for(GameVersion gameVersion : GameVersion.values()) {
            if (gameVersion.getVersion().equals(version)) return gameVersion;
        }
        return null;
    }

}
