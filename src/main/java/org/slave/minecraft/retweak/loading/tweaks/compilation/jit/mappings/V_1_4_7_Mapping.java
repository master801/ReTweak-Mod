package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import com.github.pwittchen.kirai.library.Kirai;
import cpw.mods.fml.common.Mod.EventHandler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.minecraft.retweak.loading.ReTweakModContainer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.Iterator;

/**
 * Created by Master on 4/27/2016 at 3:33 PM.
 *
 * @author Master
 */
final class V_1_4_7_Mapping extends Mapping {

    @Override
    protected boolean _class(final ReTweakModContainer reTweakModContainer, final ClassNode classNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING CLASS: {} {}", classNode.name, classNode.superName);
        if (classNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = classNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                if (annotationNode.desc.equals(Mapping.ANNOTATION_NETWORKMOD_DESC)) {
                    //TODO We should really do something else with the networkmod annotation than remove it...
                    annotationNodeIterator.remove();
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removed annotation {} from class {}",
                                annotationNode.desc,
                                classNode.name
                        );
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean method(final ReTweakModContainer reTweakModContainer, final MethodNode methodNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD: {}/{}", methodNode.name, methodNode.desc);

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
    protected boolean field(final ReTweakModContainer reTweakModContainer, final FieldNode fieldNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD: {} {}", fieldNode.name, fieldNode.desc);

        return false;
    }

    @Override
    protected boolean fieldInsn(final ReTweakModContainer reTweakModContainer, final FieldInsnNode fieldInsnNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD INSN: {}/{} {}", fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);
        if (fieldInsnNode.getOpcode() == Opcodes.PUTSTATIC && fieldInsnNode.owner.equals("net/minecraft/client/renderer/ChestItemRenderHelper") && fieldInsnNode.name.equals("field_78545_a") && fieldInsnNode.desc.equals("Lnet/minecraft/client/renderer/ChestItemRenderHelper;")) return true;

        return false;
    }

    @Override
    protected boolean methodInsn(final ReTweakModContainer reTweakModContainer, final MethodInsnNode methodInsnNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD INSN: {}/{}/{}", methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);

        //<editor-fold desc="Unsupported method invokes">
        if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
            String[][] methods = new String[][] {
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "applyModContainer",
                            "(Lcpw/mods/fml/common/ModContainer;)V"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "getModMetadata",
                            "()Lcpw/mods/fml/common/ModMetadata;"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "getAsmData",
                            "()Lcpw/mods/fml/common/discovery/ASMDataTable;"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "getModLog",
                            "()Lorg/apache/logging/log4j/Logger;"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPostInitializationEvent",
                            "buildSoftDependProxy",
                            "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;",
                    }
            };

            for(String[] method : methods) {
                checkMethodInsn(
                        method[0],
                        method[1],
                        method[2],
                        methodInsnNode
                );
            }
        }
        //</editor-fold>

        return false;
    }

    private void checkMethodInsn(final String owner, final String name, final String desc, final MethodInsnNode methodInsnNode) throws UnsupportedOperationException {
        if (methodInsnNode.owner.equals(owner) && methodInsnNode.name.equals(name) && methodInsnNode.desc.equals(desc)) {
            throw new UnsupportedOperationException(
                    Kirai.from(
                            "Mod cannot invoke method \"{class}/{name}{desc}\"!"
                    ).put(
                            "class",
                            methodInsnNode.owner
                    ).put(
                            "name",
                            methodInsnNode.name
                    ).put(
                            "desc",
                            methodInsnNode.desc
                    ).format().toString()
            );
        }
    }

}
