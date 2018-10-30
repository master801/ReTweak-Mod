package org.slave.minecraft.retweak.load.asm.tweak.migrate._1_4_7;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation.Obfuscated;

/**
 * Created by Master on 8/30/2018 at 1:21 AM.
 *
 * @author Master
 */
@Obfuscated(name = "amq")
@Deobfuscated(name = "net/minecraft/block/Block")
public class amq extends Block {

    protected amq(final int blockId, final Material material) {
        super(material);
    }

    @Obfuscated(name = "a")
    @Deobfuscated(name = "setCreativeTab")
    public amq a(final CreativeTabs tab) {
        setCreativeTab(tab);
        return this;
    }

    @Obfuscated(name = "b")
    @Deobfuscated(name = "setBlockName")
    public amq b(final String name) {
        //TODO
        return this;
    }

    @Obfuscated(name = "c")
    @Deobfuscated(name = "setHardness")
    public amq c(final float hardness) {
        this.setHardness(hardness);
        return this;
    }

    @Obfuscated(name = "r")
    @Deobfuscated(name = "setRequiresSelfNotify")
    public amq r() {//TODO
        return this;
    }

    public amq setTextureFile(final String texturePath) {//TODO
        return this;
    }

}
