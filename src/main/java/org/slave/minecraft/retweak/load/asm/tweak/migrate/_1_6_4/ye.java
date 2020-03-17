package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.item.ItemStack;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Created by master on 11/20/18 at 4:17 PM
 *
 * @author master
 */
@Mixin(value = ItemStack.class)
@Obfuscated(name = "ye")
@Deobfuscated(
        _package = @Package("net.minecraft.item"),
        name = "ItemStack"
)
public class ye /*extends ItemStack*/ {

    @Obfuscated(name = "d")
    @Deobfuscated(name = "itemID")
    public int d;

    public ye(final aqz block) {
//        super(block);
        //TODO
    }

    public ye(final yc item) {
//        super(item);
        //TODO
    }

    //TODO

}
