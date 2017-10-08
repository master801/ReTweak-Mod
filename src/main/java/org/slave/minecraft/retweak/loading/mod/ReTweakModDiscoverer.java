package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.ModClassLoader;
import cpw.mods.fml.common.discovery.ContainerType;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.discovery.ModDiscoverer;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
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
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
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

        List<ModCandidate> modCandidateList = Lists.newArrayList();
        for(File archiveFile : archiveFiles) {
            modCandidateList.add(
                new ReTweakModCandidate(
                    gameVersion,
                    archiveFile,
                    archiveFile,
                    ContainerType.JAR
                )
            );
        }

        try {
            setCandidates(
                modCandidateList
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Caught exception while setting candidates!",
                e
            );
        }
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModDiscoverer#candidates}
     */
    private List<ModCandidate> getCandidates() throws NoSuchFieldException, IllegalAccessException {
        return ReflectionHelper.getFieldValue(
            ReflectionHelper.getField(
                ModDiscoverer.class,
                "candidates"
            ),
            this
        );
    }

    /**
     * {@link cpw.mods.fml.common.discovery.ModDiscoverer#candidates}
     */
    private void setCandidates(final List<ModCandidate> modCandidateList) throws NoSuchFieldException, IllegalAccessException {
        ReflectionHelper.setFieldValue(
            ReflectionHelper.getField(
                ModDiscoverer.class,
                "candidates"
            ),
            this,
            modCandidateList
        );
    }

    private static final class ArchiveFileFilter extends AbstractFileFilter {

        static final AbstractFileFilter INSTANCE = new ArchiveFileFilter();

        private ArchiveFileFilter() {
            final Object _INTERNAL_USAGE_ONLY = null;
        }

        @Override
        public boolean accept(final File file) {
            return file.isFile() && ReTweakModDiscoverer.PATTERN_ZIP_JAR_MATCHER.matcher(file.getName()).matches();
        }

    }

}
