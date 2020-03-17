package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;

import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.MigrationClass;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.abw;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.acf;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.aqz;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.bfr;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.ms;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.ud;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.uf;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.ww;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.yc;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.ye;
import org.slave.minecraft.retweak.load.util.GameVersion;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * Created by Master on 8/30/2018 at 10:00 AM.
 *
 * @author Master
 */
public final class TweakClass_1_6_4 extends AbstractTweakClass {

    public static final TweakClass INSTANCE = new TweakClass_1_6_4();

    private TweakClass_1_6_4() {
        super(GameVersion.V_1_6_4);

        //TODO

        //net.minecraft.block.Block
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("aqz")
                        .to(aqz.class)

                        //<editor-fold desc="Fields">
                        .addFieldMapping(
                                MigrationField.builder()
                                        .setObfuscatedName("h")
                                        .setFromDescType(Type.getType("Lard;"))

                                        .setDeobfuscatedName("soundTypeWood")
                                        .setToDescType(Type.getType(SoundType.class))

                                        .build()
                        )
                        //</editor-fold>

                        //<editor-fold desc="Methods">
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Lard;"))
                                        .setObfuscatedDescReturnType(Type.getType("Laqz;"))

                                        .setDeobfuscatedName("setStepSound")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(SoundType.class))
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Lww;"))
                                        .setObfuscatedDescReturnType(Type.getType("Laqz;"))

