package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by master on 11/20/18 at 4:12 PM
 *
 * @author master
 */
@Obfuscated(name = "ud")
@Deobfuscated(
        _package = @Package("net.minecraft.entity.player"),
        name = "InventoryPlayer"
)
public class ud extends InventoryPlayer {

    public ud(final EntityPlayer p_i1750_1_) {
        super(p_i1750_1_);
    }

}
