package org.slave.minecraft.retweak.loading.tweaks.compilation;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
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

    private void field(FieldNode fieldNode) {
    }

    private void method(MethodNode methodNode) {
        int i = 0;
        Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
        while(abstractInsnNodeIterator.hasNext()) {
            i++;
            final AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();

            boolean remove = false;
            if (abstractInsnNode instanceof FieldInsnNode) {
                remove = fieldInsn((FieldInsnNode)abstractInsnNode);
            } else if (abstractInsnNode instanceof MethodInsnNode) {
                remove = methodInsn((MethodInsnNode)abstractInsnNode);
            }
            if (remove) abstractInsnNodeIterator.remove();
        }
    }

    private boolean fieldInsn(FieldInsnNode fieldInsnNode) {
        return false;
    }

    private boolean methodInsn(MethodInsnNode methodInsnNode) {
        return false;
    }

    @Override
    public int getWantedSortIndex() {
        return 0;
    }

}
