package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_5_2;

import net.minecraft.item.ItemStack;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Created by Master on 7/21/2018 at 1:44 AM.
 *
 * @author Master
 */
@Obfuscated(name = "wm")
@Deobfuscated(
        _package = @Package("net.minecraft.item"),
        name = "ItemStack"
)
@Mixin(ItemStack.class)
public class wm /*extends ItemStack*/ {//TODO
}
