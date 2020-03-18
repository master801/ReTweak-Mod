package org.slave.minecraft.retweak.load.mapping.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.lib.resources.Obfuscation;
import org.slave.lib.util.WrappingDataT;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mapping.SpiderClass;
import org.slave.minecraft.retweak.load.mapping.SpiderField;
import org.slave.minecraft.retweak.load.mapping.SpiderMethod;
import org.slave.minecraft.retweak.load.util.GameVersion;

/**
 * Created by Master on 3/17/2020 at 9:56 AM
 *
 * @author Master
 */
public final class SrgMethodVisitor extends MethodVisitor {

    private final GameVersion gameVersion;

    public SrgMethodVisitor(final int api, final MethodVisitor mv, final GameVersion gameVersion) {
        super(api, mv);
        this.gameVersion = gameVersion;
    }

    @Override
    public void visitInsn(final int opcode) {
        super.visitInsn(opcode);
    }

    @Override
    public void visitIntInsn(final int opcode, final int operand) {
        super.visitIntInsn(opcode, operand);
    }

    @Override
    public void visitVarInsn(final int opcode, final int var) {
        super.visitVarInsn(opcode, var);
    }

    @Override
    public void visitTypeInsn(final int opcode, final String type) {
        String newType = type;

        SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, type);
        if (spiderClass != null) {
            newType = spiderClass.getName().getName(Obfuscation.DEOBFUSCATED);
        }

        if (ReTweak.DEBUG && !newType.equals(type)) ReTweak.LOGGER_RETWEAK.info("Deobfuscated type insn from \"{}\" to \"{}\"", type, newType);
        super.visitTypeInsn(opcode, newType);
    }

    @Override
    public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
        String newOwner = owner, newName = name, newDesc = desc;

        WrappingDataT.WrappingDataT2<SpiderClass, SpiderField> spiderFieldData = gameVersion.getSrgMap().getSpiderClassField(Obfuscation.OBFUSCATED, owner, name, desc);
        if (!WrappingDataT.isNull(spiderFieldData)) {
            if (owner.equals(spiderFieldData.getObject1().getName().getName(Obfuscation.OBFUSCATED))) {
                newOwner = spiderFieldData.getObject1().getName().getName(Obfuscation.DEOBFUSCATED);
            }
            newName = spiderFieldData.getObject2().getName().getName(Obfuscation.DEOBFUSCATED);
            if (spiderFieldData.getObject2().getDesc() != null) newDesc = spiderFieldData.getObject2().getDesc().getDesc(Obfuscation.DEOBFUSCATED);
        }

        Type descType = Type.getType(desc);
        if (descType.getSort() == Type.ARRAY) {
            ReTweak.LOGGER_RETWEAK.debug("ARRAY NOT SUPPORTED CURRENTLY");
        } else if (descType.getSort() == Type.OBJECT) {
            String className = descType.getClassName().replace('.', '/');
            SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, className);
            if (spiderClass != null) newDesc = "L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";";
        }

        if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Deobfuscated field insn from \"{}/{} {}\" to \"{}/{} {}\"", owner, name, desc, newOwner, newName, newDesc);
        }

        super.visitFieldInsn(opcode, newOwner, newName, newDesc);
    }

    @Override
    public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
        String newOwner = owner, newName = name, newDesc = desc;

        WrappingDataT.WrappingDataT2<SpiderClass, SpiderMethod> spiderMethodData = gameVersion.getSrgMap().getSpiderClassMethod(Obfuscation.OBFUSCATED, owner, name, desc);
        if (!WrappingDataT.isNull(spiderMethodData)) {
            SpiderClass spiderClassOwner = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, owner);
            if (spiderClassOwner != null) newOwner = spiderClassOwner.getName().getName(Obfuscation.DEOBFUSCATED);
            newName = spiderMethodData.getObject2().getName().getName(Obfuscation.DEOBFUSCATED);
            newDesc = spiderMethodData.getObject2().getDesc().getDesc(Obfuscation.DEOBFUSCATED);
        }

        SpiderClass spiderClassOwner = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, owner);
        if (spiderClassOwner != null) newOwner = spiderClassOwner.getName().getName(Obfuscation.DEOBFUSCATED);

        Type[] typeArguments = Type.getArgumentTypes(newDesc);
        Type typeReturn = Type.getReturnType(newDesc);

        Type[] newTypeArguments = new Type[typeArguments.length];
        Type newTypeReturn = typeReturn;

        System.arraycopy(typeArguments, 0, newTypeArguments, 0, typeArguments.length);

        for(int i = 0; i < typeArguments.length; ++i) {
            Type typeArgument = typeArguments[i];
            if (typeArgument.getSort() == Type.ARRAY) {
                ReTweak.LOGGER_RETWEAK.debug("ARRAY NOT SUPPORTED CURRENTLY");
            } else if (typeArgument.getSort() == Type.OBJECT) {
                SpiderClass spiderClass = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeArgument.getClassName().replace('.', '/'));
                if (spiderClass != null) {//Obfuscated
                    newTypeArguments[i] = Type.getType("L" + spiderClass.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
                }
            }
        }

        if (typeReturn.getSort() == Type.ARRAY) {
            ReTweak.LOGGER_RETWEAK.debug("ARRAY NOT SUPPORTED CURRENTLY");
        } else if (typeReturn.getSort() == Type.OBJECT) {
            SpiderClass spiderClassTypeReturn = gameVersion.getSrgMap().getSpiderClass(Obfuscation.OBFUSCATED, typeReturn.getClassName().replace('.', '/'));
            if (spiderClassTypeReturn != null) {//Obfuscated
                newTypeReturn = Type.getType("L" + spiderClassTypeReturn.getName().getName(Obfuscation.DEOBFUSCATED) + ";");
            }
        }

        newDesc = Type.getMethodDescriptor(newTypeReturn, newTypeArguments);

        if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(desc)) {
            if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Deobfuscated method insn from \"{}/{} {}\" to \"{}/{} {}\"", owner, name, desc, newOwner, newName, newDesc);
        }

        super.visitMethodInsn(opcode, newOwner, newName, newDesc, itf);
    }

    @Override
    public void visitLdcInsn(final Object cst) {
        super.visitLdcInsn(cst);
    }

    @Override
    public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
        super.visitLocalVariable(name, desc, signature, start, end, index);
    }

}
