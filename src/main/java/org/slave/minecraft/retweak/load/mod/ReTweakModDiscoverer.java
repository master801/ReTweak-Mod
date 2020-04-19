package org.slave.minecraft.retweak.load.mod;

import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.ModDiscoverer}
 * </p>
 *
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class ReTweakModDiscoverer extends ModDiscoverer {

    private static Field fieldCandidates = null;

    @NonNull
    private final GameVersion gameVersion;

    @Override
    public void findClasspathMods(final ModClassLoader modClassLoader) {
        //NOOP
        //No point in doing this since older mods will not be (compiled) on the classpath
    }

    @Override
    public void findModDirMods(final File modsDir, final File[] supplementalModFileCandidates) {
        if (modsDir == null || !modsDir.isDirectory()) return;
        Collection<File> archiveFiles = FileUtils.listFiles(
            modsDir,
            ArchiveFileFilter.INSTANCE,
            DirectoryFileFilter.DIRECTORY
        );

        for(File archiveFile : archiveFiles) {
            getCandidates().add(
                new ReTweakModCandidate(
                    gameVersion,
                    archiveFile,
                    archiveFile,
                    ContainerType.JAR
                )
            );
        }
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModDiscoverer#candidates}
     */
    @NonNull
    public List<ReTweakModCandidate> getCandidates() {
        try {
            if (ReTweakModDiscoverer.fieldCandidates == null) ReTweakModDiscoverer.fieldCandidates = ReflectionHelper.getField(ModDiscoverer.class, "candidates");
            return ReflectionHelper.getFieldValue(
                    ReTweakModDiscoverer.fieldCandidates,
                    this
            );
        } catch(IllegalAccessException | NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get candidates!",
                    e
            );
            throw new NullPointerException();
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ArchiveFileFilter extends AbstractFileFilter {

        private static Field fieldZipJar;

        static final AbstractFileFilter INSTANCE = new ArchiveFileFilter();

        @Override
        public boolean accept(final File file) {
            return file.isFile() && ArchiveFileFilter.getPatternZipJar().matcher(file.getName()).matches();
        }

        @NonNull
        static Pattern getPatternZipJar() {
            try {
                if (ArchiveFileFilter.fieldZipJar == null) ArchiveFileFilter.fieldZipJar = ReflectionHelper.getField(ModDiscoverer.class, "zipJar");
                return ReflectionHelper.getFieldValue(ArchiveFileFilter.fieldZipJar, null);
            } catch(NoSuchFieldException | IllegalAccessException e) {
                ReTweak.LOGGER_RETWEAK.error("Failed to get Field Pattern \"zipJar\"", e);
            }
            throw new NullPointerException("No pattern for \"zipJar\"!");
        }

    }

}
