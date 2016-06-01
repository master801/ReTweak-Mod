package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import com.github.pwittchen.kirai.library.Kirai;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.lib.helpers.ASMHelper;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Master on 4/27/2016 at 3:33 PM.
 *
 * @author Master
 */
final class V_1_4_7_Mapping extends Mapping {

    private final HashMap<_Type, HashMap<Holder, Holder>> types = new HashMap<>();

    V_1_4_7_Mapping() {
        for(_Type type : _Type.values()) {
            types.put(
                    type,
                    new HashMap<Holder, Holder>()
            );
        }

        //<editor-fold desc="ANNOTATION">
        final HashMap<Holder, Holder> annotationMap = types.get(_Type.ANNOTATION);

        //<editor-fold desc="CLASS">
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_NETWORK_MOD_DESC
                ),
                null
        );
        //</editor-fold>

        //<editor-fold desc="METHOD">
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_PRE_INIT_DESC
                ),
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Type.getDescriptor(EventHandler.class)
                )
        );
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_INIT_DESC
                ),
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Type.getDescriptor(EventHandler.class)
                )
        );
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_POST_INIT_DESC
                ),
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Type.getDescriptor(EventHandler.class)
                )
        );
        //</editor-fold>

        //</editor-fold>

        //<editor-fold desc="CLASS">
        //</editor-fold>

        //<editor-fold desc="INNER CLASS">
        final HashMap<Holder, Holder> innerClassMap = types.get(_Type.INNER_CLASS);
        innerClassMap.put(
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mapping.ANNOTATION_PRE_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_PRE_INIT_DESC.indexOf('$')
                        ),
                        Mapping.ANNOTATION_PRE_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_PRE_INIT_DESC.length() - 1
                        ),
                        Mapping.ANNOTATION_PRE_INIT_DESC.substring(
                                Mapping.ANNOTATION_PRE_INIT_DESC.lastIndexOf('$') + 1,
                                Mapping.ANNOTATION_PRE_INIT_DESC.length() - 1
                        )
                ),
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mod.class.getCanonicalName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getSimpleName()
                )
        );
        innerClassMap.put(
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mapping.ANNOTATION_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_INIT_DESC.indexOf('$')
                        ),
                        Mapping.ANNOTATION_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_INIT_DESC.length() - 1
                        ),
                        Mapping.ANNOTATION_INIT_DESC.substring(
                                Mapping.ANNOTATION_INIT_DESC.lastIndexOf('$') + 1,
                                Mapping.ANNOTATION_INIT_DESC.length() - 1
                        )
                ),
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mod.class.getCanonicalName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getSimpleName()
                )
        );
        innerClassMap.put(
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mapping.ANNOTATION_POST_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_POST_INIT_DESC.indexOf('$')
                        ),
                        Mapping.ANNOTATION_POST_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_POST_INIT_DESC.length() - 1
                        ),
                        Mapping.ANNOTATION_POST_INIT_DESC.substring(
                                Mapping.ANNOTATION_POST_INIT_DESC.lastIndexOf('$') + 1,
                                Mapping.ANNOTATION_POST_INIT_DESC.length() - 1
                        )
                ),
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mod.class.getCanonicalName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getSimpleName()
                )
        );
        //</editor-fold>

        //<editor-fold desc="FIELD">
        //</editor-fold>

        //<editor-fold desc="METHOD">
        //</editor-fold>

        //<editor-fold desc="FIELD INSN">
        final HashMap<Holder, Holder> fieldInsnMap = types.get(_Type.FIELD_INSN);
        //<editor-fold desc="ITEMS">
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77737_bm",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151078_bh",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77770_aF",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151116_aA",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77770_aF",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151116_aA",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77703_o",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151042_j",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_72002_bp",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150411_aY",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77669_D",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151055_y",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_72061_ba",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150423_aK",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77686_W",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151027_R",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77685_T",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151015_O",
                        "Lnet/minecraft/item/Item;"
                )
        );
        //No entry holders available for field "field_77683_K"
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_71956_V",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150320_F",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_71963_Z",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150331_J",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77804_ap",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151145_ak",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77767_aC",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151137_ax",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77725_bx",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151064_bs",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77722_bw",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151065_br",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77756_aW",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151100_aR",
                        "Lnet/minecraft/item/Item;"
                )
        );
        //No holders available for field "field_71987_y"
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_82796_bJ",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151162_bE",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77746_aZ",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151105_aU",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77771_aG",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151117_aB",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_77747_aY",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151102_aT",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "field_82791_bT",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151158_bO",
                        "Lnet/minecraft/item/Item;"
                )
        );


        /*
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        null,
                        "",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "",
                        "Lnet/minecraft/item/Item;"
                )
        );
        */


        //</editor-fold>

        /*
        types.get(_Type.FIELD_INSN).put(
                new Holder(
                        _Type.FIELD_INSN,
                        -1,
                        null,
                        null,
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        -1,
                        "net/minecraft/init/Items",
                        null,
                        "Lnet/minecraft/item/Item;"
                )
        );
        */
        //</editor-fold>

        //<editor-fold desc="METHOD INSN">
        final HashMap<Holder, Holder> methodInsnMap = types.get(_Type.METHOD_INSN);
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        -1,
                        "net/minecraft/item/crafting/FurnaceRecipes",
                        "func_77600_a",
                        "(ILnet/minecraft/item/ItemStack;F)V"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        -1,
                        "net/minecraft/item/crafting/FurnaceRecipes",
                        "func_151396_a",
                        "(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;F)V"
                )
        );
        //</editor-fold>
    }

    @Override
    protected boolean _class(final String className, final ClassNode classNode) {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING CLASS: {} {}",
                    classNode.name,
                    classNode.superName
            );
        }
        if (classNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = classNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        annotationNode.desc
                );
                if (!ArrayHelper.isNullOrEmpty(holders)) {
                    for(Holder holder : holders) {
                        if (holder == null) {//Remove annotation
                            annotationNodeIterator.remove();
                            if (ReTweakResources.DEBUG) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed annotation \"{}\" from class {}",
                                        annotationNode.desc,
                                        classNode.name
                                );
                            }
                            continue;
                        }
                        holder.set(annotationNode);
                    }
                }
            }
        }
        if (classNode.innerClasses != null) {
            Iterator<InnerClassNode> innerClassNodeIterator = classNode.innerClasses.iterator();
            while(innerClassNodeIterator.hasNext()) {
                InnerClassNode innerClassNode = innerClassNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.INNER_CLASS,
                        innerClassNode.access,
                        innerClassNode.outerName,
                        innerClassNode.name,
                        innerClassNode.innerName
                );
                if (holders != null) {
                    for(Holder holder : holders) {
                        if (holder == null) {
                            innerClassNodeIterator.remove();
                            if (ReTweakResources.DEBUG) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed inner-class \"{}\" from class {}",
                                        innerClassNode.name,
                                        classNode.name
                                );
                            }
                            continue;
                        }
                        holder.set(innerClassNode);
                    }
                }
                //TODO
            }
        }

        Holder[] holders = getHolders(
                _Type.CLASS,
                -1,
                null,
                classNode.name,
                null
        );
        if (!ArrayHelper.isNullOrEmpty(holders)) {
            for(Holder holder : holders) {
                if (holder == null) continue;
                holder.set(classNode);
            }
        }
        return false;
    }

    @Override
    protected boolean field(final String className, final FieldNode fieldNode) {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING FIELD: {} {}",
                    fieldNode.name,
                    fieldNode.desc
            );
        }

        if (fieldNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = fieldNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        annotationNode.desc
                );
                if (holders != null) {
                    for(Holder holder : holders) {
                        if (holder == null) {//Remove annotation
                            annotationNodeIterator.remove();
                            if (ReTweakResources.DEBUG) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed annotation \"{}\" from field {}",
                                        annotationNode.desc,
                                        ASMHelper.toString(fieldNode)
                                );
                            }
                            continue;
                        }
                        holder.set(annotationNode);
                    }
                }
            }
        }

        Holder[] holders = getHolders(
                _Type.FIELD,
                -1,
                null,
                fieldNode.name,
                fieldNode.desc
        );
        if (!ArrayHelper.isNullOrEmpty(holders)) {
            for(Holder holder : holders) {
                if (holder == null) continue;
                holder.set(fieldNode);
            }
        }
        return false;
    }

    @Override
    protected boolean method(final String className, final MethodNode methodNode) {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING METHOD: {}{}",
                    methodNode.name,
                    methodNode.desc
            );
        }

        if (methodNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = methodNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        annotationNode.desc
                );
                if (!ArrayHelper.isNullOrEmpty(holders)) {
                    for(Holder holder : holders) {
                        if (holder == null) {
                            annotationNodeIterator.remove();
                            if (ReTweakResources.DEBUG) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed annotation \"{}\" from method {}",
                                        annotationNode.desc,
                                        ASMHelper.toString(methodNode)
                                );
                            }
                            continue;
                        }
                        holder.set(annotationNode);
                    }
                }
            }
        }

        Holder[] holders = getHolders(
                _Type.METHOD,
                -1,
                null,
                methodNode.name,
                methodNode.desc
        );
        if (!ArrayHelper.isNullOrEmpty(holders)) {
            for(Holder holder : holders) {
                if (holder == null) continue;
                holder.set(methodNode);
            }
        }
        return false;
    }

    @Override
    protected boolean fieldInsn(final String className, final int index, final FieldInsnNode fieldInsnNode) {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING FIELD INSN: {}/{} {}",
                    fieldInsnNode.owner,
                    fieldInsnNode.name,
                    fieldInsnNode.desc
            );
        }

        //<editor-fold desc="Unsupported fields">
        String[][] fields = new String[0][];//No fields are yet unsupported...
        for(String[] field : fields) {
            if (ArrayHelper.isNullOrEmpty(field)) continue;
            checkFieldInsn(
                    field[0],
                    field[1],
                    field[2],
                    fieldInsnNode
            );
        }
        //</editor-fold>

        Holder[] holders = getHolders(
                _Type.FIELD_INSN,
                fieldInsnNode.getOpcode(),
                fieldInsnNode.owner,
                fieldInsnNode.name,
                fieldInsnNode.desc
        );
        if (holders != null) {
            for(Holder holder : holders) holder.set(fieldInsnNode);
        }
        if (fieldInsnNode.getOpcode() == Opcodes.PUTSTATIC && fieldInsnNode.owner.equals("net/minecraft/client/renderer/ChestItemRenderHelper") && fieldInsnNode.name.equals("field_78545_a") && fieldInsnNode.desc.equals("Lnet/minecraft/client/renderer/ChestItemRenderHelper;")) {
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Removed field insn node \"{}\" at index {} from class \"{}\"",
                        ASMHelper.toString(fieldInsnNode),
                        index,
                        className
                );
            }
            return true;
        }
        if (fieldInsnNode.owner.equals("net/minecraft/item/Item")) {
            if (fieldInsnNode.desc.equals("I") && fieldInsnNode.getPrevious() instanceof FieldInsnNode) {//Get item id from item
                FieldInsnNode fi = (FieldInsnNode)fieldInsnNode.getPrevious();
                if (fi.desc.equals("L" + fieldInsnNode.owner + ";")) {
                    holders = getHolders(
                            _Type.FIELD_INSN,
                            fi.getOpcode(),
                            fi.owner,
                            fi.name,
                            fi.desc
                    );
                    if (holders != null) {
                        for(Holder holder : holders) holder.set(fi);
                    }
                    return true;//Remove item id instruction
                }
            }
        }
        switch(fieldInsnNode.getOpcode()) {
            case Opcodes.GETFIELD:
                switch(fieldInsnNode.owner) {
                    case "net/minecraft/item/Item":
                        if (fieldInsnNode.name.equals("field_77779_bT") && fieldInsnNode.desc.equals("I")) {
                            return true;//Remove item id call
                        }
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    protected void postMethodNode(final String className, final int index, final MethodNode methodNode) {
        if (methodNode.instructions != null) {
            Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
            int i = 0;

            while(abstractInsnNodeIterator.hasNext()) {
                AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();
                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                } else if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                }
                i++;
            }
        }
    }

    @Override
    protected boolean methodInsn(final String className, final int index, final MethodInsnNode methodInsnNode) {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING METHOD INSN: {}/{}{}",
                    methodInsnNode.owner,
                    methodInsnNode.name,
                    methodInsnNode.desc
            );
        }

        //<editor-fold desc="Unsupported method invokes">
        if (ASMHelper.isInvokeOpcode(methodInsnNode.getOpcode())) {
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
                if (ArrayHelper.isNullOrEmpty(method)) continue;
                checkMethodInsn(
                        method[0],
                        method[1],
                        method[2],
                        methodInsnNode
                );
            }
        }
        //</editor-fold>

        Holder[] holders = getHolders(
                _Type.METHOD_INSN,
                methodInsnNode.getOpcode(),
                methodInsnNode.owner,
                methodInsnNode.name,
                methodInsnNode.desc
        );
        if (holders != null) {
            for(Holder holder : holders) holder.set(methodInsnNode);
        }
        if (methodInsnNode.getOpcode() == Opcodes.INVOKESTATIC) {
            switch(methodInsnNode.owner) {
                case "cpw/mods/fml/common/registry/GameRegistry":
                    if (methodInsnNode.name.equals("addSmelting") && methodInsnNode.desc.equals("(ILnet/minecraft/item/ItemStack;F)V")) {
                        if (methodInsnNode.getPrevious().getPrevious().getPrevious().getPrevious() instanceof FieldInsnNode) {
                            FieldInsnNode fieldInsnNode = (FieldInsnNode)methodInsnNode.getPrevious().getPrevious().getPrevious().getPrevious();
                            switch(fieldInsnNode.desc) {
                                case "L" + "net/minecraft/item/Item" + ";": {
                                    final String originalDesc = methodInsnNode.desc;
                                    methodInsnNode.desc =
                                            "(" +
                                                    ("L" + "net/minecraft/item/Item" + ";") +
                                                    "Lnet/minecraft/item/ItemStack;F)V";
                                    if (ReTweakResources.DEBUG) {
                                        ReTweakResources.RETWEAK_LOGGER.info(
                                                "Remapped desc of method insn \"{}\" from \"{}\" to \"{}\"",
                                                ASMHelper.toString(methodInsnNode),
                                                originalDesc,
                                                methodInsnNode.desc
                                        );
                                    }
                                    break;
                                }
                                case "L" + "net/minecraft/block/Block" + ";": {
                                    final String originalDesc = methodInsnNode.desc;
                                    methodInsnNode.desc =
                                            "(" +
                                                    ("L" + "net/minecraft/block/Block" + ";") +
                                                    "Lnet/minecraft/item/ItemStack;F)V";
                                    if (ReTweakResources.DEBUG) {
                                        ReTweakResources.RETWEAK_LOGGER.info(
                                                "Remapped desc of method insn \"{}\" from \"{}\" to \"{}\"",
                                                ASMHelper.toString(methodInsnNode),
                                                originalDesc,
                                                methodInsnNode.desc
                                        );
                                    }
                                    break;
                                }
                                case "L" + "net/minecraft/item/ItemStack" + ";": {
                                    final String originalDesc = methodInsnNode.desc;
                                    methodInsnNode.desc =
                                            "(" +
                                                    ("L" + "net/minecraft/item/ItemStack" + ";") +
                                                    "Lnet/minecraft/item/ItemStack;F)V";
                                    if (ReTweakResources.DEBUG) {
                                        ReTweakResources.RETWEAK_LOGGER.info(
                                                "Remapped desc of method insn \"{}\" from \"{}\" to \"{}\"",
                                                ASMHelper.toString(methodInsnNode),
                                                originalDesc,
                                                methodInsnNode.desc
                                        );
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    break;
                case "net/minecraftforge/client/MinecraftForgeClient":
                    if (methodInsnNode.name.equals("preloadTexture") && methodInsnNode.desc.equals("(Ljava/lang/String;)V")) {
                        return true;//Remove method insn call
                    }
                    break;
            }
        }
        return false;
    }

    @Override
    protected boolean intInsn(final String className, final int index, final IntInsnNode intInsnNode) {
        return false;
    }

    @Override
    protected boolean ldcInsn(final String className, final int index, final LdcInsnNode ldcInsnNode) {
        if (ldcInsnNode.getNext() instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNodeNext = (MethodInsnNode)ldcInsnNode.getNext();
            if (methodInsnNodeNext.getOpcode() == Opcodes.INVOKESTATIC) {
                if (methodInsnNodeNext.owner.equals("net/minecraftforge/client/MinecraftForgeClient")) {
                    if (methodInsnNodeNext.name.equals("preloadTexture") && methodInsnNodeNext.desc.equals("(Ljava/lang/String;)V")) {
                        return true;
                    }
                }
            }
        }
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

    private void checkFieldInsn(final String owner, final String name, final String desc, final FieldInsnNode fieldInsnNode) {
        if (fieldInsnNode.owner.equals(owner) && fieldInsnNode.name.equals(name) && fieldInsnNode.desc.equals(desc)) {
            throw new UnsupportedOperationException(
                    Kirai.from(
                            "Mod cannot get or set field \"{class}/{name}{desc}\"!"
                    ).put(
                            "class",
                            fieldInsnNode.owner
                    ).put(
                            "name",
                            fieldInsnNode.name
                    ).put(
                            "desc",
                            fieldInsnNode.desc
                    ).format().toString()
            );
        }
    }

    private Holder[] getHolders(final _Type type, final int opcode, final String owner, final String name, final String desc) {
        if (type == null || (StringHelper.isNullOrEmpty(owner) && (type != _Type.CLASS && type != _Type.ANNOTATION)) || (StringHelper.isNullOrEmpty(desc) && (type != _Type.CLASS && type != _Type.ANNOTATION))) return null;
        List<Holder> holders = new ArrayList<>();
        for(Holder holder : types.get(type).keySet()) {
            switch(type) {
                case ANNOTATION:
                    if ((holder.getOpcode() == -1 && opcode == -1) && (StringHelper.isNullOrEmpty(holder.getOwner()) && StringHelper.isNullOrEmpty(owner)) && (StringHelper.isNullOrEmpty(holder.getName()) && StringHelper.isNullOrEmpty(name)) && (!StringHelper.isNullOrEmpty(holder.getDesc()) && holder.getDesc().equals(desc))) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
                case CLASS:
                    if (holder.getName().equals(name)) {
                        return new Holder[] {
                                holder
                        };
                    }
                    break;
                case INNER_CLASS:
                    if (holder.getOpcode() == opcode && (!StringHelper.isNullOrEmpty(holder.getOwner()) && holder.getOwner().equals(owner)) && (!StringHelper.isNullOrEmpty(holder.getName()) && holder.getName().equals(name)) && (!StringHelper.isNullOrEmpty(holder.getDesc()) && holder.getDesc().equals(desc))) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
                case FIELD:
                case METHOD:
                    if ((opcode == -1 || holder.getOpcode() == opcode) && (name == null || holder.getName().equals(name)) && holder.getDesc().equals(desc)) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
                case FIELD_INSN:
                case METHOD_INSN:
                        if ((holder.getOwner() == null || owner.equals(holder.getOwner())) && ((opcode == -1 || holder.getOpcode() == -1) || holder.getOpcode() == opcode) && ((name == null || holder.getName() == null) || name.equals(holder.getName())) && desc.equals(holder.getDesc())) {
                            holders.add(types.get(type).get(holder));
                        }
                    break;
            }
        }
        return holders.toArray(new Holder[holders.size()]);
    }

    private enum _Type {

        ANNOTATION,

        CLASS,

        INNER_CLASS,

        FIELD,

        METHOD,

        FIELD_INSN,

        METHOD_INSN;

        _Type() {
        }

    }

    private static final class Holder {

        private final _Type type;
        private final int opcode;
        private final String owner, name, desc;

        public Holder(final _Type type, final int opcode, final String owner, final String name, final String desc) {
            this.type = type;
            this.opcode = opcode;
            this.owner = owner;
            this.name = name;
            this.desc = desc;
        }

        public _Type getType() {
            return type;
        }

        public int getOpcode() {
            return opcode;
        }

        public String getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public String getDesc() {
            return desc;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Holder) {
                Holder holder = (Holder)obj;
                if (holder.getType() != getType()) return false;
                return (holder.getOpcode() == getOpcode()) && holder.getOwner().equals(getOwner()) && (getType() == _Type.FIELD_INSN || holder.getName().equals(getName())) && holder.getDesc().equals(getDesc());
            }
            return super.equals(obj);
        }

        public void set(final Object object) {
            if (getType() == _Type.ANNOTATION && object instanceof AnnotationNode) {
                AnnotationNode annotationNode = (AnnotationNode)object;
                if (!StringHelper.isNullOrEmpty(getDesc())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set op code from {} to {}",
                                getType().name(),
                                annotationNode.desc,
                                getDesc()
                        );
                    }
                    annotationNode.desc = getDesc();
                }
            } else if (getType() == _Type.CLASS && object instanceof ClassNode) {
                ClassNode classNode = (ClassNode)object;
                //TODO
            } else if (getType() == _Type.INNER_CLASS && object instanceof InnerClassNode) {
                InnerClassNode innerClassNode = (InnerClassNode)object;
                if (getOpcode() != -1) {//Access
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set access from {} to {}",
                                getType().name(),
                                innerClassNode.access,
                                getOpcode()
                        );
                    }
                    innerClassNode.access = getOpcode();
                }
                if (!StringHelper.isNullOrEmpty(getName())) {//Name
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set name from \"{}\" to \"{}\"",
                                getType().name(),
                                innerClassNode.name,
                                getName()
                        );
                    }
                    innerClassNode.name = getName();
                }
                if (!StringHelper.isNullOrEmpty(getOwner())) {//Outer-name
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set outer-name from \"{}\" to \"{}\"",
                                getType().name(),
                                innerClassNode.outerName,
                                getOwner()
                        );
                    }
                    innerClassNode.outerName = getOwner();
                }
                if (!StringHelper.isNullOrEmpty(getDesc())) {//Inner-name
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set inner-name from \"{}\" to \"{}\"",
                                getType().name(),
                                innerClassNode.innerName,
                                getDesc()
                        );
                    }
                    innerClassNode.innerName = getDesc();
                }
            } else if (getType() == _Type.FIELD && object instanceof FieldNode) {
                FieldNode fieldNode = (FieldNode)object;
                //TODO
            } else if (getType() == _Type.METHOD && object instanceof MethodNode) {
                MethodNode methodNode = (MethodNode)object;
                //TODO
            } else if (getType() == _Type.FIELD_INSN && object instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode)object;
                if (getOpcode() != -1) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set op code from {} to {}",
                                getType().name(),
                                fieldInsnNode.getOpcode(),
                                getOpcode()
                        );
                    }
                    fieldInsnNode.setOpcode(getOpcode());
                }
                if (!StringHelper.isNullOrEmpty(getOwner())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set owner from \"{}\" to \"{}\"",
                                getType().name(),
                                fieldInsnNode.owner,
                                getOwner()
                        );
                    }
                    fieldInsnNode.owner = getOwner();
                }
                if (!StringHelper.isNullOrEmpty(getName())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set name from \"{}\" to \"{}\"",
                                getType().name(),
                                fieldInsnNode.name,
                                getName()
                        );
                    }
                    fieldInsnNode.name = getName();
                }
                if (!StringHelper.isNullOrEmpty(getDesc())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set desc from \"{}\" to \"{}\"",
                                getType().name(),
                                fieldInsnNode.desc,
                                getDesc()
                        );
                    }
                    fieldInsnNode.desc = getDesc();
                }
            } else if (getType() == _Type.METHOD_INSN && object instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode)object;
                if (getOpcode() != -1) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set owner from \"{}\" to \"{}\"",
                                getType().name(),
                                methodInsnNode.owner,
                                getOwner()
                        );
                    }
                    methodInsnNode.setOpcode(getOpcode());
                }
                if (!StringHelper.isNullOrEmpty(getOwner())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set owner from \"{}\" to \"{}\"",
                                getType().name(),
                                methodInsnNode.owner,
                                getOwner()
                        );
                    }
                    methodInsnNode.owner = getOwner();
                }
                if (!StringHelper.isNullOrEmpty(getName())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set name from \"{}\" to \"{}\"",
                                getType().name(),
                                methodInsnNode.name,
                                getName()
                        );
                    }
                    methodInsnNode.name = getName();
                }
                if (!StringHelper.isNullOrEmpty(getDesc())) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set desc from \"{}\" to \"{}\"",
                                getType().name(),
                                methodInsnNode.owner,
                                getDesc()
                        );
                    }
                    methodInsnNode.desc = getDesc();
                }
            } else {
                throw new UnsupportedOperationException(
                        getType().name()
                );
            }
        }

    }

}
