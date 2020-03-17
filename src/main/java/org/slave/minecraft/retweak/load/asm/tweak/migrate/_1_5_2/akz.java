package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_5_2;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

import java.util.List;
import java.util.Random;

/**
 * Created by Master on 1/18/2020 at 2:58 AM
 *
 * @author Master
 */
@Obfuscated(
        name = "akz"
)
@Deobfuscated(
        _package = @Package("net.minecraft.block"),
        name = "BlockContainer"
)
public abstract class akz extends BlockContainer {

    protected akz(final int id, final Material material) {
        this(material);
    }

    protected akz(final Material material) {
        super(material);
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "idDropped")
    public int a(final int par1, final Random random, final int par3) {
        return 0;//TODO
    }

    @Obfuscated(
            name = "a",
            parameters = { int.class, Object.class, List.class }//Object.class = ve.class
    )
    @Deobfuscated(
            name = "getSubBlocks",
            parameters = { Item.class, CreativeTabs.class, List.class }
    )
    public void a(final int par1, final CreativeTabs creativeTabs, final List<ItemStack> list) {
        //TODO
    }

}
