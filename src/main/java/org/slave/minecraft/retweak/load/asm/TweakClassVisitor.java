package org.slave.minecraft.retweak.load.asm;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.MigrationClass;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * Created by Master on 7/12/2018 at 8:29 PM.
 *
 * @author Master
 */
public final class TweakClassVisitor extends ClassVisitor {

    private static final EnumMap<GameVersion, TweakClassVisitor> INSTANCES = Maps.newEnumMap(GameVersion.class);

    private final GameVersion gameVersion;

    private TweakClassVisitor(final GameVersion gameVersion) {
        super(Opcodes.ASM5);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        String newSignature = signature;
        String newSuperName = superName;
        String[] newInterfaces = interfaces != null ? Arrays.copyOf(interfaces, interfaces.length) : null;

        TweakClass tweakClass = gameVersion.getTweakClass();

        //Super name
        if (tweakClass.getClassesToMigrate().containsKey(superName)) {
            MigrationClass migrationClass = tweakClass.getClassesToMigrate().get(superName);
            newSuperName = migrationClass.getTo().getName().replace('.', '/');

            if (ReTweak.DEBUG) {
                ReTweak.LOGGER_RETWEAK.debug(
                        "Tweaked super name of class \"{}\" from \"{}\" to \"{}\"",
                        name,

                        superName,
                        newSuperName
                );
            }
        }

        //Interfaces
        if (!ArrayHelper.isNullOrEmpty(interfaces)) {
            for(int i = 0; i < interfaces.length; ++i) {
                String _interface = interfaces[i];
                String newInterface = null;

                if (tweakClass.getClassesToMigrate().containsKey(_interface)) {
                    MigrationClass migrationClass = tweakClass.getClassesToMigrate().get(_interface);
                    newInterface = migrationClass.getTo().getName().replace('.', '/');
                }

                if (newInterface != null) newInterfaces[i] = newInterface;
            }

            if (!Arrays.equals(interfaces, newInterfaces)) {
                if (ReTweak.DEBUG) {
                    ReTweak.LOGGER_RETWEAK.debug(
                            "Tweaked interfaces of class \"{}\" from \"[ {} ]\" to \"[ {} ]\"",
                            name,

                            Joiner.on(", ").join(interfaces),
                            Joiner.on(", ").join(newInterfaces)
                    );
                }
            }
        }

        super.visit(version, access, name, newSignature, newSuperName, newInterfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        return new TweakAnnotationVisitor(
                super.api,
                gameVersion,
                super.visitAnnotation(desc, visible)
        );
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        return new TweakFieldVisitor(
                super.api,
                gameVersion,
                super.visitField(access, name, desc, signature, value)
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        return new TweakMethodVisitor(
                super.api,
                gameVersion,
                super.visitMethod(access, name, desc, signature, exceptions)
        );
    }

    static TweakClassVisitor getInstance(final GameVersion gameVersion) {
        if (gameVersion == null) return null;
        if (!TweakClassVisitor.INSTANCES.containsKey(gameVersion)) {
            TweakClassVisitor.INSTANCES.put(
                    gameVersion,
                    new TweakClassVisitor(gameVersion)
            );
        }
        return TweakClassVisitor.INSTANCES.get(gameVersion);
    }

}
