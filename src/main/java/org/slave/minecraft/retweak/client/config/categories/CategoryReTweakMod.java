package org.slave.minecraft.retweak.client.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.library.client.gui.config.AbstractCategory;
import org.slave.minecraft.library.client.gui.config.elements.BasicConfigElement;
import org.slave.minecraft.retweak.resources.ReTweakStrings;
import org.slave.minecraft.retweak.loading.ReTweakModContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 3/21/2016 at 10:17 AM.
 *
 * @author Master801
 */
public final class CategoryReTweakMod extends AbstractCategory {

    public CategoryReTweakMod(ReTweakModContainer reTweakModContainer) {
        super(
                StringHelper.isNullOrEmpty(reTweakModContainer.getName()) ? "UNKNOWN_MOD_NAME" : reTweakModContainer.getName(),
                true,
                true
        );
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<>();
        elements.add(new BasicConfigElement<Boolean>(
                new Property(
                        ReTweakStrings.RETWEAK_CONFIG_ELEMENT_ENABLE,
                        Boolean.TRUE.toString(),
                        Type.BOOLEAN,
                        new String[] {
                                Boolean.TRUE.toString(),
                                Boolean.FALSE.toString()
                        }
                )
        ));
        return elements;
    }

}
