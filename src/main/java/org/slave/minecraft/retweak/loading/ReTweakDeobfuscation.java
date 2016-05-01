package org.slave.minecraft.retweak.loading;

import LZMA.LzmaInputStream;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.train.Train;
import org.slave.tool.remapper.SRG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by Master on 4/26/2016 at 10:16 PM.
 *
 * @author Master
 */
public final class ReTweakDeobfuscation {

    public static final ReTweakDeobfuscation INSTANCE = new ReTweakDeobfuscation();

    private final HashMap<GameVersion, WrappingDataT2<SRG, Train>> srgs = new HashMap<>();

    private ReTweakDeobfuscation() {
    }

    public void loadSRGs(File dir) throws IOException {
        for(GameVersion gameVersion : GameVersion.values()) {
            File dataFile = new File(
                    dir,
                    "deobfuscation_data-" + gameVersion.getVersion() + ".lzma"
            );
            if (dataFile.exists()) {
                FileInputStream fileInputStream = new FileInputStream(dataFile);
                SRG srg = loadSRG(fileInputStream);
                srgs.put(
                        gameVersion,
                        new WrappingDataT2<>(
                                srg,
                                Train.fromSRG(srg)
                        )
                );
                fileInputStream.close();
            }
        }
    }

    public SRG getSRG(GameVersion gameVersion) {
        return srgs.get(gameVersion).getObject1();
    }

    public Train getTrain(GameVersion gameVersion) {
        return srgs.get(gameVersion).getObject2();
    }

    private SRG loadSRG(InputStream inputStream) throws IOException {
        LzmaInputStream lzmaInputStream = new LzmaInputStream(inputStream);
        SRG srg = SRG.load(lzmaInputStream);
        lzmaInputStream.close();
        return srg;
    }

}
