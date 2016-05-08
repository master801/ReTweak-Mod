package org.slave.minecraft.retweak.client.config.elements;

import cpw.mods.fml.client.config.IConfigElement;
import org.slave.minecraft.library.client.gui.config.AbstractCategory;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 5/7/2016 at 4:01 PM.
 *
 * @author Master
 */
public final class GuiScreenConfigReTweakModConfig extends AbstractCategory {

    public GuiScreenConfigReTweakModConfig() {
        super(
                ReTweakStrings.RETWEAK_CONFIG_CATEGORY_MOD_CONFIG,
                true,
                true
        );
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<>();
        //TODO
        return list;
    }

}
