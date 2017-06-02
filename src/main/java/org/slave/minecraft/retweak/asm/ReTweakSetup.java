package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.mod.ReTweakClassLoader;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by Master801 on 3/18/2016 at 10:30 PM.
 *
 * @author Master801
 */
public final class ReTweakSetup implements IFMLCallHook {

    private static boolean deobfuscatedEnvironment = false;

    @Override
    public void injectData(final Map<String, Object> data) {
        ReTweakSetup.deobfuscatedEnvironment = !(boolean)data.get("runtimeDeobfuscationEnabled");
        ReTweakSetup.initReTweakClassLoader(
            (LaunchClassLoader) data.get("classLoader")
        );
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

    public static boolean isDeobfuscatedEnvironment() {
        return ReTweakSetup.deobfuscatedEnvironment;
    }

    private static void initReTweakClassLoader(final LaunchClassLoader launchClassLoader) {
        try {
            ReflectionHelper.invokeMethod(
                ReflectionHelper.getMethod(
                    ReTweakClassLoader.class,
                    "init",
                    new Class<?>[] {
                        LaunchClassLoader.class
                    }
                ),
                null,
                new Object[] {
                    launchClassLoader
                    }
            );
        } catch(InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                "Failed to init class loaders!",
                e
            );
        }
    }

}
