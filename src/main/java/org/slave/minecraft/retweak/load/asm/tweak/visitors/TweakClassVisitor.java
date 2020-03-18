package org.slave.minecraft.retweak.load.asm.tweak.visitors;

import com.google.common.base.Joiner;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.MigrationClass;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.TweakClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.Arrays;

/**
 * Created by Master on 7/12/2018 at 8:29 PM.
 *
 * @author Master
 */
public final class TweakClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;
    private final TweakClass tweakClass;

    public TweakClassVisitor(final int api, final ClassVisitor classVisitor, final GameVersion gameVersion) {
        super(api, classVisitor);
        this.gameVersion = gameVersion;
        tweakClass = gameVersion.getTweakClass();
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        String newSignature = signature;
        String newSuperName = superName;
        String[] newInterfaces = interfaces != null ? Arrays.copyOf(interfaces, interfaces.length) : null;

        //Super name
        if (tweakClass.hasMigrationClass(superName)) {
            MigrationClass migrationClass = tweakClass.getMigrationClass(superName);
            newSuperName = migrationClass.getTo().getName().replace('.', '/');

            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked super name of class \"{}\" from \"{}\" to \"{}\"", name, superName, newSuperName);
        }

        //Interfaces
        if (!ArrayHelper.isNullOrEmpty(interfaces)) {
            for(int i = 0; i < interfaces.length; ++i) {
                String _interface = interfaces[i];
                String newInterface = null;

                if (tweakClass.hasMigrationClass(_interface)) {
                    MigrationClass migrationClass = tweakClass.getMigrationClass(_interface);
                    newInterface = migrationClass.getTo().getName().replace('.', '/');
                }

                if (newInterface != null) newInterfaces[i] = newInterface;
            }

            if (!Arrays.equals(interfaces, newInterfaces)) {
                if (ReTweak.DEBUG) {
                    ReTweak.LOGGER_RETWEAK.info(
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
    public void visitInnerClass(final String name, final String outerName, final String innerName, final int access) {
        String newName;
        String newOuterName;
        String newInnerName;

        //Name
        if (tweakClass.hasMigrationClass(name)) {
            newName = tweakClass.getMigrationClass(name.replace('/', '.')).getTo().getName().replace('.', '/');
        } else {
            newName = name;
        }

        //Outer name
        if (outerName != null && tweakClass.hasMigrationClass(outerName)) {
            newOuterName = tweakClass.getMigrationClass(outerName).getTo().getName().replace('.', '/');
        } else {
            newOuterName = outerName;
        }

        //Inner name
        if (!newName.equals(name)) {
            if (newName.indexOf('$') != -1) {//Inner class
                newInnerName = newName.substring(newName.indexOf('$') + 1);
            } else {
                if (newName.lastIndexOf('/') != -1) {
                    newInnerName = newName.substring(newName.lastIndexOf('/') + 1);
                } else {
                    newInnerName = newName;
                }
            }

            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked inner-class from \"{}\" to \"{}\"", name, newName);
        } else {
            newInnerName = innerName;
        }

        super.visitInnerClass(newName, newOuterName, newInnerName, access);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String desc, final boolean visible) {
        String newDesc;

        Type descType = Type.getType(desc);
        String descName = descType.getClassName();
        if (tweakClass.hasMigrationClass(descName)) {
            MigrationClass migrationClass = tweakClass.getMigrationClass(descName);
            newDesc = Type.getType(migrationClass.getTo()).getDescriptor();
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked annotation desc from \"{}\" to \"{}\"", desc, newDesc);
        } else {
            newDesc = desc;
        }

        return new TweakAnnotationVisitor(
                super.api,
                gameVersion,
                super.visitAnnotation(newDesc, visible)
        );
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        Type descType = Type.getType(desc);
        Type newDescType;

        MigrationClass migrationClassDesc;
        if (descType.getSort() == Type.ARRAY) {
            migrationClassDesc = tweakClass.getMigrationClass(descType.getElementType().getClassName().replace('.', '/'));
        } else {
            migrationClassDesc = tweakClass.getMigrationClass(descType.getClassName().replace('.', '/'));
        }
        if (migrationClassDesc != null) {
            if (descType.getSort() == Type.ARRAY) {
                String[] array = new String[descType.getDimensions()];
                Arrays.fill(array, "[");
                newDescType = Type.getType(Joiner.on("").join(array) + Type.getDescriptor(migrationClassDesc.getTo()));
            } else {
                newDescType = Type.getType(migrationClassDesc.getTo());
            }
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked field desc from \"{}\" to \"{}\"", desc, newDescType);
        } else {
            newDescType = descType;
        }

        return new TweakFieldVisitor(
                super.api,
                gameVersion,
                super.visitField(access, name, newDescType.getDescriptor(), signature, value)
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        String newName = name;
        String newDesc = desc;
        String newSignature = signature;

        MigrationMethod migrationMethod = tweakClass.getMigrationMethod(name, desc);
        if (migrationMethod != null) {
            newName = migrationMethod.getDeobfuscatedName();
            newDesc = Type.getMethodDescriptor(
                    migrationMethod.getDeobfuscatedReturnTypeDesc(),
                    migrationMethod.getDeobfuscatedArgumentDescTypes()
            );

            if (ReTweak.DEBUG) {
                ReTweak.LOGGER_RETWEAK.info(
                        "Tweaked class method \"{} {}\" to \"{} {}\"",
                        name,
                        desc,

                        newName,
                        newDesc
                );
            }
        }

        return new TweakMethodVisitor(
                super.api,
                super.visitMethod(access, newName, newDesc, newSignature, exceptions),
                gameVersion
        );
    }

}
