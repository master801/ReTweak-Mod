package org.slave.minecraft.retweak.asm.adapters;

import org.objectweb.asm.ClassVisitor;
import org.slave.lib.helpers.ASMHelper;

/**
 * Created by Master801 on 4/20/2016 at 1:56 PM.
 *
 * @author Master801
 */
public final class ModCandidateAdapater extends ClassVisitor {

    public ModCandidateAdapater(final ClassVisitor cv) {
        super(ASMHelper.ASM_VERSION, cv);
    }

}
