package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.lib.helpers.StringHelper;
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

    protected abstract boolean field(final String className, final FieldNode fieldNode);

    protected abstract boolean method(final String className, final MethodNode methodNode);

    protected abstract void postMethodNode(final String className, final int index, final MethodNode methodNode);

    protected abstract boolean fieldInsn(final String className, final int index, final FieldInsnNode fieldInsnNode);

    protected abstract boolean methodInsn(final String className, final int index, final MethodInsnNode methodInsnNode);

    protected abstract boolean intInsn(final String className, final int index, final IntInsnNode intInsnNode);

    protected abstract boolean ldcInsn(final String className, final int index, final LdcInsnNode ldcInsnNode);

    @SuppressWarnings("ConstantConditions")
    public final boolean remap(final String className, final Object node, final int index) {
        if (StringHelper.isNullOrEmpty(className) || node == null) throw new NullPointerException();
        if (index < 0) throw new IndexOutOfBoundsException();
        if (node instanceof ClassNode || node instanceof MethodNode || node instanceof MethodInsnNode || node instanceof FieldNode || node instanceof FieldInsnNode || node instanceof IntInsnNode || node instanceof LdcInsnNode) {
            boolean remove = false;
            if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING START");
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
                remove = methodInsn(
                        className,
                        index,
                        (MethodInsnNode)node
                );
            } else if (node instanceof FieldNode) {
                remove = field(
                        className,
                        (FieldNode)node
                );
            } else if (node instanceof FieldInsnNode) {
                remove = fieldInsn(
                        className,
                        index,
                        (FieldInsnNode)node
                );
            } else if (node instanceof IntInsnNode) {
                remove = intInsn(
                        className,
                        index,
                        (IntInsnNode)node
                );
            } else if (node instanceof LdcInsnNode) {
                remove = ldcInsn(
                        className,
                        index,
                        (LdcInsnNode)node
                );
            }

            if (node instanceof MethodNode && !remove) {
                postMethodNode(
                        className,
                        index,
                        (MethodNode)node
                );
            }

            if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING END\n");
            return remove;
        }
        return false;
    }

}
