package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

import java.util.Random;

/**
 * Created by master on 10/22/18 at 10:53 AM
 *
 * @author master
 */
@Obfuscated(name = "aqz")
@Deobfuscated(
        _package = @Package("net.minecraft.block"),
        name = "Block"
)
public class aqz extends Block {

    public static int[] blockFireSpreadSpeed = new int[4096];
    public static int[] blockFlammability = new int[4096];

    @Obfuscated(name = "be")
    @Deobfuscated(name = "fence")
    public static final aqz be = wrap(Blocks.fence);

    @Obfuscated(name = "cv")
    @Deobfuscated(name = "blockID")
    public int cv;

    protected aqz(final int blockId, final Material material) {
        super(material);
    }

    @Obfuscated(name = "a", parameters = { int.class, int.class }, returnParameter = ms.class)
    @Deobfuscated(name = "setIconIndex", parameters = { int.class, int.class })
    public ms a(final int x, final int y) {
        return null;
    }

    @Obfuscated(name = "b", parameters = { abw.class, int.class, int.class, int.class, ye.class })
    @Deobfuscated(name = "dropBlockAsItem_do", parameters = { World.class, int.class, int.class, ItemStack.class })
    public void b(final abw world, final int xCoord, final int yCoord, final int zCoord, final ye stack) {
        //TODO
    }

    @Obfuscated(name = "a", parameters = { int.class, Random.class, int.class })
    @Deobfuscated(name = "idDropped", parameters = { int.class, Random.class, int.class })
    public int idDropped(final int metadata, final Random random, final int entropy) {
        //TODO
        return -1;
    }

    private static aqz wrap(final Block block) {
        aqz wrapper_aqz = new aqz(-1, block.getMaterial());
        return wrapper_aqz;
    }

}
