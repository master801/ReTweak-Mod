package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.IInventory;
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

        //<editor-fold desc="net.minecraft.creativetab.CreativeTabs">
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("ve")
                        .to(CreativeTabs.class)
                        //<editor-fold desc="Fields">
                        .addFieldMapping(
                                BuilderMigrationClass.BuilderMigrationField.instance()
                                        .setObfuscatedName("c")
                                        .setFromDescType(Type.getType("Lve;"))

                                        .setDeobfuscatedNameThroughField(CreativeTabs.class, CreativeTabs.tabDecorations)
                                        .setToDescType(Type.getType(CreativeTabs.class))
                                        .build()
                        )
                        //</editor-fold>
                        .build()
        );
        //</editor-fold>

        //<editor-fold desc="net.minecraft.block.material.Material">
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("aif")
                        .to(Material.class)
                        //<editor-fold desc="Fields">
                        .addFieldMapping(
                                BuilderMigrationClass.BuilderMigrationField.instance()
                                        .setObfuscatedName("e")
                                        .setFromDescType(Type.getType("Laif;"))

                                        .setDeobfuscatedNameThroughField(Material.class, Material.rock)
                                        .setToDescType(Type.getType(Material.class))
                                        .build()
                        )
                        //</editor-fold>
                        .build()
        );
        //</editor-fold>

        //<editor-fold desc="net.minecraft.inventory.IInventory">
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("lt")
                        .to(IInventory.class)
                        //<editor-fold desc="Methods">
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Lwm;"))

                                        .setDeobfuscatedName("getStackInSlot")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(ItemStack.class))
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.getType("Lwm;"))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("setInventorySlotContents")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.getType(ItemStack.class))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("a")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Lwm;"))

                                        .setDeobfuscatedName("decrStackSize")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(ItemStack.class))
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("b")
                                        .setObfuscatedDescReturnType(Type.getType(String.class))

                                        .setDeobfuscatedName("getInventoryName")
                                        .setDeobfuscatedDescReturnType(Type.getType(String.class))
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("b")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE)
                                        .setObfuscatedDescReturnType(Type.getType("Lwm;"))

                                        .setDeobfuscatedName("getStackInSlotOnClosing")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE)
                                        .setDeobfuscatedDescReturnType(Type.getType(ItemStack.class))
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("b")
                                        .setObfuscatedDescArgumentTypes(Type.INT_TYPE, Type.getType("Lwm;"))
                                        .setObfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .setDeobfuscatedName("isStackValidForSlot")
                                        .setDeobfuscatedDescArgumentTypes(Type.INT_TYPE, Type.getType(ItemStack.class))
                                        .setDeobfuscatedDescReturnType(Type.BOOLEAN_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("c")
                                        .setObfuscatedDescReturnType(Type.BOOLEAN_TYPE)

                                        .setDeobfuscatedName("hasCustomInventoryName")
                                        .setDeobfuscatedDescReturnType(Type.BOOLEAN_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("d")
                                        .setObfuscatedDescReturnType(Type.INT_TYPE)

                                        .setDeobfuscatedName("getInventoryStackLimit")
                                        .setDeobfuscatedDescReturnType(Type.INT_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("f")
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("openInventory")
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("g")
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("closeInventory")
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("j_")
                                        .setObfuscatedDescReturnType(Type.INT_TYPE)

                                        .setDeobfuscatedName("getSizeInventory")
                                        .setDeobfuscatedDescReturnType(Type.INT_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("k_")
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("markDirty")
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        //</editor-fold>
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
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.MigrationClass.builder()
                        .from(ClientRegistry.class)
                        .to(ClientRegistry.class)
                        //<editor-fold desc="Methods">
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod.builder()
                                        .setObfuscatedName("registerTileEntity")
                                        .setObfuscatedDescArgumentTypes(Type.getType(Class.class), Type.getType(String.class), Type.getType("Lbje;"))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("registerTileEntity")
                                        .setDeobfuscatedDescArgumentTypes(Type.getType(Class.class), Type.getType(String.class), Type.getType(TileEntitySpecialRenderer.class))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        //</editor-fold>
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from(GameRegistry.class)
                        .to(GameRegistry.class)
                        //<editor-fold desc="Methods">
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("registerBlock")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Lapa;"), Type.getType(String.class))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("registerBlock")
                                        .setObfuscatedDescArgumentTypes(Type.getType(Block.class), Type.getType(String.class))
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        .addMethodMapping(
                                BuilderMigrationClass.BuilderMigrationMethod.instance()
                                        .setObfuscatedName("addRecipe")
                                        .setObfuscatedDescArgumentTypes(Type.getType("Lwm;"), Type.getType(Object[].class))
                                        .setObfuscatedDescReturnType(Type.VOID_TYPE)

                                        .setDeobfuscatedName("addRecipe")
                                        .setDeobfuscatedDescArgumentTypes()
                                        .setDeobfuscatedDescReturnType(Type.VOID_TYPE)
                                        .build()
                        )
                        //</editor-fold>
                        .build()
        );
        //</editor-fold>
    }

}
