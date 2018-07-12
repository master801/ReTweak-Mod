package org.slave.minecraft.retweak.load.mod;

import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.discovery.ModCandidate;
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

    private static final Pattern PATTERN_ZIP_JAR_MATCHER = Pattern.compile(
        ".+(\\.(jar|zip))$",
        Pattern.MULTILINE
    );

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

        List<ModCandidate> candidates = getCandidates();
        for(File archiveFile : archiveFiles) {
            /*
            candidates.add(
                new ReTweakModCandidate(
                    gameVersion,
                    archiveFile,
                    archiveFile,
                    ContainerType.JAR
                )
            );
            */
            //TODO
        }
    }

    private static Field fieldCandidates = null;

    /**
     * {@link cpw.mods.fml.common.discovery.ModDiscoverer#candidates}
     */
    private List<ModCandidate> getCandidates() {
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
            return null;
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class ArchiveFileFilter extends AbstractFileFilter {

        static final AbstractFileFilter INSTANCE = new ArchiveFileFilter();

        @Override
        public boolean accept(final File file) {
            return file.isFile() && ReTweakModDiscoverer.PATTERN_ZIP_JAR_MATCHER.matcher(file.getName()).matches();
        }

    }

}
