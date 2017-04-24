package org.slave.minecraft.retweak.asm.transformers;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.lib.asm.transformers.MethodTransformer;
import org.slave.lib.resources.MethodStub;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 3/19/2016 at 1:49 PM.
 *
 * @author Master801
 */
public final class LoaderTransformer extends MethodTransformer implements IClassTransformer {

    public LoaderTransformer() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected void transformMethod(final MethodNode methodNode, final boolean isObfuscated) {
        for(int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);

            if (abstractInsnNode instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;

                //Injects our code after:
                //    discoverer = identifyMods();
                if (fieldInsnNode.getOpcode() == Opcodes.PUTFIELD && fieldInsnNode.owner.equals("cpw/mods/fml/common/Loader") && fieldInsnNode.name.equals("discoverer") && fieldInsnNode.desc.equals("Lcpw/mods/fml/common/discovery/ModDiscoverer;")) {
                    InsnList injectionInstructions = new InsnList();
                    injectionInstructions.add(new LabelNode(new Label()));
                    injectionInstructions.add(new FieldInsnNode(
                            Opcodes.GETSTATIC,
                            "org/slave/minecraft/retweak/loading/mod/ReTweakLoader",
                            "INSTANCE",
                            "Lorg/slave/minecraft/retweak/loading/mod/ReTweakLoader;"
                    ));
                    injectionInstructions.add(new MethodInsnNode(
                            Opcodes.INVOKEVIRTUAL,
                            "org/slave/minecraft/retweak/loading/mod/ReTweakLoader",
                            "loadMods",
                            "()V",
                            false
                    ));
                    methodNode.instructions.insert(
                            fieldInsnNode,
                            injectionInstructions
                    );
                }
            }
        }
    }

    @Override
    protected String getClassName(boolean isNameTransformed) {
        return "cpw.mods.fml.common.Loader";
    }

    @Override
    protected MethodStub getMethod() {
        return new MethodStub(
                "loadMods",
                "()V"
        );
    }

    @Override
    protected boolean writeClassFile() {
        return ReTweakResources.DEBUG;
    }

    @Override
    protected boolean writeASMFile() {
        return ReTweakResources.DEBUG;
    }

}
