package org.slave.minecraft.retweak.loading;

import com.github.pwittchen.kirai.library.Kirai;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.helpers.FileHelper;
import org.slave.lib.helpers.IOHelper;
import org.slave.minecraft.retweak.asm.ReTweakSetup;
import org.slave.minecraft.retweak.loading.capsule.CompilationMode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.Tweak.TweakException;
import org.slave.minecraft.retweak.resources.ReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.EnumMap;
import java.util.jar.JarFile;

/**
 * Created by Master on 4/26/2016 at 8:27 PM.
 *
 * @author Master
 */
public final class ReTweakClassLoader extends URLClassLoader {

    private static final Boolean OUTPUT_CLASS = Boolean.valueOf(System.getProperty(
            "org.slave.minecraft.retweak.class_loader.output_class",
            Boolean.FALSE.toString()
    ));
    private static final File ASM_CLASSES_DIRECTORY = new File(
            ReTweakResources.RETWEAK_DIRECTORY,
            "asm"
    );

    /**
     * {@link org.slave.minecraft.retweak.asm.ReTweakSetup#injectData(java.util.Map)}
     */
    private static ReTweakClassLoader instance;

    private final LaunchClassLoader parent;
    private EnumMap<GameVersion, ReTweakSRG> srgs;

    public ReTweakClassLoader(final LaunchClassLoader parent) {
        super(
                new URL[0],
                parent
        );
        this.parent = parent;
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        if (srgs == null) {
            throw new NullPointerException(
                    Kirai.from(
                            "Method \"{name}\" was not invoked!"
                    ).put(
                            "name",
                            "loadSRGs"
                    ).format().toString()
            );
        }
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("LOAD: {}", name);
        if (name.startsWith("net.minecraft.")) {
            if (ReTweakConfig.INSTANCE.getCompilationMode() == CompilationMode.INTERPRETER) {
                throw new IllegalStateException(
                        Kirai.from(
                                "Tried to access class \"{name}\"! Cannot directly access \"net.minecraft.*\" classes!"
                        ).put(
                                "name",
                                name
                        ).format().toString()
                );
            }
            String newName = name;
            if (!ReTweakSetup.isDeobfuscatedEnvironment()) throw new IllegalStateException("Non-deobfuscated environments are not yet supported!");
            return parent.loadClass(newName);
        }

        Class<?> returnClass = null;

        //Create class
        try {
            ReTweakModCandidate reTweakModCandidate = findCandidate(name);

            if (reTweakModCandidate == null) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "THIS IS NOT AN ERROR:\nCould not find mod candidate for class {}",
                        name
                );
                throw new IOException();//Jump out of try-catch block statement
            }

            final String resourceName = name.replace(
                    '.',
                    '/'
            ) + ".class";
            InputStream inputStream = super.getResourceAsStream(resourceName);
            ClassReader classReader = new ClassReader(IOHelper.toByteArray(inputStream));
            ClassNode classNode = new ClassNode();

            classReader.accept(
                    classNode,
                    0
            );

            if (srgs != null) {
                srgs.get(reTweakModCandidate.getGameVersion()).srg(classNode);
            } else {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "Failed to load SRGs?"
                );
            }

            try {
                ReTweakTweakHandler.INSTANCE.tweak(
                        classNode,
                        reTweakModCandidate.getGameVersion(),
                        null//TODO MUST NOT BE NULL!
                );
            } catch(TweakException e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        Kirai.from(
                                "Disabled ReTweak mod candidate \"{file_path}\" because an exception was caught while tweaking!"
                        ).put(
                                "file_path",
                                reTweakModCandidate.getFile().getPath()
                        ).format().toString(),
                        e
                );
                reTweakModCandidate.setEnabled(false);
                ReTweakModConfig.INSTANCE.update(false);
            }

            ClassWriter classWriter = new ClassWriter(0);
            classNode.accept(classWriter);
            inputStream.close();

            byte[] classData = classWriter.toByteArray();
            if (ReTweakClassLoader.OUTPUT_CLASS) {
                try {
                    File file = new File(
                            ReTweakClassLoader.ASM_CLASSES_DIRECTORY,
                            name.replace(
                                    '.',
                                    '/'
                            ) + ".class"
                    );
                    if (!file.getParentFile().exists()) FileHelper.createDirectory(file.getParentFile());
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(
                            classData,
                            0,
                            classData.length
                    );
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch(Exception e) {
                    //Ignore
                }
            }
            returnClass = super.defineClass(
                    name,
                    classData,
                    0,
                    classData.length
            );
        } catch(IOException ee) {
            //Ignore
        }

        if (returnClass == null) {
            try {
                returnClass = super.loadClass(name);
            } catch(ClassNotFoundException e) {
                //Ignore
            }
        }

        return returnClass;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("FIND: {}", name);
        return super.findClass(name);
    }

    public void addFile(File file) {
        try {
            super.addURL(file.toURI().toURL());
        } catch(MalformedURLException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    Kirai.from(
                            "Failed to add file \"{file_path}\" to the class loader!"
                    ).put(
                            "file_path",
                            file.getPath()
                    ).format().toString(),
                    e
            );
        }
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.ReTweakSetup#call()}
     */
    private void loadSRGs() {
        Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        if (srgs != null && !srgs.isEmpty()) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Should not have loaded SRGs! This is an unknown error!"
            );
            return;
        }
        srgs = new EnumMap<>(GameVersion.class);
        for(GameVersion gameVersion : GameVersion.values()) {
            srgs.put(
                    gameVersion,
                    new ReTweakSRG(gameVersion)
            );
        }
    }

    private ReTweakModCandidate findCandidate(String name) throws IOException {
        URL url = super.findResource(name.replace('.', '/') + ".class");
        if (url == null) return null;
        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        for(GameVersion gameVersion : GameVersion.values()) {
            for(ReTweakModCandidate reTweakModCandidate : ReTweakLoader.INSTANCE.getReTweakModDiscoverer().getModCandidates(gameVersion)) {
                int index;
                index = jarFile.getName().lastIndexOf('\\');

                //Get index of slash instead of back-slash (for unix systems)
                if (index == -1) index = jarFile.getName().lastIndexOf('/');
                if (index != -1) index++;

                if (reTweakModCandidate.getFile().getName().equals(jarFile.getName().substring(index))) return reTweakModCandidate;
            }
        }
        return null;
    }

    static ReTweakClassLoader getInstance() {
        return ReTweakClassLoader.instance;
    }

}
