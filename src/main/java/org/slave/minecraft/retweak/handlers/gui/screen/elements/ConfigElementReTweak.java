package org.slave.minecraft.retweak.handlers.gui.screen.elements;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Property;

/**
 * Created by Master on 7/25/2018 at 4:40 PM.
 *
 * @author Master
 */
public class ConfigElementReTweak<T> extends ConfigElement<T> {

    public ConfigElementReTweak(final ConfigCategory ctgy) {
        super(ctgy);
    }

    public ConfigElementReTweak(final Property prop) {
        super(prop);
    }

}
