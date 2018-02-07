package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Obfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Master on 4/19/2017 at 7:56 PM.
 *
 * @author Master
 */
@Obfuscated(
    name = "aqz",
    _package = @Package("")
)
@Deobfuscated(
    name = "Block",
    _package = @Package("net.minecraft.block")
)
public class aqz extends Block {//TODO

    /**
     * @param par1 blockName
     * @param material material
     */
    public aqz(final int par1, final Material material) {//TODO
        super(material);
    }

    public aqz(final Material material) {//TODO
        super(material);
    }

    public aqz c(final float z) {//TODO idk
        return this;
    }

    @Obfuscated(name = "b")
    @Deobfuscated(name = "setResistance")
    public aqz b(final float resistance) {
        super.setResistance(resistance);
        return this;
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "setStepSound")
    public aqz a(final ard stepSound) {
        super.setStepSound(stepSound);
        return this;
    }

    @Obfuscated(name = "c")
    @Deobfuscated(name = "setUnlocalizedName")
    public aqz c(final String unlocalizedName) {
        super.setBlockName(unlocalizedName);
        return this;
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "onBlockActivated")
    public boolean a(final abw world, final int xCoord, final int yCoord, final int zCoord, final uf entityPlayer, final int side, final float hitX, final float hitY, final float hitZ) {
        return super.onBlockActivated(
            world,
            xCoord,
            yCoord,
            zCoord,
            entityPlayer,
            side,
            hitX,
            hitY,
            hitZ
        );
    }

    @Obfuscated(name = "d")
    @Deobfuscated(name = "idPicked")
    public int d(final abw world, final int xCoord, final int yCoord, final int zCoord) {
        return -1;
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "idDropped")
    public int a(final int metadata, final Random random, final int fortune) {
        return GameData.getItemRegistry().getId(
            super.getItemDropped(
                metadata,
                random,
                fortune
            )
        );
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "quantityDroppedWithBonus")
    public int a(final Random random) {
        return super.quantityDropped(random);
    }

    @Deobfuscated(name = "getBlockDropped")
    public ArrayList<ye> getBlockDropped(final abw world, final int xCoord, final int yCoord, final int zCoord, final int metadata, final int fortune) {//Forge method
//        return super.getDrops(world, xCoord, yCoord, zCoord, metadata, fortune);
        return null;//TODO
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "registerIcons")
    public void a(final mt iconRegistry) {//TODO
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "getIcon")
    public ms a(final int side, final int metadata) {//TODO
        return null;
    }

    @Obfuscated(name = "d")
    @Deobfuscated(name = "getRenderId")
    public int d() {
        return super.getRenderType();
    }

    @Obfuscated(name = "b")
    @Deobfuscated(name = "renderAsNormalBlock")
    public boolean b() {
        return super.renderAsNormalBlock();
    }

    @Obfuscated(name = "c")
    @Deobfuscated(name = "isOpaqueCube")
    public boolean c() {
        return super.isOpaqueCube();
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "shouldSideBeRendered")
    public boolean a(acf world, int xCoord, int yCoord, int zCoord, int side) {
        return super.shouldSideBeRendered(
            world,
            xCoord,
            yCoord,
            zCoord,
            side
        );
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "addCollisionBoxesToList")
    public void a(final abw world, final int xCoord, final int yCoord, final int zCoord, final asx aabb, final List list, final nn entity) {
        super.addCollisionBoxesToList(
            world,
            xCoord,
            yCoord,
            zCoord,
            aabb,
            list,
            entity
        );
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "setBlockBoundsBasedOnState")
    public void a(final acf blockAccess, final int xCoord, final int yCoord, final int zCoord) {
        super.setBlockBoundsBasedOnState(
            blockAccess,
            xCoord,
            yCoord,
            zCoord
        );
    }

}
