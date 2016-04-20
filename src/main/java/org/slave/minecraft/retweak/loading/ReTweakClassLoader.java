package org.slave.minecraft.retweak.loading;

import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
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

    @Override
    public Class<?> loadClass(final String name) throws ClassNotFoundException {
        return findClass(name);
    }

    @Override
    protected Class<?> findClass(final String name) throws ClassNotFoundException {
        //TODO
        return super.findClass(name);
    }

    public LaunchClassLoader getRealParent() {
        return parent;
    }

    public static ReTweakClassLoader getInstance() {
        return ReTweakClassLoader.instance;
    }

}
