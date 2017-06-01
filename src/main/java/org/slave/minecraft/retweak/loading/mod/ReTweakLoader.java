package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.functions.ModIdFunction;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
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
    private final Map<GameVersion, ReTweakLoadController> reTweakLoadControllerMap;

    private Map<GameVersion, List<ModContainer>> reTweakMods = new HashMap<>();
    private Map<GameVersion, Map<String, ModContainer>> reTweakNamedMods = new HashMap<>();

    private ReTweakLoader() {
        Map<GameVersion, ReTweakModDiscoverer> reTweakModDiscovererList = new EnumMap<>(GameVersion.class);
        Map<GameVersion, File> reTweakModsDirList = new EnumMap<>(GameVersion.class);
        Map<GameVersion, ReTweakLoadController> reTweakLoadControllerMap = new EnumMap<>(GameVersion.class);

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
            reTweakLoadControllerMap.put(
                gameVersion,
                new ReTweakLoadController(
                    this,
                    gameVersion
                )
            );
        }

        this.reTweakModDiscovererList = ImmutableMap.copyOf(reTweakModDiscovererList);
        this.reTweakModsDirList = ImmutableMap.copyOf(reTweakModsDirList);
        this.reTweakLoadControllerMap = ImmutableMap.copyOf(reTweakLoadControllerMap);
    }

    public void loadMods() {
        for(GameVersion gameVersion : GameVersion.values()) {
            findMods(gameVersion);

            sortModList(gameVersion);

            List<ModContainer> immutableModList = ImmutableList.copyOf(
                getModList(gameVersion)
            );
            reTweakMods.put(
                gameVersion,
                immutableModList
            );
        }
    }

    private void sortModList(final GameVersion gameVersion) {
        //TODO
    }

    private void findMods(final GameVersion gameVersion) {
        ReTweakModDiscoverer reTweakModDiscoverer = getReTweakModDiscoverer(
                gameVersion
        );

        File reTweakModsDir = getReTweakModsDirectory(
                gameVersion
        );

        reTweakModDiscoverer.findModDirMods(
                reTweakModsDir
        );
        List<ModContainer> identifiedMods = reTweakModDiscoverer.identifyMods();

        reTweakMods.put(
            gameVersion,
            identifiedMods
        );
        this.reTweakNamedMods.put(
            gameVersion,
            Maps.uniqueIndex(
                identifiedMods,
                new ModIdFunction()
            )
        );
    }

    private void checkReTweakLoadControllerConsistency(final LoaderState loaderState) {
        if (loaderState == null) return;

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakLoadController reTweakLoadController = reTweakLoadControllerMap.get(gameVersion);

            if (reTweakLoadController.getLoaderState() != loaderState) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                    "Inconsistent loader state for game version \"{}\"!",
                    gameVersion.getVersion()
                );

                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Game version's loader state: \"{}\", Loader state: \"{}\"",
                    reTweakLoadController.getLoaderState().name(),
                    loaderState.name()
                );
            }
        }
    }

    ReTweakLoadController getReTweakLoadController(final GameVersion gameVersion) {
        return reTweakLoadControllerMap.get(gameVersion);
    }

    List<ModContainer> getModList(final GameVersion gameVersion) {
        return reTweakMods.get(gameVersion);
    }

    Map<String, ModContainer> getNamedModList(final GameVersion gameVersion) {
        return reTweakNamedMods.get(gameVersion);
    }

    ReTweakModDiscoverer getReTweakModDiscoverer(final GameVersion gameVersion) {
        return reTweakModDiscovererList.get(gameVersion);
    }

    File getReTweakModsDirectory(final GameVersion gameVersion) {
        return reTweakModsDirList.get(gameVersion);
    }

}
