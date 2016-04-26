package org.slave.minecraft.retweak.loading;

import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;
import org.slave.minecraft.retweak.resources.ReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

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

    private static final Pattern PATTERN_MOD_ID = Pattern.compile(
            "( |\t)|(\r|\n)|\""
    );

    private final EnumMap<SupportedGameVersion, ArrayList<ReTweakModCandidate>> modCandidates;
    private final EnumMap<SupportedGameVersion, ArrayList<ReTweakModContainer>> modContainers;

    private ReTweakLoader() {
        final SupportedGameVersion[] values = SupportedGameVersion.values();

        //noinspection unchecked
        WrappingDataT2<SupportedGameVersion, ArrayList<ReTweakModCandidate>>[] modCandidatesWrappingData = new WrappingDataT2[values.length];
        for(SupportedGameVersion supportedGameVersion : values) {
            modCandidatesWrappingData[supportedGameVersion.ordinal()] = new WrappingDataT2<>(
                    supportedGameVersion,
                    new ArrayList<ReTweakModCandidate>()
            );
        }
        modCandidates = new EnumMap<>(
                SupportedGameVersion.class,
                modCandidatesWrappingData
        );

        //noinspection unchecked
        WrappingDataT2<SupportedGameVersion, ArrayList<ReTweakModContainer>>[] modContainersWrappingData = new WrappingDataT2[values.length];
        for(SupportedGameVersion supportedGameVersion : values) {
            modContainersWrappingData[supportedGameVersion.ordinal()] = new WrappingDataT2<>(
                    supportedGameVersion,
                    new ArrayList<ReTweakModContainer>()
            );
        }
        modContainers = new EnumMap<>(
                SupportedGameVersion.class,
                modContainersWrappingData
        );
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoaderTransformer}
     */
    public void loadMods() {
        if (!ReTweakResources.RETWEAK_MODS_DIRECTORY.exists()) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Mods directory for ReTweak was not found, creating it now..."
            );
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

                        reTweakModDiscoverer.getReTweakModCandidates();

                        //TODO
                    } catch(IOException | IllegalAccessException | NoSuchFieldException e) {
                        ReTweakResources.RETWEAK_LOGGER.warn(
                                "Failed to load ReTweak mods due to catching an exception!",
                                e
                        );
                    }
                }
            }
        }

        config();
    }

    List<ReTweakModCandidate> getModCandidates(SupportedGameVersion supportedGameVersion) {
        if (supportedGameVersion == null) return Collections.emptyList();
        return modCandidates.get(supportedGameVersion);
    }

    public Iterable<ReTweakModContainer> getModContainers(SupportedGameVersion supportedGameVersion) {
        if (supportedGameVersion == null) return Collections.emptyList();
        return modContainers.get(supportedGameVersion);
    }

    private void config() {
        try {
            ReTweakConfig.INSTANCE.update(true);

            ReTweakMilk.INSTANCE.update(true);
        } catch(IOException e) {
            e.printStackTrace();
        }
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            List<ReTweakModContainer> reTweakModContainers = modContainers.get(supportedGameVersion);
            //TODO
        }
    }

}
