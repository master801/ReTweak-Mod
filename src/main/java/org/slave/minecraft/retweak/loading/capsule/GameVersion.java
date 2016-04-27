package org.slave.minecraft.retweak.loading.capsule;

import org.slave.lib.helpers.StringHelper;

/**
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public enum GameVersion {

    V_1_4_7("1.4.7"),

    V_1_5_2("1.5.2");

    private final String version;

    GameVersion(final String string) {
        this.version = string;
    }

    public String getVersion() {
        return version;
    }

    public static GameVersion getFromVersion(String version) {
        if (StringHelper.isNullOrEmpty(version)) return null;
        for(GameVersion gameVersion : GameVersion.values()) {
            if (gameVersion.getVersion().equals(version)) return gameVersion;
        }
        return null;
    }

}
