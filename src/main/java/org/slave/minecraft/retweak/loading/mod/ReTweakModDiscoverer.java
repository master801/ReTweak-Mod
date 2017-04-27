package org.slave.minecraft.retweak.loading.mod;

import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

import java.util.regex.Pattern;

/**
 * <p>
 *     {@link cpw.mods.fml.common.discovery.ModDiscoverer}
 * </p>
 *
 * Created by Master on 4/26/2016 at 3:28 PM.
 *
 * @author Master
 */
public final class ReTweakModDiscoverer {

    private static final Pattern PATTERN_ZIP_JAR_MATCHER = Pattern.compile(
            ".+(\\.zip|\\.jar)$",
            Pattern.MULTILINE
    );

    private final GameVersion gameVersion;

    ReTweakModDiscoverer(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

}
