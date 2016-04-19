package org.slave.minecraft.retweak.client.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.config.Property.Type;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.library.client.gui.config.AbstractCategory;
import org.slave.minecraft.library.client.gui.config.elements.BasicConfigElement;
import org.slave.minecraft.retweak.loading.ReTweakCereal;
import org.slave.minecraft.retweak.loading.ReTweakModContainer;
import org.slave.minecraft.retweak.loading.SupportedGameVersion;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Master801 on 3/21/2016 at 10:17 AM.
 *
 * @author Master801
 */
public final class CategoryReTweakMod extends AbstractCategory {

    private final SupportedGameVersion supportedGameVersion;
    private final ReTweakModContainer reTweakModContainer;

    public CategoryReTweakMod(final SupportedGameVersion supportedGameVersion, final ReTweakModContainer reTweakModContainer) {
        super(
                StringHelper.isNullOrEmpty(reTweakModContainer.getName()) ? "UNKNOWN_MOD_NAME" : reTweakModContainer.getName(),
                true,
                true
        );
        this.supportedGameVersion = supportedGameVersion;
        this.reTweakModContainer = reTweakModContainer;
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<>();

        Property property = new Property(
                ReTweakStrings.RETWEAK_CONFIG_ELEMENT_ENABLE,
                Boolean.valueOf(reTweakModContainer.isEnabled()).toString(),
                Type.BOOLEAN,
                BasicConfigElement.VALID_BOOLEAN_VALUES
        );
        elements.add(new BasicConfigElement<Boolean>(property) {

            @Override
            public void set(Boolean value) {
//                reTweakModContainer.setEnabled(value);//FIXME Config should not be allowed to directly set the container to be enabled
                try {
                    ReTweakCereal.INSTANCE.modify(
                            supportedGameVersion,
                            Collections.singletonList(reTweakModContainer)
                    );
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }

        });
        return elements;
    }

}
