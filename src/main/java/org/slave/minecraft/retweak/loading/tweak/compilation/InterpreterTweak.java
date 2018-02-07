package org.slave.minecraft.retweak.loading.tweak.compilation;

import com.google.common.base.Joiner;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.slave.lib.helpers.ASMHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.loading.tweak.Tweak;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Master on 4/27/2016 at 7:26 AM.
 *
 * @author Master
 */
public final class InterpreterTweak implements Tweak {

    private static final Pattern PATTERN_DESC = Pattern.compile("^(\\*L).+(;)$");

    private final GameVersion gameVersion;

    public InterpreterTweak(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    @Override
    public String getName() {
        return "Interpreter";
    }

    @Override
    public void tweak(final ClassNode classNode, final GameVersion gameVersion) {
        if (classNode == null || gameVersion == null) return;
        //<editor-fold desc="Class">
        _class(
            classNode
        );
        //</editor-fold>
        //<editor-fold desc="Fields">
        if (classNode.fields != null) {
            List<FieldNode> fields = classNode.fields;
            for(int i = 0; i < fields.size(); ++i) {
                field(
                    classNode.name,
                    fields.get(i),
                    i
                );
            }
        }
        //</editor-fold>
        //<editor-fold desc="Methods">
        if (classNode.methods != null) {
            List<MethodNode> methods = classNode.methods;
            for(int i = 0; i < methods.size(); i++) {
                method(
                    classNode.name,
                    methods.get(i),
                    i
                );
            }
        }
        //</editor-fold>
    }

    @Override
    public int getWantedSortIndex() {
        return 0;
    }

    private void _class(final ClassNode classNode) {
        //<editor-fold desc="Super-Name">
        classSuperName(
            classNode
        );
        //</editor-fold>
        //<editor-fold desc="Interfaces">
        classInterfaces(
            classNode
        );
        //</editor-fold>
        //<editor-fold desc="Signature">
        classSignature(
            classNode
        );
        //</editor-fold>
    }

    private void classSuperName(final ClassNode classNode) {
        String originalSuperName = classNode.superName;
        String finalSuperName = null;

        ClassInfo superClassInfo = gameVersion.getClassInfo(originalSuperName);

        Class<?> overrideClass = superClassInfo.getClassEntry().getTo();
        if (overrideClass != null) finalSuperName = Type.getInternalName(overrideClass);

        if (finalSuperName != null) {
            classNode.superName = finalSuperName;
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked super-name in class \"{}\" from \"{}\" to \"{}\"",
                classNode.name,
                originalSuperName,
                classNode.superName
            );
            //</editor-fold>
        }
    }

