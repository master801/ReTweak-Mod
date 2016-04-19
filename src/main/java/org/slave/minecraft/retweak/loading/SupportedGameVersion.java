package org.slave.minecraft.retweak.loading;

/**
 * Created by Master801 on 3/19/2016 at 8:02 AM.
 *
 * @author Master801
 */
public enum SupportedGameVersion {

    V_1_2_5("1.2.5"),

    V_1_4_6("1.4.6"),

    V_1_4_7("1.4.7"),

    V_1_5_2("1.5.2");

    private final String version;

    SupportedGameVersion(final String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public static SupportedGameVersion getFromVersion(String version) {
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            if (supportedGameVersion.getVersion().equals(version)) return supportedGameVersion;
        }
        return null;
    }

}
