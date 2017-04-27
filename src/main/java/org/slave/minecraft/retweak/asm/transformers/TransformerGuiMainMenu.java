package org.slave.minecraft.retweak.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slave.lib.asm.transformers.MethodTransformer;
import org.slave.lib.resources.MethodStub;
import org.slave.minecraft.retweak.util.ReTweakResources;

/**
 * Created by Master on 4/27/2017 at 12:07 AM.
 *
 * @author Master
 */
public final class TransformerGuiMainMenu extends MethodTransformer implements IClassTransformer {

    public TransformerGuiMainMenu() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected void transformMethod(MethodNode methodNode, boolean isObfuscated) {
        InsnList instructionsToInject = new InsnList();
        instructionsToInject.add(
                new LabelNode(new Label())
        );
        instructionsToInject.add(
                new VarInsnNode(
                        Opcodes.ALOAD,
                        2
                )
        );
        instructionsToInject.add(
                new FieldInsnNode(
                        Opcodes.GETSTATIC,
                        "org/slave/minecraft/retweak/util/ReTweakResources",
                        "RETWEAK_SPLASH_TEXT",
                        "Ljava/util/List;"
                )
        );
        instructionsToInject.add(
                new MethodInsnNode(
                        Opcodes.INVOKEVIRTUAL,
                        "java/util/List",
                        "addAll",
                        "(Ljava/util/Collection;)Z",
                        true
                )
        );
    }

    @Override
    protected MethodStub getMethod() {
        return new MethodStub(
                "<init>",
                "()V"
        );
    }

    @Override
    protected String getClassName(boolean isObfuscated) {
        return "net.minecraft.client.gui.GuiMainMenu";
    }

    @Override
    protected boolean writeClassFile() {
        return true;
    }

}
