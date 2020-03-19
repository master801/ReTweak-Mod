package org.slave.minecraft.retweak.load.asm.tweak.specific;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by Master on 3/19/2020 at 2:26 PM
 *
 * @author Master
 */
public interface SpecificTweaker {

    MethodVisitor getMethodVisitor(final int api, final MethodVisitor mv, final String owner, final String name, final String desc);

    FieldVisitor getFieldVisitor(final int api, final FieldVisitor fv, final String owner, final String name, final String desc);

}
