package org.slave.minecraft.retweak.asm.transformers.access;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.asm.ReTweakASM;

/**
 * Created by Master on 8/30/2018 at 10:33 AM.
 *
 * @author Master
 */
public final class TransformerItemStack extends BasicTransformer implements IClassTransformer {

    public TransformerItemStack() {
        super(ReTweakASM.LOGGER_RETWEAK_ASM);
    }

    @Override
    protected void transform(final ClassNode classNode) {
        if ((classNode.access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL) {////Has final
            classNode.access ^= Opcodes.ACC_FINAL;//Remove final
        }
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "net.minecraft.item.ItemStack";
    }

    @Override
    protected boolean writeClassFile() {
        return true;
    }

    @Override
    protected boolean writeASMFile() {
        return true;
    }

}
