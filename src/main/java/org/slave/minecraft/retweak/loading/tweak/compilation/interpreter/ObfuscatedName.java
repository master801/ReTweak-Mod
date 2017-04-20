package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter;

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
@Retention(RetentionPolicy.SOURCE)
public @interface ObfuscatedName {

    String value();

}
