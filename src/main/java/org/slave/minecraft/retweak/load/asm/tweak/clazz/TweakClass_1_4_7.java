package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraftforge.common.config.Property;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.net.minecraftforge.common.Configuration;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/12/2018 at 8:49 PM.
 *
 * @author Master
 */
public final class TweakClass_1_4_7 extends TweakClass {

    public static final TweakClass INSTANCE = new TweakClass_1_4_7();

    private TweakClass_1_4_7() {
        super(GameVersion.V_1_4_7);

        // FML/Forge classes
//        super.addClassToOverride("cpw/mods/fml/common/event/FMLInitializationEvent");
//        super.addClassToOverride("cpw/mods/fml/common/event/FMLPreInitializationEvent");
//        super.addClassToOverride("cpw/mods/fml/common/event/FMLPostInitializationEvent");
//        super.addClassToOverride("cpw/mods/fml/common/network/NetworkMod");

//        super.addClassToMigrate("cpw/mods/fml/common/Mod$PreInit", EventHandler.class);
//        super.addClassToMigrate("cpw/mods/fml/common/Mod$FMLInitializationEvent", EventHandler.class);
//        super.addClassToMigrate("cpw/mods/fml/common/Mod$FMLPreInitializationEvent", EventHandler.class);

//        super.addClassToMigrate("net/minecraftforge/common/Configuration", Configuration.class);

        super.addMigrationClass(
                MigrationClassBuilder.instance()
                        .from("cpw/mods/fml/common/Mod$PreInit")
                        .to(EventHandler.class)
                        .build()
        );
        super.addMigrationClass(
                MigrationClassBuilder.instance()
                        .from("cpw/mods/fml/common/Mod$FMLInitializationEvent")
                        .to(EventHandler.class)
                        .build()
        );
        super.addMigrationClass(
                MigrationClassBuilder.instance()
                        .from("cpw/mods/fml/common/Mod$FMLPreInitializationEvent")
                        .to(EventHandler.class)
                        .build()
        );

        super.addMigrationClass(
                MigrationClassBuilder.instance()
                        .from("net/minecraftforge/common/Configuration")
                        .to(Configuration.class)
                        .build()
        );
        super.addMigrationClass(
                MigrationClassBuilder.instance()
                        .from("net/minecraftforge/common/Property")
                        .to(Property.class)
                        .build()
        );
    }

}
