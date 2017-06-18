package org.slave.minecraft.retweak.loading.capsule.versions;

import cpw.mods.fml.common.Mod;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.util.Bulk;
import org.slave.minecraft.retweak.loading.capsule.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;

import java.util.Map.Entry;

/**
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public enum GameVersion {

    V_1_4_7(
        "1.4.7",
        ClassHolderWrapper_1_4_7.INSTANCE,
        Boolean.TRUE,
        new Bulk<>(
            Type.ANNOTATION,
            Mod.class
        )
    ),

    V_1_5_2(
        "1.5.2",
        ClassHolderWrapper_1_5_2.INSTANCE,
        Boolean.TRUE,
        new Bulk<>(
            Type.ANNOTATION,
            Mod.class
        )
    ),

    V_1_6_4(
        "1.6.4",
        ClassHolderWrapper_1_6_4.INSTANCE,
        Boolean.FALSE,
        new Bulk<>(
            Type.ANNOTATION,
            Mod.class
        )
    );

    private final String version;
    private final ClassHolderWrapper classHolderWrapper;
    private final boolean hasResources;
    private final Entry<Type, ?> modType;

    GameVersion(final String version, final ClassHolderWrapper classHolderWrapper, final boolean hasResources, final Bulk<Type, ?> modType) {
        this.version = version;
        this.classHolderWrapper = classHolderWrapper;
        this.hasResources = hasResources;
        this.modType = modType;
    }


    public String getVersion() {
        return version;
    }

    public ClassInfo getClassInfo(final String className) {
        if (className == null) return null;
        for(ClassInfo classInfo : classHolderWrapper.getClassHolder().getOverrideClasses()) {
            if (classInfo.getClassEntry().getFrom().equals(className.replace('/', '.'))) {
                return classInfo;
            }
        }
        return null;
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
