package org.slave.minecraft.retweak.load.asm.tweak.visitors;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.lib.resources.ASMTable;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.BuilderMigrationMethod.MigrationMethod;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.MigrationClassBuilder.MigrationClass;
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
    private final ASMTable asmTable;

    TweakMethodVisitor(final int api, final MethodVisitor mv, final GameVersion gameVersion, final ASMTable asmTable) {
        super(api, mv);
        this.gameVersion = gameVersion;
        this.tweakClass = gameVersion.getTweakClass();
        this.asmTable = asmTable;
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
        if (tweakClass.hasMigrationClass(descType.getClassName())) {
            Type newDescType = Type.getType(tweakClass.getMigrationClass(descType.getClassName()).getTo());
            newDesc = newDescType.getDescriptor();
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method annotation desc from \"{}\" to \"{}\"", desc, newDesc);
        } else {
            newDesc = desc;
        }

        return super.visitAnnotation(newDesc, visible);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        String newOwner;
        Type newDesc;

        //<editor-fold desc="Owner">
        MigrationClass ownerMigrationClass = tweakClass.getMigrationClass(owner);
        if (ownerMigrationClass != null) {
            newOwner = ownerMigrationClass.getTo().getName().replace('.', '/');
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Migrated field insn owner from \"{}\" to \"{}\"", owner, newOwner);
        } else {
            newOwner = owner;
        }
        //</editor-fold>

        if (ownerMigrationClass != null) {
            MigrationField migrationField = ownerMigrationClass.getField(name, desc);
            if (migrationField != null) {
                super.visitFieldInsn(opcode, newOwner, migrationField.getDeobfuscatedName(), migrationField.getToDescType().getDescriptor());
                return;
            }
        }

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

                newDesc = Type.getType(Joiner.on("").join(array) + Type.getDescriptor(descMigrationClass.getTo()));
            } else {
                newDesc = Type.getType(descMigrationClass.getTo());
            }
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Migrated field insn desc from \"{}\" to \"{}\"", desc, newDesc);
        } else {
            newDesc = Type.getType(desc);
        }
        //</editor-fold>

        super.visitFieldInsn(opcode, newOwner, name, newDesc.getDescriptor());
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        MigrationMethod methodEntry = tweakClass.getMigrationMethod(name, desc);

        String newOwner;
        String newDesc;

        //<editor-fold desc="Owner">
        MigrationClass ownerMigrationClass = tweakClass.getMigrationClass(owner);
        if (ownerMigrationClass != null) {
            newOwner = ownerMigrationClass.getTo().getName().replace('.', '/');
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method insn owner from \"{}\" to \"{}\"", owner, newOwner);
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
            String xx = returnType.getClassName();
            returnTypeMigrationClass = tweakClass.getMigrationClass(xx);
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

        newDesc = Type.getMethodDescriptor(newReturnType, newArgumentTypes);
        if (!desc.equals(newDesc) && ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method insn desc from \"{}\" to \"{}\"", desc, newDesc);
        //</editor-fold>

        super.visitMethodInsn(opcode, newOwner, name, newDesc, itf);
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        String newDesc;

        Type descType = Type.getType(desc);
        MigrationClass descMigrationClass = tweakClass.getMigrationClass(descType.getClassName());
        if (descMigrationClass != null) {
            newDesc = Type.getType(descMigrationClass.getTo()).getDescriptor();
        } else {
            newDesc = desc;
        }

        if (!desc.equals(newDesc) && ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked Local Variable desc from \"{}\" to \"{}\"", desc, newDesc);

        super.visitLocalVariable(name, newDesc, signature, start, end, index);
    }

}