                                        .setDeobfuscatedName("setCreativeTab")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(CreativeTabs.class))
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))

                                        .build()
                        )
                        /*
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Lms;"))

                                        .build()
                        )
                        */
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("b")
                                        .setObfuscatedDescArgumentTypes(Type.FLOAT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Laqz;"))

                                        .setDeobfuscatedName("setResistance")
                                        .setDeobfuscatedDescArgumentTypes(Type.FLOAT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("c")
                                        .setObfuscatedDescArgumentTypes(Type.FLOAT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Laqz;"))

                                        .setDeobfuscatedName("setHardness")
                                        .setDeobfuscatedDescArgumentTypes(Type.FLOAT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("c")
                                        .setObfuscatedDescArgumentTypes(Type.getType(String.class))
                                        .setObfuscatedDescReturnType(Type.getType("Laqz;"))

                                        .setDeobfuscatedName("setUnlocalizedName")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(String.class))
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))

                                        .build()
                        )
                        //</editor-fold>

                        .build()
        );

        //net.minecraft.item.Item
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("yc")
                        .to(yc.class)
                        .build()
        );

        //net.minecraft.creativetab.CreativeTabs
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("ww")
                        .to(ww.class)

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("getIconItemStack")
                                        .setObfuscatedDescReturnType(Type.getType("Lye;"))

                                        .setDeobfuscatedName("getIconItemStack")
                                        .setDeobfuscatedDescReturnType(Type.getType(ye.class))

                                        .build()
                        )

                        .build()
        );

        //net.minecraft.world.IBlockAccess
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("acf")
                        .to(acf.class)
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("r")//getBlockTileEntity
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Lasp;"))//Lnet/minecraft/tileentity/TileEntity;

                                        .setDeobfuscatedName("getTileEntity")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(TileEntity.class))

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("h")//getLightBrightnessForSkyBlocks
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.INT_TYPE)

                                        .setDeobfuscatedName("getLightBrightnessForSkyBlocks")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.INT_TYPE)

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("h")//getBlockMetadata
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.INT_TYPE)

                                        .setDeobfuscatedName("getBlockMetadata")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.INT_TYPE)

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("R")//getHeight
                                        .setObfuscatedDescReturnType(Type.INT_TYPE)

                                        .setDeobfuscatedName("getHeight")
                                        .setDeobfuscatedDescReturnType(Type.INT_TYPE)

                                        .build()
                        )
                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("c")//isAirBlock
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .setDeobfuscatedName("isAirBlock")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .build()
                        )
                        .build()
        );

        //net.minecraft.block.material.Material
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("akc")
                        .to(Material.class)
                        .addFieldMapping(
                                MigrationField.builder()
                                        .setObfuscatedName("d")
                                        .setFromDescType(Type.getType("Lakc;"))

                                        .setDeobfuscatedNameThroughField(Material.class, Material.wood)//TODO LMFAO
                                        .setToDescType(Type.getType(Material.class))

                                        .build()
                        )
                        .build()
        );

        //net.minecraft.block.StepSound
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("ard")
                        .to(SoundType.class)

                        .build()
        );

        //net.minecraft.util.Icon
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("ms")
                        .to(ms.class)

                        .build()
        );

        //net.minecraft.world.World
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("abw")
                        .to(abw.class)

                        .addFieldMapping(
                                MigrationField.builder()
                                        .setObfuscatedName("I")

                                        .setDeobfuscatedName("isRemote")

                                        .build()
                        )

                        .build()
        );

        //net.minecraft.entity.player.EntityPlayer
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("uf")
                        .to(uf.class)

                        //inventory - InventoryPlayer
                        .addFieldMapping(
                                MigrationField.builder()
                                        .setObfuscatedName("bn")
                                        .setFromDescType(Type.getType("Lye;"))

                                        .setDeobfuscatedName("inventory")
                                        .setToDescType(Type.getType(ye.class))

                                        .build()
                        )

                        .build()
        );

        //net.minecraft.item.ItemStack
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("ye")
                        .to(ye.class)

                        .build()
        );

        //net.minecraft.entity.player.InventoryPlayer
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("ud")
                        .to(ud.class)

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("h")
                                        .setObfuscatedDescArgumentTypes()
                                        .setObfuscatedDescReturnType(Type.getType("Lye;"))

                                        .setDeobfuscatedName("getCurrentItem")
                                        .setDeobfuscatedDescArgumentTypes()
                                        .setDeobfuscatedDescReturnType(Type.getType(ye.class))

                                        .build()
                        )

                        .build()
        );

        //cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
        super.addMigrationClass(
                MigrationClass.builder()
                        .from(ISimpleBlockRenderingHandler.class)
                        .to(ISimpleBlockRenderingHandler.class)

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("renderInventoryBlock")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Laqz;"), Type.INT_TYPE, Type.INT_TYPE, Type.getType("Lbfr;"))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("renderInventoryBlock")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(aqz.class), Type.INT_TYPE, Type.INT_TYPE, Type.getType(bfr.class))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)

                                        .build()
                        )

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("renderWorldBlock")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Lacf;"), Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.getType("Laqz;"), Type.INT_TYPE, Type.getType("Lbfr;"))
                                        .setObfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .setDeobfuscatedName("renderWorldBlock")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(acf.class), Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE, Type.getType(aqz.class), Type.INT_TYPE, Type.getType(bfr.class))
                                        .setDeobfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .build()
                        )

                        .build()
        );

        //net.minecraft.client.renderer.RenderBlocks
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("bfr")
                        .to(bfr.class)

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE)
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("setRenderBounds")
                                        .setDeobfuscatedDescArgumentTypes(Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE, Type.DOUBLE_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)

                                        .build()
                        )

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("p")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Laqz;"), Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .setDeobfuscatedName("renderStandardBlock")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(aqz.class), Type.INT_TYPE, Type.INT_TYPE, Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .build()
                        )

                        .build()
        );

        //net.minecraft.client.renderer.Tessellator
        super.addMigrationClass(
                MigrationClass.builder()
                        .from("bfq")
                        .to(Tessellator.class)

                        .addFieldMapping(
                                MigrationField.builder()
                                        .setObfuscatedName("a")
                                        .setFromDescType(Type.getType("Lbfq;"))

                                        .setDeobfuscatedName("instance")
                                        .setToDescType(Type.getType(Tessellator.class))

                                        .build()
                        )

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("c")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("setBrightness")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)

                                        .build()
                        )

                        .addMethodMapping(
                                MigrationMethod.builder()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("setColorOpaque_F")
                                        .setDeobfuscatedDescArgumentTypes(Type.FLOAT_TYPE, Type.FLOAT_TYPE, Type.FLOAT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)

                                        .build()
                        )

                        .build()
        );
    }

}
