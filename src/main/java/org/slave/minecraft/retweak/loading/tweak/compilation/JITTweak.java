package org.slave.minecraft.retweak.loading.tweak.compilation;

import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.loading.tweak.Tweak;

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
    public void tweak(final ClassNode classNode, final GameVersion gameVersion) {
        /*
        if (gameVersion == null) return;
        if (classNode.fields != null) {
            Iterator<FieldNode> fieldNodeIterator = classNode.fields.iterator();
            int index = 0;
            while(fieldNodeIterator.hasNext()) {
                FieldNode fieldNode = fieldNodeIterator.next();
                if (remap(classNode, index, null, fieldNode)) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removing field \"{} {}\" from class \"{}\"",
                                fieldNode.name,
                                fieldNode.desc,
                                classNode.name
                        );
                    }
                    fieldNodeIterator.remove();
                }
                index++;
            }
        }
        if (classNode.methods != null) {
            Iterator<MethodNode> methodNodeIterator = classNode.methods.iterator();
            int index = 0;
            while(methodNodeIterator.hasNext()) {
                MethodNode methodNode = methodNodeIterator.next();
                if (remap(classNode, index, methodNode.instructions, methodNode)) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removing method \"{}{}\" from class \"{}\"",
                                methodNode.name,
                                methodNode.desc,
                                classNode.name
                        );
                        methodNodeIterator.remove();
                    }
                }
                index++;
            }
        }
        remap(
                classNode,
                0,
                null,
                classNode
        );
        */
    }

    @Override
    public int getWantedSortIndex() {
        return 0;
    }

    /*
    private boolean remap(final ClassNode classNode, final int index, final InsnList insnList, final Object node) {
        Mapping mapping = Mappings.INSTANCE.getMapping(gameVersion);
        if (mapping == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Found no mapping for version {}!",
                    gameVersion.getVersion()
            );
            return false;
        }
        if (mapping.remap(classNode, insnList, node, index)) return true;

        //Remap method instructions
        if (node instanceof MethodNode) {
            MethodNode methodNode = (MethodNode)node;
            Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();

            int _index = 0;
            while(abstractInsnNodeIterator.hasNext()) {
                AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();
                if (mapping.remap(classNode, insnList, abstractInsnNode, index)) {
                    if (ReTweakResources.DEBUG_MESSAGES) {
                        ReTweakResources.RETWEAK_LOGGER.info(
                                "Removing abstract insn node \"{}\" at index {}, in method \"{}{}\", in class {}",
                                abstractInsnNode,
                                _index,
                                methodNode.name,
                                methodNode.desc,
                                classNode.name
                        );
                    }
                    abstractInsnNodeIterator.remove();
                }
                _index++;
            }
        }
        return false;
    }
    */

}