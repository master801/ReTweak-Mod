package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import org.slave.minecraft.retweak.asm.transformers.TransformerLoader;
import org.slave.minecraft.retweak.asm.transformers.TransformerSimpleReloadableResourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by master on 2/25/18 at 9:24 PM
 *
 * @author master
 */
@Name("ReTweak")
@MCVersion("1.7.10")
@TransformerExclusions({"org.slave.minecraft.retweak.asm."})
public final class ReTweakASM implements IFMLLoadingPlugin {

    public static final Logger LOGGER_RETWEAK_ASM = LoggerFactory.getLogger("ReTweak-ASM");

    @Override
    public String[] getASMTransformerClass() {
        return new String[] {
                TransformerLoader.class.getName(),
                TransformerSimpleReloadableResourceManager.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        return ReTweakModContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return ReTweakSetup.class.getName();
    }

    @Override
    public void injectData(final Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
