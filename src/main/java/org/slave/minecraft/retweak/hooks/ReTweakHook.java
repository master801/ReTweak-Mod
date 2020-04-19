package org.slave.minecraft.retweak.hooks;

import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import lombok.experimental.UtilityClass;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.slave.minecraft.retweak.ReTweak;

import java.io.IOException;

/**
 * Created by Master on 7/26/2018 at 2:45 PM.
 *
 * @author Master
 */
@UtilityClass
public final class ReTweakHook {

    /**
     * {@link cpw.mods.fml.common.Loader#loadingComplete()}
     */
    public static void onLoadingComplete() {
        try {
            ReTweak.getConfiguration().read();
        } catch (IOException e) {
            ReTweak.LOGGER_RETWEAK.warn("Failed to read the configuration file!", e);
        }
    }

    public static void addSmelting(final Object object, final ItemStack stack, final float exp) {
        if (object instanceof Item) {
            GameRegistry.addSmelting((Item)object, stack, exp);
        } else if (object instanceof Block) {
            GameRegistry.addSmelting((Block)object, stack, exp);
        } else if (object instanceof ItemStack) {
            GameRegistry.addSmelting((ItemStack)object, stack, exp);
        }
    }

    public static int getItemStackID(final ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) return -1;
        return ReTweakHook.getItemID(itemStack.getItem());
    }

    public static int getItemID(final Item item) {
        if (item == null) return -1;
        return GameData.getItemRegistry().getId(item);
    }

    public static int getBlockID(final Block block) {
        if (block == null) return -1;
        return GameData.getBlockRegistry().getId(block);
    }

    public static ItemStack createItemStack(final int id, final int metadata, final int stackSize) {
        Item item = GameData.getItemRegistry().getObjectById(id);
        Block block = GameData.getBlockRegistry().getObjectById(id);
        if (item != null) {
            return new ItemStack(item, metadata, stackSize);
        } else if (block != null) {
            return new ItemStack(block, metadata, stackSize);
        } else {
            return null;
        }
    }

}
