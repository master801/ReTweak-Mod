package org.slave.minecraft.retweak.load;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.ReTweakClassASM;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Master on 7/12/2018 at 11:53 AM.
 *
 * @author Master
 */
public final class ReTweakClassLoader extends URLClassLoader {

    private static final List<String> ALWAYS_TWEAK_CLASS_LIST = Lists.newArrayList(
            "net.minecraftforge.common.Configuration"
    );

    private static Field fieldClassLoaderExceptions;
    private static Field fieldTransformerExceptions;

    private static Map<GameVersion, ReTweakClassLoader> INSTANCES = Maps.newEnumMap(GameVersion.class);

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
        for(String classLoaderException : ReTweakClassLoader.getClassLoaderExceptions(parent)) {
            if (StringUtils.startsWithAny(name, classLoaderException)) return super.loadClass(name, resolve);
        }
        for(String transformerException : ReTweakClassLoader.getTransformerExceptions(parent)) {
            if (StringUtils.startsWithAny(name, transformerException)) return super.loadClass(name, resolve);
        }
        if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("LOAD: {}, RESOLVE: {}", name, resolve);

        String newName;
        if (name.contains(".")) {//Package
            newName = name.replace('.', '/');
        } else {
            newName = name;
        }

        Class<?> loadedClass = null;

        InputStream inputStream;

        boolean tweak = false;
        inputStream = parent.getResourceAsStream(newName + ".class");//Load parent class before loading our custom class for transformation. This is to avoid mismatching classes - For example, FMLPreInitializationEvent. We don't want to load a custom version of this class, but the parent classloader's version, instead.
        if (inputStream == null || (gameVersion.getTweakClass().hasMigrationClass(newName) && !ALWAYS_TWEAK_CLASS_LIST.contains(newName))) {//Load our class if parent is null or we have a migration class
            inputStream = super.getResourceAsStream(newName + ".class");
            tweak = true;
        }

        if (inputStream != null && tweak) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaking class {}", newName);
            try {
                ReTweakClassASM reTweakClassASM = ReTweakClassASM.instance(gameVersion);
                byte[] tweakedClassData = reTweakClassASM.tweak(inputStream);

                if (!ArrayHelper.isNullOrEmpty(tweakedClassData)) {
                    if (ReTweak.DEBUG) BasicTransformer.writeClassFile(ReTweak.FILE_DIRECTORY_RETWEAK_ASM, newName, tweakedClassData);

                    loadedClass = super.defineClass(
                            name,//Do not load newName variable - newName is only used for looking
                            tweakedClassData,
                            0,
                            tweakedClassData.length
                    );
                }
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    ReTweak.LOGGER_RETWEAK.error(
                            "Failed to close input stream while loading class!",
                            e
                    );
                }
            }
        }

        if (loadedClass == null) loadedClass = super.loadClass(name, resolve);
        return loadedClass;
    }

    public void addFile(final File file) {
        if (file == null || !file.isFile()) return;
        try {
            super.addURL(file.toURI().toURL());
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

    /**
     * {@link cpw.mods.fml.common.ModClassLoader#clearNegativeCacheFor(java.util.Set)}
     */
    public void clearNegativeCacheFor(final Set<String> classList) {
        parent.clearNegativeEntries(classList);
    }

    public void addExclusion(final String path) {
        getTransformerExceptions(parent).add(path);
        getClassLoaderExceptions(parent).add(path);
    }

    public static ReTweakClassLoader getInstance(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        return ReTweakClassLoader.INSTANCES.get(gameVersion);
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
