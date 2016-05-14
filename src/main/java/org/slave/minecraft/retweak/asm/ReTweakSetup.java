package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.ReTweakClassLoader;
import org.slave.minecraft.retweak.loading.ReTweakDeobfuscation;
import org.slave.minecraft.retweak.resources.ReTweakConfig;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.Map;

/**
 * Created by Master801 on 3/18/2016 at 10:30 PM.
 *
 * @author Master801
 */
public final class ReTweakSetup implements IFMLCallHook {

    private static boolean deobfuscatedEnvironment = false;

    @Override
    public void injectData(Map<String, Object> data) {
        try {
            ReflectionHelper.setFieldValue(
                    ReflectionHelper.getField(
                            ReTweakClassLoader.class,
                            "instance"
                    ),
                    null,
                    new ReTweakClassLoader((LaunchClassLoader)data.get("classLoader"))
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Failed to create ReTweak classloader instance! ReTweak mods will not be able to load properly!",
                    e
            );
        }
        ReTweakSetup.deobfuscatedEnvironment = !(boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public Void call() throws Exception {
        if (ReTweakResources.RETWEAK_PLAY_DIRECTORY.isDirectory()) ReTweakDeobfuscation.INSTANCE.loadSRGs(ReTweakResources.RETWEAK_PLAY_DIRECTORY);
        try {
            Object instance = ReflectionHelper.invokeMethod(
                    ReflectionHelper.getMethod(
                            ReTweakClassLoader.class,
                            "getInstance",
                            new Class<?>[0]
                    ),
                    null,
                    new Object[0]
            );

            ReflectionHelper.invokeMethod(
                    ReflectionHelper.getMethod(
                            ReTweakClassLoader.class,
                            "loadSRGs",
                            new Class<?>[0]
                    ),
                    instance,
                    new Object[0]
            );
        } catch(Exception e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Failed to load SRGs!",
                    e
            );
        }
        ReTweakConfig.INSTANCE.update(true);
        return null;
    }

    public static boolean isDeobfuscatedEnvironment() {
        return ReTweakSetup.deobfuscatedEnvironment;
    }

}
