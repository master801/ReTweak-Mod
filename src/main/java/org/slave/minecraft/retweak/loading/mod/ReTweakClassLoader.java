package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.commons.lang3.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.loading.mod.vandy.ReTweakClassVisitor;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by Master on 4/26/2016 at 8:27 PM.
 *
 * @author Master
 */
public final class ReTweakClassLoader extends URLClassLoader {

    private static final File FILE_RETWEAK_ASM_DIRECTORY = new File(
        ReTweakResources.RETWEAK_DIRECTORY,
        "asm"
    );

    private static final String[] EXCLUSIONS = new String[] {
        "java.",
        "sun.",
        "org.lwjgl.",
        "org.apache.logging.",
        "net.minecraft.launchwrapper.",

        "javax.",
        "argo.",
        "org.objectweb.asm.",
        "com.google.common.",
        "org.bouncycastle.",
        "net.minecraft.launchwrapper.injector."
    };

    private static final Set<Pattern> SET_CLASSLOADER_PARENT_EXCLUSIONS = Sets.newHashSet(
        Pattern.compile(
            "^(cpw\\.mods\\.fml\\.).+",
            Pattern.MULTILINE
        ),
        Pattern.compile(
            "^(net\\.minecraftforge\\.).+",
            Pattern.MULTILINE
        )
    );

    private static Map<GameVersion, ReTweakClassLoader> reTweakClassLoaderMap;

    private final LaunchClassLoader parent;
    private final GameVersion gameVersion;

    private ReTweakClassLoader(final LaunchClassLoader parent, final GameVersion gameVersion) {
        super(
            new URL[0],
            parent
        );
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        this.parent = parent;
        this.gameVersion = gameVersion;
        if (this.parent == null || super.getParent() == null) {
            throw new NullPointerException("Parent class loader is null!");
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

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                "FIND: {}",
                name
            );
        }
        //TODO
        return super.findClass(name);
    }

    @Override
    protected Class<?> loadClass(final String name, final boolean resolve) throws ClassNotFoundException {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.debug(
                "LOAD: {}, RESOLVE: {}",
                name,
                resolve
            );
        }

        if (StringUtils.startsWithAny(name, ReTweakClassLoader.EXCLUSIONS)) {
            return super.loadClass(
                name,
                resolve
            );
        }

        for(Pattern pattern : ReTweakClassLoader.SET_CLASSLOADER_PARENT_EXCLUSIONS) {
            if (pattern.matcher(name).matches() && gameVersion.getClassInfo(name) == null) return super.getParent().loadClass(name);
        }

        Class<?> returnClass = null;
        InputStream inputStream = super.getResourceAsStream(name.replace('.', '/') + ".class");
        if (inputStream != null) {
            try {
                ClassReader classReader = new ClassReader(inputStream);
                ClassWriter classWriter = new ClassWriter(0);
                ClassVisitor classVisitor = new ReTweakClassVisitor(
                    Opcodes.ASM5,
                    gameVersion,
                    classWriter
                );
                classReader.accept(
                    classVisitor,
                    0
                );

                byte[] classData = classWriter.toByteArray();

                //<editor-fold desc="DEBUG">
                if (ReTweakResources.DEBUG) {
                    String outputDirName = name.replace('.', '/');
                    if (outputDirName.lastIndexOf('/') != -1) {
                        outputDirName = outputDirName.substring(
                            0,
                            outputDirName.lastIndexOf('/')
                        );
                    }
                    File outputDir = new File(
                        ReTweakClassLoader.FILE_RETWEAK_ASM_DIRECTORY,
                        outputDirName
                    );

                    String outputName = name.replace('.', '/');
                    if (outputName.lastIndexOf('/') != -1) {
                        outputName = outputName.substring(
                            outputName.lastIndexOf('/') + 1,
                            outputName.length()
                        );
                    }

                    File outputFile = new File(
                        outputDir,
                        outputName + ".class"
                    );

                    if (!outputFile.getParentFile().exists()) outputFile.getParentFile().mkdirs();

                    FileOutputStream fileOutputStream = null;
                    try {
                        fileOutputStream = new FileOutputStream(outputFile);
                        fileOutputStream.write(
                            classData,
                            0,
                            classData.length
                        );
                    } catch(IOException e) {
                        //Ignore
                    } finally {
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch(IOException e) {
                                //Ignore
                            }
                        }
                    }
                }
                //</editor-fold>

                returnClass = super.defineClass(
                    name,
                    classData,
                    0,
                    classData.length
                );
            } catch(IOException e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                    String.format(
                        "Failed to transform class \"%s\"!",
                        name
                    ),
                    e
                );
            } finally {
                try {
                    inputStream.close();
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.error(
                        String.format(
                            "Failed to close inputstream while transforming class \"%s\"!",
                            name
                        ),
                        e
                    );
                }
            }
        }

        if (returnClass == null) {
            returnClass = super.loadClass(
                name,
                resolve
            );
        }
        return returnClass;
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

}
