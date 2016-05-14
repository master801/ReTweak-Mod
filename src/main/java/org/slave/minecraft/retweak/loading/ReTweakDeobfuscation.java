package org.slave.minecraft.retweak.loading;

import LZMA.LzmaInputStream;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.tool.remapper.SRG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Master on 4/26/2016 at 10:16 PM.
 *
 * @author Master
 */
public final class ReTweakDeobfuscation {

    public static final ReTweakDeobfuscation INSTANCE = new ReTweakDeobfuscation();

    private final HashMap<GameVersion, SRG> srgs = new HashMap<>();

    private ReTweakDeobfuscation() {
    }

    public void loadSRGs(File dir) throws IOException {
        for(GameVersion gameVersion : GameVersion.values()) {
            File dataFile = new File(
                    dir,
                    "deobfuscation_data-" + gameVersion.getVersion() + ".lzma"
            );
            if (dataFile.exists()) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "SRG file \"{}\" was found, JIT compiler will now work.",
                        dataFile.getPath()
                );
                FileInputStream fileInputStream = new FileInputStream(dataFile);
                SRG srg;

                LzmaInputStream lzmaInputStream = new LzmaInputStream(fileInputStream);
                try {
                    srg = SRG.load(lzmaInputStream);
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.error(
                            "Failed to load SRG file \"{}\"",
                            dataFile.getPath()
                    );
                    lzmaInputStream.close();
                    return;
                }
                lzmaInputStream.close();

                srgs.put(
                        gameVersion,
                        srg
                );
                fileInputStream.close();
            } else {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "SRG file \"{}\" was not found... JIT compiler will not work...",
                        dataFile.getPath()
                );
            }
        }
    }

    public SRG getSRG(GameVersion gameVersion) {
        return srgs.get(gameVersion);
    }

}
