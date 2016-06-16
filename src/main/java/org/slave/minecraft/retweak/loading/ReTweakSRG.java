package org.slave.minecraft.retweak.loading;

import com.github.pwittchen.kirai.library.Kirai;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.tool.retweak.mapping.ClassMapping;
import org.slave.tool.retweak.mapping.FieldMapping;
import org.slave.tool.retweak.mapping.Mapping;
import org.slave.tool.retweak.mapping.MethodMapping;

/**
 * Created by Master on 4/27/2016 at 7:22 AM.
 *
 * @author Master
 */
public final class ReTweakSRG {

    private final GameVersion gameVersion;
    private final Mapping mapping;

    public ReTweakSRG(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
        mapping = ReTweakDeobfuscation.INSTANCE.getSuperMappings(gameVersion);
    }

    public void srg(final ClassNode classNode) {
        if (mapping == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Found no mapping for version {}",
                    gameVersion.getVersion()
            );
            return;
        }
        methods(classNode);//0 - Methods
        fields(classNode);//1 - Fields
        superName(classNode);//2 - Super-name
        interfaces(classNode);//3 - Interfaces
        name(classNode);//4 - Name
    }

    private void name(ClassNode classNode) {
        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "[SRG] NAME: {}",
                    classNode.name
            );
        }
    }

    private void superName(ClassNode classNode) {
        final String originalSuperName = classNode.superName;

        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "[SRG] SUPER-NAME: {}",
                    classNode.superName
            );
        }
        ClassMapping classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                classNode.name
        );
        if (classMapping != null && (classMapping.getSuperName() != null || classMapping.getSuperClass() != null)) {
            boolean set = false;

            if (classMapping.getSuperClass() != null) {
                classNode.superName = classMapping.getSuperClass().getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString();
                set = true;
            }
            if (!set && classMapping.getSuperName() != null) {
                classNode.superName = classMapping.getSuperName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString();
                set = true;
            }
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped super-class from \"{}\" to \"{}\"",
                        classMapping.getSuperName().getName(org.slave.tool.retweak.mapping.Type.OBFUSCATED).getString(),
                        classMapping.getSuperName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString()
                );
            }
            return;
        }
        classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                classNode.superName
        );
        if (classMapping != null) {
            classNode.superName = classMapping.getName().getName(
                    org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
            ).getString();
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped super-class from \"{}\" to \"{}\"",
                        originalSuperName,
                        classMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString()
                );
            }
        }
    }

    private void interfaces(ClassNode classNode) {
        if (classNode.interfaces == null) return;
        for(int i = 0; i < classNode.interfaces.size(); ++i) {
            final String _interface = classNode.interfaces.get(i);
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "[SRG] INTERFACE: {}",
                        _interface
                );
            }

            ClassMapping classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    _interface
            );
            if (classMapping != null) {
                classNode.interfaces.set(
                        i,
                        classMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString()
                );
                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped interface from \"{}\" to \"{}\", in class \"{}\"",
                            classMapping.getName().getName(org.slave.tool.retweak.mapping.Type.OBFUSCATED).getString(),
                            classMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString(),
                            classNode.name
                    );
                }
            }
        }
    }

    private void fields(ClassNode classNode) {
        if (classNode.fields == null) return;
        for(FieldNode fieldNode : classNode.fields) {
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "[SRG] FIELD: \"{}\" \"{}\"",
                        fieldNode.name,
                        fieldNode.desc
                );
            }
            field(
                    classNode,
                    fieldNode
            );

        }
    }

    private void field(ClassNode classNode, FieldNode fieldNode) {
        final String originalName = fieldNode.name, originalDesc = fieldNode.desc;
        ClassMapping classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                classNode.name
        );
        if (classMapping != null) {
            FieldMapping fieldMapping = classMapping.findField(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    fieldNode.name
            );
            if (fieldMapping != null) {
                fieldNode.name = fieldMapping.getName().getName(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString();
                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped field name from \"{}\" to \"{}\"",
                            originalName,
                            fieldNode.name
                    );
                }
            }
        }

        Type type = Type.getType(fieldNode.desc);
        Type newType;

        if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
            String array = "";
            String descriptor = type.getDescriptor();
            if (type.getSort() == Type.ARRAY) {
                array = descriptor.substring(
                        descriptor.indexOf('['),
                        descriptor.lastIndexOf('[') + 1
                );
                descriptor = descriptor.substring(
                        descriptor.lastIndexOf('[') + 1
                );
            }
            ClassMapping _classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    descriptor.substring(
                            1,
                            descriptor.length() - 1
                    )
            );
            if (_classMapping != null) {
                newType = Type.getType(
                        array + "L" + _classMapping.getName().getName(
                                org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                        ) + ";"
                );
                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped field desc from \"{}\" to \"{}\"",
                            originalDesc,
                            newType.getDescriptor()
                    );
                }
            } else {
                newType = type;
            }
        } else {
            newType = type;
        }
        fieldNode.desc = newType.getDescriptor();
    }

    private void methods(ClassNode classNode) {
        if (classNode.methods == null) return;
        for(MethodNode methodNode : classNode.methods) {
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "[SRG] METHOD: \"{}{}\"",
                        methodNode.name,
                        methodNode.desc
                );
            }
            method(
                    classNode,
                    methodNode
            );
        }
    }

    private void method(ClassNode classNode, MethodNode methodNode) {
        ClassMapping classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                classNode.name
        );
        if (classMapping == null) {
            classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    classNode.superName
            );
        }
        if (classMapping != null) {
            MethodMapping methodMapping = classMapping.findMethod(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    methodNode.name,
                    methodNode.desc,
                    true
            );
            if (methodMapping != null) {
                methodNode.name = methodMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString();
                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped method name from \"{}\" to \"{}\" in class \"{}\"",
                            methodMapping.getName().getName(org.slave.tool.retweak.mapping.Type.OBFUSCATED).getString(),
                            methodMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString(),
                            classNode.name
                    );
                }
            }
        }

        methodNode.desc = getRemappedMethodDesc(methodNode.desc);
        for(int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
            if (abstractInsnNode instanceof FieldInsnNode) {
                fieldInsnNode(
                        classNode.name,
                        i,
                        methodNode.name + methodNode.desc,
                        (FieldInsnNode)abstractInsnNode
                );
            }
            if (abstractInsnNode instanceof MethodInsnNode) {
                methodInsnNode(
                        classNode.name,
                        i,
                        methodNode.name + methodNode.desc,
                        (MethodInsnNode)abstractInsnNode
                );
            }
            if (abstractInsnNode instanceof TypeInsnNode) {
                typeInsnNode(
                        classNode.name,
                        i,
                        methodNode.name + methodNode.desc,
                        (TypeInsnNode)abstractInsnNode
                );
            }
            if (abstractInsnNode instanceof FrameNode) {
                frameNode(
                        classNode.name,
                        i,
                        methodNode.name + methodNode.desc,
                        (FrameNode)abstractInsnNode
                );
            }
        }
        for(int i = 0; i < methodNode.localVariables.size(); ++i) {
            localVariable(
                    classNode.name,
                    i,
                    methodNode.name + methodNode.desc,
                    methodNode.localVariables.get(i)
            );
        }
    }

    private void fieldInsnNode(final String className, final int index, final String method, final FieldInsnNode fieldInsnNode) {
        final String originalOwner = fieldInsnNode.owner, originalName = fieldInsnNode.name, originalDesc = fieldInsnNode.desc;
        ClassMapping classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                fieldInsnNode.owner
        );
        if (classMapping != null) {
            FieldMapping fieldMapping = classMapping.findField(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    fieldInsnNode.name
            );
            if (fieldMapping != null) {
                fieldInsnNode.owner = classMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString();
                fieldInsnNode.name = fieldMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString();

                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped field insn (at index {}) from \"{}\" to \"{}\", in method \"{}\" from class \"{}\"",
                            index,
                            Kirai.from(
                                    "{owner}/{name}"
                            ).put(
                                    "owner",
                                    originalOwner
                            ).put(
                                    "name",
                                    originalName
                            ).format().toString(),
                            Kirai.from(
                                    "{owner}/{name}"
                            ).put(
                                    "owner",
                                    fieldInsnNode.owner
                            ).put(
                                    "name",
                                    fieldInsnNode.name
                            ).format().toString(),
                            method,
                            className
                    );
                }
            }
        }

        final Type type = Type.getType(fieldInsnNode.desc);
        Type newType;

        if (type.getSort() == Type.OBJECT || type.getSort() == Type.ARRAY) {
            String descriptor = type.getDescriptor().replace(
                    '.',
                    '/'
            );
            String array = "";
            if (type.getSort() == Type.ARRAY) {
                array = descriptor.substring(
                        descriptor.indexOf('['),
                        descriptor.lastIndexOf('[') + 1
                );
                descriptor = descriptor.substring(
                        descriptor.lastIndexOf('[') + 1
                );
            }
            ClassMapping _classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    descriptor.substring(
                            1,
                            descriptor.length() - 1
                    )
            );
            if (_classMapping != null) {
                descriptor = 'L' + _classMapping.getName().getName(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString() + ';';
            }
            newType = Type.getType(
                    array + descriptor
            );
        } else {
            newType = type;
        }

        fieldInsnNode.desc = newType.getDescriptor();


        if (!fieldInsnNode.owner.equals(originalOwner) || !fieldInsnNode.name.equals(originalName) || !fieldInsnNode.desc.equals(originalDesc)) {
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped field insn (at index {}) from \"{}\" to \"{}\", in method \"{}\" from class \"{}\"",
                        index,
                        Kirai.from(
                                "{owner}/{name}{desc}"
                        ).put(
                                "owner",
                                originalOwner
                        ).put(
                                "name",
                                originalName
                        ).put(
                                "desc",
                                originalDesc
                        ).format().toString(),
                        Kirai.from(
                                "{owner}/{name}{desc}"
                        ).put(
                                "owner",
                                fieldInsnNode.owner
                        ).put(
                                "name",
                                fieldInsnNode.name
                        ).put(
                                "desc",
                                fieldInsnNode.desc
                        ).format().toString(),
                        method,
                        className
                );
            }
        }
    }

    private void methodInsnNode(final String className, final int index, final String method, final MethodInsnNode methodInsnNode) {
        final String originalOwner = methodInsnNode.owner, originalName = methodInsnNode.name, originalDesc = methodInsnNode.desc;

        ClassMapping classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                methodInsnNode.owner
        );
        if (classMapping != null) {
            MethodMapping methodMapping = classMapping.findMethod(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    methodInsnNode.name,
                    methodInsnNode.desc
            );
            if (methodMapping != null) {
                methodInsnNode.owner = methodMapping.getParentClassMapping().getName().getName(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString();
                methodInsnNode.name = methodMapping.getName().getName(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString();
                methodInsnNode.desc = methodMapping.getDesc().getDesc(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString();

                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped method insn (at index {}) from \"{}\" to \"{}\", in method \"{}\" from class \"{}\"",
                            index,
                            Kirai.from(
                                    "{owner}/{name}{desc}"
                            ).put(
                                    "owner",
                                    originalOwner
                            ).put(
                                    "name",
                                    originalName
                            ).put(
                                    "desc",
                                    originalDesc
                            ).format().toString(),
                            Kirai.from(
                                    "{owner}/{name}{desc}"
                            ).put(
                                    "owner",
                                    methodInsnNode.owner
                            ).put(
                                    "name",
                                    methodInsnNode.name
                            ).put(
                                    "desc",
                                    methodInsnNode.desc
                            ).format().toString(),
                            method,
                            className
                    );
                }
                if (!methodInsnNode.name.equals("<init>") && !methodInsnNode.name.equals("<clinit>")) return;//Skip constructors
            }
            methodInsnNode.owner = classMapping.getName().getName(org.slave.tool.retweak.mapping.Type.DEOBFUSCATED).getString();
        }

        Type returnType = Type.getReturnType(methodInsnNode.desc);
        Type newReturnType;

        if (returnType.getSort() == Type.OBJECT || returnType.getSort() == Type.ARRAY) {
            String array = "";
            String returnTypeClassName = returnType.getDescriptor().replace(
                    '.',
                    '/'
            );
            if (returnType.getSort() == Type.ARRAY) {
                array = returnTypeClassName.substring(
                        returnTypeClassName.indexOf('['),
                        returnTypeClassName.lastIndexOf('[') + 1
                );
                returnTypeClassName = returnTypeClassName.substring(
                        returnTypeClassName.lastIndexOf('[') + 1
                );
            }

            ClassMapping _classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    returnTypeClassName.substring(
                            1,
                            returnTypeClassName.length() - 1
                    )
            );
            if (_classMapping != null) {
                returnTypeClassName = 'L' + _classMapping.getName().getName(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString() + ';';
            }

            newReturnType = Type.getType(
                    array + returnTypeClassName
            );
        } else {
            newReturnType = returnType;
        }

        Type[] argumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
        Type[] newArgumentTypes = new Type[argumentTypes.length];

        for(int i = 0; i < argumentTypes.length; ++i) {
            final Type argumentType = argumentTypes[i];

            if (argumentType.getSort() == Type.OBJECT || argumentType.getSort() == Type.ARRAY) {
                String array = "";

                String argumentTypeClassName = argumentType.getDescriptor().replace(
                        '.',
                        '/'
                );
                if (argumentType.getSort() == Type.ARRAY) {
                    array = argumentTypeClassName.substring(
                            argumentTypeClassName.indexOf('['),
                            argumentTypeClassName.lastIndexOf('[') + 1
                    );
                    argumentTypeClassName = argumentTypeClassName.substring(
                            argumentTypeClassName.lastIndexOf('[') + 1
                    );
                }
                ClassMapping _classMapping = mapping.getClass(
                        org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                        argumentTypeClassName.substring(
                                1,
                                argumentTypeClassName.length() - 1
                        )
                );
                if (_classMapping != null) {
                    argumentTypeClassName = 'L' + _classMapping.getName().getName(
                            org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                    ).getString() + ';';
                }

                newArgumentTypes[i] = Type.getType(
                        array + argumentTypeClassName
                );
            } else {
                newArgumentTypes[i] = argumentType;
            }
        }

        methodInsnNode.desc = Type.getMethodDescriptor(
                newReturnType,
                newArgumentTypes
        );

        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "Remapped method insn (at index {}) from \"{}\" to \"{}\", in method \"{}\" from class \"{}\"",
                    index,
                    Kirai.from(
                            "{owner}/{name}{desc}"
                    ).put(
                            "owner",
                            originalOwner
                    ).put(
                            "name",
                            originalName
                    ).put(
                            "desc",
                            originalDesc
                    ).format().toString(),
                    Kirai.from(
                            "{owner}/{name}{desc}"
                    ).put(
                            "owner",
                            methodInsnNode.owner
                    ).put(
                            "name",
                            methodInsnNode.name
                    ).put(
                            "desc",
                            methodInsnNode.desc
                    ).format().toString(),
                    method,
                    className
            );
        }
    }

    private void frameNode(final String className, final int index, final String method, final FrameNode frameNode) {
        if (frameNode.local != null) {
            for(int i = 0; i < frameNode.local.size(); ++i) {
                final Object local = frameNode.local.get(i);
                if (local instanceof String) {
                    ClassMapping classMapping = mapping.getClass(
                            org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                            (String)local
                    );
                    if (classMapping != null) {
                        frameNode.local.set(
                                i,
                                classMapping.getName().getName(
                                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                                ).getString()
                        );
                        if (ReTweakResources.DEBUG_MESSAGES) {
                            ReTweakResources.RETWEAK_LOGGER.info(
                                    "Remapped frame \"{}\", from \"{}\" to \"{}\"",
                                    Kirai.from(
                                            "Class: \"{class}\", Method: \"{method}\": Index: {index}"
                                    ).put(
                                            "class",
                                            className
                                    ).put(
                                            "method",
                                            method
                                    ).put(
                                            "index",
                                            index
                                    ).format().toString(),
                                    local,
                                    frameNode.local.get(i)
                            );
                        }
                    }
                }
            }
        }
    }

    private void typeInsnNode(final String className, final int index, final String method, final TypeInsnNode typeInsnNode) {
        if (StringHelper.isNullOrEmpty(typeInsnNode.desc)) return;
        ClassMapping classMapping = mapping.getClass(
                org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                typeInsnNode.desc
        );
        if (classMapping != null) {
            typeInsnNode.desc = classMapping.getName().getName(
                    org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
            ).getString();
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped type insn \"{}\", from \"{}\" to \"{}\"",
                        Kirai.from(
                                "Class: \"{class}\", Method: \"{method}\": Index: {index}"
                        ).put(
                                "class",
                                className
                        ).put(
                                "method",
                                method
                        ).put(
                                "index",
                                index
                        ).format().toString(),
                        classMapping.getName().getName(
                                org.slave.tool.retweak.mapping.Type.OBFUSCATED
                        ).getString(),
                        classMapping.getName().getName(
                                org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                        ).getString()
                );
            }
        }
    }

    private void localVariable(final String className, final int index, final String method, final LocalVariableNode localVariableNode) {
        Type descType = Type.getType(localVariableNode.desc);
        Type newDescType;

        String descriptor = descType.getDescriptor().replace(
                '.',
                '/'
        );

        if (descType.getSort() == Type.OBJECT || descType.getSort() == Type.ARRAY) {
            String array = "";
            if (descType.getSort() == Type.ARRAY) {
                array = descriptor.substring(
                        descriptor.indexOf('['),
                        descriptor.lastIndexOf('[') + 1
                );
                descriptor = descriptor.substring(
                        descriptor.lastIndexOf('[') + 1
                );
            }
            ClassMapping classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    descriptor.substring(
                            1,
                            descriptor.length() - 1
                    )
            );
            if (classMapping != null) {
                newDescType = Type.getType(
                        array + "L" + classMapping.getName().getName(
                                org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                        ).getString() + ";"
                );
                if (ReTweakResources.DEBUG_MESSAGES) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped local variable \"{}\", desc from \"{}\" to \"{}\"",
                            Kirai.from(
                                    "Class: \"{class}\", Method: \"{method}\": Index: {index}"
                            ).put(
                                    "class",
                                    className
                            ).put(
                                    "method",
                                    method
                            ).put(
                                    "index",
                                    index
                            ).format().toString(),
                            descType.getDescriptor(),
                            newDescType.getDescriptor()
                    );
                }
            } else {
                newDescType = descType;
            }
        } else {
            newDescType = descType;
        }
        localVariableNode.desc = newDescType.getDescriptor();
    }

    private String getRemappedMethodDesc(String desc) {
        final Type[] argumentTypes = Type.getArgumentTypes(desc);
        final Type returnType = Type.getReturnType(desc);

        Type[] newArgumentTypes = new Type[argumentTypes.length];
        Type newReturnType;

        for(int i = 0; i < argumentTypes.length; ++i) {
            Type argumentType = argumentTypes[i];
            if (argumentType.getSort() == Type.OBJECT || argumentType.getSort() == Type.ARRAY) {
                String array = "";
                String classDesc = argumentType.getDescriptor().replace(
                        '.',
                        '/'
                );
                if (argumentType.getSort() == Type.ARRAY) {
                    array = classDesc.substring(
                            classDesc.indexOf('['),
                            classDesc.lastIndexOf('[') + 1
                    );
                    classDesc = classDesc.substring(
                            classDesc.lastIndexOf('[') + 1
                    );
                }
                ClassMapping classMapping = mapping.getClass(
                        org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                        classDesc.substring(
                                1,
                                classDesc.length() - 1
                        )
                );
                if (classMapping != null) {
                    classDesc = 'L' + classMapping.getName().getName(
                            org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                    ).getString() + ';';
                }
                newArgumentTypes[i] = Type.getType(
                        array + classDesc
                );
            } else {
                newArgumentTypes[i] = argumentType;
            }
        }

        String classDesc = returnType.getDescriptor().replace(
                '.',
                '/'
        );
        if (returnType.getSort() == Type.OBJECT || returnType.getSort() == Type.ARRAY) {
            String array = "";
            if (returnType.getSort() == Type.ARRAY) {
                array = classDesc.substring(
                        classDesc.indexOf('['),
                        classDesc.lastIndexOf('[') + 1
                );
                classDesc = classDesc.substring(
                        classDesc.lastIndexOf('[') + 1
                );
            }

            ClassMapping classMapping = mapping.getClass(
                    org.slave.tool.retweak.mapping.Type.OBFUSCATED,
                    classDesc.substring(
                            1,
                            classDesc.length() - 1
                    )
            );
            if (classMapping != null) {
                classDesc = 'L' + classMapping.getName().getName(
                        org.slave.tool.retweak.mapping.Type.DEOBFUSCATED
                ).getString() + ';';
            }

            newReturnType = Type.getType(array + classDesc);
        } else {
            newReturnType = returnType;
        }

        return Type.getMethodDescriptor(
                newReturnType,
                newArgumentTypes
        );
    }

}
