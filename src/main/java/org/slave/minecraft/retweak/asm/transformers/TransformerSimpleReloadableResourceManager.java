package org.slave.minecraft.retweak.asm.transformers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.minecraft.retweak.asm.ReTweakASM;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper;
import org.slave.minecraft.retweak.asm.util.ReTweakASMHelper.Injection;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Master on 7/11/2018 at 4:52 AM.
 *
 * @author Master
 */
public final class TransformerSimpleReloadableResourceManager implements IClassTransformer {

    private static final String CLASS_NAME = "net.minecraft.client.resources.SimpleReloadableResourceManager";

    public TransformerSimpleReloadableResourceManager() {
    }

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] basicClass) {
        if (transformedName.equals(CLASS_NAME)) {
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            ClassVisitor cv = new SimpleReloadableResourceManagerClassVisitor(Opcodes.ASM5, classNode);
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

    @AllArgsConstructor
    private enum _Injection implements Injection {

        GET_RESOURCE(
                new String[] {
                        "getResource"
                },
                new String[] {
                        "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/resources/IResource;"
                },
                MethodVisitorGetResource.class
        );

        @Getter
        private final String[] name;

        @Getter
        private final String[] desc;

        @Getter
        private final Class<? extends MethodVisitor> methodVisitorClass;

    }

    public static final class SimpleReloadableResourceManagerClassVisitor extends ClassVisitor {

        public SimpleReloadableResourceManagerClassVisitor(final int api, final ClassVisitor cv) {
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

    public static final class MethodVisitorGetResource extends MethodVisitor {//TODO

        public MethodVisitorGetResource(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        private static class as {

            private Map domainResourceManagers = null;

            public IResource getResource(ResourceLocation p_110536_1_) throws IOException {
                IResourceManager iresourcemanager = (IResourceManager) this.domainResourceManagers.get(p_110536_1_.getResourceDomain());

                if (iresourcemanager != null) {
                    return iresourcemanager.getResource(p_110536_1_);
                } else {
                    throw new FileNotFoundException(p_110536_1_.toString());
                }
            }

        }

    }

}
