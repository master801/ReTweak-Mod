package org.slave.minecraft.retweak.load.asm;

import com.google.common.collect.Maps;
import cpw.mods.fml.common.discovery.ModCandidate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakLoader;
import org.slave.minecraft.retweak.load.asm.deobfuscate.visitors.DeobfuscateClassVisitor;
import org.slave.minecraft.retweak.load.asm.tweak.visitors.TweakClassVisitor;
import org.slave.minecraft.retweak.load.mod.ReTweakModCandidate;
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

    public byte[] deobfuscate(final InputStream inputStream) {
        ClassReader classReader;
        try {
            classReader = new ClassReader(inputStream);
        } catch(IOException e) {
            ReTweak.LOGGER_RETWEAK.error("Failed to deobfuscate class! Could not be read!");
            return null;
        }

        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        classNode.accept(new DeobfuscateClassVisitor(Opcodes.ASM5, gameVersion));

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);

        return classWriter.toByteArray();
    }

    public byte[] tweak(final byte[] data) {
        ClassReader classReader = new ClassReader(data);
        ClassNode classNode = new ClassNode();
        ASMTable asmTable = getASMTable(classReader.getClassName());
        TweakClassVisitor tweakClassVisitor = new TweakClassVisitor(classNode, gameVersion, asmTable);
        classReader.accept(tweakClassVisitor, 0);

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }

    public ASMTable getASMTable(final String className) {
        ASMTable asmTable = null;
        for(ModCandidate modCandidate : ReTweakLoader.instance().getReTweakModDiscoverer(gameVersion).getCandidates()) {
            ReTweakModCandidate reTweakModCandidate = (ReTweakModCandidate)modCandidate;
            for(TableClass iteratingTableClass : reTweakModCandidate.getASMTable().getTableClasses()) {
                if (iteratingTableClass.getName().equals(className)) {
                    asmTable = reTweakModCandidate.getASMTable();
                    break;
                }
            }
        }
        return asmTable;
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
