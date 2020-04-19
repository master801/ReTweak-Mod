package org.slave.minecraft.retweak.load.asm.tweak.visitors;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 7/12/2018 at 8:35 PM.
 *
 * @author Master
 */
public final class TweakMethodVisitor extends MethodVisitor {

    private final GameVersion gameVersion;
    private final TweakClass tweakClass;

    TweakMethodVisitor(final int api, final MethodVisitor mv, final GameVersion gameVersion) {
        super(api, mv);
        this.gameVersion = gameVersion;
        this.tweakClass = gameVersion.getTweakClass();
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        String newType = type;

        if (!newType.equals(type)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Migrated method type insn from \"{}\" to \"{}\"", type, newType);
        }

        super.visitTypeInsn(opcode, newType);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        String newDesc = desc;

        if (!newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method annotation desc from \"{}\" to \"{}\"", desc, newDesc);
        }

        return super.visitAnnotation(newDesc, visible);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        String newOwner = owner, newName = name, newDesc = desc;

        if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Migrated field insn from \"{} {}{}\" to \"{} {}{}\"", owner, name, desc, newOwner, newName, newDesc);
        }

        super.visitFieldInsn(opcode, newOwner, newName, newDesc);
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        String newOwner = owner;
        String newName = name;
        String newDesc = desc;

        if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(name)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method insn from \"{} {}{}\" to \"{} {}{}\"", owner, name, desc, newOwner, newName, newDesc);
        }

        super.visitMethodInsn(opcode, newOwner, newName, newDesc, itf);
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        String newName = name, newDesc = desc;

        if (!desc.equals(newDesc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked local variable from \"{} {}\" to \"{} {}\"", name, desc, newName, newDesc);
        }

        super.visitLocalVariable(newName, newDesc, signature, start, end, index);
    }



}
