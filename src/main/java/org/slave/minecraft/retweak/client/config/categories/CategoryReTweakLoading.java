package org.slave.minecraft.retweak.client.config.categories;

import cpw.mods.fml.client.config.IConfigElement;
import org.slave.minecraft.library.client.gui.config.AbstractCategory;
import org.slave.minecraft.library.client.gui.config.elements.EnumConfigElement;
import org.slave.minecraft.retweak.loading.CompilationMode;
import org.slave.minecraft.retweak.resources.ReTweakStrings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 4/24/2016 at 11:49 PM.
 *
 * @author Master
 */
public final class CategoryReTweakLoading extends AbstractCategory {

    public CategoryReTweakLoading() {
        super(
                ReTweakStrings.RETWEAK_GUI_CONFIG_CATEGORY_LOADING,
                true,
                true
        );
    }

    @Override
    protected List<IConfigElement> getConfigElements() {
        ArrayList<IConfigElement> elements = new ArrayList<>();
        elements.add(new EnumConfigElement<>(
                ReTweakStrings.RETWEAK_CONFIG_ELEMENT_COMPILATION_MODE,
                null,
                CompilationMode.values(),
                CompilationMode.JIT,
                true,
                true
        ));
        return elements;
    }

}
