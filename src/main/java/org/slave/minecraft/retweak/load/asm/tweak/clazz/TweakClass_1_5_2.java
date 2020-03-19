package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.IScheduledTickHandler;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.TickType;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.registry.TickRegistry;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/21/2018 at 1:38 AM.
 *
 * @author Master
 */
public final class TweakClass_1_5_2 extends AbstractTweakClass {

    public static final TweakClass INSTANCE = new TweakClass_1_5_2();

    private TweakClass_1_5_2() {
        super(GameVersion.V_1_5_2);

        //<editor-fold desc="Minecraft">
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from(Block.class)
                        .to(Block.class)
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("onBlockPlacedBy")
                                        .setObfuscatedDescArgumentTypes(Type.getArgumentTypes("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLiving;Lnet/minecraft/item/ItemStack;)V"))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("onBlockPlacedBy")
                                        .setDeobfuscatedDescArgumentTypes(Type.getArgumentTypes("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V"))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from(BlockContainer.class)
                        .to(BlockContainer.class)
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("onBlockPlacedBy")
                                        .setObfuscatedDescArgumentTypes(Type.getArgumentTypes("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLiving;Lnet/minecraft/item/ItemStack;)V"))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("onBlockPlacedBy")
                                        .setDeobfuscatedDescArgumentTypes(Type.getArgumentTypes("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V"))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .build()
        );

        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from(ItemStack.class)
                        .to(ItemStack.class)
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("damageItem")
                                        .setObfuscatedDescArgumentTypes(Type.getArgumentTypes("(ILnet/minecraft/entity/EntityLiving;)V"))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("damageItem")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.getType(EntityLivingBase.class))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .build()
        );
        //</editor-fold>

        //<editor-fold desc="FML">
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("cpw/mods/fml/common/IScheduledTickHandler")
                        .to(IScheduledTickHandler.class)
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("cpw/mods/fml/common/TickType")
                        .to(TickType.class)
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("cpw/mods/fml/common/registry/TickRegistry")
                        .to(TickRegistry.class)
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("registerScheduledTickHandler")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Lcpw/mods/fml/common/IScheduledTickHandler;"), Type.getType(Side.class))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)


                                        .setDeobfuscatedName("registerScheduledTickHandler")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(IScheduledTickHandler.class), Type.getType(Side.class))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .build()
        );
        //</editor-fold>
    }

}
