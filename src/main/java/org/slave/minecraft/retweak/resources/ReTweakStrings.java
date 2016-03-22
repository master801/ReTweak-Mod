package org.slave.minecraft.retweak.resources;

/**
 * Created by Master801 on 3/21/2016 at 9:33 AM.
 *
 * @author Master801
 */
public final class ReTweakStrings {

    private ReTweakStrings() {
        throw new IllegalStateException();
    }

    //Mod stuff
    //-----------------------------------
    public static final String RETWEAK_MOD = "ReTweak-Mod";
    public static final String RETWEAK_VERSION = "@MOD_VERSION@";
    public static final String RETWEAK_PROXY_CLIENT = "org.slave.minecraft.retweak.proxies.ReTweakClientProxy";
    public static final String RETWEAK_PROXY_SERVER = "org.slave.minecraft.retweak.proxies.ReTweakServerProxy";

    //Config GUI
    //-----------------------------------
    public static final String RETWEAK_CONFIG_PREFIX = "retweak.gui.config.";
    public static final String RETWEAK_CONFIG_ELEMENT = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "element.";

    public static final String RETWEAK_CONFIG_CATEGORY_PREFIX = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "category.";
    public static final String RETWEAK_GUI_CONFIG_ID = "ReTweak-Mod_Config";
    public static final String RETWEAK_CONFIG_ELEMENT_ENABLE = ReTweakStrings.RETWEAK_CONFIG_ELEMENT + "enable";
    public static final String RETWEAK_GUI_CONFIG_CATEGORY_MODS = ReTweakStrings.RETWEAK_CONFIG_CATEGORY_PREFIX + "mods";

}
