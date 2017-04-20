package org.slave.minecraft.retweak.loading.tweak.compilation;

import com.google.common.base.Joiner;
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
        _class(
                classNode
        );
        if (classNode.fields != null) {
            for(FieldNode fieldNode : classNode.fields) {
                field(
                        classNode.name,
                        fieldNode
                );
            }
        }
        if (classNode.methods != null) {
            for(MethodNode methodNode : classNode.methods) {
                method(
                        classNode.name,
                        methodNode
                );
            }
        }
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
        if (overrideClass != null) {
            finalSuperName = Type.getInternalName(overrideClass);
        }

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
        //TODO
    }

    private void field(final String className, final FieldNode fieldNode) {
        //<editor-fold desc="Desc">
        fieldDesc(
                className,
                fieldNode
        );
        //</editor-fold?
    }

    private void fieldDesc(final String className, final FieldNode fieldNode) {
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

    private void method(final String className, final MethodNode methodNode) {
        //<editor-fold desc="Desc">
        methodDesc(
                className,
                methodNode
        );
        //</editor-fold>
        //<editor-fold desc="Instructions">
        Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
        while(abstractInsnNodeIterator.hasNext()) {
            AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();

            boolean remove = false;
            if (abstractInsnNode instanceof FieldInsnNode) {
                remove = fieldInsn(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        (FieldInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof MethodInsnNode) {
                remove = methodInsn(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        (MethodInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof TypeInsnNode) {
                remove = typeInsnNode(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        (TypeInsnNode)abstractInsnNode
                );
            } else if (abstractInsnNode instanceof FrameNode) {
                remove = frameNode(
                        className,
                        methodNode.name,
                        methodNode.desc,
                        (FrameNode)abstractInsnNode
                );
            }
            if (remove) abstractInsnNodeIterator.remove();
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
                        localVariableNode
                );
                if (remove) localVariableNodeIterator.remove();
            }
        }
        //</editor-fold>
    }

    private void methodDesc(final String className, final MethodNode methodNode) {
        String originalDesc = methodNode.desc;
        String finalDesc;

        //<editor-fold desc="Return type">
        Type originalReturnType = Type.getReturnType(methodNode.desc);
        Type finalReturnType = originalReturnType;

        Class<?> returnTypeOverrideClass = gameVersion.getOverrideClass(originalReturnType.getClassName());
        if (returnTypeOverrideClass != null) {
            finalReturnType = Type.getType(returnTypeOverrideClass);
        }
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
            Type finalArgumentType = null;

            Class<?> argumentTypeOverrideClass = gameVersion.getOverrideClass(originalArgumentType.getClassName());
            if (argumentTypeOverrideClass != null) {
                finalArgumentType = Type.getType(argumentTypeOverrideClass);
            }

            if (finalArgumentType != null) {
                finalArgumentTypes[i] = finalArgumentType;
            }
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

    private boolean fieldInsn(final String className, final String methodName, final String methodDesc, final FieldInsnNode fieldInsnNode) {
        //<editor-fold desc="Owner">
        fieldInsnOwner(
                className,
                methodName,
                methodDesc,
                fieldInsnNode
        );
        //</editor-fold>
        //<editor-fold desc="Name">
        fieldInsnDesc(
                className,
                methodName,
                methodDesc,
                fieldInsnNode
        );
        //</editor-fold>
        return false;
    }

    private void fieldInsnOwner(final String className, final String methodName, final String methodDesc, final FieldInsnNode fieldInsnNode) {
        String originalOwner = fieldInsnNode.owner;
        String finalOwner = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalOwner);
        if (overrideClass != null) finalOwner = Type.getInternalName(overrideClass);

        if (finalOwner != null) {
            fieldInsnNode.owner = finalOwner;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked field insn owner in method \"{}/{}{}\" from \"{}\" to \"{}\"",
                    className,
                    methodName,
                    methodDesc,
                    originalOwner,
                    fieldInsnNode.owner
            );
        }
    }

    private void fieldInsnDesc(final String className, final String methodName, final String methodDesc, final FieldInsnNode fieldInsnNode) {
        Type originalType = Type.getType(fieldInsnNode.desc);
        Type modifyType;
        Type finalType;
        switch(originalType.getSort()) {
            case Type.ARRAY:
                modifyType = originalType.getElementType();
                break;
            case Type.OBJECT:
            default:
                modifyType = originalType;
                break;
        }

        Class<?> overrideClass = gameVersion.getOverrideClass(originalType.getClassName());
        if (overrideClass != null) {
            modifyType = Type.getType(
                    overrideClass
            );
        }

        switch(originalType.getSort()) {
            case Type.ARRAY:
                finalType = Type.getObjectType(
                        "[" + modifyType.getDescriptor()
                );
                break;
            default:
                finalType = modifyType;
                break;
        }

        if (fieldInsnNode.desc.equals(finalType.getDescriptor())) return;

        fieldInsnNode.desc = finalType.getDescriptor();

        ReTweakResources.RETWEAK_LOGGER.debug(
                "Tweaked field insn desc in method \"{}/{}{}\" from \"{}\" to \"{}\"",
                className,
                methodName,
                methodDesc,
                originalType.getDescriptor(),
                finalType.getDescriptor()
        );
    }

    private boolean methodInsn(final String className, final String methodName, final String methodDesc, final MethodInsnNode methodInsnNode) {
        //<editor-fold desc="Owner">
        methodInsnOwner(
                className,
                methodInsnNode.name,
                methodInsnNode.desc,
                methodInsnNode
        );
        //</editor-fold>
        //<editor-fold desc="Desc">
        methodInsnDesc(
                className,
                methodInsnNode.name,
                methodInsnNode.desc,
                methodInsnNode
        );
        //</editor-fold>
        return false;
    }

    private void methodInsnOwner(final String className, final String methodName, final String methodDesc, final MethodInsnNode methodInsnNode) {
        String originalOwner = methodInsnNode.owner;
        String finalOwner = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalOwner);
        if (overrideClass != null) finalOwner = Type.getInternalName(overrideClass);

        if (finalOwner != null) {
            methodInsnNode.owner = finalOwner;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked owner in method insn \"{}/{}{}\" from \"{}\" to \"{}\"",
                    className,
                    methodName,
                    methodDesc,

                    originalOwner,
                    finalOwner
            );
        }
    }

    private void methodInsnDesc(final String className, final String methodName, final String methodDesc, final MethodInsnNode methodInsnNode) {
        String originalDesc = methodInsnNode.desc;
        String finalDesc;

        //<editor-fold desc="Return type">
        Type originalReturnType = Type.getReturnType(methodInsnNode.desc);
        Type finalReturnType = originalReturnType;

        Class<?> returnTypeOverrideClass = gameVersion.getOverrideClass(originalReturnType.getClassName());
        if (returnTypeOverrideClass != null) {
            finalReturnType = Type.getType(returnTypeOverrideClass);
        }
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
            Type finalArgumentType = null;

            Class<?> argumentTypeOverrideClass = gameVersion.getOverrideClass(originalArgumentType.getClassName());
            if (argumentTypeOverrideClass != null) {
                finalArgumentType = Type.getType(argumentTypeOverrideClass);
            }

            if (finalArgumentType != null) {
                finalArgumentTypes[i] = finalArgumentType;
            }
        }
        //</editor-fold>

        finalDesc = Type.getMethodDescriptor(
                finalReturnType,
                finalArgumentTypes
        );

        if (!finalDesc.equals(originalDesc)) {
            methodInsnNode.desc = finalDesc;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked desc in method insn \"{}/{}{}\" from \"{}\" to \"{}\"",
                    className,
                    methodName,
                    methodDesc,

                    originalDesc,
                    finalDesc
            );
        }
    }

    private boolean typeInsnNode(final String className, final String methodName, final String methodDesc, final TypeInsnNode typeInsnNode) {
        String originalDesc = typeInsnNode.desc;
        String finalDesc = null;

        Class<?> overrideClass = gameVersion.getOverrideClass(originalDesc);
        if (overrideClass != null) finalDesc = Type.getInternalName(overrideClass);

        if (finalDesc != null) {
            typeInsnNode.desc = finalDesc;
            ReTweakResources.RETWEAK_LOGGER.debug(
                    "Tweaked type from method \"{}/{}{}\" from \"{}\" to \"{}\"",
                    className,
                    methodName,
                    methodDesc,
                    originalDesc,
                    finalDesc
            );
        }
        return false;
    }

    private boolean frameNode(final String className, final String methodName, final String methodDesc, final FrameNode frameNode) {
        //<editor-fold desc="Local">
        frameNodeLocal(
                className,
                methodName,
                methodDesc,
                frameNode
        );
        //</editor-fold>

        //<editor-fold desc="Stack">
        frameNodeStack(
                className,
                methodName,
                methodDesc,
                frameNode
        );
        //</editor-fold>
        return false;
    }

    private void frameNodeLocal(final String className, final String methodName, final String methodDesc, final FrameNode frameNode) {
        List<Object> originalLocalList = frameNode.local;
        List<Object> finalLocalList = new ArrayList<>();

        if (originalLocalList != null) {
            finalLocalList.addAll(originalLocalList);

            for(int i = 0; i < originalLocalList.size(); ++i) {
                Object originalLocal = originalLocalList.get(i);
                Object finalLocal = null;

                if (originalLocal instanceof String) {
                    Class<?> overrideClass = gameVersion.getOverrideClass((String)originalLocal);
                    if (overrideClass != null) finalLocal = Type.getInternalName(overrideClass);
                }

                if (finalLocal != null) finalLocalList.set(i, finalLocal);
            }

            if (!originalLocalList.equals(finalLocalList)) {
                frameNode.local = finalLocalList;
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Tweaked frame-node local method \"{}/{}{}\" from \"{}\" to \"{}\"",
                        className,
                        methodName,
                        methodDesc,

                        "[" + Joiner.on(", ").join(originalLocalList) + "]",
                        "[" + Joiner.on(", ").join(finalLocalList) + "]"
                );
            }
        }
    }

    private void frameNodeStack(final String className, final String methodName, final String methodDesc, final FrameNode frameNode) {
        List<Object> originalStackList = frameNode.stack;
        List<Object> finalStackList = new ArrayList<>();

        if (originalStackList != null) {
            finalStackList.addAll(originalStackList);

            for(int i = 0; i < originalStackList.size(); ++i) {
                Object originalStack = originalStackList.get(i);
                Object finalStack = null;

                if (originalStack instanceof String) {
                    Class<?> overrideClass = gameVersion.getOverrideClass((String)originalStack);
                    if (overrideClass != null) finalStack = Type.getInternalName(overrideClass);
                }

                if (finalStack != null) finalStackList.set(i, finalStack);
            }

            if (!originalStackList.equals(finalStackList)) {
                frameNode.stack = finalStackList;
                ReTweakResources.RETWEAK_LOGGER.debug(
                        "Tweaked frame-node stack in method \"{}/{}{}\" from \"{}\" to \"{}\"",
                        className,
                        methodName,
                        methodDesc,

                        "[" + Joiner.on(", ").join(originalStackList) + "]",
                        "[" + Joiner.on(", ").join(finalStackList) + "]"
                );
            }
        }
    }

    private boolean localVariableNode(final String className, final String methodName, final String methodDesc, final LocalVariableNode localVariableNode) {
        String originalDesc = localVariableNode.desc;
        String finalDesc = null;

        Type originalType = Type.getType(originalDesc);
        Type finalType = originalType;

        Class<?> overrideClass = gameVersion.getOverrideClass(
                originalType.getClassName()
        );
        if (overrideClass != null) finalType = Type.getType(overrideClass);
        if (!finalType.equals(originalType)) finalDesc = finalType.getDescriptor();

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
        //TODO
        return false;
    }

}
