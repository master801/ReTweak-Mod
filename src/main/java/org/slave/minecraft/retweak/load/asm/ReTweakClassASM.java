package org.slave.minecraft.retweak.load.asm;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

/**
 * Created by Master on 7/12/2018 at 12:14 PM.
 *
 * @author Master
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReTweakClassASM {

    private static final EnumMap<GameVersion, ReTweakClassASM> INSTANCES = Maps.newEnumMap(GameVersion.class);

    private final GameVersion gameVersion;

    public byte[] build(final InputStream inputStream) {
        ClassReader classReader = null;
        try {
            classReader = new ClassReader(inputStream);
        } catch (IOException ignored) {
        }

        if (classReader != null) {
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, 0);

            TweakClassVisitor tweakClassVisitor = TweakClassVisitor.getInstance(gameVersion);
            classNode.accept(tweakClassVisitor);

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return null;
    }

    public static ReTweakClassASM instance(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        if (!ReTweakClassASM.INSTANCES.containsKey(gameVersion)) {
            ReTweakClassASM.INSTANCES.put(
                    gameVersion,
                    new ReTweakClassASM(gameVersion)
            );
        }
        return ReTweakClassASM.INSTANCES.get(gameVersion);
    }

}
