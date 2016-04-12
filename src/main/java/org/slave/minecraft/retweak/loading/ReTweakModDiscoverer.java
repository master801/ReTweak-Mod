package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.relauncher.FileListHelper;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.ModDiscoverer}
 * </p>
 *
 * Created by Master801 on 3/19/2016 at 8:58 AM.
 *
 * @author Master801
 */
public final class ReTweakModDiscoverer {

    private static Pattern fmlZipJar = null;

    private final SupportedGameVersion supportedGameVersion;
    private final ArrayList<ReTweakModCandidate> reTweakModCandidates = new ArrayList<>();

    public ReTweakModDiscoverer(SupportedGameVersion supportedGameVersion) {
        this.supportedGameVersion = supportedGameVersion;
    }

    public void findModsInDir(File dir) throws NoSuchFieldException, IllegalAccessException {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            ReTweakResources.RETWEAK_LOGGER.error("Invalid ReTweak mods directory!");
            return;
        }
        File[] modList = dir.listFiles();
        if (ArrayHelper.isNullOrEmpty(modList)) return;

        if (ReTweakModDiscoverer.fmlZipJar == null) {
            ReTweakModDiscoverer.fmlZipJar = ReflectionHelper.getFieldValue(
                    ReflectionHelper.getField(
                            ModDiscoverer.class,
                            "zipJar"
                    ),
                    null
            );
        }
        modList = FileListHelper.sortFileList(modList);

        for(File mod : modList) {
            if (mod.isFile()) {
                if (ReTweakModDiscoverer.fmlZipJar.matcher(mod.getName()).matches()) {
                    ReTweakResources.RETWEAK_LOGGER.info("Found a candidate mod!");//TODO Should be a debug message with more info
                    reTweakModCandidates.add(new ReTweakModCandidate(supportedGameVersion, mod));
                }
            } else {
                ReTweakResources.RETWEAK_LOGGER.warn("Mod \"{}\" is not a file or is not a mod! ReTweak does not support this!", mod.getName());
            }
        }
    }

    public void identify() throws IOException {
        for(ReTweakModCandidate reTweakModContainer : reTweakModCandidates) reTweakModContainer.search();
    }

    List<ReTweakModCandidate> getReTweakModCandidates() {
        return reTweakModCandidates;
    }

}
