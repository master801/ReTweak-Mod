package org.slave.minecraft.retweak.loading.capsule.versions;

import org.slave.lib.helpers.StringHelper;
import org.slave.lib.util.Bulk;
import org.slave.minecraft.retweak.loading.capsule.Type;

import java.util.List;

/**
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public enum GameVersion {

    V_1_4_7(
            "1.4.7",
            ClassHolder_1_4_7.CLASS_HOLDER_1_4_7,
            true,
            new Bulk<>(
                    Type.ANNOTATION,
                    "L" + "cpw/mods/fml/common/Mod" + ";"
            )
    ),

    V_1_5_2(
            "1.5.2",
            ClassHolder_1_5_2.CLASS_HOLDER_1_5_2,
            true,
            new Bulk<>(
                    Type.ANNOTATION,
                    "L" + "cpw/mods/fml/common/Mod" + ";"
            )
    ),

    V_1_6_4(
            "1.6.4",
            ClassHolder_1_6_4.CLASS_HOLDER_1_6_4,
            false,
            new Bulk<>(
                    Type.ANNOTATION,
                    "L" + "cpw/mods/fml/common/Mod" + ";"
            )
    );

    private static final String INTERPRETER_PACKAGE_PREFIX = "org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.";

    private final String version;
    private final ClassHolder classHolder;
    private final String interpreterPackagePrefix;
    private final boolean hasResources;
    private final Bulk<Type, String> modType;

    GameVersion(final String version, final ClassHolder classHolder, final boolean hasResources, final Bulk<Type, String> modType) {
        this.version = version;
        this.classHolder = classHolder;
        interpreterPackagePrefix = GameVersion.INTERPRETER_PACKAGE_PREFIX + "_" + version.replace('.', '_') + ".";
        this.hasResources = hasResources;
        this.modType = modType;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getClasses() {
        return classHolder.getClasses();
    }

    public List<Class<?>> getOverrideClassList() {
        return classHolder.getInterpreterClasses();
    }

    public Class<?> getOverrideClass(final String className) {
        for(Class<?> interpreterClass : getOverrideClassList()) {
            String name;
            name = interpreterClass.getCanonicalName();
            name = name.substring(
                    interpreterPackagePrefix.length(),
                    name.length()
            );
            if (name.equals(className)) return interpreterClass;
        }
        return null;
    }

    public String getInterpreterPackagePrefix() {
        return interpreterPackagePrefix;
    }

    public boolean hasResources() {
        return hasResources;
    }

    public Bulk<Type, String> getModType() {
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
