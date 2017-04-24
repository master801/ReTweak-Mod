package org.slave.minecraft.retweak.loading.mod;

import org.slave.lib.helpers.ArrayHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * {@link cpw.mods.fml.common.discovery.ModDiscoverer}
 *
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public final class ReTweakModDiscoverer {

    private static final Pattern PATTERN_ZIP_JAR_MATCHER = Pattern.compile(
            ".+(\\.zip|\\.jar)$",
            Pattern.MULTILINE
    );

    private final Map<GameVersion, List<ReTweakModCandidate>> modCandidates = new HashMap<>();

    ReTweakModDiscoverer() {
        for(GameVersion gameVersion : GameVersion.values()) {
            modCandidates.put(
                    gameVersion,
                    new ArrayList<>()
            );
        }
    }

    public void discoverInDir(final GameVersion gameVersion, final File dir) throws FileNotFoundException {
        if (gameVersion == null || dir == null) return;
        if (!dir.exists()) {
            throw new FileNotFoundException(
                    "Directory \"" + dir.getPath() + "\" does not exist!"
            );
        }
        if (!dir.isDirectory()) {
            throw new FileNotFoundException(
                    "Not a directory! \"" + dir.getPath() + "\""
            );
        }

        File[] files = dir.listFiles();
        if (!ArrayHelper.isNullOrEmpty(files)) {
            for(File file : files) {
                if (!file.isFile()) continue;
                if (ReTweakModDiscoverer.PATTERN_ZIP_JAR_MATCHER.matcher(file.getName()).matches()) {
                    ReTweakResources.RETWEAK_LOGGER.debug(
                            "Added file \"{}\" as a candidate mod.",
                            file.getPath()
                    );
                    ReTweakModCandidate reTweakModCandidate = new ReTweakModCandidate(
                            gameVersion,
                            file
                    );
                    if (reTweakModCandidate.getASMTable().getTableClasses() == null) {
                        ReTweakResources.RETWEAK_LOGGER.warn(
                                "Found non-mod file \"{}\" that is marked as a mod candidate. Removing...",
                                file.getPath()
                        );
                        continue;
                    }
                    modCandidates.get(gameVersion).add(reTweakModCandidate);
                    //FIXME?
                    /*
                } else {
                    ReTweakResources.RETWEAK_LOGGER.debug(
                            "Added file \"{}\" as a candidate mod. This may or may not be an actual mod.",
                            file.getPath()
                    );
                    modCandidates.get(gameVersion).add(
                            new ReTweakModCandidate(
                                    gameVersion,
                                    file
                            )
                    );
                    */
                }
            }
        }
    }

    public List<ReTweakModCandidate> getModCandidates(GameVersion gameVersion) {
        if (gameVersion == null || !modCandidates.containsKey(gameVersion)) return null;
        return modCandidates.get(gameVersion);
    }

}
