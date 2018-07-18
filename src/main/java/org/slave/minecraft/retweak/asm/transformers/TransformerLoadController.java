package org.slave.minecraft.retweak.asm.transformers;

import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.IFMLHandledException;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.LoaderState;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.asm.ReTweakASM;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper.Injection;
import org.slave.minecraft.retweak.load.ReTweakLoader;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.lang.reflect.GenericArrayType;
import java.util.Map.Entry;

/**
 * Created by Master on 7/11/2018 at 5:26 AM.
 *
 * @author Master
 */
public final class TransformerLoadController implements IClassTransformer {

    private static final String CLASS_NAME = "cpw.mods.fml.common.LoadController";

    public TransformerLoadController() {
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (name.equals(CLASS_NAME)) {
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            ClassVisitor cv = new LoadControllerClassVisitor(Opcodes.ASM5, classNode);
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

        DISTRIBUTE_STATE_MESSAGE(
                new String[] {
                        "distributeStateMessage"
                },
                new String[] {
                        "(Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V"
                },
                MethodVisitorDistributeStateMessage.class
//                null
        ),

        TRANSITION(
                new String[] {
                        "transition"
                },
                new String[] {
                        "(Lcpw/mods/fml/common/LoaderState;Z)V"
                },
                MethodVisitorTransition.class
//                null
        ),

        DISTRIBUTE_STATE_EVENT(
                new String[] {
                        "distributeStateMessage"
                },
                new String[] {
                        "(Ljava/lang/Class;)V"
                },
                MethodVisitorDistributeStateEvent.class
//                null
        );

        @Getter
        private final String[] name;

        @Getter
        private final String[] desc;

        @Getter
        private final Class<? extends MethodVisitor> methodVisitorClass;

    }

    private static final class LoadControllerClassVisitor extends ClassVisitor {

        public LoadControllerClassVisitor(final int api, final ClassVisitor cv) {
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

    public static final class MethodVisitorDistributeStateMessage extends MethodVisitor {

        public MethodVisitorDistributeStateMessage(final int api, final MethodVisitor mv) {
            super(api, mv);
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

            if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("com/google/common/eventbus/EventBus") && name.equals("post") && desc.equals("(Ljava/lang/Object;)V")) {
                super.visitLabel(new Label());
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ALOAD, 1);
                super.visitVarInsn(Opcodes.ALOAD, 2);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "distributeStateMessage", "(Lcpw/mods/fml/common/LoadController;Lcpw/mods/fml/common/LoaderState;[Ljava/lang/Object;)V", false);
            }
        }

        private static final class XX extends LoadController {

            public XX(final Loader loader) {
                super(loader);
            }

            private EventBus masterChannel;

            public void distributeStateMessage(LoaderState state, Object... eventData) {
                if (state.hasEvent()) {
                    masterChannel.post(state.getEvent(eventData));
                    ReTweakLoader.instance().distributeStateMessage(this, state, eventData);
                }
            }

        }

    }

    public static final class MethodVisitorTransition extends MethodVisitor {

        public MethodVisitorTransition(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitInsn(final int opcode) {
            if (opcode == Opcodes.RETURN) {
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);

                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ALOAD, 1);
                super.visitVarInsn(Opcodes.ILOAD, 2);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "transition", "(Lcpw/mods/fml/common/LoadController;Lcpw/mods/fml/common/LoaderState;Z)V", false);

                super.visitLabel(new Label());
            }
            super.visitInsn(opcode);
        }

        private static final class XX extends LoadController {

            public XX(final Loader loader) {
                super(loader);
            }

            private LoaderState state;
            Multimap<String, Throwable> errors = null;

            public void transition(LoaderState desiredState, boolean forceState) {
                LoaderState oldState = state;
                state = state.transition(!errors.isEmpty());
                if (state != desiredState && !forceState) {
                    Throwable toThrow = null;
                    FMLLog.severe("Fatal errors were detected during the transition from %s to %s. Loading cannot continue", oldState, desiredState);
                    StringBuilder sb = new StringBuilder();
                    printModStates(sb);
                    FMLLog.severe("%s", sb.toString());
                    if (errors.size() > 0) {
                        FMLLog.severe("The following problems were captured during this phase");
                        for (Entry<String, Throwable> error: errors.entries()) {
                            FMLLog.log(Level.ERROR, error.getValue(), "Caught exception from %s", error.getKey());
                            if (error.getValue() instanceof IFMLHandledException) {
                                toThrow = error.getValue();
                            } else if (toThrow == null) {
                                toThrow = error.getValue();
                            }
                        }
                    } else {
                        FMLLog.severe("The ForgeModLoader state engine has become corrupted. Probably, a state was missed by and invalid modification to a base class" +
                                "ForgeModLoader depends on. This is a critical error and not recoverable. Investigate any modifications to base classes outside of" +
                                "ForgeModLoader, especially Optifine, to see if there are fixes available.");
                        throw new RuntimeException("The ForgeModLoader state engine is invalid");
                    }
                    if (toThrow != null && toThrow instanceof RuntimeException) {
                        throw (RuntimeException) toThrow;
                    } else {
                        throw new LoaderException(toThrow);
                    }
                } else if (state != desiredState && forceState) {
                    FMLLog.info("The state engine was in incorrect state %s and forced into state %s. Errors may have been discarded.", state, desiredState);
                    forceState(desiredState);
                }

                ReTweakLoader.instance().transition(this, desiredState, forceState);
            }

            private void forceState(final LoaderState e) {
            }

        }

    }

    public static class MethodVisitorDistributeStateEvent extends MethodVisitor {

        public MethodVisitorDistributeStateEvent(final int api, final MethodVisitor mv) {
            super(api, mv);
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

            if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("com/google/common/eventbus/EventBus") && name.equals("post") && desc.equals("(Ljava/lang/Object;)V")) {
                super.visitLabel(new Label());
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "org/slave/minecraft/retweak/load/ReTweakLoader", "instance", "()Lorg/slave/minecraft/retweak/load/ReTweakLoader;", false);
                super.visitVarInsn(Opcodes.ALOAD, 0);
                super.visitVarInsn(Opcodes.ALOAD, 1);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/slave/minecraft/retweak/load/ReTweakLoader", "distributeStateMessage", "(Lcpw/mods/fml/common/LoadController;Ljava/lang/Class;)V", false);
            }
        }

        private static final class XX extends LoadController {

            private EventBus masterChannel;

            public XX(final Loader loader) {
                super(loader);
            }

            @Override
            public void distributeStateMessage(final Class<?> customEvent) {
                try {
                    masterChannel.post(customEvent.newInstance());
                    ReTweakLoader.instance().distributeStateMessage(this, customEvent);
                } catch (Exception e) {
                    FMLLog.log(Level.ERROR, e, "An unexpected exception");
                    throw new LoaderException(e);
                }
            }

        }

    }

}
