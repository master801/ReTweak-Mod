package org.slave.minecraft.retweak.resources;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slave.minecraft.library.api.Metadata;

import java.io.File;

/**
 * Created by Master801 on 3/18/2016 at 9:11 PM.
 *
 * @author Master801
 */
public final class ReTweakResources {

    public static final String RETWEAK_MOD = "ReTweak-Mod", RETWEAK_VERSION = "@MOD_VERSION@";
    public static final String RETWEAK_PROXY_CLIENT = "org.slave.minecraft.retweak.proxies.ReTweakClientProxy", RETWEAK_PROXY_SERVER = "org.slave.minecraft.retweak.proxies.ReTweakServerProxy";

    public static final Metadata RETWEAK_METADATA = ReTweakMetadata.INSTANCE;

    public static final File RETWEAK_DIRECTORY = new File("retweak");

    public static final Logger RETWEAK_LOGGER = LogManager.getLogger("ReTweak-Mod");

    private ReTweakResources() {
        throw new IllegalStateException();
    }

}
