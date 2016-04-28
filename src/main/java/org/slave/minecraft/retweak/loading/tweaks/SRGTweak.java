package org.slave.minecraft.retweak.loading.tweaks;

import com.github.pwittchen.kirai.library.Kirai;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
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
    public void tweak(final ClassNode classNode) {
        if (srg == null) return;
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("TWEAK START");
        name(classNode);
        superName(classNode);
        fields(classNode);
        methods(classNode);
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("TWEAK END\n\n");
    }

    private void name(ClassNode classNode) {
        String[] nameEntry = srg.getClassEntry(classNode.name);
        if (nameEntry != null) {
            classNode.name = nameEntry[1];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Remapped class name from \"{}\" to \"{}\".",
                        nameEntry[0],
                        nameEntry[1]
                );
            }
        }

        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "TWEAK NAME: {}",
                    classNode.name
            );
        }
    }

    private void superName(ClassNode classNode) {
        String[] superNameEntry = srg.getClassEntry(classNode.superName);
        if (superNameEntry != null) {
            classNode.name = superNameEntry[1];
            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Renamed super-class from \"{}\" to \"{}\".",
                        superNameEntry[0],
                        superNameEntry[1]
                );
            }
        }

        if (ReTweakResources.DEBUG) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "TWEAK SUPER_NAME: {}",
                    classNode.superName
            );
        }
    }

    private void fields(ClassNode classNode) {
        for(FieldNode fieldNode : classNode.fields) {
            field(
                    classNode,
                    fieldNode
            );

            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "TWEAK FIELD: \"{}\" \"{}\"",
                        fieldNode.name,
                        fieldNode.desc
                );
            }
        }
    }

    private void field(ClassNode classNode, FieldNode fieldNode) {
        Type type = Type.getType(fieldNode.desc);

    }

    private void methods(ClassNode classNode) {
        for(MethodNode methodNode : classNode.methods) {
            method(
                    classNode,
                    methodNode
            );

            if (ReTweakResources.DEBUG) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "TWEAK METHOD: \"{}\" \"{}\"",
                        methodNode.name, methodNode.desc
                );
                /*
                ReTweakResources.RETWEAK_LOGGER.info("TWEAK METHOD INSTRUCTIONS START");
                ASMHelper.printMethodInstructions(
                        classNode.name,
                        methodNode.name,
                        methodNode.desc,
                        methodNode.instructions,
                        methodNode.localVariables,
                        ReTweakResources.RETWEAK_LOGGER
                );
                ReTweakResources.RETWEAK_LOGGER.info("TWEAK METHOD INSTRUCTIONS END");
                */
            }
        }
    }

    private void method(ClassNode classNode, MethodNode methodNode) {
        methodNode.desc = getRemappedMethodDesc(methodNode.desc);
        for(int i = 0; i < methodNode.instructions.size(); ++i) {
            AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
            if (abstractInsnNode instanceof MethodInsnNode) {
                MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;

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
                                        "Class: {class}, Method: {method}, Index: {index}"
                                ).put(
                                        "class",
                                        classNode.name
                                ).put(
                                        "method",
                                        methodNode.name
                                ).put(
                                        "index",
                                        i
                                ).format().toString(),
                                ownerEntry[0],
                                ownerEntry[1]
                        );
                    }
                } else {
                    newOwnerType = ownerType;
                }

                Type[] argumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
                Type returnType = Type.getReturnType(methodInsnNode.desc);

                Type[] newArgumentTypes = new Type[argumentTypes.length];
                Type newReturnType;

                for(int a = 0; a < argumentTypes.length; ++a) {
                    Type argumentType = argumentTypes[a];
                    String[] entry = srg.getClassEntry(argumentType.getClassName().replace(
                            '.',
                            '/'
                    ));
                    if (entry != null) {
                        if (argumentType.getSort() == Type.ARRAY) throw new IllegalStateException("Array not supported yet!");
                        newArgumentTypes[a] = Type.getType("L" + entry[1] + ";");
                    } else {
                        newArgumentTypes[a] = argumentType;
                    }
                }
                String[] returnEntry = srg.getClassEntry(returnType.getClassName().replace(
                        '.',
                        '/'
                ));
                if (returnEntry != null) {
                    if (returnType.getSort() == Type.ARRAY) throw new IllegalStateException("Array not supported yet!");
                    newReturnType = Type.getType("L" + returnEntry[1] + ";");
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Remapped return type in method insn node \"{}\", from \"{}\" to \"{}\"",
                                Kirai.from(
                                        "Class: {class}, Method: {method}, Index: {index}"
                                ).put(
                                        "class",
                                        classNode.name
                                ).put(
                                        "method",
                                        methodNode.name
                                ).put(
                                        "index",
                                        i
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
            if (abstractInsnNode instanceof FieldInsnNode) {
                FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                String[] entry = srg.getFieldEntry(
                        fieldInsnNode.owner,
                        fieldInsnNode.name
                );
                if (entry != null) {
                    final String _1 = entry[1];

                    String owner = _1.substring(
                            0,
                            _1.lastIndexOf('/')
                    );
                    String name = _1.substring(
                            _1.lastIndexOf('/') + 1
                    );
                    String oldOwner = fieldInsnNode.owner;
                    String oldName = fieldInsnNode.name;

                    fieldInsnNode.owner = owner;
                    fieldInsnNode.name = name;
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Remapped field insn \"{}\", from \"{} {}\" to \"{} {}\"",
                                Kirai.from(
                                        "Class: {class}, Method: {method}, Index: {index}"
                                ).put(
                                        "class",
                                        classNode.name
                                ).put(
                                        "method",
                                        methodNode.name
                                ).put(
                                        "index",
                                        i
                                ).format().toString(),
                                oldOwner,
                                oldName,
                                owner,
                                name
                        );
                    }
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
        }
    }

    private String getRemappedMethodDesc(String desc) {
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