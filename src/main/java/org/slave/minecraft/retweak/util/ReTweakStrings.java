package org.slave.minecraft.retweak.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String RETWEAK_PACKAGE_PREFIX = "org.slave.minecraft.retweak.";
    //

    @Usage(UsageType.MOD)
    public static final String RETWEAK_MOD = "ReTweak-Mod";

    @Usage(UsageType.MOD)
    public static final String RETWEAK_VERSION = "@MOD_VERSION@";

    @Usage(UsageType.MOD)
    public static final String RETWEAK_GUI_FACTORY = ReTweakStrings.RETWEAK_PACKAGE_PREFIX + "client.config.ReTweakGUIFactory";
    //Prefixes
    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String TEXTURES_PREFIX = "textures" + '/';

    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String RETWEAK_TEXTURES_GUI_PREFIX = ReTweakStrings.TEXTURES_PREFIX + "gui" + '/';
    //Texture folders (prefixes?)
    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String RETWEAK_TEXTURES_GUI_RETWEAK_MODS = RETWEAK_TEXTURES_GUI_PREFIX + "retweak_mods" + '/';

    //Config GUI
    //-----------------------------------
    @Usage(value = UsageType.MOD)
    public static final String RETWEAK_GUI_CONFIG_ID = "ReTweak-Mod_Config";
    //Prefixes

    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String RETWEAK_CONFIG_PREFIX = "retweak.gui.config" + '/';

    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String RETWEAK_CONFIG_CATEGORY_PREFIX = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "category" + '/';

    @Usage(value = UsageType.MOD, flag = Flag.PREFIX)
    public static final String RETWEAK_CONFIG_ELEMENT_PREFIX = ReTweakStrings.RETWEAK_CONFIG_PREFIX + "element" + '/';

    //Categories
    //TODO

    //Config
    //-----------------------------------
    //Keys
    //TODO

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    private @interface Usage {

        UsageType value();

        Flag flag() default Flag.ANY;

    }

    private enum UsageType {

        MOD;

        UsageType() {
        }

    }

    private enum Flag {

        PREFIX,

        ANY;

        Flag() {
        }

    }

}
