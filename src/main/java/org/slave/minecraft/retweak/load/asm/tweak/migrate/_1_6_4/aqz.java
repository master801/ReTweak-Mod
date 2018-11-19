package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_6_4;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;

/**
 * Created by master on 10/22/18 at 10:53 AM
 *
 * @author master
 */
@Obfuscated(name = "aqz")
@Deobfuscated(name = "net/minecraft/block/Block")
public class aqz extends Block {

    public static int[] blockFireSpreadSpeed = new int[4096];
    public static int[] blockFlammability = new int[4096];

    protected aqz(final int blockId, final Material material) {
        super(material);
    }

}
