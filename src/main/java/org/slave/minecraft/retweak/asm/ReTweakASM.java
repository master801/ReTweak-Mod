package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;

import java.util.Map;

/**
 * Created by master on 2/25/18 at 9:24 PM
 *
 * @author master
 */
public final class ReTweakASM implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(final Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
