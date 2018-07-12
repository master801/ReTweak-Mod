package org.slave.minecraft.retweak.load.util.discoverer;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ITypeDiscoverer;
import cpw.mods.fml.common.discovery.ModCandidate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.asm.ReTweakModContainer;
import org.slave.minecraft.retweak.load.mod.ReTweakModCandidate;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainerFactory;
import org.slave.minecraft.retweak.load.mod.asm.ReTweakASMModParser;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.zip.ZipEntry;

/**
 * Created by Master on 7/11/2018 at 11:34 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class JarAnnotationDiscoverer implements ITypeDiscoverer {

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

        JarFile jarFile = null;

        try {
            jarFile = new JarFile(candidate.getModContainer());

            ZipEntry modInfo = jarFile.getEntry("mcmod.info");
            MetadataCollection metadata;
            if (modInfo != null) {
                ReTweak.LOGGER_RETWEAK.info("Found mcmod.info for mod {}", candidate.getModContainer().getName());
                metadata = MetadataCollection.from(jarFile.getInputStream(modInfo), candidate.getModContainer().getName());
            } else {
                ReTweak.LOGGER_RETWEAK.info("Found no mcmod.info for mod {}", candidate.getModContainer().getName());
                metadata = MetadataCollection.from(null, "");
            }

            Enumeration<JarEntry> entryEnumeration = jarFile.entries();
            while(entryEnumeration.hasMoreElements()) {
                JarEntry jarEntry = entryEnumeration.nextElement();
                Matcher matcher = ITypeDiscoverer.classFile.matcher(jarEntry.getName());
                if (matcher.matches()) {
                    ReTweakASMModParser reTweakASMModParser;
                    try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                        reTweakASMModParser = new ReTweakASMModParser(gameVersion, inputStream);
                        reTweakASMModParser.sendToTable(table, candidate);
                    }

                    ReTweakModContainer reTweakModContainer = ReTweakModContainerFactory.instance().build(
                            reTweakASMModParser,
                            candidate.getModContainer(),
                            (ReTweakModCandidate)candidate
                    );
                    if (reTweakModContainer != null) {
                        table.addContainer(reTweakModContainer);
                        foundMods.add(reTweakModContainer);
                        reTweakModContainer.bindMetadata(metadata);
                    }
                }
            }
        } catch (IOException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Caught IOException while creating JarFile for JarAnnotationDiscoverer!",
                    e
            );
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    ReTweak.LOGGER_RETWEAK.error(
                            "Failed to close jarFile!",
                            e
                    );
                }
            }
        }

        return foundMods;
    }

}
