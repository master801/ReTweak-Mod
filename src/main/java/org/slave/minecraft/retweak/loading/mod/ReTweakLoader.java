package org.slave.minecraft.retweak.loading.mod;

import com.google.common.base.Joiner;
import org.slave.lib.helpers.FileHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.resources.ASMAnnotation;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.loading.capsule.Type;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    private static final String[] RESOURCE_NAMES = new String[] {
            "cavemusic",
            "music",
            "sound",
            "streaming"
    };
    private static final Pattern PATTERN_MATCH_OGG = Pattern.compile(
            "(.+)(\\.ogg$)",
            Pattern.MULTILINE
    );

    private final ReTweakModDiscoverer reTweakModDiscoverer = new ReTweakModDiscoverer();

    private final EnumMap<GameVersion, List<ReTweakModContainer>> mods;

    private ReTweakLoader() {
        mods = new EnumMap<>(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            mods.put(
                    gameVersion,
                    new ArrayList<>()
            );
        }
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoaderTransformer}
     */
    public void loadMods() {
        //<editor-fold desc="Find mods">
        if (!ReTweakResources.RETWEAK_MODS_DIRECTORY.exists()) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "ReTweak's mod directory does not exist... not finding mods."
            );
            FileHelper.createDirectory(ReTweakResources.RETWEAK_MODS_DIRECTORY);
            return;
        }
        for(GameVersion gameVersion : GameVersion.values()) {
            File gameVersionDir = new File(
                    ReTweakResources.RETWEAK_MODS_DIRECTORY,
                    gameVersion.getVersion()
            );
            if (!gameVersionDir.exists()) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "ReTweak's game version dir \"{}\" does not exist... not searching for mods for that version.",
                        gameVersionDir.getPath()
                );
                FileHelper.createDirectory(gameVersionDir);
                continue;
            } else if (!gameVersionDir.isDirectory()) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "ReTweak's game version dir \"{}\" is not a directory!",
                        gameVersionDir.getPath()
                );
                continue;
            }
            try {
                reTweakModDiscoverer.discoverInDir(
                        gameVersion,
                        gameVersionDir
                );
            } catch(FileNotFoundException e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "Failed to discover mods for version \"" + gameVersion.getVersion() + "\"!",
                        e
                );
            }

            if (gameVersion.hasResources()) {//Search for resources
                File resources = new File(
                        gameVersionDir,
                        "resources"
                );
                if (resources.isDirectory()) {
                    File modResources = new File(
                            resources,
                            "mod"
                    );
                    if (modResources.isDirectory()) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Found resources folder for game version {}! Searching for usable sub-folders...",
                                gameVersion.getVersion()
                        );
                        for(final String resourceName : RESOURCE_NAMES) {
                            File resourceFile = new File(
                                    modResources,
                                    resourceName
                            );
                            if (resourceFile.exists()) {
                                ReTweakResources.RETWEAK_LOGGER.debug(
                                        "Resource folder \"{}\" exists! Adding all \"*.ogg\" files to the class-loader...",
                                        resourceFile.getPath()
                                );
                                File[] subFiles = resourceFile.listFiles();
                                if (subFiles != null && subFiles.length > 0) {
                                    for(File subFile : subFiles) {
                                        if (ReTweakLoader.PATTERN_MATCH_OGG.matcher(subFile.getName()).matches()) {
                                            ReTweakClassLoader.getClassLoader(gameVersion).addFile(subFile);
                                            ReTweakResources.RETWEAK_LOGGER.debug(
                                                    "Added resource file \"{}\" to the class-loader",
                                                    subFile.getPath()
                                            );
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="Find classes">
        for(GameVersion gameVersion : GameVersion.values()) {
            for(ReTweakModCandidate reTweakModCandidate : reTweakModDiscoverer.getModCandidates(gameVersion)) reTweakModCandidate.find();
        }
        //</editor-fold>

        //<editor-fold desc="Validate">
        for(GameVersion gameVersion : GameVersion.values()) {
            Iterator<ReTweakModCandidate> reTweakModCandidateIterator = reTweakModDiscoverer.getModCandidates(gameVersion).iterator();
            while(reTweakModCandidateIterator.hasNext()) {
                try {
                    if (!reTweakModCandidateIterator.next().isValid()) {
                        ReTweakResources.RETWEAK_LOGGER.warn(
                                "Found a non-mod ReTweak candidate. Removing..."
                        );
                        reTweakModCandidateIterator.remove();
                    }
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "Caught an I/O exception while validating a mod candidate! Removing it to avoid future issues...",
                            e
                    );
                    reTweakModCandidateIterator.remove();
                }
            }
        }
        //</editor-fold>

        //<editor-fold desc="Load">
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Found {} mods for version {}!",
                    reTweakModDiscoverer.getModCandidates(gameVersion).size(),
                    gameVersion.getVersion()
            );
            for(ReTweakModCandidate reTweakModCandidate : reTweakModDiscoverer.getModCandidates(gameVersion)) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Found {} mod classes! Classes: \"{}\"",
                        reTweakModCandidate.getModClasses().size(),
                        Joiner.on(", ").join(reTweakModCandidate.getModClasses())
                );
            }
            Iterator<ReTweakModCandidate> iterator = reTweakModDiscoverer.getModCandidates(gameVersion).iterator();
            while (iterator.hasNext()) {
                ReTweakModCandidate reTweakModCandidate = iterator.next();
                String modClass = null;
                Map<String, Object> info = new HashMap<>();

                if (reTweakModCandidate.getModClasses().isEmpty()) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "Found a non-mod candidate ({}), removing it...",
                            reTweakModCandidate.getSource().getPath()
                    );
                    iterator.remove();
                    continue;
                }

                Entry<Type, String> entry = reTweakModCandidate.getGameVersion().getModType();
                for(TableClass tableClass : reTweakModCandidate.getModClasses()) {
                    switch(entry.getKey()) {
                        case EXTENDS:
                            modClass = tableClass.getName();
                            info.put(
                                    "modid",
                                    tableClass.getName()
                            );
                            info.put(//TODO
                                    "name",
                                    null
                            );
                            info.put(//TODO
                                    "version",
                                    null
                            );
                            break;
                        case ANNOTATION:
                            for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                                if (asmAnnotation.getDesc().equals(entry.getValue())) {
                                    modClass = tableClass.getName();
                                    info.putAll(asmAnnotation.getValues());
                                    break;
                                }
                            }
                            break;
                    }
                }

                /*
                try {
                    ZipFile zipFile = new ZipFile(reTweakModCandidate.getSource());
                    Mapping mapping = ReTweakDeobfuscation.INSTANCE.getSuperMappings(gameVersion);
                    mapping.read(zipFile);
                    zipFile.close();
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "Failed to read candidate mod \"{}\" to super mappings.",
                            reTweakModCandidate.getSource().getPath()
                    );
                }
                */

                if (modClass != null) {
                    List<ReTweakModContainer> reTweakModContainerList = mods.get(gameVersion);
                    reTweakModContainerList.add(
                            new ReTweakModContainer(
                                    modClass.replace(
                                            '/',
                                            '.'
                                    ),
                                    reTweakModCandidate,
                                    info

                            )
                    );
                }
            }
            //TODO

            /*
            try {
                ReTweakModConfig.INSTANCE.update(true);
            } catch(IOException e) {
                e.printStackTrace();
            }
            */
        }
        //</editor-fold>

        //<editor-fold desc="Sort">
        for(GameVersion gameVersion : GameVersion.values()) {
            List<ReTweakModContainer> reTweakModContainerList = mods.get(gameVersion);
            //TODO
        }
        /*
        for(final GameVersion gameVersion : GameVersion.values()) {
            ReTweakDeobfuscation.INSTANCE.getSuperMappings(gameVersion).sort();
        }
        */
        //TODO
        //</editor-fold>
    }

    public ReTweakModContainer getReTweakModContainer(final GameVersion gameVersion, final String modid) {
        if (gameVersion == null || StringHelper.isNullOrEmpty(modid)) return null;
        for(ReTweakModContainer reTweakModContainer : mods.get(gameVersion)) {
            if (reTweakModContainer.getModId().equals(modid)) return reTweakModContainer;
        }
        return null;
    }

    public ReTweakModContainer[] getReTweakModContainers(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        List<ReTweakModContainer> list = mods.get(gameVersion);
        return list.toArray(new ReTweakModContainer[list.size()]);
    }

    ReTweakModDiscoverer getReTweakModDiscoverer() {
        return reTweakModDiscoverer;
    }

}
