package org.slave.minecraft.retweak.load.asm._super.visitors;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.slave.lib.resources.ASMTable;
import org.slave.minecraft.retweak.load.mapping._super.SuperMap;
import org.slave.minecraft.retweak.load.util.GameVersion;

import lombok.Getter;

/**
 * Created by master on 11/18/18 at 10:59 AM
 *
 * @author master
 */
public final class SuperClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;
    private final ASMTable asmTable;

    @Getter
    private final SuperMap superMap = new SuperMap();

    public SuperClassVisitor(final int api, final GameVersion gameVersion, final ASMTable asmTable) {
        super(api);
        this.gameVersion = gameVersion;
        this.asmTable = asmTable;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        return new SuperMethodVisitor(
                super.api,
                super.visitMethod(access, name, desc, signature, exceptions),
                superMap
        );
    }

}
