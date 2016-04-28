package org.slave.minecraft.retweak.loading.tweaks.compilation;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.loading.mappings.Mapping;
import org.slave.minecraft.retweak.loading.mappings.Mappings;
import org.slave.minecraft.retweak.loading.tweaks.Tweak;
import org.slave.minecraft.retweak.resources.ReTweakResources;

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
            for(int i = 0; i < classNode.methods.size(); ++i) remap(classNode.methods.get(i));
        }
        if (classNode.fields != null) {
            for(int i = 0; i < classNode.fields.size(); ++i) remap(classNode.fields.get(i));
        }
    }

    private void remap(Object node) {
        Mapping mapping = Mappings.INSTANCE.getMapping(gameVersion);
        if (mapping == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Found no mapping for version {}!",
                    gameVersion.getVersion()
            );
            return;
        }
        mapping.remap(node);
        if (node instanceof MethodNode) {
            MethodNode methodNode = (MethodNode)node;
            for(int i = 0; i < methodNode.instructions.size(); ++i) mapping.remap(methodNode.instructions.get(i));
        }
    }

}
