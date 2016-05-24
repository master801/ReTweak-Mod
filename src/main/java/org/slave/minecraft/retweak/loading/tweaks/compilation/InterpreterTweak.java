package org.slave.minecraft.retweak.loading.tweaks.compilation;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;

import java.util.Iterator;

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
    public void tweak(final ClassNode classNode) {
        if (gameVersion == null) return;
        if (classNode.fields != null) {
            for(FieldNode fieldNode : classNode.fields) field(fieldNode);
        }
        if (classNode.methods != null) {
            for(MethodNode methodNode : classNode.methods) method(methodNode);
        }
    }

    @Override
    public int getWantedSortIndex() {
        return 0;
    }

    private void field(FieldNode fieldNode) {
        //TODO
    }

    private void method(MethodNode methodNode) {
        int i = 0;
        Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
        while(abstractInsnNodeIterator.hasNext()) {
            final AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();

            boolean remove = false;
            if (abstractInsnNode instanceof FieldInsnNode) {
                remove = fieldInsn((FieldInsnNode)abstractInsnNode);
            } else if (abstractInsnNode instanceof MethodInsnNode) {
                remove = methodInsn((MethodInsnNode)abstractInsnNode);
            } else if (abstractInsnNode instanceof TypeInsnNode) {
                remove = typeInsnNode((TypeInsnNode)abstractInsnNode);
            }
            if (remove) abstractInsnNodeIterator.remove();
            i++;
        }
    }

    private boolean fieldInsn(FieldInsnNode fieldInsnNode) {
        if (gameVersion.getClasses().contains(fieldInsnNode.owner)) {
            fieldInsnNode.owner = gameVersion.getInterpreterPackagePrefix().replace(
                    '.',
                    '/'
            ) + fieldInsnNode.owner;
        }
        Type returnType = Type.getType(fieldInsnNode.desc);
        Type newReturnType;

        final String returnTypeClassName = returnType.getClassName();
        if (gameVersion.getClasses().contains(returnTypeClassName)) {
            String array = "";
            if (returnType.getSort() == Type.ARRAY) {
                //TODO
                array = "";
            }
            newReturnType = Type.getType(
                    array +
                            'L' +
                            (
                                    gameVersion.getInterpreterPackagePrefix() +
                                            returnTypeClassName
                            ).replace(
                                    '.',
                                    '/'
                            ) +
                            ';'
            );
        } else {
            newReturnType = returnType;
        }
        if (newReturnType != returnType) fieldInsnNode.desc = newReturnType.getDescriptor();
        return false;
    }

    private boolean methodInsn(MethodInsnNode methodInsnNode) {
        if (gameVersion.getClasses().contains(methodInsnNode.owner)) {
            methodInsnNode.owner = gameVersion.getInterpreterPackagePrefix().replace(
                    '.',
                    '/'
            ) +
                    methodInsnNode.owner;
        }
        Type returnType = Type.getReturnType(methodInsnNode.desc);
        Type newReturnType;

        final String returnTypeClassName = returnType.getClassName();
        if (gameVersion.getClasses().contains(returnTypeClassName)) {
            String array = "";
            if (returnType.getSort() == Type.ARRAY) {
                //TODO
                array = "";
            }
            newReturnType = Type.getType(
                    array +
                            'L' +
                            (
                                    gameVersion.getInterpreterPackagePrefix() +
                                            returnTypeClassName
                            ).replace(
                                    '.',
                                    '/'
                            ) +
                            ';'
            );
        } else {
            newReturnType = returnType;
        }

        Type[] argumentTypes = Type.getArgumentTypes(methodInsnNode.desc);
        Type[] newArgumentTypes = new Type[argumentTypes.length];

        for(int i = 0; i < argumentTypes.length; ++i) {
            final Type argumentType = argumentTypes[i];
            final String argumentTypeClassName = argumentType.getClassName();
            if (gameVersion.getClasses().contains(argumentTypeClassName)) {
                String array = "";
                if (argumentType.getSort() == Type.ARRAY) {
                    array = "";
                }
                newArgumentTypes[i] = Type.getType(
                        array +
                                "L" +
                                (
                                        gameVersion.getInterpreterPackagePrefix() +
                                                argumentTypeClassName
                                ).replace(
                                        '.',
                                        '/'
                                ) +
                                ";"
                );
            } else {
                newArgumentTypes[i] = argumentTypes[i];
            }
        }

        if (newReturnType != returnType || newArgumentTypes != argumentTypes) {
            methodInsnNode.desc = Type.getMethodDescriptor(
                    newReturnType,
                    newArgumentTypes
            );
        }
        return false;
    }

    private boolean typeInsnNode(TypeInsnNode typeInsnNode) {
        if (gameVersion.getClasses().contains(typeInsnNode.desc)) {
            typeInsnNode.desc = gameVersion.getInterpreterPackagePrefix().replace(
                    '.',
                    '/'
            ) + typeInsnNode.desc;
        }
        return false;
    }

}
