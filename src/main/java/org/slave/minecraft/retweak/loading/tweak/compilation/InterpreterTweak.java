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
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.loading.tweak.Tweak;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Master on 4/27/2016 at 7:26 AM.
 *
 * @author Master
 */
public final class InterpreterTweak implements Tweak {

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
    }

    private void classSuperName(final ClassNode classNode) {
        String originalSuperName = classNode.superName;
        String finalSuperName = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalSuperName);
        if (overrideClass != null) finalSuperName = Type.getInternalName(overrideClass);

        if (finalSuperName != null) {
            classNode.superName = finalSuperName;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked super-name in class \"{}\" from \"{}\" to \"{}\"",
                    classNode.name,
                    originalSuperName,
                    classNode.superName
            );
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

                Class<?> overrideClass = gameVersion.getOverrideClass(originalInterface);
                if (overrideClass != null) finalInterface = Type.getInternalName(overrideClass);

                if (finalInterface != null) finalInterfaces.set(i, finalInterface);
            }
            if (!finalInterfaces.equals(originalInterfaces)) {
                classNode.interfaces = finalInterfaces;
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Tweaked interfaces in class \"{}\" from \"{}\" to \"{}\"",
                        classNode.name,
                        "[" + Joiner.on(", ").join(originalInterfaces) + "]",
                        "[" + Joiner.on(", ").join(finalInterfaces) + "]"
                );
            }
        }
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
    }

    private void fieldAnnotations(final String className, final FieldNode fieldNode, final int methodIndex) {
        //TODO
    }

    private void fieldDesc(final String className, final FieldNode fieldNode, final int methodIndex) {
        Type originalType = Type.getType(fieldNode.desc);
        Type finalType = originalType;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalType.getClassName());
        if (overrideClass != null) {
            finalType = Type.getType(
                    overrideClass
            );
        }

        if (fieldNode.desc.equals(finalType.getDescriptor())) return;

        fieldNode.desc = finalType.getDescriptor();

        ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked field desc in class \"{}\" from \"{}\" to \"{}\"",
                className,
                originalType.getDescriptor(),
                finalType.getDescriptor()
        );
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
        //<editor-fold desc="Instructions">
        int i = 0;
        Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
        while(abstractInsnNodeIterator.hasNext()) {
            AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();

            boolean remove = false;
            if (abstractInsnNode instanceof FieldInsnNode) {
                remove = fieldInsn(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        i,
                        (FieldInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof MethodInsnNode) {
                remove = methodInsn(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        i,
                        (MethodInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof TypeInsnNode) {
                remove = typeInsnNode(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        i,
                        (TypeInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof LdcInsnNode) {
                remove = ldcInsnNode(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        i,
                        (LdcInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof FrameNode) {
                remove = frameNode(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        i,
                        (FrameNode)abstractInsnNode
                );
            }
            if (remove) abstractInsnNodeIterator.remove();
            i++;
        }
        //</editor-fold>
        //<editor-fold desc="Local variables">
        if (methodNode.localVariables != null) {
            Iterator<LocalVariableNode> localVariableNodeIterator = methodNode.localVariables.iterator();
            while(localVariableNodeIterator.hasNext()) {
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
        Type modifyReturnType = originalReturnType;
        Type finalReturnType;

        Class<?> returnTypeOverrideClass;

        if (originalReturnType.getSort() == Type.ARRAY) modifyReturnType = originalReturnType.getElementType();

        returnTypeOverrideClass = gameVersion.getOverrideClass(modifyReturnType.getClassName());

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
            Type modifyArgumentType = originalArgumentType;
            Type finalArgumentType;

            Class<?> argumentTypeOverrideClass;

            if (originalArgumentType.getSort() == Type.ARRAY) modifyArgumentType = originalArgumentType.getElementType();

            argumentTypeOverrideClass = gameVersion.getOverrideClass(modifyArgumentType.getClassName());

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
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked method desc in class \"{}/{}\" from \"{}\" to \"{}\"",
                    className,
                    methodNode.name,

                    originalDesc,
                    finalDesc
            );
        }
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
        return false;
    }

    private void fieldInsnOwner(final String className, final String methodName, final String methodDesc, final int index, final FieldInsnNode fieldInsnNode) {
        String originalOwner = fieldInsnNode.owner;
        String finalOwner = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalOwner);
        if (overrideClass != null) finalOwner = Type.getInternalName(overrideClass);

        if (finalOwner != null) {
            fieldInsnNode.owner = finalOwner;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked field insn owner in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    originalOwner,
                    fieldInsnNode.owner,

                    index
            );
        }
    }

    private void fieldInsnDesc(final String className, final String methodName, final String methodDesc, final int index, final FieldInsnNode fieldInsnNode) {
        Type originalType = Type.getType(fieldInsnNode.desc);
        Type modifyType = originalType;
        Type finalType;

        Class<?> overrideClass;

        if (originalType.getSort() == Type.ARRAY) modifyType = originalType.getElementType();

        overrideClass = gameVersion.getOverrideClass(modifyType.getClassName());

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
        }

        finalType = modifyType;

        if (!finalType.equals(originalType)) {
            fieldInsnNode.desc = finalType.getDescriptor();
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked field insn desc in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    originalType.getDescriptor(),
                    fieldInsnNode.desc,

                    index
            );
        }
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
        return false;
    }

    private void methodInsnOwner(final String className, final String methodName, final String methodDesc, final int index, final MethodInsnNode methodInsnNode) {
        String originalOwner = methodInsnNode.owner;
        String finalOwner = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalOwner);
        if (overrideClass != null) finalOwner = Type.getInternalName(overrideClass);

        if (finalOwner != null) {
            methodInsnNode.owner = finalOwner;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked owner in method insn \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    originalOwner,
                    finalOwner,

                    index
            );
        }
    }

    private void methodInsnDesc(final String className, final String methodName, final String methodDesc, final int index, final MethodInsnNode methodInsnNode) {
        String originalDesc = methodInsnNode.desc;
        String finalDesc;

        //<editor-fold desc="Return type">
        Type originalReturnType = Type.getReturnType(methodInsnNode.desc);
        Type modifyReturnType = originalReturnType;
        Type finalReturnType;

        Class<?> returnTypeOverrideClass;

        if (originalReturnType.getSort() == Type.ARRAY) modifyReturnType = originalReturnType.getElementType();

        returnTypeOverrideClass = gameVersion.getOverrideClass(modifyReturnType.getClassName());

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

            argumentTypeOverrideClass = gameVersion.getOverrideClass(modifyArgumentType.getClassName());

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
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked desc in method insn \"{}/{}{}\" from \"{}\" to \"{}\" at index",
                    className,
                    methodName,
                    methodDesc,

                    originalDesc,
                    finalDesc,

                    index
            );
        }
    }

    private boolean typeInsnNode(final String className, final String methodName, final String methodDesc, final int index, final TypeInsnNode typeInsnNode) {
        String originalDesc = typeInsnNode.desc;
        String finalDesc = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalDesc);
        if (overrideClass != null) finalDesc = Type.getInternalName(overrideClass);

        if (finalDesc != null) {
            typeInsnNode.desc = finalDesc;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked type insn from method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                    className,
                    methodName,
                    methodDesc,

                    originalDesc,
                    typeInsnNode.desc,

                    index
            );
        }
        return false;
    }

    private boolean ldcInsnNode(final String className, final String methodName, final String methodDesc, final int index, final LdcInsnNode ldcInsnNode) {
        if (ldcInsnNode.cst instanceof Type) {
            Type originalType = (Type)ldcInsnNode.cst;
            Type modifyType = originalType;
            Type finalType;

            Class<?> overrideClass;

            if (originalType.getSort() == Type.ARRAY) modifyType = originalType.getElementType();

            overrideClass = gameVersion.getOverrideClass(modifyType.getClassName());

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
            }

            finalType = modifyType;

            if (!finalType.equals(originalType)) {
                ldcInsnNode.cst = finalType;
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Tweaked ldc insn in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                        className,
                        methodName,
                        methodDesc,

                        originalType.toString(),
                        ldcInsnNode.cst.toString(),

                        index
                );
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
                    String stringLocal = (String)originalLocal;
                    boolean isDesc = ASMHelper.PATTERN_DESC_MATCHER.matcher(stringLocal).matches();

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
                        overrideClass = gameVersion.getOverrideClass(modifyType.getInternalName());
                    } else {
                        overrideClass = gameVersion.getOverrideClass(stringLocal);
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
                    }
                }
                //</editor-fold>

                if (finalLocal != null) finalLocalList.set(i, finalLocal);
            }

            if (!originalLocalList.equals(finalLocalList)) {
                frameNode.local = finalLocalList;
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Tweaked frame-node local method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                        className,
                        methodName,
                        methodDesc,

                        "[" + Joiner.on(", ").join(originalLocalList) + "]",
                        "[" + Joiner.on(", ").join(finalLocalList) + "]",

                        index
                );
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
                    String stringStack = (String)originalStack;
                    boolean isDesc = ASMHelper.PATTERN_DESC_MATCHER.matcher(stringStack).matches();

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
                        overrideClass = gameVersion.getOverrideClass(modifyType.getInternalName());
                    } else {
                        overrideClass = gameVersion.getOverrideClass(stringStack);
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
                    }
                }
                //</editor-fold>

                if (finalStack != null) finalStackList.set(i, finalStack);
            }

            if (!originalStackList.equals(finalStackList)) {
                frameNode.stack = finalStackList;
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Tweaked frame-node stack in method \"{}/{}{}\" from \"{}\" to \"{}\" at index {}",
                        className,
                        methodName,
                        methodDesc,

                        "[" + Joiner.on(", ").join(originalStackList) + "]",
                        "[" + Joiner.on(", ").join(finalStackList) + "]",

                        index
                );
            }
        }
    }

    private boolean localVariableNode(final String className, final String methodName, final String methodDesc, final LocalVariableNode localVariableNode, final int methodIndex) {
        String originalDesc = localVariableNode.desc;
        String finalDesc;

        Type originalType = Type.getType(originalDesc);
        Type modifyType = originalType;
        Type finalType;

        Class<?> overrideClass;

        if (originalType.getSort() == Type.ARRAY) modifyType = originalType.getElementType();

        overrideClass = gameVersion.getOverrideClass(modifyType.getClassName());

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
        }

        finalType = modifyType;
        finalDesc = finalType.getDescriptor();

        if (finalDesc != null) {
            localVariableNode.desc = finalDesc;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked local variable in method \"{}/{}{}\" from \"{}\" to \"{}\"",
                    className,
                    methodName,
                    methodDesc,

                    originalDesc,
                    localVariableNode.desc
            );
        }
        return false;
    }

}
