package org.slave.minecraft.retweak.loading;

import net.minecraft.launchwrapper.LaunchClassLoader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.helpers.IOHelper;
import org.slave.minecraft.retweak.asm.visitors.DeobfuscationClassNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * Created by Master on 4/26/2016 at 8:27 PM.
 *
 * @author Master
 */
public final class ReTweakClassLoader extends URLClassLoader {

    /**
     * {@link org.slave.minecraft.retweak.asm.ReTweakSetup#injectData(java.util.Map)}
     */
    private static ReTweakClassLoader instance;

    private final LaunchClassLoader parent;

    public ReTweakClassLoader(final LaunchClassLoader parent) {
        super(
                new URL[0],
                parent
        );
        this.parent = parent;
    }

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        if (name.startsWith("net.minecraft.")) throw new IllegalStateException("Cannot directly access \"net.minecraft.\" classes!");

        Class<?> returnClass = null;

        try {
            ReTweakModCandidate reTweakModCandidate = getCandidate(name);

            if (reTweakModCandidate == null) throw new IOException();//Jump out of try-catch block statement

            InputStream inputStream = super.getResourceAsStream(name.replace('.', '/') + ".class");
            ClassReader classReader = new ClassReader(IOHelper.toByteArray(inputStream));
            ClassNode classNode = new DeobfuscationClassNode(reTweakModCandidate.getGameVersion());
            classReader.accept(
                    classNode,
                    0
            );
            ClassWriter classWriter = new ClassWriter(0);
            classNode.accept(classWriter);
            inputStream.close();

            byte[] classData = classWriter.toByteArray();

            returnClass = super.defineClass(
                    name,
                    classData,
                    0,
                    classData.length
            );
        } catch(IOException ee) {
            //Ignore
        }

        if (returnClass == null) returnClass = super.loadClass(name);

        ReTweakResources.RETWEAK_LOGGER.info("LOAD: {}", name);
        return returnClass;
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        ReTweakResources.RETWEAK_LOGGER.info("FIND: {}", name);
        return super.findClass(name);
    }

    public void addFile(File file) {
        try {
            super.addURL(file.toURI().toURL());
        } catch(MalformedURLException e) {
            //Ignore
        }
    }

    private ReTweakModCandidate getCandidate(String name) throws IOException {
        URL url = super.findResource(name.replace('.', '/') + ".class");
        if (url == null) return null;
        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
        JarFile jarFile = jarURLConnection.getJarFile();
        for(GameVersion gameVersion : GameVersion.values()) {
            for(ReTweakModCandidate reTweakModCandidate : ReTweakLoader.INSTANCE.getReTweakModDiscoverer().getModCandidates(gameVersion)) {
                int index;
                index = jarFile.getName().lastIndexOf('\\');
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
