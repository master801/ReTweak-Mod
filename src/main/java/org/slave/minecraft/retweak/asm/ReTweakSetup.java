package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.ReTweakClassLoader;
import org.slave.minecraft.retweak.loading.ReTweakDeobfuscation;
import org.slave.minecraft.retweak.loading.capsule.CompilationMode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
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
        ReTweakClassLoader.createClassLoaders((LaunchClassLoader)data.get("classLoader"));
        ReTweakSetup.deobfuscatedEnvironment = !(boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public Void call() throws Exception {
        ReTweakConfig.INSTANCE.update(true);


        if (ReTweakConfig.INSTANCE.getCompilationMode() == CompilationMode.JIT) {
            if (ReTweakResources.RETWEAK_PLAY_DIRECTORY.isDirectory()) {
                ReTweakDeobfuscation.INSTANCE.loadSRGs();
                ReTweakDeobfuscation.INSTANCE.loadSupers();
            } else {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Could not find play directory ( {} ) and compilation mode is {}. Compilation mode will be set to {} instead.",
                        ReTweakResources.RETWEAK_PLAY_DIRECTORY.getPath(),
                        CompilationMode.JIT.name(),
                        CompilationMode.INTERPRETER.name()
                );
                ReTweakConfig.INSTANCE.setCompilationMode(
                        CompilationMode.INTERPRETER
                );
            }
        }
        for(GameVersion gameVersion : GameVersion.values()) {
            ReflectionHelper.invokeMethod(
                    ReflectionHelper.getMethod(
                            ReTweakClassLoader.class,
                            "loadSRG",
                            new Class<?>[0]
                    ),
                    ReTweakClassLoader.getClassLoader(
                            gameVersion
                    ),
                    new Object[0]
            );
        }
        return null;
    }

    public static boolean isDeobfuscatedEnvironment() {
        return ReTweakSetup.deobfuscatedEnvironment;
    }

}
