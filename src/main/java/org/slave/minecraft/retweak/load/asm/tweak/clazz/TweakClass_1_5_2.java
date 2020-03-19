package org.slave.minecraft.retweak.load.asm.tweak.clazz;

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
        //</editor-fold>
    }

}
