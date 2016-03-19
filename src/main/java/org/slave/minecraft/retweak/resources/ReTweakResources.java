package org.slave.minecraft.retweak.resources;

/**
 * Created by Master801 on 3/18/2016 at 9:11 PM.
 *
 * @author Master801
 */
public final class ReTweakResources {

    public static final String RETWEAK_MOD = "ReTweak-Mod", RETWEAK_VERSION = "@MOD_VERSION@";
    public static final String RETWEAK_PROXY_CLIENT = "org.slave.minecraft.retweak.proxies.ReTweakClientProxy", RETWEAK_PROXY_SERVER = "org.slave.minecraft.retweak.proxies.ReTweakServerProxy";

    private ReTweakResources() {
        throw new IllegalStateException();
    }

}
