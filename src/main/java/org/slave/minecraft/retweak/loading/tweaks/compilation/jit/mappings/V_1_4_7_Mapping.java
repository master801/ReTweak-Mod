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
import org.slave.lib.helpers.ASMHelper;
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


        //<editor-fold desc="FIELD INSN">
        types.get(_Type.FIELD_INSN).put(
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
        types.get(_Type.FIELD_INSN).put(
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
        //</editor-fold>

        //<editor-fold desc="METHOD INSN">
        types.get(_Type.METHOD_INSN).put(
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
                                "Removed annotation \"{}\" from class {}",
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
    protected boolean method(final String className, final MethodNode methodNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD: {}{}", methodNode.name, methodNode.desc);

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
    protected boolean field(final String className, final FieldNode fieldNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD: {} {}", fieldNode.name, fieldNode.desc);

        return false;
    }

    @Override
    protected boolean fieldInsn(final String className, final int index, final FieldInsnNode fieldInsnNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING FIELD INSN: {}/{} {}", fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc);

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
            if (fieldInsnNode.desc.equals("I") && fieldInsnNode.getPrevious() instanceof FieldInsnNode && ((FieldInsnNode)fieldInsnNode.getPrevious()).desc.equals("L" + fieldInsnNode.owner + ";")) {//Get item id from item
                FieldInsnNode fi = (FieldInsnNode)fieldInsnNode.getPrevious();
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

        return false;
    }

    @Override
    protected boolean methodInsn(final String className, final int index, final MethodInsnNode methodInsnNode) {
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING METHOD INSN: {}/{}{}", methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc);

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

    private Holder[] getHolders(final _Type type, final int opcode, final String owner, final String name, final String desc) {
        if (type == null || (StringHelper.isNullOrEmpty(owner) && type != _Type.CLASS) || (StringHelper.isNullOrEmpty(desc) && type != _Type.CLASS)) return null;
        List<Holder> holders = new ArrayList<>();
        for(Holder holder : types.get(type).keySet()) {
            switch(type) {
                case CLASS:
                    if (holder.getName().equals(name)) {
                        return new Holder[] {
                                holder
                        };
                    }
                    break;
                case FIELD:
                case METHOD:
                    if ((opcode == -1 || holder.getOpcode() == opcode) && (name == null || holder.getName().equals(name)) && holder.getDesc().equals(desc)) holders.add(types.get(type).get(holder));
                    break;
                case FIELD_INSN:
                case METHOD_INSN:
                        if ((holder.getOwner() == null || owner.equals(holder.getOwner())) && ((opcode == -1 || holder.getOpcode() == -1) || holder.getOpcode() == opcode) && ((name == null || holder.getName() == null) || name.equals(holder.getName())) && desc.equals(holder.getDesc())) holders.add(types.get(type).get(holder));
                    break;
            }
        }
        return holders.toArray(new Holder[holders.size()]);
    }

    private enum _Type {

        CLASS,

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
                return holder.getType() == getType() && (holder.getOpcode() == getOpcode()) && holder.getOwner().equals(getOwner()) && (getType() == _Type.FIELD_INSN || holder.getName().equals(getName())) && holder.getDesc().equals(getDesc());
            }
            return super.equals(obj);
        }

        public void set(final Object object) {
            if (getType() == _Type.CLASS && object instanceof ClassNode) {
                ClassNode classNode = (ClassNode)object;
                //TODO
            } else if (getType() == _Type.FIELD && object instanceof FieldNode) {
                FieldNode fieldNode = (FieldNode)object;
                //TODO
            } else if (getType() == _Type.METHOD && object instanceof MethodNode) {
                MethodNode methodNode = (MethodNode)object;
                //TODO
            } else if (getType() == _Type.FIELD_INSN && object instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode)object;
                if (getOpcode() != -1) fieldInsnNode.setOpcode(getOpcode());
                if (!StringHelper.isNullOrEmpty(getOwner())) fieldInsnNode.owner = getOwner();
                if (!StringHelper.isNullOrEmpty(getName())) fieldInsnNode.name = getName();
                if (!StringHelper.isNullOrEmpty(getDesc())) fieldInsnNode.desc = getDesc();
            } else if (getType() == _Type.METHOD_INSN && object instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode)object;
                if (getOpcode() != -1) methodInsnNode.setOpcode(getOpcode());
                if (!StringHelper.isNullOrEmpty(getOwner())) methodInsnNode.owner = getOwner();
                if (!StringHelper.isNullOrEmpty(getName())) methodInsnNode.name = getName();
                if (!StringHelper.isNullOrEmpty(getDesc())) methodInsnNode.desc = getDesc();
            }
        }

    }

}
