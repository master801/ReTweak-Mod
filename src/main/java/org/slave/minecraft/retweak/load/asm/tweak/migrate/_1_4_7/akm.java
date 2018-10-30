package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.IBlockAccess;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;

/**
 * Created by Master on 9/1/2018 at 5:54 AM.
 *
 * @author Master
 */
@Obfuscated(name = "akm")
@Deobfuscated(name = "net/minecraft/block/BlockBreakable")
public class akm extends amq {

//    private final BlockBreakable _this;

    protected akm(final int blockId, final int textureIndex, final Material material, final boolean par4) {//TODO
        super(blockId, material);
//        _this = new BlockBreakable(null, material, par4) {};
    }

    @Override
    public boolean isOpaqueCube() {
//        return _this.isOpaqueCube();
        return false;
    }

    @Override
    public boolean shouldSideBeRendered(final IBlockAccess p_149646_1_, final int p_149646_2_, final int p_149646_3_, final int p_149646_4_, final int p_149646_5_) {
//        return _this.shouldSideBeRendered(p_149646_1_, p_149646_2_, p_149646_3_, p_149646_4_, p_149646_5_);
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister p_149651_1_) {
        //TODO Do not register with _this
    }

}
