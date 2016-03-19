package org.slave.minecraft.retweak.asm.discovery;

import cpw.mods.fml.common.discovery.ModDiscoverer;
import cpw.mods.fml.relauncher.FileListHelper;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.tweaking.ReTweakModContainer;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.minecraft.retweak.tweaking.SupportedGameVersion;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.ModDiscoverer}
 * </p>
 *
 * Created by Master801 on 3/19/2016 at 8:58 AM.
 *
 * @author Master801
 */
public final class ReTweakModDiscoverer {

    private final SupportedGameVersion supportedGameVersion;

    private final ArrayList<ReTweakModContainer> reTweakModContainers = new ArrayList<>();

    public ReTweakModDiscoverer(SupportedGameVersion supportedGameVersion) {
        this.supportedGameVersion = supportedGameVersion;
    }

    public void findModsInDir(File dir) throws NoSuchFieldException, IllegalAccessException {
        if (dir == null || !dir.exists() || !dir.isDirectory()) return;
        File[] modList = dir.listFiles();
        if (ArrayHelper.isNullOrEmpty(modList)) return;

        modList = FileListHelper.sortFileList(modList);

        for(File mod : modList) {
            if (mod.isFile()) {
                Matcher matcher = ((Pattern)ReflectionHelper.getFieldValue(ReflectionHelper.getField(ModDiscoverer.class, "zipJar"), null)).matcher(mod.getName());
                if (matcher.matches()) {
                    ReTweakResources.RETWEAK_LOGGER.info("Found a candidate mod!");
                    reTweakModContainers.add(new ReTweakModContainer(supportedGameVersion, mod));
                }
            } else {
                ReTweakResources.RETWEAK_LOGGER.warn("Mod \"{}\" is not a file or is not a mod! ReTweak does not support this!", mod.getName());
            }
        }
    }

    public void identify() throws IOException {
        for(ReTweakModContainer reTweakModContainer : reTweakModContainers) reTweakModContainer.search();
    }

}
