package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.minecraft.retweak.loading.ReTweakModContainer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master on 4/27/2016 at 3:31 PM.
 *
 * @author Master
 */
public abstract class Mapping {

    public static final String ANNOTATION_PREINIT_DESC = "L" + "cpw/mods/fml/common/Mod$PreInit" + ";";
    public static final String ANNOTATION_INIT_DESC = "L" + "cpw/mods/fml/common/Mod$Init" + ";";
    public static final String ANNOTATION_POSTINIT_DESC = "L" + "cpw/mods/fml/common/Mod$PostInit" + ";";
    public static final String ANNOTATION_NETWORKMOD_DESC = "L" + "cpw/mods/fml/common/network/NetworkMod" + ";";

    protected abstract boolean _class(final ReTweakModContainer reTweakModContainer, final ClassNode classNode);

    protected abstract boolean field(final ReTweakModContainer reTweakModContainer, final FieldNode fieldNode);

    protected abstract boolean method(final ReTweakModContainer reTweakModContainer, final MethodNode methodNode);

    protected abstract boolean fieldInsn(final ReTweakModContainer reTweakModContainer, final FieldInsnNode fieldInsnNode);

    protected abstract boolean methodInsn(final ReTweakModContainer reTweakModContainer, final MethodInsnNode methodInsnNode);

    /**
     * @param node {@link org.objectweb.asm.tree.MethodNode} {@link org.objectweb.asm.tree.MethodInsnNode} {@link org.objectweb.asm.tree.FieldNode} {@link org.objectweb.asm.tree.FieldInsnNode}
     */
    @SuppressWarnings("ConstantConditions")
    public final boolean remap(final ReTweakModContainer reTweakModContainer, final Object node) {
        if (!(node instanceof ClassNode) && !(node instanceof MethodNode) && !(node instanceof MethodInsnNode) && !(node instanceof FieldNode) && !(node instanceof FieldInsnNode)) return false;
        boolean remove;
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING START");
        if (node instanceof ClassNode) {
            remove = _class(
                    reTweakModContainer,
                    (ClassNode)node
            );
        } else if (node instanceof MethodNode) {
            remove = method(
                    reTweakModContainer,
                    (MethodNode)node
            );
        } else if (node instanceof MethodInsnNode) {
            remove = methodInsn(
                    reTweakModContainer,
                    (MethodInsnNode)node
            );
        } else if (node instanceof FieldNode) {
            remove = field(
                    reTweakModContainer,
                    (FieldNode)node
            );
        } else if (node instanceof FieldInsnNode) {
            remove = fieldInsn(
                    reTweakModContainer,
                    (FieldInsnNode)node
            );
        } else {
            return false;
        }
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING END\n");
        return remove;
    }

}
