package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Obfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Interfaces;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

/**
 * Created by Master on 4/20/2017 at 5:51 AM.
 *
 * @author Master
 */
@Obfuscated(
    name = "amw",
    _package = @Package("")
)
@Deobfuscated(
    name = "BlockContainer",
    _package = @Package("net.minecraft.block")
)
@Interfaces(
    {
        aoe.class
    }
)
public class amw extends aqz implements aoe {

    public amw(final int par1, final Material material) {
        super(par1, material);
        isBlockContainer = true;
    }

    public amw(final Material material) {
        super(material);
        isBlockContainer = true;
    }

    @Override
    public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
        return null;
    }

}
