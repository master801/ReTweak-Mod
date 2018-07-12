package org.slave.minecraft.retweak.load;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.util.GameVersion;
import org.slave.minecraft.retweak.load.mod.ReTweakModDiscoverer;

import java.io.File;
import java.util.Map;

/**
 * Created by Master on 7/11/2018 at 5:21 AM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakLoader {

    private static ReTweakLoader instance = null;

    private Map<GameVersion, File> modDirs = null;
    private Map<GameVersion, File> configDirs = null;
    private Map<GameVersion, ReTweakModDiscoverer> modDiscoverers = null;

    private ReTweakLoadController reTweakLoadController = null;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void initialize() {
        //Create retweak dir
        if (!ReTweak.FILE_DIRECTORY_RETWEAK.exists()) ReTweak.FILE_DIRECTORY_RETWEAK.mkdirs();

        //Create retweak mods dir
        if (!ReTweak.FILE_DIRECTORY_RETWEAK_MODS.exists()) ReTweak.FILE_DIRECTORY_RETWEAK_MODS.mkdirs();

        //Create mod dirs for game versions in retweak dir
        modDirs = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            File gameVersionModsDir = new File(ReTweak.FILE_DIRECTORY_RETWEAK_MODS, gameVersion.getVersion());
            if (!gameVersionModsDir.exists()) gameVersionModsDir.mkdirs();
            modDirs.put(
                    gameVersion,
                    gameVersionModsDir
            );
        }

        //Create config dirs for game versions in retweak dir
        configDirs = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            File gameVersionConfigDir = new File(ReTweak.FILE_DIRECTORY_RETWEAK_CONFIG, gameVersion.getVersion());
            if (!gameVersionConfigDir.exists()) gameVersionConfigDir.mkdirs();
            configDirs.put(
                    gameVersion,
                    gameVersionConfigDir
            );
        }

        //Create mod discoverers for game versions
        modDiscoverers = Maps.newEnumMap(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakModDiscoverer reTweakModDiscoverer = new ReTweakModDiscoverer(gameVersion);
            modDiscoverers.put(
                    gameVersion,
                    reTweakModDiscoverer
            );
        }
    }

    public void identifyMods() {
    }

    public ReTweakLoadController getReTweakLoadController() {
        if (reTweakLoadController == null) reTweakLoadController = new ReTweakLoadController();
        return reTweakLoadController;
    }

    public static ReTweakLoader instance() {
        if (ReTweakLoader.instance == null) ReTweakLoader.instance = new ReTweakLoader();
        return ReTweakLoader.instance;
    }

}
