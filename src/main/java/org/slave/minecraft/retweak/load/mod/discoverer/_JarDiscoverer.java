package org.slave.minecraft.retweak.load.mod.discoverer;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import cpw.mods.fml.common.discovery.ModCandidate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.lib.resources.ASMTable;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mod.ReTweakModCandidate;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainer;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainerFactory;
import org.slave.minecraft.retweak.load.mod.asm.ReTweakASMModParser;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;

/**
 * Created by Master on 7/11/2018 at 11:34 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class _JarDiscoverer implements ITypeDiscoverer {

    @Getter
    private final GameVersion gameVersion;

    /**
     * Stolen mainly from FML's implementation
     *
     * {@link cpw.mods.fml.common.discovery.JarDiscoverer#discover(cpw.mods.fml.common.discovery.ModCandidate, cpw.mods.fml.common.discovery.ASMDataTable)}
     */
    @Override
    public List<ModContainer> discover(final ModCandidate candidate, final ASMDataTable table) {
        List<ModContainer> foundMods = Lists.newArrayList();

        try(JarFile jarFile = new JarFile(candidate.getModContainer())) {
            //<editor-fold desc="ASM Table">
            ASMTable asmTable = new ASMTable();
            try {
                asmTable.load(jarFile);
            } catch(IOException e) {
                ReTweak.LOGGER_RETWEAK.error("Failed to load the ASM Table! The mod will not be loaded correctly!", e);
            }
            //</editor-fold>

            //<editor-fold desc="Mod info metadata">
            ZipEntry modInfoEntry = jarFile.getEntry("mcmod.info");
            MetadataCollection metadata = null;
            if (modInfoEntry != null) {
                ReTweak.LOGGER_RETWEAK.info("Found mcmod.info for mod {}", candidate.getModContainer().getName());

                try(InputStream inputStream = jarFile.getInputStream(modInfoEntry)) {
                    metadata = MetadataCollection.from(inputStream, candidate.getModContainer().getName());
                } catch(IOException e) {
                    ReTweak.LOGGER_RETWEAK.error("Failed to open stream to \"mcmod.info\" entry!", e);
                    ReTweak.LOGGER_RETWEAK.debug("Jar File: {}", jarFile.getName());
                }
            }
            if (metadata == null) {
                ReTweak.LOGGER_RETWEAK.info("Found no mcmod.info for mod {}", candidate.getModContainer().getName());
                metadata = MetadataCollection.from(null, "");
            }
            //</editor-fold>

            //<editor-fold desc="Manifest">
            ZipEntry manifestEntry = jarFile.getEntry("META-INF/MANIFEST.MF");
            if (manifestEntry != null) {
                try(InputStream inputStream = jarFile.getInputStream(manifestEntry)) {
                    Manifest manifest = new Manifest();
                    try {
                        manifest.read(inputStream);
                    } catch (IOException e) {
                        ReTweak.LOGGER_RETWEAK.error("Failed to read manifest!", e);
                        ReTweak.LOGGER_RETWEAK.debug("Jar File: {}, Jar Entry: {}", jarFile.getName(), manifestEntry.getName());
                    }

                    if (manifest.getEntries().containsKey("FMLCorePlugin")) {
                        ReTweak.LOGGER_RETWEAK.warn("Mod manifest contains coremod... this is not supported!");
                        ReTweak.LOGGER_RETWEAK.debug("Jar File: {}, Jar Entry: {}", jarFile.getName(), manifestEntry.getName());
                    }
                } catch (IOException e) {
                    ReTweak.LOGGER_RETWEAK.error("Failed to open stream to manifest!", e);
                    ReTweak.LOGGER_RETWEAK.debug("Jar File: {}, Jar Entry: {}", jarFile.getName(), manifestEntry.getName());
                }
            }
            //</editor-fold>

            //<editor-fold desc="Entries">
            Enumeration<JarEntry> entryEnumeration = jarFile.entries();
            while(entryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = entryEnumeration.nextElement();
                Matcher matcher = ITypeDiscoverer.classFile.matcher(jarEntry.getName());
                if (matcher.matches()) {//Check if jar entry is class
                    candidate.addClassEntry(jarEntry.getName());

                    try(InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                        ReTweakASMModParser reTweakASMModParser;
                        try {
                            reTweakASMModParser = new ReTweakASMModParser(gameVersion, inputStream);
                            reTweakASMModParser.sendToTable(table, candidate);

                            ReTweakModContainer reTweakModContainer = ReTweakModContainerFactory
                                    .instance()
                                    .build(
                                            reTweakASMModParser,
                                            asmTable,
                                            candidate.getModContainer(),
                                            (ReTweakModCandidate) candidate
                                    );
                            if (reTweakModContainer != null) {
                                table.addContainer(reTweakModContainer);
                                foundMods.add(reTweakModContainer);
                                reTweakModContainer.bindMetadata(metadata);
                            }
                        } catch (IOException e) {
                            ReTweak.LOGGER_RETWEAK.error("Failed to open stream to class file!", e);
                            ReTweak.LOGGER_RETWEAK.info("Jar File: {}, Jar Entry: {}", jarFile.getName(), jarEntry.getName());
                        }
                    } catch(IOException e) {
                        ReTweak.LOGGER_RETWEAK.warn("Failed to read a class file from the jar file!", e);
                    }
                }
            }
            //</editor-fold>
        } catch(IOException e) {
            ReTweak.LOGGER_RETWEAK.error("Caught IOException while creating JarFile in JarAnnotationDiscoverer!", e);

//            ReTweak.LOGGER_RETWEAK.error("Failed to close jarFile!", e);
//            ReTweak.LOGGER_RETWEAK.info("Jar File: {}", jarFile.getName());
        }
        return foundMods;
    }

}
