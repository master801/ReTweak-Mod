package org.slave.minecraft.retweak.loading.mappings;

import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * Created by Master on 4/27/2016 at 3:31 PM.
 *
 * @author Master
 */
public abstract class Mapping {

    protected abstract void method(MethodNode methodNode);

    protected abstract void methodInsn(MethodInsnNode methodInsnNode);

    protected abstract void field(FieldNode fieldNode);

    protected abstract void fieldInsn(FieldInsnNode fieldInsnNode);

    /**
     * @param node {@link org.objectweb.asm.tree.MethodNode} {@link org.objectweb.asm.tree.MethodInsnNode} {@link org.objectweb.asm.tree.FieldNode} {@link org.objectweb.asm.tree.FieldInsnNode}
     */
    public void remap(final Object node) {
        if (!(node instanceof MethodNode) && !(node instanceof MethodInsnNode) && !(node instanceof FieldNode) && !(node instanceof FieldInsnNode)) return;
        if (node instanceof MethodNode) method((MethodNode)node);
        if (node instanceof MethodInsnNode) methodInsn((MethodInsnNode)node);
        if (node instanceof FieldNode) field((FieldNode)node);
        if (node instanceof FieldInsnNode) fieldInsn((FieldInsnNode)node);
    }

}
