package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.ImmutableMap;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by Master on 4/26/2016 at 8:27 PM.
 *
 * @author Master
 */
public final class ReTweakClassLoader extends URLClassLoader {

    private static Map<GameVersion, ReTweakClassLoader> reTweakClassLoaderMap;

    private final LaunchClassLoader parent;
    private final GameVersion gameVersion;

    private ReTweakClassLoader(final LaunchClassLoader parent, final GameVersion gameVersion) {
        super(new URL[0], parent);
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        this.parent = parent;
        this.gameVersion = gameVersion;
        if (this.parent == null || super.getParent() == null) {
            throw new NullPointerException(
                "Parent class loader is null!"
            );
        }
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        ReTweakResources.RETWEAK_LOGGER.debug(
            "LOAD: {}, RESOLVE: {}",
            name,
            resolve
        );
        //TODO
        return super.loadClass(
            name,
            resolve
        );
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        ReTweakResources.RETWEAK_LOGGER.debug(
                "FIND: {}",
                name
        );
        //TODO
        return super.findClass(name);
    }

    void addFile(final File file) {
        try {
            super.addURL(
                file.toURI().toURL()
            );
        } catch(MalformedURLException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to add file \"" + file.getPath() + "\" to the class loader!",
                e
            );
        }
    }

    static ReTweakClassLoader getReTweakClassLoader(final GameVersion gameVersion) {
        return reTweakClassLoaderMap.get(gameVersion);
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.ReTweakSetup#initReTweakClassLoader(net.minecraft.launchwrapper.LaunchClassLoader)}
     */
    private static void init(final LaunchClassLoader launchClassLoader) {
        if (ReTweakClassLoader.reTweakClassLoaderMap != null) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Already init class loader? This should not happen!"
            );
            return;
        }


        Map<GameVersion, ReTweakClassLoader> reTweakClassLoaderMap = new EnumMap<>(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            reTweakClassLoaderMap.put(
                gameVersion,
                new ReTweakClassLoader(
                    launchClassLoader,
                    gameVersion
                )
            );
        }

        ReTweakClassLoader.reTweakClassLoaderMap = ImmutableMap.copyOf(
            reTweakClassLoaderMap
        );
    }

}
