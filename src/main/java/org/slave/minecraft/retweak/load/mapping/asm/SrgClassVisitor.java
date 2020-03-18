package org.slave.minecraft.retweak.load.mapping.asm;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.lib.resources.Obfuscation;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mapping.SpiderClass;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 3/16/2020 at 7:54 PM
 *
 * @author Master
 */
public final class SrgClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

    private String className;
    private String newSuperName;

    public SrgClassVisitor(final int api, final ClassVisitor cv, final GameVersion gameVersion) {
        super(api, cv);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        className = name;
        newSuperName = superName;
        String[] newInterfaces = new String[interfaces.length];
        System.arraycopy(interfaces, 0, newInterfaces, 0, interfaces.length);

        //TODO look at asm table first, before searching srg map
        SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, superName);
        if (spiderClass != null) {
            newSuperName = spiderClass.getName().getName(Obfuscation.DEOBFUSCATED);
            ReTweak.LOGGER_RETWEAK.info("Deobfuscated super name of class \"{}\", from \"{}\" to \"{}\"", name, superName, newSuperName);
        }

        super.visit(version, access, name, signature, newSuperName, newInterfaces);
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
        String newName = name, newDesc = desc;

        Type typeDesc = Type.getType(desc), newTypeDesc = typeDesc;
        if (typeDesc.getSort() == Type.ARRAY) {
            ReTweak.LOGGER_RETWEAK.debug("");
        } else if (typeDesc.getSort() == Type.OBJECT) {
            SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeDesc.getClassName().replace('.', '/'));
            if (spiderClass != null) {
                newTypeDesc = Type.getType("L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
            }
        }

        newDesc = newTypeDesc.getDescriptor();

        if (ReTweak.DEBUG) {
            if (!newName.equals(name) || !newDesc.equals(desc)) ReTweak.LOGGER_RETWEAK.info("Deobfuscated field from \"{} {}\" to \"{} {}\"", name, desc, newName, newDesc);
        }

        return new SrgFieldVisitor(
                super.api,
                super.visitField(access, newName, newDesc, signature, value),
                gameVersion
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        String newName = name, newDesc = desc, newSignature = signature;

        //TODO

        return new SrgMethodVisitor(
                super.api,
                super.visitMethod(access, newName, newDesc, newSignature, exceptions),
                gameVersion
        );
    }

}
