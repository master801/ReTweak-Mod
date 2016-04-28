package org.slave.minecraft.retweak.loading.mappings;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master on 4/27/2016 at 3:33 PM.
 *
 * @author Master
 */
final class V_1_4_7_Mapping extends Mapping {

    V_1_4_7_Mapping() {
    }

    @Override
    protected void method(final MethodNode methodNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD: {} {}", methodNode.name, methodNode.desc);
    }

    @Override
    protected void methodInsn(final MethodInsnNode methodInsnNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD INSN: {} {} {}", methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);
    }

    @Override
    protected void field(final FieldNode fieldNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD: {} {}", fieldNode.name, fieldNode.desc);
    }

    @Override
    protected void fieldInsn(final FieldInsnNode fieldInsnNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD INSN: {} {} {}", fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
    }

}
