package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.loading.ReTweakDeobfuscation;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master on 4/27/2016 at 3:31 PM.
 *
 * @author Master
 */
public abstract class Mapping {

    public static final String ANNOTATION_PRE_INIT_DESC = "L" + "cpw/mods/fml/common/Mod$PreInit" + ";";
    public static final String ANNOTATION_INIT_DESC = "L" + "cpw/mods/fml/common/Mod$Init" + ";";
    public static final String ANNOTATION_POST_INIT_DESC = "L" + "cpw/mods/fml/common/Mod$PostInit" + ";";
    public static final String ANNOTATION_NETWORK_MOD_DESC = "L" + "cpw/mods/fml/common/network/NetworkMod" + ";";

    protected abstract boolean _class(final String className, final ClassNode classNode);

    protected abstract void postClass(final String className, final ClassNode classNode);

    protected abstract boolean field(final String className, final FieldNode fieldNode);

    protected abstract boolean method(final String className, final MethodNode methodNode);

    protected abstract void postMethodNode(final ClassNode classNode, final int index, final MethodNode methodNode);

    protected abstract boolean fieldInsn(final String className, final int index, final InsnList insnList, final FieldInsnNode fieldInsnNode);

    protected abstract boolean methodInsn(final String className, final int index, final InsnList insnList, final MethodInsnNode methodInsnNode);

    protected abstract boolean intInsn(final String className, final int index, final InsnList insnList, final IntInsnNode intInsnNode);

    protected abstract boolean ldcInsn(final String className, final int index, final InsnList insnList, final LdcInsnNode ldcInsnNode);

    protected abstract boolean typeInsn(final String className, final int index, final InsnList insnList, final TypeInsnNode typeInsnNode);

    public abstract boolean ignoreField(final int opcode, final String owner, final String name, final String desc);

    public abstract boolean ignoreMethod(final int opcode, final String owner, final String name, final String desc);

    private final GameVersion gameVersion;
    private final org.slave.tool.retweak.mapping.Mapping mapping;

    Mapping(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
        mapping = ReTweakDeobfuscation.INSTANCE.getSuperMappings(gameVersion);
    }

    @SuppressWarnings("ConstantConditions")
    public final boolean remap(final ClassNode classNode, final InsnList insnList, final Object node, final int index) {
        if (classNode == null || node == null) throw new NullPointerException();
        if (index < 0) throw new IndexOutOfBoundsException();
        if (node instanceof ClassNode || node instanceof MethodNode || node instanceof MethodInsnNode || node instanceof FieldNode || node instanceof FieldInsnNode || node instanceof IntInsnNode || node instanceof LdcInsnNode || node instanceof TypeInsnNode) {
            String className = classNode.name;
            boolean remove = false;
            if (ReTweakResources.DEBUG_MESSAGES) ReTweakResources.RETWEAK_LOGGER.info("MAPPING START");
            if (node instanceof ClassNode) {
                remove = _class(
                        className,
                        (ClassNode)node
                );
            } else if (node instanceof MethodNode) {
                remove = method(
                        className,
                        (MethodNode)node
                );
            } else if (node instanceof MethodInsnNode) {
                if (insnList == null) throw new NullPointerException();
                remove = methodInsn(
                        className,
                        index,
                        insnList,
                        (MethodInsnNode)node
                );
            } else if (node instanceof FieldNode) {
                remove = field(
                        className,
                        (FieldNode)node
                );
            } else if (node instanceof FieldInsnNode) {
                if (insnList == null) throw new NullPointerException();
                remove = fieldInsn(
                        className,
                        index,
                        insnList,
                        (FieldInsnNode)node
                );
            } else if (node instanceof IntInsnNode) {
                if (insnList == null) throw new NullPointerException();
                remove = intInsn(
                        className,
                        index,
                        insnList,
                        (IntInsnNode)node
                );
            } else if (node instanceof LdcInsnNode) {
                if (insnList == null) throw new NullPointerException();
                remove = ldcInsn(
                        className,
                        index,
                        insnList,
                        (LdcInsnNode)node
                );
            } else if (node instanceof TypeInsnNode) {
                if (insnList == null) throw new NullPointerException();
                remove = typeInsn(
                        className,
                        index,
                        insnList,
                        (TypeInsnNode)node
                );
            }

            if (!remove) {
                if (node instanceof ClassNode) {
                    postClass(
                            className,
                            (ClassNode)node
                    );
                } else if (node instanceof MethodNode) {
                    postMethodNode(
                            classNode,
                            index,
                            (MethodNode)node
                    );
                }
            }

            if (ReTweakResources.DEBUG_MESSAGES) ReTweakResources.RETWEAK_LOGGER.info("MAPPING END\n");
            return remove;
        }
        return false;
    }

