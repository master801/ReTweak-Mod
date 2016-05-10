package org.slave.minecraft.retweak.loading.tweaks;

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
import org.slave.minecraft.retweak.loading.ReTweakDeobfuscation;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.tool.remapper.SRG;

/**
 * Created by Master on 4/27/2016 at 7:22 AM.
 *
 * @author Master
 */
public final class SRGTweak implements Tweak {

    private final GameVersion gameVersion;
    private final SRG srg;

    public SRGTweak(GameVersion gameVersion) {
        this.gameVersion = gameVersion;
        srg = ReTweakDeobfuscation.INSTANCE.getSRG(gameVersion);
    }

    @Override
    public String getName() {
        return "SRG";
    }

    @Override
    public void tweak(final ClassNode classNode) throws TweakException {
        if (srg == null) return;
        methods(classNode);//0 - Methods
        fields(classNode);//1 - Fields
        superName(classNode);//2 - Super-name
        interfaces(classNode);//3 - Interfaces
        name(classNode);//4 - Name
    }

    @Override
    public int getSortIndex() {
        return 0;
    }

    private void name(ClassNode classNode) throws TweakException {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "TWEAK NAME: {}",
                    classNode.name
            );
        }

        String[] nameEntry = srg.getClassEntry(classNode.name);
        if (nameEntry != null) {
            if (!ReTweakResources.DEBUG) {//Throw exception unless in DEBUG mode
                throw new StopTweakException(Kirai.from(
                        "Found Minecraft related class file? ReTweak-Mod cannot load base-classes from Minecraft!"
                ).format().toString());
            }
            classNode.name = nameEntry[1];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped class name from \"{}\" to \"{}\".",
                        nameEntry[0],
                        nameEntry[1]
                );
            }
        }
    }

    private void superName(ClassNode classNode) throws TweakException {
        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "TWEAK SUPER-NAME: {}",
                    classNode.superName
            );
        }

        String[] superNameEntry = srg.getClassEntry(classNode.superName);
        if (superNameEntry != null) {
            classNode.superName = superNameEntry[1];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped super-class from \"{}\" to \"{}\"",
                        superNameEntry[0],
                        superNameEntry[1]
                );
            }
        }
    }

    private void interfaces(ClassNode classNode) throws TweakException {
        if (classNode.interfaces == null) return;
        for(int i = 0; i < classNode.interfaces.size(); ++i) {
            final String _interface = classNode.interfaces.get(i);
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "TWEAK INTERFACE: {}",
                        _interface
                );
            }
            String[] entry = srg.getClassEntry(_interface);
            if (entry != null) {
                classNode.interfaces.set(
                        i,
                        entry[1]
                );
                if (ReTweakResources.DEBUG) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped interface from \"{}\" to \"{}\"",
                            entry[0],
                            entry[1]
                    );
                }
            }
        }
    }

    private void fields(ClassNode classNode) throws TweakException {
        if (classNode.fields == null) return;
        for(FieldNode fieldNode : classNode.fields) {
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "TWEAK FIELD: \"{}\" \"{}\"",
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

    private void field(ClassNode classNode, FieldNode fieldNode) throws TweakException {
        final String originalName = fieldNode.name, originalDesc = fieldNode.desc;
        String[] nameEntry = srg.getFieldEntry(
                classNode.name,
                fieldNode.name
        );
        if (nameEntry == null) {
            //Super-class may have field?
            nameEntry = srg.getFieldEntry(
                    classNode.superName,
                    fieldNode.name
            );
        }
        if (nameEntry != null) {
            fieldNode.name = nameEntry[3];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped field name from \"{}\" to \"{}\"",
                        originalName,
                        fieldNode.name
                );
            }
        }

        Type type = Type.getType(fieldNode.desc);
        if (type.getSort() == Type.ARRAY) {
            throw new IllegalStateException("Arrays are not yet supported!");
        }
        String[] entry = srg.getClassEntry(Type.getType(fieldNode.desc).getClassName().replace(
                '.',
                '/'
        ));
        Type newType;
        if (entry != null) {
            newType = Type.getType("L" + entry[1] + ";");
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped field desc from \"{}\" to \"{}\"",
                        originalDesc,
                        newType.getDescriptor()
                );
            }
        } else {
            newType = type;
        }
        fieldNode.desc = newType.getDescriptor();
    }

    private void methods(ClassNode classNode) throws TweakException {
        if (classNode.methods == null) return;
        for(MethodNode methodNode : classNode.methods) {
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "TWEAK METHOD: \"{}{}\"",
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

    private void method(ClassNode classNode, MethodNode methodNode) throws TweakException {
        String[] entry = srg.getMethodEntry(
                classNode.name,
                methodNode.name,
                methodNode.desc
        );
        if (entry == null) {
            //Super-class may have method?
            entry = srg.getMethodEntry(
                    classNode.superName,
                    methodNode.name,
                    methodNode.desc
            );
        }
        if (entry != null) {
            methodNode.name = entry[4];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped method name from \"{}\" to \"{}\"",
                        entry[1],
                        entry[4]
                );
            }
        }

        methodNode.desc = getRemappedMethodDesc(methodNode.desc);
        for(int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
            if (abstractInsnNode instanceof MethodInsnNode) {
                methodInsnNode(
                        classNode.name,
                        i,
                        methodNode.name + methodNode.desc,
                        (MethodInsnNode)abstractInsnNode
                );
            }
            if (abstractInsnNode instanceof FieldInsnNode) {
                fieldInsnNode(
                        classNode.name,
                        i,
                        methodNode.name + methodNode.desc,
                        (FieldInsnNode)abstractInsnNode
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

    private void methodInsnNode(final String className, final int index, final String method, final MethodInsnNode methodInsnNode) throws TweakException {
        final String originalOwner = methodInsnNode.owner, originalName = methodInsnNode.name, originalDesc = methodInsnNode.desc;

        String[] entry = srg.getMethodEntry(
                methodInsnNode.owner,
                methodInsnNode.name,
                methodInsnNode.desc
        );
        if (entry != null) {
            methodInsnNode.owner = entry[3];
            methodInsnNode.name = entry[4];
            methodInsnNode.desc = entry[5];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Remapped method insn node \"{}\", from \"{} {} {}\" to \"{} {} {}\".",
                        Kirai.from(
                                "Class: \"{class}\", Method: \"{method}\", Index: {index}"
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
                        originalOwner,
                        originalName,
                        originalDesc,
                        methodInsnNode.owner,
                        methodInsnNode.name,
                        methodInsnNode.desc
                );
            }
            return;
        }

        Type ownerType = Type.getType(methodInsnNode.owner);
        Type newOwnerType;

        String[] ownerEntry = srg.getClassEntry(ownerType.getInternalName().replace(
                '.',
                '/'
        ));
        if (ownerEntry != null) {
            newOwnerType = Type.getType(ownerEntry[1].replace(
                    '.',
                    '/'
            ));
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Remapped owner in method insn node \"{}\", from \"{}\" to \"{}\".",
                        Kirai.from(
                                "Class: \"{class}\", Method: \"{method}\", Index: {index}"
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
                        ownerEntry[0],
                        ownerEntry[1]
                );
            }
        } else {
            newOwnerType = ownerType;
        }

        Type[] argumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
        Type[] newArgumentTypes = new Type[argumentTypes.length];

        Type returnType = Type.getReturnType(methodInsnNode.desc);
        Type newReturnType;

        for(int a = 0; a < argumentTypes.length; ++a) {
            Type argumentType = argumentTypes[a];
            String cs = argumentType.getClassName().replace(
                    '.',
                    '/'
            );
            if (argumentType.getSort() == Type.ARRAY) {
                cs = cs.substring(
                        0,
                        cs.lastIndexOf("[]")
                );
            }
            entry = srg.getClassEntry(cs);
            if (entry != null) {
                newArgumentTypes[a] = Type.getType((argumentType.getSort() == Type.ARRAY ? "[" : "") + "L" + entry[1] + ";");
            } else {
                newArgumentTypes[a] = argumentType;
            }
        }

        String cs = returnType.getClassName().replace(
                '.',
                '/'
        );
        if (returnType.getSort() == Type.ARRAY) {
            cs = cs.substring(
                    0,
                    cs.lastIndexOf("[]")
            );
        }
        String[] returnEntry = srg.getClassEntry(cs);
        if (returnEntry != null) {
            newReturnType = Type.getType("L" + returnEntry[1] + ";" + ((returnType.getSort() == Type.ARRAY ? "[]" : "")));
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped return type in method insn node \"{}\", from \"{}\" to \"{}\"",
                        Kirai.from(
                                "Class: \"{class}\", Method: \"{method}\", Index: {index}"
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
                        returnEntry[0],
                        returnEntry[1]
                );
            }
        } else {
            newReturnType = returnType;
        }

        methodInsnNode.owner = newOwnerType.getInternalName();
        methodInsnNode.desc = Type.getMethodDescriptor(
                newReturnType,
                newArgumentTypes
        );
    }

    private void fieldInsnNode(final String className, final int index, final String method, final FieldInsnNode fieldInsnNode) throws TweakException {
        String[] entry = srg.getFieldEntry(
                fieldInsnNode.owner,
                fieldInsnNode.name
        );
        if (entry != null) {
            fieldInsnNode.owner = entry[2];
            fieldInsnNode.name = entry[3];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped field insn \"{}\", from \"{}/{}\" to \"{}/{}\"",
                        Kirai.from(
                                "Class: \"{class}\", Method: \"{method}\", Index: {index}"
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
                        entry[0],
                        entry[1],
                        entry[2],
                        entry[3]
                );
            }
        }

        if (entry == null) {
            Type ownerType = Type.getType(fieldInsnNode.owner);
            Type newOwnerType;

            String[] ownerEntry = srg.getClassEntry(fieldInsnNode.owner);
            if (ownerEntry != null) {
                newOwnerType = Type.getType(ownerEntry[1]);
                if (ReTweakResources.DEBUG) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Remapped field insn \"{}\", owner from \"{}\" to \"{}\"",
                            Kirai.from(
                                    "Class: \"{class}\", Method: \"{method}\", Index: {index}"
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
                            ownerEntry[0],
                            ownerEntry[1]
                    );
                }
            } else {
                newOwnerType = ownerType;
            }
            fieldInsnNode.owner = newOwnerType.getInternalName();
        }

        Type descType = Type.getType(fieldInsnNode.desc);
        Type newDescType;

        if (descType.getSort() == Type.ARRAY) throw new IllegalStateException("Arrays are not yet supported!");

        String[] descEntry = srg.getClassEntry(descType.getClassName().replace(
                '.',
                '/'
        ));

        if (descEntry != null) {
            newDescType = Type.getType("L" + descEntry[1] + ";");
        } else {
            newDescType = descType;
        }

        fieldInsnNode.desc = newDescType.getDescriptor();
    }

    private void frameNode(final String className, final int index, final String method, final FrameNode frameNode) throws TweakException {
        if (frameNode.local != null) {
            for(int i = 0; i < frameNode.local.size(); ++i) {
                final Object local = frameNode.local.get(i);
                if (local instanceof String) {
                    String[] entry = srg.getClassEntry(
                            (String)local
                    );

                    if (entry != null) {
                        frameNode.local.set(
                                i,
                                entry[1]
                        );
                        if (ReTweakResources.DEBUG) {
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
                                    entry[0],
                                    entry[1]
                            );
                        }
                    }
                }
            }
        }
    }

    private void typeInsnNode(final String className, final int index, final String method, final TypeInsnNode typeInsnNode) throws TweakException {
        String[] entry = srg.getClassEntry(typeInsnNode.desc);
        if (entry != null) {
            typeInsnNode.desc = entry[1];
            if (ReTweakResources.DEBUG) {
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
                        entry[0],
                        entry[1]
                );
            }
        }
    }

    private void localVariable(final String className, final int index, final String method, final LocalVariableNode localVariableNode) throws TweakException {
        Type descType = Type.getType(localVariableNode.desc);
        Type newDescType;

        String cs = descType.getClassName().replace(
                '.',
                '/'
        );
        if (descType.getSort() == Type.ARRAY) {
            cs = cs.substring(
                    0,
                    cs.lastIndexOf("[]")
            );
        }
        String[] descEntry = srg.getClassEntry(cs);
        if (descEntry != null) {
            newDescType = Type.getType((descType.getSort() == Type.ARRAY ? "[" : "") + "L" + descEntry[1] + ";");
            if (ReTweakResources.DEBUG) {
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

        localVariableNode.desc = newDescType.getDescriptor();
    }

    private String getRemappedMethodDesc(String desc) throws TweakException {
        final Type[] argumentTypes = Type.getArgumentTypes(desc);
        final Type returnType = Type.getReturnType(desc);

        Type[] newArgumentTypes = new Type[argumentTypes.length];
        Type newReturnType;

        for(int i = 0; i < argumentTypes.length; ++i) {
            Type argumentType = argumentTypes[i];
            String[] entry = srg.getClassEntry(argumentType.getClassName().replace(
                    '.',
                    '/'
            ));

            if (entry != null) {
                if (argumentType.getSort() == Type.ARRAY) throw new IllegalStateException("Arrays not yet supported!");
                newArgumentTypes[i] = Type.getType("L" + entry[1] + ";");
            } else {
                newArgumentTypes[i] = argumentType;
            }
        }

        String[] entry = srg.getClassEntry(returnType.getClassName().replace(
                '.',
                '/'
        ));

        if (entry != null) {
            if (returnType.getSort() == Type.ARRAY) throw new IllegalStateException("Arrays are not yet supported!");
            newReturnType = Type.getReturnType("L" + entry[1] + ";");
        } else {
            newReturnType = returnType;
        }
        return Type.getMethodDescriptor(
                newReturnType,
                newArgumentTypes
        );
    }

}
