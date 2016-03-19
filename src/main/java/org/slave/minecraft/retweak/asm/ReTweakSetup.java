package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.util.Map;

/**
 * Created by Master801 on 3/18/2016 at 10:30 PM.
 *
 * @author Master801
 */
public final class ReTweakSetup implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {
        LaunchClassLoader launchClassLoader = (LaunchClassLoader)data.get("classLoader");
        //TODO
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

}
