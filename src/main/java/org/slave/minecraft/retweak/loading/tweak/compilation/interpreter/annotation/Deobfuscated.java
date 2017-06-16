package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation;

import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Master on 4/20/2017 at 4:48 AM.
 *
 * @author Master
 */
@Target(
    {
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.FIELD
    }
)
@Retention(RetentionPolicy.RUNTIME)
public @interface Deobfuscated {

    Package[] _package() default {};

    String name();

}
