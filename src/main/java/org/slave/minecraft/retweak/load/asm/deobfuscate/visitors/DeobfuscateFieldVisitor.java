package org.slave.minecraft.retweak.load.asm.deobfuscate.visitors;

import org.objectweb.asm.FieldVisitor;

/**
 * Created by master on 10/27/18 at 3:03 PM
 *
 * @author master
 */
public final class DeobfuscateFieldVisitor extends FieldVisitor {

    public DeobfuscateFieldVisitor(final int api, final FieldVisitor fv) {
        super(api, fv);
    }

}
