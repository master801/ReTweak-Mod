package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Master on 6/16/2017 at 8:49 PM.
 *
 * @author Master
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassInfo {

    boolean substitute() default false;

}
