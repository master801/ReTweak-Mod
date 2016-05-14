package org.slave.minecraft.retweak.asm.transformers;

import com.google.gson.Gson;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
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
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Master801 on 4/20/2016 at 2:53 PM.
 *
 * @author Master801
 */
public final class GsonTransformer extends BasicTransformer implements IClassTransformer {

    private static final String FIELD_INDENT_NAME = "indent";
    private static final String METHOD_INDENT_SETTER_NAME = "setIndent", METHOD_INDENT_SETTER_DESC = "(Ljava/lang/String;)V";
    private static final String METHOD_INDENT_GETTER_NAME = "getIndent", METHOD_INDENT_GETTER_DESC = "()Ljava/lang/String;";

    public GsonTransformer() {
        super(ReTweakResources.RETWEAK_LOGGER);
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        MethodNode constructor = null, newJsonWriter = null;

        for(MethodNode methodNode : classNode.methods) {
            if (methodNode.name.equals("<init>") && methodNode.desc.equals("(Lcom/google/gson/internal/Excluder;Lcom/google/gson/FieldNamingStrategy;Ljava/util/Map;ZZZZZZLcom/google/gson/LongSerializationPolicy;Ljava/util/List;)V")) {
                constructor = methodNode;
            }
            if (methodNode.name.equals("newJsonWriter") && methodNode.desc.equals("(Ljava/io/Writer;)Lcom/google/gson/stream/JsonWriter;")) {
                newJsonWriter = methodNode;
            }

            if (constructor != null && newJsonWriter != null) break;
        }

        if (constructor != null && newJsonWriter != null) {
            classNode.visitField(
                    Opcodes.ACC_PRIVATE,
                    GsonTransformer.FIELD_INDENT_NAME,
                    "Ljava/lang/String;",
                    null,
                    null
            ).visitEnd();

            MethodVisitor methodVisitor;

            methodVisitor = classNode.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    GsonTransformer.METHOD_INDENT_GETTER_NAME,
                    GsonTransformer.METHOD_INDENT_GETTER_DESC,
                    null,
                    null
            );
            methodVisitor.visitCode();
            methodVisitor.visitLabel(new Label());
            methodVisitor.visitVarInsn(
                    Opcodes.ALOAD,
                    0
            );
            methodVisitor.visitFieldInsn(
                    Opcodes.GETFIELD,
                    "com/google/gson/Gson",
                    GsonTransformer.FIELD_INDENT_NAME,
                    "Ljava/lang/String;"
            );
            methodVisitor.visitInsn(Opcodes.ARETURN);
            methodVisitor.visitMaxs(
                    0,
                    0
            );
            methodVisitor.visitEnd();

            methodVisitor = classNode.visitMethod(
                    Opcodes.ACC_PUBLIC,
                    GsonTransformer.METHOD_INDENT_SETTER_NAME,
                    GsonTransformer.METHOD_INDENT_SETTER_DESC,
                    null,
                    null
            );
            methodVisitor.visitCode();
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
                    "com/google/gson/Gson",
                    GsonTransformer.FIELD_INDENT_NAME,
                    "Ljava/lang/String;"
            );
            methodVisitor.visitInsn(Opcodes.RETURN);
            methodVisitor.visitMaxs(
                    0,
                    0
            );
            methodVisitor.visitEnd();


            for(int i = 0; i < constructor.instructions.size(); ++i) {
                AbstractInsnNode abstractInsnNode = constructor.instructions.get(i);
                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                    if (fieldInsnNode.getOpcode() == Opcodes.PUTFIELD && fieldInsnNode.owner.equals("com/google/gson/Gson") && fieldInsnNode.name.equals("prettyPrinting") && fieldInsnNode.desc.equals("Z")) {
                        InsnList injectionList = new InsnList();
                        injectionList.add(new LabelNode(new Label()));
                        injectionList.add(new VarInsnNode(
                                Opcodes.ALOAD,
                                0
                        ));
                        injectionList.add(new LdcInsnNode("  "));//Default indent value
                        injectionList.add(new FieldInsnNode(
                                Opcodes.PUTFIELD,
                                "com/google/gson/Gson",
                                GsonTransformer.FIELD_INDENT_NAME,
                                "Ljava/lang/String;"
                        ));

                        constructor.instructions.insert(
                                abstractInsnNode,
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
                    if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && methodInsnNode.owner.equals("com/google/gson/stream/JsonWriter") && methodInsnNode.name.equals(GsonTransformer.METHOD_INDENT_SETTER_NAME) && methodInsnNode.desc.equals("(Ljava/lang/String;)V")) {
                        InsnList injectionList = new InsnList();
                        injectionList.add(new VarInsnNode(
                                Opcodes.ALOAD,
                                0
                        ));
                        injectionList.add(new MethodInsnNode(
                                Opcodes.INVOKESPECIAL,
                                "com/google/gson/Gson",
                                GsonTransformer.METHOD_INDENT_GETTER_NAME,
                                "()Ljava/lang/String;",
                                false
                        ));

                        newJsonWriter.instructions.remove(newJsonWriter.instructions.get(i - 1));
                        newJsonWriter.instructions.insertBefore(
                                abstractInsnNode,
                                injectionList
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
        return ReTweakResources.DEBUG;
    }

    @Override
    protected boolean writeASMFile() {
        return ReTweakResources.DEBUG;
    }

    @Override
    protected String getClassName(final boolean isNameTransformed) {
        return "com.google.gson.Gson";
    }

    public static String getIndent(Gson gson) {
        if (gson == null) return null;
        try {
            return (String)ReflectionHelper.getFieldValue(
                    ReflectionHelper.getField(
                            Gson.class,
                            GsonTransformer.FIELD_INDENT_NAME
                    ),
                    gson
            );
        } catch(IllegalAccessException | NoSuchFieldException e) {
            //Ignore
        }
        return null;
    }

    public static void setIndent(Gson gson, String indent) {
        if (gson == null || StringHelper.isNullOrEmpty(indent)) return;
        try {
            ReflectionHelper.invokeMethod(
                    ReflectionHelper.getMethod(
                            Gson.class,
                            GsonTransformer.METHOD_INDENT_SETTER_NAME,
                            new Class<?>[] {
                                    String.class
                            }
                    ),
                    gson,
                    new Object[] {
                            indent
                    }
            );
        } catch(IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            //Ignore
        }
    }

}
