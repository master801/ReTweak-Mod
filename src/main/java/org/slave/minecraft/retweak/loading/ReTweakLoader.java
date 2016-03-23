package org.slave.minecraft.retweak.loading;

import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.json.garden.InvalidBranchException;
import org.slave.lib.json.garden.JSONEnum;
import org.slave.lib.json.garden.JSONPeach;
import org.slave.lib.resources.Bulk;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;
import org.slave.minecraft.retweak.loading.ReTweakCereal.CocoaPuffs;
import org.slave.minecraft.retweak.loading.discovery.ReTweakModDiscoverer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

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
                    if (subFile.isDirectory() && subFile.getName().equals(supportedGameVersion.getDirectoryName())) {
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
            try {
                List<ReTweakModContainer> reTweakModContainers = modContainers.get(supportedGameVersion);
                /*
                ReTweakCereal.INSTANCE.writeReTweakModConfig(
                        supportedGameVersion,
                        m.toArray(new ReTweakModContainer[m.size()])
                );
                */

                JSONPeach[] mods = new JSONPeach[reTweakModContainers.size()];
                for(int i = 0; i < mods.length; ++i) {
                    ReTweakModContainer reTweakModContainer = reTweakModContainers.get(i);
                    ArrayList<Entry<? extends JSONEnum, Object>> objects = new ArrayList<>();
                    objects.add(
                            new Bulk<JSONEnum, Object>(
                                    CocoaPuffs.MODID,
                                    reTweakModContainer.getModid()
                            )
                    );
                    objects.add(
                            new Bulk<JSONEnum, Object>(
                                    CocoaPuffs.ENABLE,
                                    Boolean.TRUE
                            )
                    );
                    mods[i] = new JSONPeach(objects.toArray(new Entry[objects.size()]));
                }

                HashMap<String, JSONPeach[]> jsonPeaches = new HashMap<>();

                jsonPeaches.put(
                        ReTweakCereal.SUPPORTED_GAME_VERSION_IDENTIFIER,
                        new JSONPeach[] {
                                new JSONPeach(
                                        new Entry[] {
                                                new Bulk<>(
                                                        CocoaPuffs.VERSION,
                                                        supportedGameVersion.getDirectoryName()
                                                )
                                        }
                                )
                        }
                );
                jsonPeaches.put(
                        ReTweakCereal.MODS_IDENTIFIER,
                        mods
                );

                ReTweakCereal.INSTANCE.getConfig().stupidlySave(
                        ReTweakCereal.RETWEAK_CONFIG_FILE,
                        jsonPeaches
                );
            } catch(IOException e) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Failed to write ReTweak's mod configs!"
                );
            } catch(InvalidBranchException e) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "ReTweak's file \"{}\" is invalid! Deleting...",
                        ReTweakCereal.RETWEAK_CONFIG_FILE.getName()
                );
                ReTweakCereal.RETWEAK_CONFIG_FILE.delete();
            }
        }
    }

}
