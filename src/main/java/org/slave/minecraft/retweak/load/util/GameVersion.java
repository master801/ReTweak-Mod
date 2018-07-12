package org.slave.minecraft.retweak.load.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by Master on 7/11/2018 at 8:45 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public enum GameVersion {

    V_1_4_7(
            "1.4.7"
    );

    @Getter
    private final String version;

}
