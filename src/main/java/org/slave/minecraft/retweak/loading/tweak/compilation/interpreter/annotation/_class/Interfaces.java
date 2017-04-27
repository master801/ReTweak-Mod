package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Master on 4/20/2017 at 7:00 AM.
 *
 * @author Master
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Interfaces {

    Class<?>[] value();

}
