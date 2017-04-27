package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;

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
        ReTweakSetup.deobfuscatedEnvironment = !(boolean)data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public Void call() throws Exception {
        return null;
    }

    public static boolean isDeobfuscatedEnvironment() {
        return ReTweakSetup.deobfuscatedEnvironment;
    }

}
