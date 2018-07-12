package org.slave.minecraft.retweak.asm.transformers;

import lombok.AllArgsConstructor;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.util.ResourceLocation;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.asm.transformers.BasicTransformer;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.asm.ReTweakASM;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Created by Master on 7/11/2018 at 4:52 AM.
 *
 * @author Master
 */
public final class TransformerSimpleReloadableResourceManager extends BasicTransformer implements IClassTransformer {

    private static final String CLASS_NAME = SimpleReloadableResourceManager.class.getName();

    public TransformerSimpleReloadableResourceManager() {
        super(ReTweakASM.LOGGER_RETWEAK_ASM);
    }

    @Override
    protected boolean transformClass(final ClassNode classNode) throws Exception {
        return true;
    }

    @Override
    protected ClassNode getClassNode() {
        return new ClassNode() {

            @Override
            public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

                for_:
                for(Injection injection : Injection.values()) {
                    for(String _name : injection.name) {
                        if (_name.equals(name)) {
                            for(String _desc : injection.desc) {
                                if (_desc.equals(desc)) {
                                    try {
                                        Constructor<? extends MethodVisitor> constructor = ReflectionHelper.getConstructor(
                                                injection.classMv,
                                                new Class<?>[] {
                                                        int.class,
                                                        MethodVisitor.class
                                                }
                                        );
                                        mv = ReflectionHelper.createFromConstructor(
                                                constructor,
                                                new Object[] {
                                                        super.api,
                                                        mv
                                                }
                                        );
                                    } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                                        TransformerSimpleReloadableResourceManager.this.getLogger().error(
                                                "Caught exception while creating method visitor!",
                                                e
                                        );
                                    }
                                    continue for_;
                                }
                            }
                        }
                    }
                }

                return mv;
            }

        };
    }

    @Override
    protected String getClassName(final boolean b) {
        return TransformerSimpleReloadableResourceManager.CLASS_NAME.replace('/', '.');
    }

    @AllArgsConstructor
    private enum Injection {

        GET_RESOURCE(
                new String[] {
                        "getResource"
                },
                new String[] {
                        "(Lnet/minecraft/util/ResourceLocation;)Lnet/minecraft/client/resources/IResource;"
                },
                MethodVisitorGetResource.class
        );

        private final String[] name;
        private final String[] desc;
        private final Class<? extends MethodVisitor> classMv;

    }

    static final class MethodVisitorGetResource extends MethodVisitor {//TODO

        public MethodVisitorGetResource(final int api, final MethodVisitor mv) {
            super(api, mv);
        }

        private static class as {

            private Map domainResourceManagers = null;

            public IResource getResource(ResourceLocation p_110536_1_) throws IOException
            {
                IResourceManager iresourcemanager = (IResourceManager)this.domainResourceManagers.get(p_110536_1_.getResourceDomain());

                if (iresourcemanager != null)
                {
                    return iresourcemanager.getResource(p_110536_1_);
                }
                else
                {
                    throw new FileNotFoundException(p_110536_1_.toString());
                }
            }

        }

    }

}
