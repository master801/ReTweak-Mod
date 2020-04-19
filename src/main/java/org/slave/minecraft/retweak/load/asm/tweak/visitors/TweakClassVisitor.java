package org.slave.minecraft.retweak.load.asm.tweak.visitors;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.Tweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.Arrays;

/**
 * Created by Master on 7/12/2018 at 8:29 PM.
 *
 * @author Master
 */
public final class TweakClassVisitor extends ClassVisitor {

    private final ClassNode classNode;
    private final GameVersion gameVersion;
    private final TweakClass tweakClass;

    public TweakClassVisitor(final int api, final ClassNode classNode, final GameVersion gameVersion) {
        super(api, classNode);
        this.classNode = classNode;
        this.gameVersion = gameVersion;
        tweakClass = gameVersion.getTweakClass();
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        int newVersion = version, newAccess = access;
        String newName = name, newSignature = signature, newSuperName = superName;
        String[] newInterfaces = interfaces != null ? Arrays.copyOf(interfaces, interfaces.length) : null;

        if (ReTweak.DEBUG) {
            if (newVersion != version || newAccess != access || !newName.equals(name) || !newSignature.equals(signature) || !newSuperName.equals(superName) || !Arrays.deepEquals(interfaces, newInterfaces)) {
                ReTweak.LOGGER_RETWEAK.info("Tweaked class from \"{} {} [ {} ]\" to \"{} {} [ {} ]\"", name, superName, interfaces != null ? Joiner.on(", ").join(interfaces) : "", name, newSuperName, newInterfaces != null ? Joiner.on(", ").join(newInterfaces) : "");
            }
        }

        super.visit(version, access, name, newSignature, newSuperName, newInterfaces);
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        String newName = name, newOuterName = outerName, newInnerName = innerName;
        int newAccess = access;

        if (ReTweak.DEBUG) {
            if (!newName.equals(name) || !newOuterName.equals(outerName) || !newInnerName.equals(innerName) || access != newAccess) {
                ReTweak.LOGGER_RETWEAK.info("Tweaked inner-class from \"{} {} {} {}\" to \"{} {} {} {}\"", name, outerName, innerName, access, newName, newOuterName, newInnerName, newAccess);
            }
        }

        super.visitInnerClass(newName, newOuterName, newInnerName, access);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        String newDesc = desc;

        return new TweakAnnotationVisitor(
                super.api,
                gameVersion,
                super.visitAnnotation(newDesc, visible)
        );
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        String newName = name, newDesc = desc;

        if (ReTweak.DEBUG) {
            if (!newName.equals(name) || !newDesc.equals(desc)) ReTweak.LOGGER_RETWEAK.info("Tweaked field from \"{} {}\" to \"{} {}\"", name, desc, newName, newDesc);
        }
        return new TweakFieldVisitor(
                super.api,
                gameVersion,
                super.visitField(access, newName, newDesc, signature, value)
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        String newName = name;
        String newDesc = desc;
        String newSignature = signature;

        if (ReTweak.DEBUG) {
            if (!newName.equals(name) || !newDesc.equals(desc) || !newSignature.equals(signature)) ReTweak.LOGGER_RETWEAK.info("Tweaked class method \"{} {}\" to \"{} {}\"", name, desc, newName, newDesc);
        }

        return Tweak.getSpecificTweaker(gameVersion).getMethodVisitor(
                super.api,
                new TweakMethodVisitor(
                        super.api,
                        super.visitMethod(access, newName, newDesc, signature, exceptions),
                        gameVersion
                ),
                null,
                newName,
                newDesc
        );
    }

}
