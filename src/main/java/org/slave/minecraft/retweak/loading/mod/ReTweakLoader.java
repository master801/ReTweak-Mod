package org.slave.minecraft.retweak.loading.mod;

import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

/**
 * <p>
 *     {@link cpw.mods.fml.common.Loader}
 * </p>
 *
 * Created by Master801 on 3/21/2016 at 9:00 AM.
 *
 * @author Master801
 */
public final class ReTweakLoader {

    public static final ReTweakLoader INSTANCE = new ReTweakLoader();

    private final Map<GameVersion, ReTweakModDiscoverer> reTweakModDiscovererList;
    private final Map<GameVersion, File> reTweakModsDirList;

    private ReTweakLoader() {
        reTweakModDiscovererList = new EnumMap<>(GameVersion.class);
        reTweakModsDirList = new EnumMap<>(GameVersion.class);

        for(GameVersion gameVersion : GameVersion.values()) {
            reTweakModDiscovererList.put(
                    gameVersion,
                    new ReTweakModDiscoverer(gameVersion)
            );
            reTweakModsDirList.put(
                    gameVersion,
                    new File(
                            ReTweakResources.RETWEAK_MODS_DIRECTORY,
                            gameVersion.getVersion()
                    )
            );
        }
    }

    public void loadMods() {
        for(GameVersion gameVersion : GameVersion.values()) {
        }
        //TODO
    }

    ReTweakModDiscoverer getReTweakModDiscoverer(final GameVersion gameVersion) {
        return reTweakModDiscovererList.get(gameVersion);
    }

    File getReTweakModsDirectory(final GameVersion gameVersion) {
        return reTweakModsDirList.get(gameVersion);
    }

}
