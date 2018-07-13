package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import net.minecraft.creativetab.CreativeTabs;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by Master on 7/13/2018 at 11:09 AM.
 *
 * @author Master
 */
@Obfuscated(
        _package = @Package(""),
        name = "tf"
)
@Deobfuscated(
        _package = @Package("net.minecraft.creativetab"),
        name = "CreativeTabs"
)
public abstract class tf extends CreativeTabs {

    public static final CreativeTabs f = CreativeTabs.tabMisc;

    public tf(final String label) {
        super(label);
    }

    public tf(final int p_i1853_1_, final String p_i1853_2_) {
        super(p_i1853_1_, p_i1853_2_);
    }

}
