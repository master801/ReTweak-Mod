package org.slave.minecraft.retweak.load;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.ModContainer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mapping.SrgMap;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainer;
import org.slave.minecraft.retweak.load.mod.ReTweakModDiscoverer;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * {@link cpw.mods.fml.common.Loader}
 *
 *
 * Created by Master on 7/11/2018 at 5:21 AM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakLoader {

    private static ReTweakLoader instance = null;

    private Map<GameVersion, File> modDirs = null;
    private Map<GameVersion, File> configDirs = null;

    private Map<GameVersion, SrgMap> srgMaps = null;

    private Map<GameVersion, List<ReTweakModContainer>> mods = null;

    private Map<GameVersion, ReTweakModDiscoverer> reTweakModDiscoverers = null;
    private Map<GameVersion, ReTweakLoadController> reTweakLoadControllers = null;

    /**
     * {@link cpw.mods.fml.common.Loader#loadMods()}
     */
    @SuppressWarnings({ "unused", "ResultOfMethodCallIgnored" })
    public void initialize() {
        //Create retweak dir
        if (!ReTweak.FILE_DIRECTORY_RETWEAK.exists()) ReTweak.FILE_DIRECTORY_RETWEAK.mkdirs();

        //Create retweak mods dir
        if (!ReTweak.FILE_DIRECTORY_RETWEAK_MODS.exists()) ReTweak.FILE_DIRECTORY_RETWEAK_MODS.mkdirs();

        //Create mod dirs for game versions in retweak dir
        modDirs = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            File gameVersionModsDir = new File(ReTweak.FILE_DIRECTORY_RETWEAK_MODS, gameVersion.getVersion());
            if (!gameVersionModsDir.exists()) {
                ReTweak.LOGGER_RETWEAK.info("Mod directory for game version {} does not exist. Creating...", gameVersion.getVersion());
                ReTweak.LOGGER_RETWEAK.debug("Path: {}", gameVersionModsDir.getPath());
                gameVersionModsDir.mkdirs();
            }
            modDirs.put(gameVersion, gameVersionModsDir);
        }

        //Create config dirs for game versions in retweak dir
        configDirs = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            File gameVersionConfigDir = new File(ReTweak.FILE_DIRECTORY_RETWEAK_CONFIG, gameVersion.getVersion());
            if (!gameVersionConfigDir.exists()) {
                ReTweak.LOGGER_RETWEAK.info("Config directory for game version {} does not exist. Creating...", gameVersion.getVersion());
                ReTweak.LOGGER_RETWEAK.debug("Path: {}", gameVersionConfigDir.getPath());
                gameVersionConfigDir.mkdirs();
            }
            configDirs.put(gameVersion, gameVersionConfigDir);
        }

        mods = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) mods.put(gameVersion, Lists.newArrayList());

        //Create mod discoverers for game versions
        reTweakModDiscoverers = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakModDiscoverer reTweakModDiscoverer = new ReTweakModDiscoverer(gameVersion);
            reTweakModDiscoverers.put(gameVersion, reTweakModDiscoverer);
        }

        reTweakLoadControllers = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakLoadController reTweakLoadController = new ReTweakLoadController(gameVersion);
            reTweakLoadControllers.put(gameVersion, reTweakLoadController);
        }
    }

    /**
     * {@link cpw.mods.fml.common.Loader#loadMods()}
     */
    @SuppressWarnings({ "unused" })
    public void identifyMods() {
        for(GameVersion gameVersion : GameVersion.values()) {
            if (gameVersion.isDisabled()) {
                ReTweak.LOGGER_RETWEAK.info("Game version \"{}\" has been disabled. Not finding mods...", gameVersion.getVersion());
                continue;
            } else if (gameVersion.getSrgMap() == null) {
                ReTweak.LOGGER_RETWEAK.warn("No SRG map was specified for game version \"{}\"!", gameVersion.getVersion());
                continue;
            }

            ReTweakModDiscoverer reTweakModDiscoverer = reTweakModDiscoverers.get(gameVersion);
            reTweakModDiscoverer.findModDirMods(getModDirectory(gameVersion));

            List<ModContainer> modContainers = reTweakModDiscoverer.identifyMods();

            List<ReTweakModContainer> reTweakModContainers = mods.get(gameVersion);
            for(ModContainer modContainer : modContainers) {
                ReTweakModContainer reTweakModContainer = (ReTweakModContainer)modContainer;

                reTweakModContainers.add(reTweakModContainer);
                gameVersion.getSrgMap().merge(reTweakModContainer.getReTweakModCandidate().getASMTable());
            }
            gameVersion.getSrgMap().sort();

            mods.put(gameVersion, reTweakModContainers);
        }
    }

    public List<ReTweakModContainer> getMods(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        return mods.get(gameVersion);
    }

    /**
     * Lazy
     *
     * {@link org.slave.minecraft.retweak.load.ReTweakLoadController#distributeStateMessage(cpw.mods.fml.common.LoaderState, Object...)}
     */
    public void distributeStateMessage(final LoadController loadController, final LoaderState state, final Object... eventData) {
        if (loadController.getClass() == LoadController.class && state != null) {//Make sure to check if the load controller we're invoking from is LoadController. If it is ReTweakLoadController, this method will keep getting called - causing a infinite loop -- not pretty
            for(GameVersion gameVersion: GameVersion.values()) {
                if (gameVersion.isDisabled()) {//TODO Read from config
                    ReTweak.LOGGER_RETWEAK.info("Game version \"{}\" has been disabled. Not distributing state message \"{}\"", gameVersion.getVersion(), state.name());
                    continue;
                }
                ReTweakLoadController reTweakLoadController = getReTweakLoadController(gameVersion);
                reTweakLoadController.distributeStateMessage(state, eventData);
            }
        }
    }

    /**
     * Lazy
     *
     * {@link org.slave.minecraft.retweak.load.ReTweakLoadController#distributeStateMessage(Class)}
     */
    public void distributeStateMessage(final LoadController loadController, final Class<?> customEvent) {
        if (loadController.getClass() == LoadController.class && customEvent != null) {//Make sure to check if the load controller we're invoking from is LoadController. If it is ReTweakLoadController, this method will keep getting called - causing a infinite loop -- not pretty
            for(GameVersion gameVersion: GameVersion.values()) {
                if (gameVersion.isDisabled()) {//TODO Read if disabled from config
                    ReTweak.LOGGER_RETWEAK.info("Game version \"{}\" has been disabled. Not distributing state message \"{}\"", gameVersion.getVersion(), customEvent);
                    continue;
                }
                ReTweakLoadController reTweakLoadController = getReTweakLoadController(gameVersion);
                reTweakLoadController.distributeStateMessage(customEvent);
            }
        }
    }

    /**
     * Lazy
     *
     * {@link org.slave.minecraft.retweak.load.ReTweakLoadController#transition(cpw.mods.fml.common.LoaderState, boolean)}
     */
    public void transition(final LoadController loadController, LoaderState desiredState, final boolean forceState) {
        if (loadController.getClass() == LoadController.class) {//Make sure to check if the load controller we're invoking from is LoadController. If it is ReTweakLoadController, this method will keep getting called - causing a infinite loop -- not pretty
            for(GameVersion gameVersion: GameVersion.values()) {
                ReTweakLoadController reTweakLoadController = getReTweakLoadController(gameVersion);
                reTweakLoadController.transition(desiredState, forceState);
            }
        }
    }

    private File getModDirectory(final GameVersion gameVersion) {
        if (gameVersion == null || modDirs == null) return null;
        return modDirs.get(gameVersion);
    }

    public File getConfigDirectory(final GameVersion gameVersion) {
        if (gameVersion == null || configDirs == null) return null;
        return configDirs.get(gameVersion);
    }

    public ReTweakModDiscoverer getReTweakModDiscoverer(final GameVersion gameVersion) {
        if (gameVersion == null || reTweakModDiscoverers == null) return null;
        return reTweakModDiscoverers.get(gameVersion);
    }

    public ReTweakLoadController getReTweakLoadController(final GameVersion gameVersion) {
        if (gameVersion == null || reTweakLoadControllers == null) return null;
        return reTweakLoadControllers.get(gameVersion);
    }

    public static ReTweakLoader instance() {
        if (ReTweakLoader.instance == null) ReTweakLoader.instance = new ReTweakLoader();
        return ReTweakLoader.instance;
    }

}
