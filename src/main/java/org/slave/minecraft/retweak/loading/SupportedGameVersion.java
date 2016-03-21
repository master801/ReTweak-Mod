package org.slave.minecraft.retweak.loading;

/**
 * Created by Master801 on 3/19/2016 at 8:02 AM.
 *
 * @author Master801
 */
public enum SupportedGameVersion {

    V_1_2_5("1.2.5"),

    V_1_4_6("1.4.6"),

    V_1_4_7("1.4.7");

    private final String directoryName;

    SupportedGameVersion(final String directoryName) {
        this.directoryName = directoryName;
    }

    public String getDirectoryName() {
        return directoryName;
    }

}
