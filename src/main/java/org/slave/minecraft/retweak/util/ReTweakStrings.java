package org.slave.minecraft.retweak.util;

/**
 * Created by Master801 on 3/21/2016 at 9:33 AM.
 *
 * @author Master801
 */
public final class ReTweakStrings {

    private ReTweakStrings() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        throw new IllegalStateException();
    }

    //Mod stuff
    //-----------------------------------
    public static final String RETWEAK_PACKAGE_PREFIX = "org.slave.minecraft.retweak.";
    //
    public static final String RETWEAK_MOD = "ReTweak-Mod";
    public static final String RETWEAK_VERSION = "@MOD_VERSION@";
    public static final String RETWEAK_GUI_FACTORY = ReTweakStrings.RETWEAK_PACKAGE_PREFIX + "client.config.ReTweakGUIFactory";
    //Prefixes
    public static final String TEXTURES_PREFIX = "textures" + '/';
    public static final String RETWEAK_TEXTURES_GUI_PREFIX = ReTweakStrings.TEXTURES_PREFIX + "gui" + '/';
    //Texture folders (prefixes?)
    public static final String RETWEAK_TEXTURES_GUI_RETWEAK_MODS = RETWEAK_TEXTURES_GUI_PREFIX + "retweak_mods" + '/';

    //Config GUI
    //-----------------------------------
    public static final String RETWEAK_GUI_CONFIG_ID = "ReTweak-Mod_Config";
    //Prefixes
    public static final String RETWEAK_CONFIG_PREFIX = "retweak.gui.config" + '/';
    public static final String RETWEAK_CONFIG_CATEGORY_PREFIX = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "category" + '/';
    public static final String RETWEAK_CONFIG_ELEMENT_PREFIX = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "element" + '/';
    //Categories
    //TODO

    //Config
    //-----------------------------------
    //Keys
    //TODO

}