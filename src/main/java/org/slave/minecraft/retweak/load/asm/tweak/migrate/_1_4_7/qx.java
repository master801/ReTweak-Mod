package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 7/13/2018 at 11:04 AM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package(""),
        name = "qx"
)
@Deobfuscated(
        _package = @Package("net.minecraft.entity.player"),
        name = "EntityPlayer"
)
public abstract class qx extends EntityPlayer {

    public qx(final World p_i45324_1_, final GameProfile p_i45324_2_) {
        super(p_i45324_1_, p_i45324_2_);
    }

}