    private void classInterfaces(final ClassNode classNode) {
        List<String> originalInterfaces = classNode.interfaces;
        List<String> finalInterfaces = new ArrayList<>();

        if (originalInterfaces != null) {
            finalInterfaces.addAll(originalInterfaces);
            for(int i = 0; i < originalInterfaces.size(); ++i) {
                String originalInterface = originalInterfaces.get(i);
                String finalInterface = null;

                ClassInfo interfaceClassInfo = gameVersion.getClassInfo(originalInterface);

                Class<?> overrideClass = interfaceClassInfo.getClassEntry().getTo();
                if (overrideClass != null) finalInterface = Type.getInternalName(overrideClass);

                if (finalInterface != null) finalInterfaces.set(i, finalInterface);
            }
            if (!finalInterfaces.equals(originalInterfaces)) {
                classNode.interfaces = finalInterfaces;
                //<editor-fold desc="DEBUG">
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked interfaces in class \"{}\" from \"{}\" to \"{}\"",
                    classNode.name,
                    "[" + Joiner.on(", ").join(originalInterfaces) + "]",
                    "[" + Joiner.on(", ").join(finalInterfaces) + "]"
                );
                //</editor-fold>
            }
        }
    }

    private void classSignature(final ClassNode classNode) {
        //TODO
    }

    private void field(final String className, final FieldNode fieldNode, final int index) {
        //<editor-fold desc="Annotations">
        fieldAnnotations(
            className,
            fieldNode,
            index
        );
        //</editor-fold>
        //<editor-fold desc="Desc">
        fieldDesc(
            className,
            fieldNode,
            index
        );
        //</editor-fold?
        //<editor-fold desc="Signature">
        fieldSignature(
            className,
            fieldNode,
            index
        );
        //</editor-fold>
    }

    private void fieldAnnotations(final String className, final FieldNode fieldNode, final int methodIndex) {
        //TODO
    }

    private void fieldDesc(final String className, final FieldNode fieldNode, final int methodIndex) {
        Type originalDescType = Type.getType(fieldNode.desc);
        Type finalDescType = originalDescType;

        //<editor-fold desc="Desc">
        ClassInfo descClassInfo = gameVersion.getClassInfo(originalDescType.getClassName());

        Class<?> overrideClass = descClassInfo.getClassEntry().getTo();
        if (overrideClass != null) {
            finalDescType = Type.getType(
                overrideClass
            );
        }

        if (fieldNode.desc.equals(finalDescType.getDescriptor())) return;

        fieldNode.desc = finalDescType.getDescriptor();
        //</editor-fold>

        //<editor-fold desc="DEBUG">
        ReTweakResources.RETWEAK_LOGGER.debug(
            "Tweaked field desc in class \"{}\" from \"{}\" to \"{}\"",
            className,
            originalDescType.getDescriptor(),
            finalDescType.getDescriptor()
        );
        //</editor-fold>
    }

    private void fieldSignature(final String className, final FieldNode fieldNode, final int index) {
        //TODO
    }

    private void method(final String className, final MethodNode methodNode, final int index) {
        //<editor-fold desc="Annotations">
        methodAnnotations(
            className,
            methodNode,
            index
        );
        //</editor-fold>
        //<editor-fold desc="Desc">
        methodDesc(
            className,
            methodNode,
            index
        );
        //</editor-fold>
        //<editor-fold desc="Signature">
        methodSignature(
            className,
            methodNode,
            index
        );
        //</editor-fold>
        //<editor-fold desc="Instructions">
        int i = 0;
        Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
        while (abstractInsnNodeIterator.hasNext()) {
            AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();

            boolean remove = false;
            if (abstractInsnNode instanceof FieldInsnNode) {
                remove = fieldInsn(
                    className,
                    methodNode.name,
                    methodNode.desc,
                    i,
                    (FieldInsnNode) abstractInsnNode
                );
            } else if (abstractInsnNode instanceof MethodInsnNode) {
                remove = methodInsn(
                    className,
                    methodNode.name,
                    methodNode.desc,
                    i,
                    (MethodInsnNode) abstractInsnNode
                );
            } else if (abstractInsnNode instanceof TypeInsnNode) {
                remove = typeInsnNode(
                    className,
                    methodNode.name,
                    methodNode.desc,
                    i,
                    (TypeInsnNode) abstractInsnNode
                );
            } else if (abstractInsnNode instanceof LdcInsnNode) {
                remove = ldcInsnNode(
                    className,
                    methodNode.name,
                    methodNode.desc,
                    i,
                    (LdcInsnNode) abstractInsnNode
                );
            } else if (abstractInsnNode instanceof FrameNode) {
                remove = frameNode(
                    className,
                    methodNode.name,
                    methodNode.desc,
                    i,
                    (FrameNode) abstractInsnNode
                );
            }
            if (remove) abstractInsnNodeIterator.remove();
            i++;
        }
        //</editor-fold>
        //<editor-fold desc="Local variables">
        if (methodNode.localVariables != null) {
            Iterator<LocalVariableNode> localVariableNodeIterator = methodNode.localVariables.iterator();
            while (localVariableNodeIterator.hasNext()) {
                LocalVariableNode localVariableNode = localVariableNodeIterator.next();

                boolean remove = localVariableNode(
                    className,
                    methodNode.name,
                    methodNode.desc,
                    localVariableNode,
                    index
                );
                if (remove) localVariableNodeIterator.remove();
            }
        }
        //</editor-fold>
    }

    private void methodAnnotations(final String className, final MethodNode methodNode, final int methodIndex) {
        //TODO
    }

    private void methodDesc(final String className, final MethodNode methodNode, final int methodIndex) {
        String originalDesc = methodNode.desc;
        String finalDesc;

        //<editor-fold desc="Return type">
        Type originalReturnType = Type.getReturnType(methodNode.desc);
        Type modifyReturnType;
        Type finalReturnType;

        Class<?> returnTypeOverrideClass;

        if (originalReturnType.getSort() == Type.ARRAY) {
            modifyReturnType = originalReturnType.getElementType();
        } else {
            modifyReturnType = originalReturnType;
        }

        returnTypeOverrideClass = gameVersion.getClassInfo(modifyReturnType.getClassName()).getClassEntry().getTo();

        if (returnTypeOverrideClass != null) {
            //<editor-fold desc="Array">
            if (originalReturnType.getSort() == Type.ARRAY) {
                String desc = modifyReturnType.getDescriptor();
                for(int i = 0; i < originalReturnType.getDimensions(); ++i) {
                    desc = StringHelper.insert(
                        desc,
                        "[",
                        0
                    );
                }
                modifyReturnType = Type.getType(desc);
            } else {
                modifyReturnType = Type.getType(returnTypeOverrideClass);
            }
            //</editor-fold>
        } else {
            modifyReturnType = originalReturnType;
        }

        finalReturnType = modifyReturnType;
        //</editor-fold>
        //<editor-fold desc="Argument types">
        Type[] originalArgumentTypes = Type.getArgumentTypes(methodNode.desc);
        Type[] finalArgumentTypes = new Type[originalArgumentTypes.length];
        System.arraycopy(
            originalArgumentTypes,
            0,
            finalArgumentTypes,
            0,
            originalArgumentTypes.length
        );

        for(int i = 0; i < finalArgumentTypes.length; ++i) {
            Type originalArgumentType = originalArgumentTypes[i];
            Type modifyArgumentType;
            Type finalArgumentType;

            Class<?> argumentTypeOverrideClass;

            if (originalArgumentType.getSort() == Type.ARRAY) {
                modifyArgumentType = originalArgumentType.getElementType();
            } else {
                modifyArgumentType = originalArgumentType;
            }

            ClassInfo argumentClassType = gameVersion.getClassInfo(modifyArgumentType.getClassName());

            argumentTypeOverrideClass = argumentClassType.getClassEntry().getTo();

            if (argumentTypeOverrideClass != null) {
                if (originalArgumentType.getSort() == Type.ARRAY) {
                    //<editor-fold desc="Array">
                    String desc = modifyArgumentType.getDescriptor();
                    for(int j = 0; j < originalArgumentType.getDimensions(); ++j) {
                        desc = StringHelper.insert(
                            desc,
                            "[",
                            0
                        );
                    }
                    modifyArgumentType = Type.getType(desc);
                    //</editor-fold>
                } else {
                    modifyArgumentType = Type.getType(argumentTypeOverrideClass);
                }
            } else {
                modifyArgumentType = originalArgumentType;
            }

            finalArgumentType = modifyArgumentType;
            finalArgumentTypes[i] = finalArgumentType;
        }
        //</editor-fold>

        finalDesc = Type.getMethodDescriptor(
            finalReturnType,
            finalArgumentTypes
        );

        if (!finalDesc.equals(originalDesc)) {
            methodNode.desc = finalDesc;
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked method desc in class \"{}/{}\" from \"{}\" to \"{}\"",
                className,
                methodNode.name,

                originalDesc,
                finalDesc
            );
            //</editor-fold>
        }
    }

    private void methodSignature(final String className, final MethodNode methodNode, final int index) {
        //TODO
    }

    private boolean fieldInsn(final String className, final String methodName, final String methodDesc, final int index, final FieldInsnNode fieldInsnNode) {
        //<editor-fold desc="Owner">
        fieldInsnOwner(
            className,
            methodName,
            methodDesc,
            index,
            fieldInsnNode
        );
        //</editor-fold>
        //<editor-fold desc="Name">
        fieldInsnDesc(
            className,
            methodName,
            methodDesc,
            index,
            fieldInsnNode
        );
        //</editor-fold>
        //<editor-fold desc="Signature">
        fieldInsnSignature(
            className,
            methodName,
            methodDesc,
            index,
            fieldInsnNode
        );
        //</editor-fold>
        return false;
    }

    private void fieldInsnOwner(final String className, final String methodName, final String methodDesc, final int index, final FieldInsnNode fieldInsnNode) {
        String originalOwner = fieldInsnNode.owner;
        String finalOwner = null;

        //<editor-fold desc="Owner">
        ClassInfo ownerClassInfo = gameVersion.getClassInfo(originalOwner);

        Class<?> overrideClass = ownerClassInfo.getClassEntry().getTo();
        if (overrideClass != null) finalOwner = Type.getInternalName(overrideClass);

        if (finalOwner != null) {
            fieldInsnNode.owner = finalOwner;
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked field insn owner in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                className,
                methodName,
                methodDesc,

                originalOwner,
                fieldInsnNode.owner,

                index
            );
            //</editor-fold>
        }
        //</editor-fold>
    }

    private void fieldInsnDesc(final String className, final String methodName, final String methodDesc, final int index, final FieldInsnNode fieldInsnNode) {
        Type originalDescType = Type.getType(fieldInsnNode.desc);
        Type modifyDescType = originalDescType;
        Type finalDescType;

        Class<?> overrideClass;

        if (originalDescType.getSort() == Type.ARRAY) modifyDescType = originalDescType.getElementType();

        ClassInfo descClassInfo = gameVersion.getClassInfo(modifyDescType.getClassName());

        overrideClass = descClassInfo.getClassEntry().getTo();

        if (overrideClass != null) {
            if (originalDescType.getSort() == Type.ARRAY) {
                //<editor-fold desc="Array">
                String desc = modifyDescType.getDescriptor();
                for(int i = 0; i < originalDescType.getDimensions(); ++i) {
                    desc = StringHelper.insert(
                        desc,
                        "[",
                        0
                    );
                }
                modifyDescType = Type.getType(desc);
                //</editor-fold>
            } else {
                modifyDescType = Type.getType(overrideClass);
            }
        } else {
            modifyDescType = originalDescType;
        }

        finalDescType = modifyDescType;

        if (!finalDescType.equals(originalDescType)) {
            fieldInsnNode.desc = finalDescType.getDescriptor();
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked field insn desc in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                className,
                methodName,
                methodDesc,

                originalDescType.getDescriptor(),
                fieldInsnNode.desc,

                index
            );
            //</editor-fold>
        }
    }

    private void fieldInsnSignature(final String className, final String methodName, final String methodDesc, final int index, final FieldInsnNode fieldInsnNode) {
        //TODO
    }

    private boolean methodInsn(final String className, final String methodName, final String methodDesc, final int index, final MethodInsnNode methodInsnNode) {
        //<editor-fold desc="Owner">
        methodInsnOwner(
            className,
            methodName,
            methodDesc,
            index,
            methodInsnNode
        );
        //</editor-fold>
        //<editor-fold desc="Desc">
        methodInsnDesc(
            className,
            methodName,
            methodDesc,
            index,
            methodInsnNode
        );
        //</editor-fold>
        //<editor-fold desc="Signature">
        methodInsnSignature(
            className,
            methodName,
            methodDesc,
            index,
            methodInsnNode
        );
        //</editor-fold>
        return false;
    }

    private void methodInsnOwner(final String className, final String methodName, final String methodDesc, final int index, final MethodInsnNode methodInsnNode) {
        String originalOwner = methodInsnNode.owner;
        String finalOwner = null;

        //<editor-fold desc="Owner">
        ClassInfo ownerClassInfo = gameVersion.getClassInfo(originalOwner);

        Class<?> overrideClass = ownerClassInfo.getClassEntry().getTo();
        if (overrideClass != null) finalOwner = Type.getInternalName(overrideClass);

        if (finalOwner != null) {
            methodInsnNode.owner = finalOwner;
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked owner in method insn \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                className,
                methodName,
                methodDesc,

                originalOwner,
                finalOwner,

                index
            );
            //</editor-fold>
        }
        //</editor-fold>
    }

    private void methodInsnDesc(final String className, final String methodName, final String methodDesc, final int index, final MethodInsnNode methodInsnNode) {
        String originalDesc = methodInsnNode.desc;
        String finalDesc;

        //<editor-fold desc="Return type">
        Type originalReturnType = Type.getReturnType(methodInsnNode.desc);
        Type modifyReturnType;
        Type finalReturnType;

        Class<?> returnTypeOverrideClass;

        if (originalReturnType.getSort() == Type.ARRAY) {
            modifyReturnType = originalReturnType.getElementType();
        } else {
            modifyReturnType = originalReturnType;
        }

        ClassInfo returnClassInfo = gameVersion.getClassInfo(modifyReturnType.getClassName());

        returnTypeOverrideClass = returnClassInfo.getClassEntry().getTo();

        if (returnTypeOverrideClass != null) {
            if (originalReturnType.getSort() == Type.ARRAY) {
                //<editor-fold desc="Array">
                String desc = modifyReturnType.getDescriptor();
                for(int i = 0; i < originalReturnType.getDimensions(); ++i) {
                    desc = StringHelper.insert(
                        desc,
                        "[",
                        0
                    );
                }
                modifyReturnType = Type.getType(desc);
                //</editor-fold>
            } else {
                modifyReturnType = Type.getType(returnTypeOverrideClass);
            }
        } else {
            modifyReturnType = originalReturnType;
        }
        finalReturnType = modifyReturnType;
        //</editor-fold>
        //<editor-fold desc="Argument types">
        Type[] originalArgumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
        Type[] finalArgumentTypes = new Type[originalArgumentTypes.length];
        System.arraycopy(
            originalArgumentTypes,
            0,
            finalArgumentTypes,
            0,
            originalArgumentTypes.length
        );

        for(int i = 0; i < finalArgumentTypes.length; ++i) {
            Type originalArgumentType = originalArgumentTypes[i];
            Type modifyArgumentType = originalArgumentType;
            Type finalArgumentType;

            Class<?> argumentTypeOverrideClass;

            if (originalArgumentType.getSort() == Type.ARRAY) modifyArgumentType = originalArgumentType.getElementType();

            ClassInfo argumentClassInfo = gameVersion.getClassInfo(modifyArgumentType.getClassName());

            argumentTypeOverrideClass = argumentClassInfo.getClassEntry().getTo();

            if (argumentTypeOverrideClass != null) {
                //<editor-fold desc="Array">
                if (originalArgumentType.getSort() == Type.ARRAY) {
                    String desc = modifyArgumentType.getDescriptor();
                    for(int j = 0; j < originalArgumentType.getDimensions(); ++j) {
                        desc = StringHelper.insert(
                            desc,
                            "[",
                            0
                        );
                    }
                    modifyArgumentType = Type.getType(desc);
                } else {
                    modifyArgumentType = Type.getType(argumentTypeOverrideClass);
                }
                //</editor-fold>
            } else {
                modifyArgumentType = originalArgumentType;
            }

            finalArgumentType = modifyArgumentType;
            finalArgumentTypes[i] = finalArgumentType;
        }
        //</editor-fold>

        finalDesc = Type.getMethodDescriptor(
            finalReturnType,
            finalArgumentTypes
        );

        if (!finalDesc.equals(originalDesc)) {
            methodInsnNode.desc = finalDesc;
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked desc in method insn \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                className,
                methodName,
                methodDesc,

                originalDesc,
                methodInsnNode.desc,

                index
            );
            //</editor-fold>
        }
    }

    private void methodInsnSignature(final String className, final String methodName, final String methodDesc, final int index, final MethodInsnNode methodInsnNode) {
        //TODO
    }

    private boolean typeInsnNode(final String className, final String methodName, final String methodDesc, final int index, final TypeInsnNode typeInsnNode) {
        String originalDesc = typeInsnNode.desc;

        Type originalDescType = Type.getObjectType(originalDesc);
        Type finalDescType;

        //<editor-fold desc="Desc">
        ClassInfo descClassInfo = gameVersion.getClassInfo(originalDescType.getClassName());

        Class<?> overrideClass = descClassInfo.getClassEntry().getTo();

        if (overrideClass != null) {
            finalDescType = Type.getType(overrideClass);
        } else {
            finalDescType = originalDescType;
        }

        if (finalDescType != null) {
            typeInsnNode.desc = finalDescType.getInternalName();
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked type insn from method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                className,
                methodName,
                methodDesc,

                originalDesc,
                typeInsnNode.desc,

                index
            );
            //</editor-fold>
        }
        //</editor-fold>
        return false;
    }

    private boolean ldcInsnNode(final String className, final String methodName, final String methodDesc, final int index, final LdcInsnNode ldcInsnNode) {
        if (ldcInsnNode.cst instanceof Type) {
            Type originalType = (Type) ldcInsnNode.cst;
            Type modifyType = originalType;
            Type finalType;

            Class<?> overrideClass;

            if (originalType.getSort() == Type.ARRAY) modifyType = originalType.getElementType();

            ClassInfo descClassInfo = gameVersion.getClassInfo(modifyType.getClassName());

            overrideClass = descClassInfo.getClassEntry().getTo();

            if (overrideClass != null) {
                if (originalType.getSort() == Type.ARRAY) {
                    //<editor-fold desc="Array">
                    String desc = modifyType.getDescriptor();
                    for(int i = 0; i < originalType.getDimensions(); ++i) {
                        desc = StringHelper.insert(
                            desc,
                            "[",
                            0
                        );
                    }
                    modifyType = Type.getType(desc);
                    //</editor-fold>
                } else {
                    modifyType = Type.getType(overrideClass);
                }
            } else {
                modifyType = originalType;
            }

            finalType = modifyType;

            if (!finalType.equals(originalType)) {
                ldcInsnNode.cst = finalType;
                //<editor-fold desc="DEBUG">
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked ldc insn in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    originalType.toString(),
                    ldcInsnNode.cst.toString(),

                    index
                );
                //</editor-fold>
            }
        }
        return false;
    }

    private boolean frameNode(final String className, final String methodName, final String methodDesc, final int index, final FrameNode frameNode) {
        //<editor-fold desc="Local">
        frameNodeLocal(
            className,
            methodName,
            methodDesc,
            index,
            frameNode
        );
        //</editor-fold>
        //<editor-fold desc="Stack">
        frameNodeStack(
            className,
            methodName,
            methodDesc,
            index,
            frameNode
        );
        //</editor-fold>
        return false;
    }

    private void frameNodeLocal(final String className, final String methodName, final String methodDesc, final int index, final FrameNode frameNode) {
        List<Object> originalLocalList = frameNode.local;
        List<Object> finalLocalList = new ArrayList<>();

        if (originalLocalList != null) {
            finalLocalList.addAll(originalLocalList);

            for(int i = 0; i < originalLocalList.size(); ++i) {
                Object originalLocal = originalLocalList.get(i);
                Object finalLocal = null;
                //<editor-fold desc="String">
                if (originalLocal instanceof String) {
                    String stringLocal = (String) originalLocal;
                    boolean isDesc = InterpreterTweak.PATTERN_DESC.matcher(stringLocal).matches();

                    Type originalType = null;
                    Type modifyType = null;
                    Type finalType;

                    Class<?> overrideClass;

                    if (isDesc) {
                        originalType = Type.getType(stringLocal);
                        if (originalType.getSort() == Type.ARRAY) {
                            modifyType = originalType.getElementType();
                        } else {
                            modifyType = originalType;
                        }
                        overrideClass = gameVersion.getClassInfo(modifyType.getInternalName()).getClassEntry().getTo();
                    } else {
                        overrideClass = gameVersion.getClassInfo(stringLocal).getClassEntry().getTo();
                    }

                    if (overrideClass != null) {
                        if (isDesc) {
                            if (originalType.getSort() == Type.ARRAY) {
                                //<editor-fold desc="Array">
                                String desc = modifyType.getDescriptor();
                                for(int j = 0; j < originalType.getDimensions(); j++) {
                                    desc = StringHelper.insert(
                                        desc,
                                        "[",
                                        0
                                    );
                                }
                                finalType = Type.getType(desc);
                                //</editor-fold>
                            } else {
                                finalType = originalType;
                            }
                            finalLocal = finalType.getDescriptor();
                        } else {
                            finalLocal = Type.getInternalName(overrideClass);
                        }
                    } else {
                        finalLocal = originalLocal;
                    }
                }
                //</editor-fold>
                if (finalLocal != null) finalLocalList.set(i, finalLocal);
            }

            if (!originalLocalList.equals(finalLocalList)) {
                frameNode.local = finalLocalList;
                //<editor-fold desc="DEBUG">
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked frame-node local method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    "[" + Joiner.on(", ").join(originalLocalList) + "]",
                    "[" + Joiner.on(", ").join(finalLocalList) + "]",

                    index
                );
                //</editor-fold>
            }
        }
    }

    private void frameNodeStack(final String className, final String methodName, final String methodDesc, final int index, final FrameNode frameNode) {
        List<Object> originalStackList = frameNode.stack;
        List<Object> finalStackList = new ArrayList<>();

        if (originalStackList != null) {
            finalStackList.addAll(originalStackList);

            for(int i = 0; i < originalStackList.size(); ++i) {
                Object originalStack = originalStackList.get(i);
                Object finalStack = null;
                //<editor-fold desc="String">
                if (originalStack instanceof String) {
                    String stringStack = (String) originalStack;
                    boolean isDesc = InterpreterTweak.PATTERN_DESC.matcher(stringStack).matches();

                    Type originalType = null;
                    Type modifyType = null;
                    Type finalType;

                    Class<?> overrideClass;

                    if (isDesc) {
                        originalType = Type.getType(stringStack);
                        if (originalType.getSort() == Type.ARRAY) {
                            modifyType = originalType.getElementType();
                        } else {
                            modifyType = originalType;
                        }
                        overrideClass = gameVersion.getClassInfo(modifyType.getInternalName()).getClassEntry().getTo();
                    } else {
                        overrideClass = gameVersion.getClassInfo(stringStack).getClassEntry().getTo();
                    }

                    if (overrideClass != null) {
                        if (isDesc) {
                            if (originalType.getSort() == Type.ARRAY) {
                                //<editor-fold desc="Array">
                                String desc = modifyType.getDescriptor();
                                for(int j = 0; j < originalType.getDimensions(); j++) {
                                    desc = StringHelper.insert(
                                        desc,
                                        "[",
                                        0
                                    );
                                }
                                finalType = Type.getType(desc);
                                //</editor-fold>
                            } else {
                                finalType = originalType;
                            }
                            finalStack = finalType.getDescriptor();
                        } else {
                            finalStack = Type.getInternalName(overrideClass);
                        }
                    } else {
                        finalStack = originalStack;
                    }
                }
                //</editor-fold>
                if (finalStack != null) finalStackList.set(i, finalStack);
            }

            if (!originalStackList.equals(finalStackList)) {
                frameNode.stack = finalStackList;
                //<editor-fold desc="DEBUG">
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked frame-node stack in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    "[" + Joiner.on(", ").join(originalStackList) + "]",
                    "[" + Joiner.on(", ").join(finalStackList) + "]",

                    index
                );
                //</editor-fold>
            }
        }
    }

    private boolean localVariableNode(final String className, final String methodName, final String methodDesc, final LocalVariableNode localVariableNode, final int methodIndex) {
        String originalDesc = localVariableNode.desc;
        String finalDesc = null;

        Type originalType = Type.getType(originalDesc);
        Type modifyType;
        Type finalType;

        Class<?> overrideClass;

        if (originalType.getSort() == Type.ARRAY) {
            modifyType = originalType.getElementType();
        } else {
            modifyType = originalType;
        }

        overrideClass = gameVersion.getClassInfo(modifyType.getClassName()).getClassEntry().getTo();

        if (overrideClass != null) {
            if (originalType.getSort() == Type.ARRAY) {
                //<editor-fold desc="Array">
                String desc = modifyType.getDescriptor();
                for(int i = 0; i < originalType.getDimensions(); ++i) {
                    desc = StringHelper.insert(
                        desc,
                        "[",
                        0
                    );
                }
                modifyType = Type.getType(desc);
                //</editor-fold>
            } else {
                modifyType = Type.getType(overrideClass);
            }
        } else {
            modifyType = null;
        }

        if (modifyType != null) {
            finalType = modifyType;
            finalDesc = finalType.getDescriptor();
        }


        if (finalDesc != null) {
            localVariableNode.desc = finalDesc;
            //<editor-fold desc="DEBUG">
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked local variable in method \"{}/{}{}\" from \"{}\" to \"{}\"",
                className,
                methodName,
                methodDesc,

                originalDesc,
                localVariableNode.desc
            );
            //</editor-fold>
        }
        return false;
    }

}
