package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import net.minecraft.block.Block;
import net.minecraft.block.Block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationMethod.MigrationMethod;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.MigrationClass;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.acf;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.aqz;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4.yc;
import org.slave.minecraft.retweak.load.util.GameVersion;

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

                        .addFieldMapping(
                                MigrationField.builder()
                                        .setObfuscatedName("h")
                                        .setFromDescType(Type.getType("Lard;"))

                                        .setDeobfuscatedName("soundTypeWood")
                                        .setToDescType(Type.getType(SoundType.class))

                                        .build()
                        )

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
                        .to(CreativeTabs.class)
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
    }

}
