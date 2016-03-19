package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.minecraft.retweak.asm.discovery.ReTweakModDiscoverer;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.minecraft.retweak.resources.SupportedGameVersion;

import java.io.File;
import java.util.Map;

/**
 * Created by Master801 on 3/18/2016 at 10:30 PM.
 *
 * @author Master801
 */
public final class ReTweakSetup implements IFMLCallHook {

    @Override
    public void injectData(Map<String, Object> data) {
        ReTweakClassLoader.instance = new ReTweakClassLoader((LaunchClassLoader)data.get("classLoader"));
    }

    @Override
    public Void call() throws Exception {
        if (!ReTweakResources.RETWEAK_DIRECTORY.exists()) {
            ReTweakResources.RETWEAK_LOGGER.warn("Could not find directory \"{}\"! Creating it now...", ReTweakResources.RETWEAK_DIRECTORY.getName());
            ReTweakResources.RETWEAK_DIRECTORY.mkdir();
        }
        findMods();
        return null;
    }

    private void findMods() throws Exception {
        File[] subFiles = ReTweakResources.RETWEAK_DIRECTORY.listFiles();
        if (!ArrayHelper.isNullOrEmpty(subFiles)) {
            for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
                File supportedGameVersionDir = null;
                for(File subFile : subFiles) {
                    if (subFile.isDirectory() && subFile.getName().equals(supportedGameVersion.getDirectoryName())) {
                        ReTweakResources.RETWEAK_LOGGER.debug("Found supported mods dir \"{}\"... searching it now for mods...", subFile.getName());
                        supportedGameVersionDir = subFile;
                        break;
                    }
                }
                if (supportedGameVersionDir != null) {
                    ReTweakModDiscoverer reTweakModDiscoverer = new ReTweakModDiscoverer();
                    reTweakModDiscoverer.findModsInDir(supportedGameVersionDir);
                    reTweakModDiscoverer.identify();
                }
            }
        }
    }

}
