package org.slave.minecraft.retweak.asm.transformers;

import cpw.mods.fml.common.LoaderState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.lib.helpers.ASMHelper;
import org.slave.minecraft.retweak.asm.ReTweakASM;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper.Injection;
import org.slave.minecraft.retweak.load.ReTweakLoader;

/**
 * Created by master on 2/25/18 at 9:33 PM
 *
 * @author master
 */
public final class TransformerLoader implements IClassTransformer  {

    private static final String CLASS_NAME = "cpw.mods.fml.common.Loader";

    public TransformerLoader() {
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (transformedName.equals(CLASS_NAME)) {
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            ClassVisitor cv = new LoaderClassVisitor(Opcodes.ASM5, classNode);
            classReader.accept(cv, 0);

            ClassWriter classWriter = new ClassWriter(0);
            classNode.accept(classWriter);

            byte[] data = classWriter.toByteArray();
            BasicTransformer.writeASMFile(classNode, name.replace('.', '/'));
            BasicTransformer.writeClassFile(name.replace('.', '/'), data);
            return data;
        }
        return basicClass;
    }

    @RequiredArgsConstructor
    private enum _Injection implements Injection {

        LOAD_MODS(
                new String[] {
                        "loadMods"
                },
                new String[] {
                        "()V"
                },
                MethodVisitorLoadMods.class
        );

        @Getter
        private final String[] name;

        @Getter
        private final String[] desc;

        @Getter
        private final Class<? extends MethodVisitor> methodVisitorClass;

    }

    private static class LoaderClassVisitor extends ClassVisitor {

        public LoaderClassVisitor(final int api, final ClassVisitor cv) {
            super(api, cv);
        }

        @Override
        public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
            return ReTweakASMHelper.visitMethod(
                    super.api,
                    super.visitMethod(
                            access,
                            name,
                            desc,
                            signature,
                            exceptions
                    ),
                    _Injection.values(),
                    ReTweakASM.LOGGER_RETWEAK_ASM,

                    access,
                    name,
                    desc,
                    signature,
                    exceptions
            );
        }

    }

    public static final class MethodVisitorLoadMods extends MethodVisitor {

        public MethodVisitorLoadMods(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitFieldInsn(opcode, owner, name, desc);

            if (owner.equals(CLASS_NAME.replace('.', '/'))) {
                switch (opcode) {
                    case Opcodes.PUTFIELD:
                        if (name.equals("discoverer") && desc.equals("Lcpw/mods/fml/common/discovery/ModDiscoverer;")) {
                            super.visitLabel(new Label());
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "identifyMods", "()V", false);
                        }
                        break;
                }
            }
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            super.visitMethodInsn(
                    opcode,
                    owner,
                    name,
                    desc,
                    itf
            );

            if (owner.equals(TransformerLoader.CLASS_NAME.replace('.', '/'))) {
                switch (opcode) {
                    case Opcodes.INVOKESPECIAL:
                        if (name.equals("initializeLoader") && desc.equals("()V")) {
                            super.visitLabel(new Label());
                            super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "initialize", "()V", false);
                            break;
                        }
                        break;
                }
            }
        }

    }

}
