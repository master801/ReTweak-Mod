package org.slave.minecraft.retweak.loading.tweaker.base;

import LZMA.LzmaInputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.slave.minecraft.retweak.loading.SupportedGameVersion;
import org.slave.minecraft.retweak.loading.tweaker.Tweak;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.tool.remapper.SRG;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Master801 on 4/19/2016 at 6:19 PM.
 *
 * @author Master801
 */
public final class DeobfuscationTweak implements Tweak {

    private SRG srg = null;

    public DeobfuscationTweak(final SupportedGameVersion supportedGameVersion) {
        if (ReTweakResources.RETWEAK_PLAY_DIRECTORY.exists()) {
            File deobfuscationData = new File(
                    ReTweakResources.RETWEAK_PLAY_DIRECTORY,
                    "deobfusction_data-" + supportedGameVersion.getVersion() + ".lzma"
            );
            if (deobfuscationData.exists() && deobfuscationData.isFile()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(deobfuscationData);
                    LzmaInputStream lzmaInputStream = new LzmaInputStream(fileInputStream);
                    srg = SRG.load(lzmaInputStream);
                    lzmaInputStream.close();
                    fileInputStream.close();
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "",
                            e
                    );
                }
            }
        }
    }

    @Override
    public byte[] transform(final String s, final String s1, final byte[] bytes) {
        if (srg == null) return bytes;

        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(
                ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS
        );
        classReader.accept(
                classWriter,
                0
        );
        RemappingClassAdapter remappingClassAdapter = new RemappingClassAdapter(
                classWriter,
                new DeobfusctionRemapper(srg)
        );
        classReader.accept(
                remappingClassAdapter,
                0
        );
        byte[] data = classWriter.toByteArray();
        if (data == null || data.length <= 0) {
            data = bytes;
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Invalid ClassWriter data? {} {}",
                    s,
                    s1
            );
        }
        return data;
    }

}
