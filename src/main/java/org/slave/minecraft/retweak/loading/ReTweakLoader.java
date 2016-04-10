package org.slave.minecraft.retweak.loading;

import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;
import org.slave.minecraft.retweak.loading.discovery.ReTweakModDiscoverer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private final EnumMap<SupportedGameVersion, ArrayList<ReTweakModContainer>> modContainers;

    private ReTweakLoader() {
        //noinspection unchecked
        WrappingDataT2<SupportedGameVersion, ArrayList<ReTweakModContainer>>[] wrappingDataT2 = new WrappingDataT2[SupportedGameVersion.values().length];

        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            wrappingDataT2[supportedGameVersion.ordinal()] = new WrappingDataT2<>(
                    supportedGameVersion,
                    new ArrayList<ReTweakModContainer>()
            );
        }

        modContainers = new EnumMap<>(SupportedGameVersion.class, wrappingDataT2);
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoaderTransformer}
     */
    public void loadMods() {
        if (!ReTweakResources.RETWEAK_MODS_DIRECTORY.exists()) {
            ReTweakResources.RETWEAK_LOGGER.warn("Mods directory for ReTweak was not found, creating it now...");
            //noinspection ResultOfMethodCallIgnored
            ReTweakResources.RETWEAK_MODS_DIRECTORY.mkdirs();
        }

        File[] subFiles = ReTweakResources.RETWEAK_MODS_DIRECTORY.listFiles();
        if (!ArrayHelper.isNullOrEmpty(subFiles)) {
            for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
                File supportedGameVersionDir = null;
                for(File subFile : subFiles) {
                    if (subFile.isDirectory() && subFile.getName().equals(supportedGameVersion.getVersion())) {
                        ReTweakResources.RETWEAK_LOGGER.debug(
                                "Found supported mods dir \"{}\", searching it now for mods...",
                                subFile.getPath()
                        );
                        supportedGameVersionDir = subFile;
                        break;
                    }
                }
                if (supportedGameVersionDir != null) {
                    ReTweakModDiscoverer reTweakModDiscoverer = new ReTweakModDiscoverer(supportedGameVersion);
                    try {
                        reTweakModDiscoverer.findModsInDir(supportedGameVersionDir);
                        reTweakModDiscoverer.identify();

                        for(ReTweakModCandidate reTweakModCandidate : reTweakModDiscoverer.getReTweakModCandidates()) {
                            for(ReTweakModContainer reTweakModContainer : reTweakModCandidate.getModContainers()) {
                                modContainers.get(supportedGameVersion).add(reTweakModContainer);
                            }
                        }
                    } catch(IOException | IllegalAccessException | NoSuchFieldException e) {
                        ReTweakResources.RETWEAK_LOGGER.warn(
                                "Failed to load ReTweak mods due to catching an exception! Exception: {}",
                                e
                        );
                    }
                }
            }
        }
        writeConfig();
    }

    public List<ReTweakModContainer> getModContainers(SupportedGameVersion supportedGameVersion) {
        if (supportedGameVersion == null) return Collections.emptyList();
        return modContainers.get(supportedGameVersion);
    }

    private void writeConfig() {
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            List<ReTweakModContainer> reTweakModContainers = modContainers.get(supportedGameVersion);
            try {
                ReTweakCereal.INSTANCE.modify(
                        supportedGameVersion,
                        reTweakModContainers
                );
            } catch(IOException e) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Caught an IO Exception while modifying the config file! Renaming to \"{}.broken\"...",
                        ReTweakCereal.RETWEAK_CONFIG_FILE.getPath()
                );
                if (ReTweakCereal.RETWEAK_CONFIG_FILE.exists()) {
                    //noinspection ResultOfMethodCallIgnored
                    ReTweakCereal.RETWEAK_CONFIG_FILE.renameTo(new File(ReTweakCereal.RETWEAK_CONFIG_FILE.getPath() + ".broken"));
                }
            }
        }
    }

}
