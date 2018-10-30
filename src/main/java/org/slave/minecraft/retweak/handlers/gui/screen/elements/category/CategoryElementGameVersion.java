package org.slave.minecraft.retweak.handlers.gui.screen.elements.category;

import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import org.slave.minecraft.retweak.handlers.gui.screen.elements.ConfigElementReTweak;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/25/2018 at 4:43 PM.
 *
 * @author Master
 */
public final class CategoryElementGameVersion extends ConfigElementReTweak {

    public CategoryElementGameVersion() {
        super(
                new Property("", "", Type.BOOLEAN)
        );
    }

    public ConfigElementReTweak init() {
        for(GameVersion gameVersion : GameVersion.values()) {
        }
        return this;
    }

}
