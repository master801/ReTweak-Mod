package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.relauncher.FileListHelper;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Invalid ReTweak mods directory!"
            );
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
            if (ReTweakModDiscoverer.fmlZipJar.matcher(mod.getName()).matches()) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Found candidate mod \"{}\"!",
                        mod.getPath()
                );
                reTweakModCandidates.add(new ReTweakModCandidate(
                        supportedGameVersion,
                        mod
                ));
            }
        }
    }

    public void identify() throws IOException {
        Iterator<ReTweakModCandidate> reTweakModCandidateIterator = reTweakModCandidates.iterator();
        while(reTweakModCandidateIterator.hasNext()) {
            ReTweakModCandidate reTweakModCandidate = reTweakModCandidateIterator.next();
            boolean check;
            try {
                check = reTweakModCandidate.check();
            } catch(IOException e) {
                check = false;
            }
            if (!check) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "File \"{}\" is not a mod.",
                        reTweakModCandidate.getFile()
                );
                reTweakModCandidateIterator.remove();
                continue;
            }
            //TODO
        }
    }

    Iterable<ReTweakModCandidate> getReTweakModCandidates() {
        return reTweakModCandidates;
    }

}
