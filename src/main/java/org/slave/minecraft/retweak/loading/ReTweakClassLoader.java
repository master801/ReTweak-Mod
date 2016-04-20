package org.slave.minecraft.retweak.loading;

import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.IOHelper;
import org.slave.minecraft.retweak.loading.tweaker.ReTweakTweakLoaderIndexer;
import org.slave.minecraft.retweak.loading.tweaker.TweakLoader;
import org.slave.minecraft.retweak.loading.tweaker.Tweak;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * <p>
 *     {@link cpw.mods.fml.common.ModClassLoader}
 * </p>
 *
 * Created by Master801 on 3/18/2016 at 10:38 PM.
 *
 * @author Master801
 */
public final class ReTweakClassLoader extends URLClassLoader {

    private static final Boolean DEBUG_WRITE_ORIGINAL = Boolean.valueOf(System.getProperty(
            "org.slave.retweak.class_loader.debug.write_original",
            Boolean.FALSE.toString()
    ));
    private static final Boolean DEBUG_WRITE_MODIFIED = Boolean.valueOf(System.getProperty(
            "org.slave.retweak.class_loader.debug.write_modified",
            Boolean.FALSE.toString()
    ));

    /**
     * {@link org.slave.minecraft.retweak.asm.ReTweakSetup}
     */
    private static ReTweakClassLoader instance = null;

    private final LaunchClassLoader parent;

    public ReTweakClassLoader(LaunchClassLoader parent) {
        super(
                new URL[0],
                parent
        );
        this.parent = parent;
    }

    public void loadFile(File file) throws MalformedURLException {
        if (file == null) throw new NullPointerException();
        addURL(file.toURI().toURL());
    }

    public LaunchClassLoader getRealParent() {
        return parent;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        String[] split = name.split(
                "#-#",
                2
        );
        if (split.length == 2) {
            return findClass(
                    split[0],
                    SupportedGameVersion.valueOf(split[1])
            );
        }

        return findClass(
                name,
                null
        );
    }

    protected Class<?> findClass(final String name, final SupportedGameVersion supportedGameVersion) throws ClassNotFoundException {
        byte[] classBytes;
        String newName = name;
        if (name.indexOf('.') != -1 && !name.toLowerCase().endsWith(".class")) {
            newName = newName.replace('.', '/');
            newName += ".class";
        }
        classBytes = getClassBytes(newName);
        if (!ArrayHelper.isNullOrEmpty(classBytes)) {
            return loadClassFromBytes(//Null for third parameter -- not needed for default implementation
                    name,
                    classBytes,
                    supportedGameVersion
            );
        }
        Class<?> returnClass = null;
        try {
            returnClass = super.findClass(name);
        } catch(ClassNotFoundException e) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Caught an exception while finding class \"" + name + "\"!",
                    e
            );
        }

        if (returnClass == null) throw new ClassNotFoundException();//TODO ASM the return class?
        return returnClass;
    }

    Class<?> loadClassFromBytes(final String name, byte[] classBytes, SupportedGameVersion supportedGameVersion) throws ClassNotFoundException {
        final byte[] originalClassBytes = classBytes;

        if (ReTweakClassLoader.DEBUG_WRITE_ORIGINAL) {
            BasicTransformer.writeClassFile(
                    "original",
                    name,
                    originalClassBytes
            );
        }
        if (supportedGameVersion != null) {
            TweakLoader tweakLoader = ReTweakTweakLoaderIndexer.INSTANCE.getTweakLoader(supportedGameVersion);
            if (tweakLoader != null) {
                Iterable<Tweak> tweaks = tweakLoader.getTweaks();
                if (tweaks != null) {
                    for(Tweak tweak : tweaks) {
                        classBytes = tweak.transform(
                                name,
                                name,
                                classBytes
                        );
                    }
                }
            }
        }
        if (ReTweakClassLoader.DEBUG_WRITE_MODIFIED) {
            BasicTransformer.writeClassFile(
                    "modified",
                    name,
                    classBytes
            );
        }
        return super.defineClass(
                name,
                classBytes,
                0,
                classBytes.length
        );
    }

    byte[] getClassBytes(final String name) {
        byte[] classBytes = null;
        URL url = super.findResource(name);
        if (url != null) {
            try {
                InputStream inputStream = url.openStream();
                classBytes = IOHelper.toByteArray(inputStream);
                inputStream.close();
            } catch(IOException e) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Caught an exception while loading class " + name + "!",
                        e
                );
            }
        }
        return classBytes;
    }

    public static ReTweakClassLoader getInstance() {
        return ReTweakClassLoader.instance;
    }

}
