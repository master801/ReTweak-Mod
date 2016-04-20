package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.Mod;
import org.slave.lib.resources.EnumMap;
import org.slave.lib.resources.wrappingdata.WrappingDataT.WrappingDataT2;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.ModCandidate}
 * </p>
 *
 * Created by Master801 on 3/19/2016 at 9:17 AM.
 *
 * @author Master801
 */
public final class ReTweakModCandidate {

    @SuppressWarnings("unchecked")
    private static final EnumMap<SupportedGameVersion, WrappingDataT2<Type, String>> MOD_CLASS_MAP = new EnumMap<>(
            SupportedGameVersion.class,
            new WrappingDataT2[] {
                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_2_5,
                            new WrappingDataT2<>(
                                    Type.EXTENDS,
                                    "forge.NetworkMod"
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_4_6,
                            new WrappingDataT2<>(
                                    Type.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_4_7,
                            new WrappingDataT2<>(
                                    Type.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    ),

                    new WrappingDataT2<>(
                            SupportedGameVersion.V_1_5_2,
                            new WrappingDataT2<>(
                                    Type.ANNOTATION,
                                    Mod.class.getCanonicalName()
                            )
                    )
            }
    );

    private final SupportedGameVersion supportedGameVersion;
    private final File file;

    public ReTweakModCandidate(final SupportedGameVersion supportedGameVersion, final File file) {
        this.supportedGameVersion = supportedGameVersion;
        this.file = file;
    }

    File getFile() {
        return file;
    }

    /**
     * @return If the candidate is a mod
     */
    public boolean check() throws IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> zipEntryEnumeration = zipFile.entries();
        while(zipEntryEnumeration.hasMoreElements()) {
            ZipEntry zipEntry = zipEntryEnumeration.nextElement();
            //TODO
        }
        zipFile.close();
        return false;
    }

    //TODO

    enum Type {

        ANNOTATION,

        EXTENDS;

        Type() {
        }

    }

}
