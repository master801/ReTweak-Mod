package org.slave.minecraft.retweak.loading.tweaks.compilation;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings.Mapping;
import org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings.Mappings;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.Iterator;

/**
 * Created by Master on 4/27/2016 at 7:26 AM.
 *
 * @author Master
 */
public final class JITTweak implements Tweak {

    private final GameVersion gameVersion;

    public JITTweak(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    @Override
    public String getName() {
        return "JIT";
    }

    @Override
    public void tweak(final ClassNode classNode) {
        if (gameVersion == null) return;
        if (classNode.methods != null) {
            Iterator<MethodNode> methodNodeIterator = classNode.methods.iterator();
            while(methodNodeIterator.hasNext()) {
                MethodNode methodNode = methodNodeIterator.next();
                if (remap(methodNode)) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removing method \"{}{}\" from class \"{}\"",
                                methodNode.name,
                                methodNode.desc,
                                classNode.name
                        );
                        methodNodeIterator.remove();
                    }
                }
            }
        }
        if (classNode.fields != null) {
            Iterator<FieldNode> fieldNodeIterator = classNode.fields.iterator();
            while(fieldNodeIterator.hasNext()) {
                FieldNode fieldNode = fieldNodeIterator.next();
                if (remap(fieldNode)) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removing field \"{} {}\" from class \"{}\"",
                                fieldNode.name,
                                fieldNode.desc,
                                classNode.name
                        );
                    }
                    fieldNodeIterator.remove();
                }
            }
        }
        remap(classNode);
    }

    @Override
    public int getSortIndex() {
        return 1;
    }

    private boolean remap(Object node) {
        Mapping mapping = Mappings.INSTANCE.getMapping(gameVersion);
        if (mapping == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Found no mapping for version {}!",
                    gameVersion.getVersion()
            );
            return false;
        }
        if (mapping.remap(node)) return true;

        //Remap method instructions
        if (node instanceof MethodNode) {
            MethodNode methodNode = (MethodNode)node;
            Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();

            int index = 0;
            while(abstractInsnNodeIterator.hasNext()) {
                AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();
                if (mapping.remap(abstractInsnNode)) {
                    if (ReTweakResources.DEBUG) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removing abstract insn node \"{}\" at index {}, in method \"{}{}\"",
                                abstractInsnNode,
                                index,
                                methodNode.name,
                                methodNode.desc
                        );
                    }
                    abstractInsnNodeIterator.remove();
                }
                index++;
            }
        }
        return false;
    }

}
