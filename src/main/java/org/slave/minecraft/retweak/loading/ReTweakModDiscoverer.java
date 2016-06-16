package org.slave.minecraft.retweak.loading;

import com.github.pwittchen.kirai.library.Kirai;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * {@link cpw.mods.fml.common.discovery.ModDiscoverer}
 *
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public final class ReTweakModDiscoverer {

    private static Pattern archivePattern;

    private final HashMap<GameVersion, List<ReTweakModCandidate>> modCandidates = new HashMap<>();

    ReTweakModDiscoverer() {
        try {
            ReTweakModDiscoverer.archivePattern = ReflectionHelper.getFieldValue(
                    ReflectionHelper.getField(
                            ModDiscoverer.class,
                            "zipJar"
                    ),
                    null
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Failed to reflectively get archivePattern for mod discoverer? Things are not going to work well!",
                    e
            );
        }

        for(GameVersion gameVersion : GameVersion.values()) {
            modCandidates.put(
                    gameVersion,
                    new ArrayList<ReTweakModCandidate>()
            );
        }
    }

    public void discoverInDir(GameVersion gameVersion, File dir) throws FileNotFoundException {
        if (gameVersion == null || dir == null) return;
        if (!dir.exists()) {
            throw new FileNotFoundException(
                    Kirai.from(
                            "Directory \"{path}\" does not exist!"
                    ).put(
                            "path",
                            dir.getPath()
                    ).format().toString()
            );
        }
        if (!dir.isDirectory()) {
            throw new FileNotFoundException(
                    Kirai.from(
                            "Not a directory! \"{path}\""
                    ).put(
                            "path",
                            dir.getPath()
                    ).format().toString()
            );
        }

        File[] files = dir.listFiles();
        if (!ArrayHelper.isNullOrEmpty(files)) {
            for(File file : files) {
                if (!file.isFile()) continue;
                if (ReTweakModDiscoverer.archivePattern != null) {
                    if (ReTweakModDiscoverer.archivePattern.matcher(file.getName()).matches()) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Added file \"{}\" as a candidate mod.",
                                file.getPath()
                        );
                        ReTweakModCandidate reTweakModCandidate = new ReTweakModCandidate(
                                gameVersion,
                                file
                        );
                        if (reTweakModCandidate.getASMTable().getClasses() == null) {
                            ReTweakResources.RETWEAK_LOGGER.warn(
                                    "Found non-mod file \"{}\" that is marked as a mod candidate. Removing...",
                                    file.getPath()
                            );
                            continue;
                        }
                        modCandidates.get(gameVersion).add(reTweakModCandidate);
                    }
                } else {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "Added file \"{}\" as a candidate mod. This may or may not be an actual mod.",
                            file.getPath()
                    );
                    modCandidates.get(gameVersion).add(
                            new ReTweakModCandidate(
                                    gameVersion,
                                    file
                            )
                    );
                }
            }
        }
    }

    public List<ReTweakModCandidate> getModCandidates(GameVersion gameVersion) {
        if (gameVersion == null || !modCandidates.containsKey(gameVersion)) return null;
        return modCandidates.get(gameVersion);
    }

}
