package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

import java.util.Map;

/**
 * Created by Master801 on 3/18/2016 at 10:24 PM.
 *
 * @author Master801
 */
@Name("ReTweak-Mod")
@TransformerExclusions({ "org.slave.minecraft.retweak.*" })
public final class ReTweakASM implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
        };
    }

    @Override
    public String getModContainerClass() {
        return ReTweakModContainer.class.getCanonicalName();
    }

    @Override
    public String getSetupClass() {
        return ReTweakSetup.class.getCanonicalName();
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
