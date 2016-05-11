package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import cpw.mods.fml.common.Mod.EventHandler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
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

    @Override
    protected boolean _class(final ClassNode classNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING CLASS: {} {}", classNode.name, classNode.superName);

        return false;
    }

    @Override
    protected boolean method(final MethodNode methodNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD: {}/{}", methodNode.name, methodNode.desc);

        if (methodNode.visibleAnnotations != null) {
            for(AnnotationNode annotationNode : methodNode.visibleAnnotations) {
                if (annotationNode.desc.equals(Mapping.ANNOTATION_PREINIT_DESC) || annotationNode.desc.equals(Mapping.ANNOTATION_INIT_DESC) || annotationNode.desc.equals(Mapping.ANNOTATION_POSTINIT_DESC)) {
                    String previousDesc = annotationNode.desc;
                    annotationNode.desc = Type.getDescriptor(EventHandler.class);
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Remapped annotation desc from \"{}\" to \"{}\"",
                                previousDesc,
                                annotationNode.desc
                        );
                    }
                    break;
                }
            }
        }

        return false;
    }

    @Override
    protected boolean field(final FieldNode fieldNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD: {} {}", fieldNode.name, fieldNode.desc);

        return false;
    }

    @Override
    protected boolean fieldInsn(final FieldInsnNode fieldInsnNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD INSN: {}/{} {}", fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
        if (fieldInsnNode.getOpcode() == Opcodes.PUTSTATIC && fieldInsnNode.owner.equals("net/minecraft/client/renderer/ChestItemRenderHelper") && fieldInsnNode.name.equals("field_78545_a") && fieldInsnNode.desc.equals("Lnet/minecraft/client/renderer/ChestItemRenderHelper;")) return true;

        return false;
    }

    @Override
    protected boolean methodInsn(final MethodInsnNode methodInsnNode) {
        ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD INSN: {}/{}/{}", methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);

//        ClientRegistry.registerTileEntity(null,null,null);

        return false;
    }

}
