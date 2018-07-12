package org.slave.minecraft.retweak.load;

import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

/**
 * Created by Master on 7/12/2018 at 11:53 AM.
 *
 * @author Master
 */
public final class ReTweakClassLoader extends URLClassLoader {

    private static Field fieldClassLoaderExceptions;
    private static Field fieldTransformerExceptions;

    private final GameVersion gameVersion;
    private final LaunchClassLoader parent;

    public ReTweakClassLoader(final GameVersion gameVersion, final LaunchClassLoader parent) {
        super(new URL[0], parent);
        this.gameVersion = gameVersion;
        this.parent = parent;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("FIND: {}", name);
        return super.findClass(name);
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("LOAD: {}, RESOLVE: {}", name, resolve);
        return super.loadClass(name, resolve);
    }

    public void addFile(final File file) {
        if (file == null || !file.isFile()) return;
        try {
            super.addURL(
                    file.toURI().toURL()
            );
        } catch (MalformedURLException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to add file to classpath!",
                    e
            );
            ReTweak.LOGGER_RETWEAK.debug(
                    "Game Version: {}, File: {}",
                    gameVersion.getVersion(),
                    file.getPath()
            );
        }
    }

    private static Set<String> getClassLoaderExceptions(final LaunchClassLoader launchClassLoader) {
        if (launchClassLoader == null) return null;
        try {
            if (ReTweakClassLoader.fieldClassLoaderExceptions == null) {
                ReTweakClassLoader.fieldClassLoaderExceptions = ReflectionHelper.getField(
                        LaunchClassLoader.class,
                        "classLoaderExceptions"
                );
            }
        } catch (NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"classLoaderExceptions\" in class LaunchClassLoader!",
                    e
            );
            return null;
        }
        try {
            return ReflectionHelper.getFieldValue(
                    ReTweakClassLoader.fieldClassLoaderExceptions,
                    launchClassLoader
            );
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get the value of field \"classLoaderExceptions\"!",
                    e
            );
            return null;
        }
    }

    private static Set<String> getTransformerExceptions(final LaunchClassLoader launchClassLoader) {
        if (launchClassLoader == null) return null;
        try {
            if (ReTweakClassLoader.fieldTransformerExceptions == null) {
                ReTweakClassLoader.fieldTransformerExceptions = ReflectionHelper.getField(
                        LaunchClassLoader.class,
                        "transformerExceptions"
                );
            }
        } catch (NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"transformerExceptions\" in class LaunchClassLoader!",
                    e
            );
            return null;
        }

        try {
            return ReflectionHelper.getFieldValue(
                    ReTweakClassLoader.fieldTransformerExceptions,
                    launchClassLoader
            );
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get the value of field \"transformerExceptions\"!",
                    e
            );
            return null;
        }
    }

}
