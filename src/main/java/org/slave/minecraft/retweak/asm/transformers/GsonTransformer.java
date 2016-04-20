package org.slave.minecraft.retweak.asm.transformers;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

/**
 * Created by Master801 on 4/20/2016 at 2:53 PM.
 *
 * @author Master801
 */
public final class GsonTransformer extends BasicTransformer {

    public GsonTransformer() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        MethodNode constructor = null, newJsonWriter = null;
        for(MethodNode m : classNode.methods) {
            if (constructor == null && m.name.equals("<init>") && m.desc.equals("(Lcom/google/gson/internal/Excluder;Lcom/google/gson/FieldNamingStrategy;Ljava/util/Map;ZZZZZZLcom/google/gson/LongSerializationPolicy;Ljava/util/List;)V")) {
                constructor = m;
            }
            if (newJsonWriter == null && m.name.equals("newJsonWriter") && m.desc.equals("(Ljava/io/Writer;)Lcom/google/gson/stream/JsonWriter;")) {
                newJsonWriter = m;
            }
        }
        if (constructor != null && newJsonWriter != null) {
            final String fieldName = "indent";
            final String methodName_0 = "getIndent", methodName_1 = "setIndent";
            MethodVisitor methodVisitor;
            InsnList injectionList = new InsnList();



            //Create field
            classNode.visitField(
                    0,
                    fieldName,
                    "Ljava/lang/String;",
                    null,
                    null
            ).visitEnd();

            //Create getter
            methodVisitor = classNode.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    methodName_0,
                    "()Ljava/lang/String;",
                    null,
                    null
            );
            methodVisitor.visitLabel(new Label());
            methodVisitor.visitVarInsn(
                    Opcodes.ALOAD,
                    0
            );
            methodVisitor.visitFieldInsn(
                    Opcodes.GETFIELD,
                    getClassName(false).replace('.', '/'),
                    fieldName,
                    "Ljava/lang/String;"
            );
            newJsonWriter.visitInsn(Opcodes.ARETURN);
            methodVisitor.visitEnd();

            //Create setter
            methodVisitor = classNode.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    methodName_1,
                    "(Ljava/lang/String;)V",
                    null,
                    null
            );
            methodVisitor.visitLabel(new Label());
            methodVisitor.visitVarInsn(
                    Opcodes.ALOAD,
                    0
            );
            methodVisitor.visitVarInsn(
                    Opcodes.ALOAD,
                    1
            );
            methodVisitor.visitFieldInsn(
                    Opcodes.PUTFIELD,
                    getClassName(false).replace('.', '/'),
                    fieldName,
                    "(Ljava/lang/String;)V"
            );
            methodVisitor.visitEnd();

            //Set default value for field
            injectionList.add(new LabelNode(new Label()));
            injectionList.add(new VarInsnNode(
                    Opcodes.ALOAD,
                    0
            ));
            injectionList.add(new LdcInsnNode("  "));
            injectionList.add(new FieldInsnNode(
                    Opcodes.PUTFIELD,
                    getClassName(false).replace('.', '/'),
                    fieldName,
                    "Ljava/lang/String;"
            ));



            for(int i = 0; i < constructor.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = constructor.instructions.get(i);
                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                    if (fieldInsnNode.getOpcode() == Opcodes.PUTFIELD && fieldInsnNode.name.equals("prettyPrinting") && fieldInsnNode.desc.equals("Z")) {
                        constructor.instructions.insert(
                                fieldInsnNode,
                                injectionList
                        );
                        break;
                    }
                }
            }

            for(int i = 0; i < newJsonWriter.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = newJsonWriter.instructions.get(i);
                if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && methodInsnNode.name.equals("setIndent") && methodInsnNode.desc.equals(
                            Type.getMethodDescriptor(
                                    Type.VOID_TYPE,
                                    Type.getType(String.class)
                            ))) {
                        newJsonWriter.instructions.remove(newJsonWriter.instructions.get(i - 1));
                        newJsonWriter.instructions.insertBefore(
                                methodInsnNode,
                                new MethodInsnNode(
                                        Opcodes.INVOKEVIRTUAL,
                                        "com/google/gson/Gson",
                                        "",
                                        "()Ljava/lang/String;",
                                        false
                                )
                        );
                        break;
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean writeClassFile() {
        return true;
    }

    @Override
    protected boolean writeASMFile() {
        return true;
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "com.google.gson.Gson";
    }

}
