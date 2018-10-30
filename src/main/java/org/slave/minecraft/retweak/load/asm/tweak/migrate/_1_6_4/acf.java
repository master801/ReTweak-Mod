package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.block.material.Material;
import net.minecraft.world.biome.BiomeGenBase;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

/**
 * Created by master on 10/22/18 at 12:09 PM
 *
 * @author master
 */
@Obfuscated(name = "acf")
@Deobfuscated(
        _package = @Package("net.minecraft.world"),
        name = "IBlockAccess"
)
public interface acf {

    @Obfuscated(name = "a")
    @Deobfuscated(name = "getBlockId")
    int a();

    @Obfuscated(name = "i")
    @Deobfuscated(name = "getBrightness")
    float i(final int x, final int y, final int z);

    @Obfuscated(name = "q")
    @Deobfuscated(name = "getLightBrightness")
    float q(final int x, final int y, final int z);

    @Obfuscated(name = "g")
    @Deobfuscated(name = "getBlockMaterial")
    Material g(final int x, final int y, final int z);

    @Obfuscated(name = "t")
    @Deobfuscated(name = "isBlockOpaqueCube")
    boolean t(final int x, final int y, final int z);

    @Obfuscated(name = "u")
    @Deobfuscated(name = "isBlockNormalCube")
    boolean u(final int x, final int y, final int z);

    @Obfuscated(name = "a")
    @Deobfuscated(name = "getBiomeGenForCoords")
    BiomeGenBase a(final int par1, final int par2);

    @Obfuscated(name = "T")
    @Deobfuscated(name = "extendedLevelsInChunkCache")
    boolean T();

    @Obfuscated(name = "w")
    @Deobfuscated(name = "doesBlockHaveSolidTopSurface")
    boolean w(final int x, final int y, final int z);

    @Obfuscated(name = "V")
    @Deobfuscated(name = "getWorldVec3Pool")
    atd V();

    @Obfuscated(name = "j")
    @Deobfuscated(name = "isBlockProvidingPowerTo")
    int j(int x, int y, int z, int direction);

}
