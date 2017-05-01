package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLLoadEvent;
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

    private final Map<GameVersion, ReTweakLoadController> reTweakLoadControllers;
    private final Map<GameVersion, ReTweakModDiscoverer> reTweakModDiscovererList;
    private final Map<GameVersion, File> reTweakModsDirList;

    private Map<GameVersion, List<ModContainer>> reTweakMods = new HashMap<>();
    private Map<GameVersion, Map<String, ModContainer>> reTweakNamedMods = new HashMap<>();

    private ReTweakLoader() {
        Map<GameVersion, ReTweakLoadController> reTweakLoadControllers = new EnumMap<>(GameVersion.class);
        Map<GameVersion, ReTweakModDiscoverer> reTweakModDiscovererList = new EnumMap<>(GameVersion.class);
        Map<GameVersion, File> reTweakModsDirList = new EnumMap<>(GameVersion.class);

        for(GameVersion gameVersion : GameVersion.values()) {
            reTweakLoadControllers.put(
                    gameVersion,
                    new ReTweakLoadController(
                            gameVersion,
                            this
                    )
            );
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

        this.reTweakLoadControllers = ImmutableMap.copyOf(reTweakLoadControllers);
        this.reTweakModDiscovererList = ImmutableMap.copyOf(reTweakModDiscovererList);
        this.reTweakModsDirList = ImmutableMap.copyOf(reTweakModsDirList);
    }

    public void loadMods() {
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakLoadController reTweakLoadController = getReTweakLoadController(gameVersion);
            reTweakLoadController.transition(
                    LoaderState.LOADING,
                    false
            );
            ReTweakModDiscoverer reTweakModDiscoverer = findMods(gameVersion);
            List<ModContainer> modList = getModList(gameVersion);
            modList.addAll(
                    reTweakModDiscoverer.identifyMods()
            );
            reTweakLoadController.distributeStateMessage(
                    FMLLoadEvent.class
            );
            sortModList(gameVersion);
            modList = ImmutableList.copyOf(modList);
            this.reTweakMods.put(
                    gameVersion,
                    modList
            );
            reTweakLoadController.transition(
                    LoaderState.CONSTRUCTING,
                    false
            );
            reTweakLoadController.distributeStateMessage(
                    LoaderState.CONSTRUCTING,
                    ReTweakClassLoader.getReTweakClassLoader(gameVersion),
                    reTweakModDiscoverer.getASMTable(),
                    null
            );
            reTweakLoadController.transition(
                    LoaderState.PREINITIALIZATION,
                    false
            );
        }
        //TODO
    }

    private void sortModList(final GameVersion gameVersion) {
        //TODO
    }

    private ReTweakModDiscoverer findMods(final GameVersion gameVersion) {
        ReTweakModDiscoverer reTweakModDiscoverer = getReTweakModDiscoverer(
                gameVersion
        );

        File reTweakModsDir = getReTweakModsDirectory(
                gameVersion
        );

        reTweakModDiscoverer.findModDirMods(
                reTweakModsDir
        );

        List<ModContainer> modList = getModList(
                gameVersion
        );
        modList.addAll(
                reTweakModDiscoverer.identifyMods()
        );

        this.reTweakNamedMods.put(
                gameVersion,
                Maps.uniqueIndex(
                        modList,
                        new ModIdFunction()
                )
        );
        return reTweakModDiscoverer;
    }

    private ReTweakLoadController getReTweakLoadController(final GameVersion gameVersion) {
        return reTweakLoadControllers.get(gameVersion);
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
