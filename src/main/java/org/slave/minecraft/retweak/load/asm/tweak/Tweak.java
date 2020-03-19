package org.slave.minecraft.retweak.load.asm.tweak;

import org.slave.minecraft.retweak.load.asm.tweak.specific.SpecificTweaker;
import org.slave.minecraft.retweak.load.asm.tweak.specific._1_5_2.SpecificTweaker152;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.EnumMap;

/**
 * Created by Master on 7/12/2018 at 9:11 PM.
 *
 * @author Master
 */
public final class Tweak {

    private static EnumMap<GameVersion, SpecificTweaker> specificTweakerMap = null;

    public static SpecificTweaker getSpecificTweaker(final GameVersion gameVersion) {
        if (Tweak.specificTweakerMap == null) Tweak.init();
        return Tweak.specificTweakerMap.get(gameVersion);
    }

    private static void init() {
        if (Tweak.specificTweakerMap != null) return;
        Tweak.specificTweakerMap = new EnumMap<>(GameVersion.class);
        Tweak.specificTweakerMap.put(GameVersion.V_1_5_2, new SpecificTweaker152());
    }

}
