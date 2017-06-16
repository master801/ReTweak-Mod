package org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.cpw.mods.fml.common.network;

import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Master on 6/9/2017 at 1:04 PM.
 *
 * @author Master
 */
@Deobfuscated(
    name = "NetworkMod",
    _package = @Package("cpw.mods.fml.common.network")
)
@Target(
    {
        ElementType.FIELD
    }
)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetworkMod {

    boolean clientSideRequired() default true;

    boolean serverSideRequired() default false;

}
