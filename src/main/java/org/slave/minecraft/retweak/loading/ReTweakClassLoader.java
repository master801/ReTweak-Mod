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
import java.util.HashMap;
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
    private static final HashMap<GameVersion, ReTweakClassLoader> CLASS_LOADERS = new HashMap<>();

    private final LaunchClassLoader parent;
    private ReTweakSRG srg;
    private final GameVersion gameVersion;

    private ReTweakClassLoader(final LaunchClassLoader parent, final GameVersion gameVersion) {
        super(
                new URL[0],
                parent
        );
        this.parent = parent;
        this.gameVersion = gameVersion;
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        if (srg == null) {
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
        } else if (name.startsWith("java")) {
            return super.loadClass(
                    name,
                    false
            );
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

            //Enable SRG for only JIT
            if (ReTweakConfig.INSTANCE.getCompilationMode() == CompilationMode.JIT) {
                if (srg != null) {
                    srg.srg(classNode);
                } else {
                    ReTweakResources.RETWEAK_LOGGER.error(
                            "Failed to load SRGs?"
                    );
                }
            }

            try {
                ReTweakTweakHandler.INSTANCE.tweak(
                        classNode,
                        reTweakModCandidate.getGameVersion()
                );
            } catch(TweakException e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        Kirai.from(
                                "Disabled ReTweak mod candidate \"{file_path}\" because an exception was caught while tweaking!"
                        ).put(
                                "file_path",
                                reTweakModCandidate.getSource().getPath()
                        ).format().toString(),
                        e
                );
                reTweakModCandidate.setEnabled(false);
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
                returnClass = super.loadClass(
                        name,
                        false
                );
            } catch(ClassNotFoundException e) {
                //Ignore
            }
        }

        if (gameVersion.getClasses().contains(name)) {
            //noinspection StringBufferReplaceableByString
            returnClass = super.loadClass(
                    gameVersion.getInterpreterPackagePrefix() + name,
                    true
            );
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
     * Returns jar file for given candidate<br/>
     * Must have at least one available class file!
     *
     * @param reTweakModCandidate
     *
     * @return Unclosed JarFile
     *
     * @throws IOException
     */
    JarFile findJarFileForCandidate(ReTweakModCandidate reTweakModCandidate) throws IOException {
        if (reTweakModCandidate == null || (reTweakModCandidate.getClasses() == null || reTweakModCandidate.getClasses().isEmpty())) return null;
        URL url = super.findResource(reTweakModCandidate.getClasses().get(0) + ".class");
        if (url == null) return null;
        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
        return jarURLConnection.getJarFile();
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.ReTweakSetup#call()}
     */
    private void loadSRG() {
        Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;

        if (srg != null) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Should not have loaded SRGs! This is an unknown error!"
            );
            return;
        }
        srg = new ReTweakSRG(gameVersion);
    }

    private ReTweakModCandidate findCandidate(final String name) throws IOException {
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

                if (reTweakModCandidate.getSource().getName().equals(jarFile.getName().substring(index))) {
                    jarFile.close();
                    return reTweakModCandidate;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("FinalStaticMethod")
    public static final void createClassLoaders(final LaunchClassLoader launchClassLoader) {
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakClassLoader.CLASS_LOADERS.put(
                    gameVersion,
                    new ReTweakClassLoader(
                            launchClassLoader,
                            gameVersion
                    )
            );
        }
    }

    public static ReTweakClassLoader getClassLoader(final GameVersion gameVersion) {
        return ReTweakClassLoader.CLASS_LOADERS.get(gameVersion);
    }

}
