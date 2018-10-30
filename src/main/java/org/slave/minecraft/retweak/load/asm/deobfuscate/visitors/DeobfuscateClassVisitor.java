package org.slave.minecraft.retweak.load.asm.deobfuscate.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by master on 10/27/18 at 2:58 PM
 *
 * @author master
 */
public final class DeobfuscateClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

    public DeobfuscateClassVisitor(final int api, final GameVersion gameVersion) {
        super(api);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        FieldVisitor fv = super.visitField(access, name, desc, signature, value);
        return new DeobfuscateFieldVisitor(
                super.api,
                fv
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return new DeobfuscateMethodVisitor(
                super.api,
                mv
        );
    }

}
