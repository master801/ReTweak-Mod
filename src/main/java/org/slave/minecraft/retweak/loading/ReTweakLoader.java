package org.slave.minecraft.retweak.loading;

import com.github.pwittchen.kirai.library.Kirai;
import com.google.common.base.Joiner;
import org.slave.lib.helpers.FileHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.resources.ASMAnnotation;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.asm.visitors.ModClassVisitor;
import org.slave.minecraft.retweak.asm.visitors.ModClassVisitor.Type;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
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

    private final ReTweakModDiscoverer reTweakModDiscoverer = new ReTweakModDiscoverer();

    private final EnumMap<GameVersion, List<ReTweakModContainer>> mods;

    private ReTweakLoader() {
        mods = new EnumMap<>(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            mods.put(
                    gameVersion,
                    new ArrayList<ReTweakModContainer>()
            );
        }
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoaderTransformer}
     */
    public void loadMods() {
        //<editor-fold desc="Find">
        if (!ReTweakResources.RETWEAK_MODS_DIRECTORY.exists()) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "ReTweak's mod directory does not exist... not loading mods."
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
                        "ReTweak's game version dir \"%s\" does not exist... not searching for mods for that version.",
                        gameVersionDir.getPath()
                );
                FileHelper.createDirectory(gameVersionDir);
                continue;
            } else if (!gameVersionDir.isDirectory()) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "ReTweak's game version dir \"%s\" is not a directory!",
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
                        Kirai.from(
                                "Failed to discover mods for version \"{version}\"!"
                        ).put(
                                "version",
                                gameVersion.getVersion()
                        ).format().toString(),
                        e
                );
            }
        }
        //</editor-fold>

        //<editor-fold desc="Find">
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
            if (ReTweakResources.DEBUG) {
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
            }
            for(ReTweakModCandidate reTweakModCandidate : reTweakModDiscoverer.getModCandidates(gameVersion)) {
                String modClass = null;
                String modid = null;
                String name = null;
                String version = null;

                Entry<Type, String> entry = ModClassVisitor.TYPES.get(reTweakModCandidate.getGameVersion());
                for(TableClass tableClass : reTweakModCandidate.getModClasses()) {
                    switch(entry.getKey()) {
                        case EXTENDS:
                            modClass = tableClass.getName();
                            modid = tableClass.getName();
                            name = null;//TODO
                            version = null;//TODO
                            break;
                        case ANNOTATION:
                            for(ASMAnnotation asmAnnotation : tableClass.getAnnotations()) {
                                if (asmAnnotation.getDesc().equals(entry.getValue())) {
                                    modClass = tableClass.getName();
                                    modid = (String)asmAnnotation.get().get("modid");
                                    name = (String)asmAnnotation.get().get("name");
                                    version = (String)asmAnnotation.get().get("version");
                                    break;
                                }
                            }
                            break;
                    }
                }

                if (modClass != null && modid != null) {
                    mods.get(gameVersion).add(new ReTweakModContainer(
                            modClass.replace(
                                    '/',
                                    '.'
                            ),
                            modid,
                            name,
                            version,
                            reTweakModCandidate
                    ));
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
        //TODO
        //</editor-fold>
    }

    public ReTweakModContainer getReTweakModContainer(final GameVersion gameVersion, final String modid) {
        if (gameVersion == null || StringHelper.isNullOrEmpty(modid)) return null;
        for(ReTweakModContainer reTweakModContainer : mods.get(gameVersion)) {
            if (reTweakModContainer.getModid().equals(modid)) return reTweakModContainer;
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
