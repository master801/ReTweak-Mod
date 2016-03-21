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

    private static ReTweakClassLoader instance = null;

    private LaunchClassLoader parent = null;

    public ReTweakClassLoader(LaunchClassLoader parent) {
        super(new URL[0], parent);
        this.parent = parent;
    }

    public void loadMod(File mod) throws MalformedURLException {
        parent.addURL(mod.toURI().toURL());
    }

    public static ReTweakClassLoader getInstance() {
        return ReTweakClassLoader.instance;
    }

}
