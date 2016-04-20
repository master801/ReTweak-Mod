package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.ReTweakClassLoader;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.Map;

/**
 * Created by Master801 on 3/18/2016 at 10:30 PM.
 *
 * @author Master801
 */
public final class ReTweakSetup implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {
        //Set private field value
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
                    "Failed to create class loader for ReTweak! ReTweak will not be able to load mods!"
            );
        }
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

}
