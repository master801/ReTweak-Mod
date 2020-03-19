package org.slave.minecraft.retweak.load.asm.tweak.visitors;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.MigrationClass;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.Arrays;

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
        String newType;

        MigrationClass migrationClass = tweakClass.getMigrationClass(type);
        if (migrationClass != null) {
            newType = migrationClass.getTo().getName().replace('.', '/');
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Migrated method type insn from \"{}\" to \"{}\"", type, newType);
        } else {
            newType = type;
        }

        super.visitTypeInsn(opcode, newType);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        String newDesc;

        Type descType = Type.getType(desc);
        if (tweakClass.hasMigrationClass(descType.getClassName(), false)) {
            Type newDescType = Type.getType(tweakClass.getMigrationClass(descType.getClassName()).getTo());
            newDesc = newDescType.getDescriptor();
        } else {
            newDesc = desc;
        }

        if (!newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method annotation desc from \"{}\" to \"{}\"", desc, newDesc);
        }

        return super.visitAnnotation(newDesc, visible);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        String newOwner = owner, newName = name, newDesc = desc;
        Type typeNewDesc = null;

        //<editor-fold desc="Owner">
        MigrationClass ownerMigrationClass = tweakClass.getMigrationClass(owner);
        if (ownerMigrationClass != null) newOwner = ownerMigrationClass.getTo().getName().replace('.', '/');
        //</editor-fold>

        if (ownerMigrationClass != null) {
            MigrationField migrationField = ownerMigrationClass.getField(name, desc);
            if (migrationField != null) {
                super.visitFieldInsn(
                        opcode,
                        newOwner,
                        migrationField.getDeobfuscatedName(),
                        migrationField.getToDescType().getDescriptor()
                );
                return;
            }
        }

        MigrationField migrationField = tweakClass.getMigrationField(name, desc);
        if (migrationField != null) newName = migrationField.getDeobfuscatedName();

        //<editor-fold desc="Desc">
        Type descType = Type.getType(desc);

        MigrationClass descMigrationClass;
        if (descType.getSort() == Type.ARRAY) {
            descMigrationClass = tweakClass.getMigrationClass(descType.getElementType().getClassName());
        } else {
            descMigrationClass = tweakClass.getMigrationClass(descType.getClassName());
        }

        if (descMigrationClass != null) {
            if (descType.getSort() == Type.ARRAY) {
                String[] array = new String[descType.getDimensions()];
                Arrays.fill(array, "[");

                typeNewDesc = Type.getType(Joiner.on("").join(array) + Type.getDescriptor(descMigrationClass.getTo()));
            } else {
                typeNewDesc = Type.getType(descMigrationClass.getTo());
            }
        } else if (migrationField != null) {
            typeNewDesc = migrationField.getToDescType();
        }
        //</editor-fold>

        if (typeNewDesc != null) newDesc = typeNewDesc.getDescriptor();

        if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Migrated field insn from \"{} {}{}\" to \"{} {}{}\"", owner, name, desc, newOwner, newName, newDesc);
        }

        super.visitFieldInsn(opcode, newOwner, newName, newDesc);
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        MigrationMethod methodEntry = tweakClass.getMigrationMethod(owner, name, desc);

        String newOwner;
        String newName;
        String newDesc;

        //<editor-fold desc="Owner">
        MigrationClass ownerMigrationClass = tweakClass.getMigrationClass(owner);
        if (ownerMigrationClass != null) {
            newOwner = ownerMigrationClass.getTo().getName().replace('.', '/');
        } else {
            newOwner = owner;
        }
        //</editor-fold>

        if (methodEntry != null) {//Make sure to tweak owner first
            String deobfuscatedName = methodEntry.getDeobfuscatedName();
            Type deobfuscatedReturnType = methodEntry.getDeobfuscatedReturnTypeDesc();
            Type[] deobfuscatedArgumentTypes = methodEntry.getDeobfuscatedArgumentDescTypes();
            super.visitMethodInsn(opcode, newOwner, deobfuscatedName, Type.getMethodDescriptor(deobfuscatedReturnType, deobfuscatedArgumentTypes), itf);
            return;
        }

        MigrationMethod migrationMethod = tweakClass.getMigrationMethod(owner, name, desc);
        if (migrationMethod != null) {
            newName = migrationMethod.getDeobfuscatedName();
        } else {
            newName = name;
        }

        //<editor-fold desc="Desc">
        //<editor-fold desc="Arguments">
        Type[] argumentTypes = Type.getArgumentTypes(desc);
        Type returnType = Type.getReturnType(desc);

        Type[] newArgumentTypes = new Type[argumentTypes.length];
        Type newReturnType;

        for(int i = 0; i < argumentTypes.length; i++) {
            Type argumentType = argumentTypes[i];

            MigrationClass migrationClass;
            if (argumentType.getSort() == Type.ARRAY) {
                migrationClass = tweakClass.getMigrationClass(argumentType.getElementType().getClassName());
            } else {
                migrationClass = tweakClass.getMigrationClass(argumentType.getClassName());
            }

            if (migrationClass != null) {
                if (argumentType.getSort() == Type.ARRAY) {
                    String[] arrays = new String[argumentType.getDimensions()];
                    Arrays.fill(arrays, "[");
                    newArgumentTypes[i] = Type.getType(Joiner.on("").join(arrays) + Type.getDescriptor(migrationClass.getTo()));
                } else {
                    newArgumentTypes[i] = Type.getType(migrationClass.getTo());
                }
            } else {
                newArgumentTypes[i] = argumentType;
            }
        }
        //</editor-fold>

        //<editor-fold desc="Return">
        MigrationClass returnTypeMigrationClass;
        if (returnType.getSort() == Type.ARRAY) {
            returnTypeMigrationClass = tweakClass.getMigrationClass(returnType.getElementType().getClassName());
        } else {
            returnTypeMigrationClass = tweakClass.getMigrationClass(returnType.getClassName());
        }

        if (returnTypeMigrationClass != null) {
            if (returnType.getSort() == Type.ARRAY) {
                String[] array = new String[returnType.getDimensions()];
                Arrays.fill(array, "[");
                newReturnType = Type.getType(Joiner.on("").join(array) + Type.getDescriptor(returnTypeMigrationClass.getTo()));
            } else {
                newReturnType = Type.getType(returnTypeMigrationClass.getTo());
            }
        } else {
            newReturnType = returnType;
        }
        //</editor-fold>

        if (migrationMethod != null) {
            newDesc = Type.getMethodDescriptor(migrationMethod.getDeobfuscatedReturnTypeDesc(), migrationMethod.getDeobfuscatedArgumentDescTypes());
        } else {
            newDesc = Type.getMethodDescriptor(newReturnType, newArgumentTypes);
        }
        //</editor-fold>

        if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(name)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method insn from \"{} {}{}\" to \"{} {}{}\"", owner, name, desc, newOwner, newName, newDesc);
        }

        super.visitMethodInsn(opcode, newOwner, newName, newDesc, itf);
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        String newName = name, newDesc;

        Type descType = Type.getType(desc);
        MigrationClass descMigrationClass = tweakClass.getMigrationClass(descType.getClassName());
        if (descMigrationClass != null) {
            newDesc = Type.getType(descMigrationClass.getTo()).getDescriptor();
        } else {
            newDesc = desc;
        }

        if (!desc.equals(newDesc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked local variable from \"{} {}\" to \"{} {}\"", name, desc, newName, newDesc);
        }

        super.visitLocalVariable(newName, newDesc, signature, start, end, index);
    }



}
