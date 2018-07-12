package org.slave.minecraft.retweak;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by master on 2/25/18 at 9:25 PM
 *
 * @author master
 */
@UtilityClass
public final class ReTweak {

    public static final Logger LOGGER_RETWEAK = LoggerFactory.getLogger("ReTweak");

    public static final File FILE_DIRECTORY_RETWEAK = new File("retweak");
    public static final File FILE_DIRECTORY_RETWEAK_MODS = new File(ReTweak.FILE_DIRECTORY_RETWEAK, "mods");
    public static final File FILE_DIRECTORY_RETWEAK_CONFIG = new File(ReTweak.FILE_DIRECTORY_RETWEAK, "config");

}
