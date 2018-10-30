package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.cpw.mods.fml.client.registry;

import net.minecraft.entity.Entity;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;
import org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7.bbv;

/**
 * Created by Master on 7/19/2018 at 2:05 PM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package("cpw.mods.fml.client.registry"),
        name = "RenderingRegistry"
)
@Deobfuscated(
        _package = @Package("cpw.mods.fml.client.registry"),
        name = "RenderingRegistry"
)
public final class RenderingRegistry {

    public void registerEntityRenderingHandler(final Class<? extends Entity> entityClass, final bbv renderer) {
        //TODO
        ReTweak.LOGGER_RETWEAK.debug("");
        cpw.mods.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(entityClass, renderer);
    }

}
