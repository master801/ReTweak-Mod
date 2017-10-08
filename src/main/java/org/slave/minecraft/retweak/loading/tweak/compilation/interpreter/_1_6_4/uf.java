package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Obfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

/**
 * Created by Master on 4/20/2017 at 10:01 AM.
 *
 * @author Master
 */
@Obfuscated(
    name = "uf",
    _package = @Package("")
)
@Deobfuscated(
    name = "EntityPlayer",
    _package = @Package("net.minecraft.entity.player")
)
public abstract class uf extends EntityPlayer {//TODO

    public uf(final World world, final GameProfile gameProfile) {//TODO GameProfile not in 1.6.4?
        super(world, gameProfile);
    }

}
