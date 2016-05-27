package org.slave.minecraft.retweak.client.config.elements;

import cpw.mods.fml.client.config.IConfigElement;
import org.slave.minecraft.library.client.gui.config.AbstractCategory;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 5/7/2016 at 3:58 PM.
 *
 * @author Master
 */
public final class GuiScreenConfigReTweakConfig extends AbstractCategory {

    public GuiScreenConfigReTweakConfig() {
        super(
                ReTweakStrings.RETWEAK_CONFIG_CATEGORY_CONFIG,
                false,
                false
        );
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        ArrayList<IConfigElement> list = new ArrayList<>();
        //TODO
        return list;
    }

}
