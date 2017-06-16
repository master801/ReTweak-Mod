package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Obfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

/**
 * Created by Master on 6/15/2017 at 1:33 PM.
 *
 * @author Master
 */
@Obfuscated(
    name = "akc",
    _package = @Package("")
)
@Deobfuscated(
    name = "Material",
    _package = @Package("net.minecraft.block.material")
)
public class akc extends Material {

    @Obfuscated(
        name = "d",
        _package = @Package("")
    )
    @Deobfuscated(
        name = "wood",
        _package = @Package("")
    )
    public static final Material d = Material.wood;

    public akc(final MapColor mapColor) {
        super(mapColor);
    }

}
