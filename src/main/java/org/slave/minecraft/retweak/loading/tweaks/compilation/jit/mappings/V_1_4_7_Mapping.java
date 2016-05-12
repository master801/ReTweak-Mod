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
            if (methodInsnNode.owner.equals("cpw/mods/fml/common/event/FMLPreInitializationEvent")) {
                if (methodInsnNode.name.equals("applyModContainer") && methodInsnNode.desc.equals("(Lcpw/mods/fml/common/ModContainer;)V")) {
                    throw new UnsupportedOperationException(
                            Kirai.from(
                                    "Mod cannot invoke method {class} {name}/{desc}!"
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
                } else if (methodInsnNode.name.equals("getModMetadata") && methodInsnNode.desc.equals("()Lcpw/mods/fml/common/ModMetadata;")) {
                    throw new UnsupportedOperationException(
                            Kirai.from(
                                    "Mod cannot invoke method {class} {name}/{desc}!"
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
                } else if (methodInsnNode.name.equals("getAsmData") && methodInsnNode.desc.equals("()Lcpw/mods/fml/common/discovery/ASMDataTable;")) {
                    throw new UnsupportedOperationException(
                            Kirai.from(
                                    "Mod cannot invoke method {class} {name}/{desc}!"
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
                } else if (methodInsnNode.name.equals("getModLog") && methodInsnNode.desc.equals("()Lorg/apache/logging/log4j/Logger;")) {
                    throw new UnsupportedOperationException(
                            Kirai.from(
                                    "Mod cannot invoke method {class} {name}/{desc}!"
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
            } else if (methodInsnNode.owner.equals("cpw/mods/fml/common/event/FMLPostInitializationEvent")) {
                if (methodInsnNode.name.equals("buildSoftDependProxy") && methodInsnNode.desc.equals("(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;")) {
                    throw new UnsupportedOperationException(
                            Kirai.from(
                                    "Mod cannot invoke method {class} {name}/{desc}!"
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
        //</editor-fold>

        return false;
    }

}
