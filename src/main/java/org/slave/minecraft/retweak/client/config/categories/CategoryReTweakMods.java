package org.slave.minecraft.retweak.client.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import org.slave.minecraft.library.client.gui.config.AbstractCategory;
import org.slave.minecraft.retweak.resources.ReTweakStrings;
import org.slave.minecraft.retweak.loading.ReTweakLoader;
import org.slave.minecraft.retweak.loading.ReTweakModContainer;
import org.slave.minecraft.retweak.loading.SupportedGameVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 3/21/2016 at 9:31 AM.
 *
 * @author Master801
 */
public final class CategoryReTweakMods extends AbstractCategory {

    public CategoryReTweakMods() {
        super(
                ReTweakStrings.RETWEAK_GUI_CONFIG_CATEGORY_MODS,
                false,
                false
        );
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        ArrayList<IConfigElement> elements = new ArrayList<>();
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            final List<IConfigElement> modContainerElements = new ArrayList<>();

            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getModContainers(supportedGameVersion)) {
                modContainerElements.add(new CategoryReTweakMod(
                        reTweakModContainer
                ));
            }

            elements.add(new AbstractCategory(
                    ReTweakStrings.RETWEAK_CONFIG_CATEGORY_PREFIX + supportedGameVersion.getVersion().replace('.', '_'),
                    true,
                    true
            ) {

                @Override
                protected List<IConfigElement> getConfigElements() {
                    return modContainerElements;
                }

            });
        }
        return elements;
    }

}
