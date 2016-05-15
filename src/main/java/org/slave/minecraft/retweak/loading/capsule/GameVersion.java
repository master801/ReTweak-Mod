package org.slave.minecraft.retweak.loading.capsule;

import org.slave.lib.helpers.StringHelper;

/**
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public enum GameVersion {

    V_1_4_7(
            "1.4.7",
            true
    ),

    V_1_5_2(
            "1.5.2",
            true
    );

    private final String version;
    private final boolean hasResources;

    GameVersion(final String string, final boolean hasResources) {
        this.version = string;
        this.hasResources = hasResources;
    }

    public String getVersion() {
        return version;
    }

    public boolean hasResources() {
        return hasResources;
    }

    public static GameVersion getFromVersion(String version) {
        if (StringHelper.isNullOrEmpty(version)) return null;
        for(GameVersion gameVersion : GameVersion.values()) {
            if (gameVersion.getVersion().equals(version)) return gameVersion;
        }
        return null;
    }

}
