package org.slave.minecraft.retweak.load.asm.tweak.migrate.cpw.mods.fml.common.network;

import org.slave.minecraft.retweak.load.asm.tweak.annotation.Deobfuscated;
import org.slave.minecraft.retweak.load.asm.tweak.annotation._class.Package;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Master on 7/12/2018 at 10:03 PM.
 *
 * @author Master
 */
@Deobfuscated(
        _package = @Package("cpw.mods.fml.common.network"),
        name = "NetworkMod"
)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NetworkMod {

    boolean clientSideRequired() default true;

    boolean serverSideRequired() default false;

}
