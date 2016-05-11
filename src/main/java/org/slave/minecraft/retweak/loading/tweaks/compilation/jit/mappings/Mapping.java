package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master on 4/27/2016 at 3:31 PM.
 *
 * @author Master
 */
public abstract class Mapping {

    public static final String ANNOTATION_PREINIT_DESC = "L" + "cpw/mods/fml/common/Mod/PreInit" + ";";
    public static final String ANNOTATION_INIT_DESC = "L" + "cpw/mods/fml/common/Mod/Init" + ";";
    public static final String ANNOTATION_POSTINIT_DESC = "L" + "cpw/mods/fml/common/Mod/PostInit" + ";";

    protected abstract void _class(final ClassNode classNode);

    protected abstract void method(final MethodNode methodNode);

    protected abstract void field(final FieldNode fieldNode);

    protected abstract void fieldInsn(final FieldInsnNode fieldInsnNode);

    protected abstract void methodInsn(final MethodInsnNode methodInsnNode);

    /**
     * @param node {@link org.objectweb.asm.tree.MethodNode} {@link org.objectweb.asm.tree.MethodInsnNode} {@link org.objectweb.asm.tree.FieldNode} {@link org.objectweb.asm.tree.FieldInsnNode}
     */
    public final void remap(final Object node) {
        if (!(node instanceof ClassNode) && !(node instanceof MethodNode) && !(node instanceof MethodInsnNode) && !(node instanceof FieldNode) && !(node instanceof FieldInsnNode)) return;
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING START");
        if (node instanceof ClassNode) _class((ClassNode)node);
        if (node instanceof MethodNode) method((MethodNode)node);
        if (node instanceof MethodInsnNode) methodInsn((MethodInsnNode)node);
        if (node instanceof FieldNode) field((FieldNode)node);
        if (node instanceof FieldInsnNode) fieldInsn((FieldInsnNode)node);
        if (ReTweakResources.DEBUG) ReTweakResources.RETWEAK_LOGGER.info("MAPPING END\n");
    }

}