    public final GameVersion getGameVersion() {
        return gameVersion;
    }

    public final org.slave.tool.retweak.mapping.Mapping getMapping() {
        return mapping;
    }

    enum _Type {

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

    static final class Holder {

        private final _Type type;
        private final int opcode;
        private final String owner, name, desc;

        private AbstractInsnNode[] abstractInsnNodes;

        public Holder(final _Type type, final AbstractInsnNode[] abstractInsnNodes) {
            this(
                    type,
                    -1,
                    null,
                    null,
                    null
            );
            this.abstractInsnNodes = abstractInsnNodes;
        }

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

        public AbstractInsnNode[] getAbstractInsnNodes() {
            return abstractInsnNodes;
        }

        public void set(final Object object) {
            set(
                    object,
                    null
            );
        }

        public void set(final Object object, final InsnList insnList) {
            if (object == null) throw new NullPointerException();

            if (getType() == _Type.ANNOTATION && object instanceof AnnotationNode) {
                AnnotationNode annotationNode = (AnnotationNode)object;
                if (!StringHelper.isNullOrEmpty(getDesc())) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set desc from {} to {}",
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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

                if (getOpcode() != -1) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set access from {} to {}",
                                getType().name(),
                                methodNode.access,
                                getOpcode()
                        );
                    }
                    methodNode.access = getOpcode();
                }
                if (!StringHelper.isNullOrEmpty(getName())) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set name from \"{}\" to \"{}\"",
                                getType().name(),
                                methodNode.name,
                                getName()
                        );
                    }
                    methodNode.name = getName();
                }
                if (!StringHelper.isNullOrEmpty(getDesc())) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Holder - {} -- Set desc from \"{}\" to \"{}\"",
                                getType().name(),
                                methodNode.desc,
                                getDesc()
                        );
                    }
                    methodNode.desc = getDesc();
                }
            } else if (getType() == _Type.FIELD_INSN && object instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode)object;

                if (insnList != null && !ArrayHelper.isNullOrEmpty(getAbstractInsnNodes())) {
                    final InsnList inject = new InsnList();
                    for(AbstractInsnNode iterating : getAbstractInsnNodes()) inject.add(iterating);
                    insnList.insert(
                            fieldInsnNode,
                            inject
                    );
                    insnList.remove(fieldInsnNode);
                    return;
                }

                if (getOpcode() != -1) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                final MethodInsnNode methodInsnNode = (MethodInsnNode)object;

                if (insnList != null && !ArrayHelper.isNullOrEmpty(getAbstractInsnNodes())) {
                    final InsnList inject = new InsnList();
                    for(AbstractInsnNode iterating : getAbstractInsnNodes()) inject.add(iterating);
                    insnList.insert(
                            methodInsnNode,
                            inject
                    );
                    insnList.remove(methodInsnNode);
                    return;
                }

                if (getOpcode() != -1) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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
                    if (ReTweakResources.DEBUG_MESSAGES) {
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

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Holder) {
                Holder holder = (Holder)obj;
                if (holder.getType() != getType()) return false;
                return (holder.getOpcode() == getOpcode()) && holder.getOwner().equals(getOwner()) && (getType() == _Type.FIELD_INSN || holder.getName().equals(getName())) && holder.getDesc().equals(getDesc());
            }
            return super.equals(obj);
        }

    }

}
