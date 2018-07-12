package org.slave.minecraft.retweak.asm.util;

import lombok.experimental.UtilityClass;
import org.objectweb.asm.MethodVisitor;
import org.slave.lib.helpers.ReflectionHelper;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Master on 7/11/2018 at 5:33 AM.
 *
 * @author Master
 */
@UtilityClass
public final class ReTweakASMHelper {

    public static MethodVisitor visitMethod(final int api, final MethodVisitor mv, final Injection[] injections, final Logger logger, final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        for(Injection _injection : injections) {
            for(String _name : _injection.getName()) {
                if (_name.equals(name)) {
                    for(String _desc : _injection.getDesc()) {
                        if (_desc.equals(desc)) {
                            try {
                                Constructor<? extends MethodVisitor> constructor = ReflectionHelper.getConstructor(
                                        _injection.getMethodVisitorClass(),
                                        new Class<?>[] {
                                                int.class,
                                                MethodVisitor.class
                                        }
                                );
                                return ReflectionHelper.createFromConstructor(
                                        constructor,
                                        new Object[] {
                                                api,
                                                mv
                                        }
                                );
                            } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                                logger.error(
                                        "Caught exception while creating method visitor!",
                                        e
                                );
                            }
                        }
                    }
                }
            }
        }
        return mv;
    }

    public interface Injection {

        String[] getName();

        String[] getDesc();

        Class<? extends MethodVisitor> getMethodVisitorClass();

    }

}
