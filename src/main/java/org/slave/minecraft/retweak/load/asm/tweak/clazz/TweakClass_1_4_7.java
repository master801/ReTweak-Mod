package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.akm;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.amq;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.axa;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.bbv;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.bcj;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.cpw.mods.fml.client.registry.RenderingRegistry;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.me;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.tj;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.up;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.yy;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.ITickHandler;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.TickType;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/12/2018 at 8:49 PM.
 *
 * @author Master
 */
public final class TweakClass_1_4_7 extends AbstractTweakClass {

    public static final TweakClass INSTANCE = new TweakClass_1_4_7();

    private TweakClass_1_4_7() {
        super(GameVersion.V_1_4_7);

        super.addMigrationClass(//net.minecraft.block.Block
                BuilderMigrationClass.instance()
                        .from("amq")
                        .to(amq.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.block.BlockBreakable
                BuilderMigrationClass.instance()
                        .from("akm")
                        .to(akm.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.block.material.Material
                BuilderMigrationClass.instance()
                        .from("agi")
                        .to(Material.class)
                        .addFieldMapping(
                                BuilderMigrationField.instance()
                                        .setObfuscatedName("r")
                                        .setDeobfuscatedName("glass")
                                        .setFromDescType(Type.getType("Lagi;"))
                                        .setToDescType(Type.getType(Material.class))
                                        .build()
                        )
                        .build()
        );
        super.addMigrationClass(//net.minecraft.client.model.ModelBase
                BuilderMigrationClass.instance()
                        .from("axa")
                        .to(axa.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.client.renderer.entity.Render
                BuilderMigrationClass.instance()
                        .from("bbv")
                        .to(bbv.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.client.renderer.entity.RenderLiving
                BuilderMigrationClass.instance()
                        .from("bcj")
                        .to(bcj.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.item.Item
                BuilderMigrationClass.instance()
                        .from("up")
                        .to(up.class)
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()//net.minecraft.entity.EnumCreatureType
                        .from("me")
                        .to(me.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.world.biome.BiomeGenBase
                BuilderMigrationClass.instance()
                        .from("yy")
                        .to(yy.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.creativetab.CreativeTabs
                BuilderMigrationClass.instance()
                        .from("tj")
                        .to(tj.class)
                        .build()
        );
        super.addMigrationClass(//net.minecraft.item.ItemStack
                BuilderMigrationClass.instance()
                        .from("ur")
                        .to(ItemStack.class)
                        .build()
        );

        // FML/Forge classes
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("cpw/mods/fml/common/TickType")
                        .to(TickType.class)
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("cpw/mods/fml/common/ITickHandler")
                        .to(ITickHandler.class)
                        .build()
        );
        super.addMigrationClass(
                BuilderMigrationClass.instance()
                        .from("cpw/mods/fml/client/registry/RenderingRegistry")
                        .to(RenderingRegistry.class)
                        .build()
        );
    }

}