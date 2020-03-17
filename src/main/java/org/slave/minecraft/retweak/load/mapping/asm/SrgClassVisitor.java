package org.slave.minecraft.retweak.load.mapping.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 3/16/2020 at 7:54 PM
 *
 * @author Master
 */
public final class SrgClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

    public SrgClassVisitor(final int api, final ClassVisitor cv, final GameVersion gameVersion) {
        super(api, cv);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitSource(final String source, final String debug) {
        super.visitSource(source, debug);
    }

    @Override
    public void visitOuterClass(final String owner, final String name, final String desc) {
        super.visitOuterClass(owner, name, desc);
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        return new SrgFieldVisitor(
                super.api,
                super.visitField(access, name, desc, signature, value),
                gameVersion
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        return new SrgMethodVisitor(
                super.api,
                super.visitMethod(access, name, desc, signature, exceptions),
                gameVersion
        );
    }

}
