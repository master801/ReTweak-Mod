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
//        parent.addURL(file.toURI().toURL());//TODO Should I add this to the parent?
        addURL(file.toURI().toURL());
    }

    public LaunchClassLoader getRealParent() {
        return parent;
    }

    public static ReTweakClassLoader getInstance() {
        return ReTweakClassLoader.instance;
    }

}
