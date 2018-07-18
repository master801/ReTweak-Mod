package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.minecraft.retweak.load.ReTweakClassLoader;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by master on 2/25/18 at 9:27 PM
 *
 * @author master
 */
public final class ReTweakSetup implements IFMLCallHook {

    private LaunchClassLoader launchClassLoader;

    @Override
    public void injectData(final Map<String, Object> data) {
        launchClassLoader = (LaunchClassLoader)data.get("classLoader");
    }

    @Override
    public Void call() throws Exception {
        //Populate class loaders
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakClassLoader reTweakClassLoader = new ReTweakClassLoader(gameVersion, launchClassLoader);

            Field field = ReTweakClassLoader.class.getDeclaredField("INSTANCES");
            field.setAccessible(true);

            Map<GameVersion, ReTweakClassLoader> instances = (Map<GameVersion, ReTweakClassLoader>)field.get(null);
            instances.put(gameVersion, reTweakClassLoader);

            field.setAccessible(false);
        }
        return null;
    }

}
