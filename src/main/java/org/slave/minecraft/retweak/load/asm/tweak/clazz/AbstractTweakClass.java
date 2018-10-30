package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import cpw.mods.fml.common.Mod.EventHandler;
import net.minecraftforge.common.config.Property;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.network.NetworkMod;
import org.slave.minecraft.retweak.load.asm.tweak.migrate.net.minecraftforge.common.Configuration;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/21/2018 at 1:42 AM.
 *
 * @author Master
 */
abstract class AbstractTweakClass extends TweakClass {

    AbstractTweakClass(final GameVersion gameVersion) {
        super(gameVersion);

        // FML/Forge classes
        if (gameVersion != GameVersion.V_1_2_5) {//Do not add if 1.2.5
            super.addMigrationClass(
                    MigrationClassBuilder.instance()
                            .from("cpw/mods/fml/common/Mod$PreInit")
                            .to(EventHandler.class)
                            .build()
            );
            super.addMigrationClass(
                    MigrationClassBuilder.instance()
                            .from("cpw/mods/fml/common/Mod$Init")
                            .to(EventHandler.class)
                            .build()
            );
            super.addMigrationClass(
                    MigrationClassBuilder.instance()
                            .from("cpw/mods/fml/common/Mod$PostInit")
                            .to(EventHandler.class)
                            .build()
            );
        }

        if (gameVersion != GameVersion.V_1_2_5) {//Do not add if 1.2.5
            super.addMigrationClass(
                    MigrationClassBuilder.instance()
                            .from("cpw/mods/fml/common/network/NetworkMod")
                            .to(NetworkMod.class)
                            .build()
            );
        }

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
