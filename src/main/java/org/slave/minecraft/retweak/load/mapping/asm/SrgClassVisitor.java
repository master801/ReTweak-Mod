package org.slave.minecraft.retweak.load.mapping.asm;

import com.google.common.base.Joiner;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.lib.resources.Obfuscation;
import org.slave.lib.util.WrappingDataT;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mapping.SpiderClass;
import org.slave.minecraft.retweak.load.mapping.SpiderMethod;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.Arrays;

/**
 * Created by Master on 3/16/2020 at 7:54 PM
 *
 * @author Master
 */
public final class SrgClassVisitor extends ClassVisitor {

    private final GameVersion gameVersion;

    private String newClassName;
    private String newSuperName;
    private String[] newInterfaces;

    public SrgClassVisitor(final int api, final ClassVisitor cv, final GameVersion gameVersion) {
        super(api, cv);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        newClassName = name;
        newSuperName = superName;
        newInterfaces = new String[interfaces.length];
        System.arraycopy(interfaces, 0, newInterfaces, 0, interfaces.length);

        SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, superName);
        if (spiderClass != null) newSuperName = spiderClass.getName().getName(Obfuscation.DEOBFUSCATED);

        for(int i = 0; i < interfaces.length; ++i) {
            String _interface = interfaces[i];
            SpiderClass spiderClassInterface = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, _interface);
            if (spiderClassInterface != null) newInterfaces[i] = spiderClassInterface.getName().getName(Obfuscation.DEOBFUSCATED);
        }

        if (ReTweak.DEBUG) {
            if (!newSuperName.equals(superName)) ReTweak.LOGGER_RETWEAK.info("Deobfuscated super name of class \"{}\", from \"{}\" to \"{}\"", newClassName, superName, newSuperName);
            if (!Arrays.equals(interfaces, newInterfaces)) ReTweak.LOGGER_RETWEAK.info("Deobfuscated interfaces of class \"{}\", from \"{}\" to \"{}\"", newClassName, Joiner.on(", ").join(interfaces), Joiner.on(", ").join(newInterfaces));
        }

        super.visit(version, access, newClassName, signature, newSuperName, newInterfaces);
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
            SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeDesc.getElementType().getClassName().replace('.', '/'));
            if (spiderClass != null) {
                String[] dimensions = new String[typeDesc.getDimensions()];
                Arrays.fill(dimensions, "[");
                newTypeDesc = Type.getType(Joiner.on("").join(dimensions) + "L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
            }
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

        WrappingDataT.WrappingDataT2<SpiderClass, SpiderMethod> spiderMethodData = gameVersion.getSrgMap().getSpiderClassMethod(Obfuscation.OBFUSCATED, newClassName, newInterfaces, name, desc);
        if (!WrappingDataT.isNull(spiderMethodData)) {
            newName = spiderMethodData.getObject2().getName().getName(Obfuscation.DEOBFUSCATED);
            newDesc = spiderMethodData.getObject2().getDesc().getDesc(Obfuscation.DEOBFUSCATED);
        }

        Type[] typeArguments = Type.getArgumentTypes(newDesc), typeNewArguments = new Type[typeArguments.length];
        Type typeReturn = Type.getReturnType(newDesc), typeNewReturn = typeReturn;
        System.arraycopy(typeArguments, 0, typeNewArguments, 0, typeArguments.length);

        for(int i = 0; i < typeArguments.length; ++i) {
            Type typeArgument = typeArguments[i];
            if (typeArgument.getSort() == Type.ARRAY) {
                SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeArgument.getElementType().getClassName().replace('.', '/'));
                if (spiderClass != null) {
                    String[] dimensions = new String[typeArgument.getDimensions()];
                    Arrays.fill(dimensions, "[");
                    typeNewArguments[i] = Type.getType(Joiner.on("").join(dimensions) + "L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
                }
            } else if (typeArgument.getSort() == Type.OBJECT) {
                SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeArgument.getClassName().replace('.', '/'));
                if (spiderClass != null) typeNewArguments[i] = Type.getType("L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
            }
        }

        if (typeReturn.getSort() == Type.ARRAY) {
            SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeReturn.getElementType().getClassName().replace('.', '/'));
            if (spiderClass != null) {
                String[] dimensions = new String[typeReturn.getDimensions()];
                Arrays.fill(dimensions, "[");
                typeNewReturn = Type.getType(Joiner.on("").join(dimensions) + "L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
            }
        } else if (typeReturn.getSort() == Type.OBJECT) {
            SpiderClass spiderClassTypeReturn = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeReturn.getClassName().replace('.', '/'));
            if (spiderClassTypeReturn != null) {//Obfuscated
                typeNewReturn = Type.getType("L" + spiderClassTypeReturn.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
            }
        }

        newDesc = Type.getMethodDescriptor(typeNewReturn, typeNewArguments);

        if (!newName.equals(name) || !newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Deobfuscated method from \"{} {}\" to \"{} {}\"", name, desc, newName, newDesc);
        }

        return new SrgMethodVisitor(
                super.api,
                super.visitMethod(access, newName, newDesc, newSignature, exceptions),
                gameVersion
        );
    }

}
