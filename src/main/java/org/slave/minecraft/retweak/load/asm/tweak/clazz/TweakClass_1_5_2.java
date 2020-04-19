package org.slave.minecraft.retweak.load.asm.tweak.clazz;

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
        /*
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
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("setLightValue")
                                        .setObfuscatedDescArgumentTypes(Type.FLOAT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType(Block.class))

                                        .setDeobfuscatedName("setLightLevel")
                                        .setDeobfuscatedDescArgumentTypes(Type.FLOAT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("setUnlocalizedName")
                                        .setObfuscatedDescArgumentTypes(Type.getType(String.class))
                                        .setObfuscatedDescReturnType(Type.getType(Block.class))

                                        .setDeobfuscatedName("setBlockName")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(String.class))
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))
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
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from(Achievement.class)
                        .to(Achievement.class)
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("registerAchievement")
                                        .setObfuscatedDescReturnType(Type.getType(Achievement.class))

                                        .setDeobfuscatedName("registerStat")
                                        .setDeobfuscatedDescReturnType(Type.getType(Achievement.class))
                                        .build()
                        )
                        .build()
        );

        super.addSuperMigration(
                BuilderSuperMigration.instance()
                        .from(BlockContainer.class)
                        .to(Migrate152BlockContainer.class)
                        .build()
        );

        super.addSuperMigration(
                BuilderSuperMigration.instance()
                        .from(BlockFlower.class)
                        .to(Migrate152BlockBush.class)
                        .build()
        );

        super.addSuperMigration(
                BuilderSuperMigration.instance()
                        .from(Item.class)
                        .to(Migrate152Item.class)
                        .build()
        );

        super.addSuperMigration(
                BuilderSuperMigration.instance()
                        .from(ItemBlock.class)
                        .to(Migrate152ItemBlock.class)
                        .build()
        );

        super.addSuperMigration(
                BuilderSuperMigration.instance()
                        .from(ItemFood.class)
                        .to(Migrate152ItemFood.class)
                        .build()
        );

        super.addSuperMigration(
                BuilderSuperMigration.instance()
                        .from(Achievement.class)
                        .to(Migrate152Achievement.class)
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
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from(GameRegistry.class)
                        .to(GameRegistry.class)
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("registerBlock")
                                        .setObfuscatedDescArgumentTypes(Type.getType(Block.class), Type.getType(Class.class), Type.getType(String.class))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("registerBlock")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(Block.class), Type.getType(Class.class), Type.getType(String.class))
                                        .setDeobfuscatedDescReturnType(Type.getType(Block.class))
                                        .build()
                        )
                        .build()
        );
        */
        //</editor-fold>
    }

}
