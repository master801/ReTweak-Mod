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
    public static final String RETWEAK_PACKAGE_PREFIX = "org.slave.minecraft.retweak.";
    //
    public static final String RETWEAK_MOD = "ReTweak-Mod";
    public static final String RETWEAK_VERSION = "@MOD_VERSION@";
    public static final String RETWEAK_GUI_FACTORY = ReTweakStrings.RETWEAK_PACKAGE_PREFIX + "client.config.ReTweakGUIFactory";

    //Config GUI
    //-----------------------------------
    public static final String RETWEAK_GUI_CONFIG_ID = "ReTweak-Mod_Config";
    //Prefixes
    public static final String RETWEAK_CONFIG_PREFIX = "retweak.gui.config.";
    public static final String RETWEAK_CONFIG_CATEGORY_PREFIX = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "category.";
    public static final String RETWEAK_CONFIG_ELEMENT = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "element.";

    //Config
    //-----------------------------------
    //Keys
    public static final String RETWEAK_CONFIG_KEY_COMPILATION_MODE = "retweak.config.compilation_mode";

}
