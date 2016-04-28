package org.slave.minecraft.retweak.loading.mappings;

import org.slave.minecraft.retweak.loading.capsule.GameVersion;

import java.util.HashMap;

/**
 * Created by Master on 4/27/2016 at 3:31 PM.
 *
 * @author Master
 */
public final class Mappings {

    public static final Mappings INSTANCE = new Mappings();

    private final HashMap<GameVersion, Mapping> mappings = new HashMap<>();

    private Mappings() {
        mappings.put(
                GameVersion.V_1_4_7,
                new V_1_4_7_Mapping()
        );
    }

    public Mapping getMapping(GameVersion gameVersion) {
        if (gameVersion == null) return null;
        return mappings.get(gameVersion);
    }

}
