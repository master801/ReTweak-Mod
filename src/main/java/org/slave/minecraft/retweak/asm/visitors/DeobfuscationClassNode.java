package org.slave.minecraft.retweak.asm.visitors;

import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.slave.lib.helpers.ASMHelper;
import org.slave.minecraft.retweak.loading.ReTweakDeobfuscation;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;
import org.slave.tool.remapper.SRG;

/**
 * Created by Master on 4/26/2016 at 9:19 PM.
 *
 * @author Master
 */
public final class DeobfuscationClassNode extends ClassNode {

    private String className = null;

    private final SRG srg;

    public DeobfuscationClassNode(GameVersion gameVersion) {
        super(ASMHelper.ASM_VERSION);
        srg = ReTweakDeobfuscation.INSTANCE.getSRG(gameVersion);
    }

    @Override
    public void visit(final int version, final int access, final String name, final String signature, final String superName, final String[] interfaces) {
        String newName = name;
        String newSuperName = superName;

        if (srg != null) {
            String[] nameEntry = srg.getClassEntry(name);
            if (nameEntry != null) newName = nameEntry[1];

            String[] superNameEntry = srg.getClassEntry(newSuperName);
            if (superNameEntry != null) newSuperName = superNameEntry[1];
        }

        super.visit(
                version,
                access,
                className = newName,
                signature,
                newSuperName,
                interfaces
        );
    }

    @Override
    public FieldVisitor visitField(final int access, final String name, final String desc, final String signature, final Object value) {
        String newName = name;
        String newDesc = desc;

        if (srg != null) {
            ReTweakResources.RETWEAK_LOGGER.info("FIELD C: {} N: {} D: {}", className, name, desc);
        }

        return super.visitField(
                access,
                name,
                desc,
                signature,
                value
        );
    }

    @Override
    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature, final String[] exceptions) {
        String newName = name;
        String newDesc = desc;

        if (srg != null) {
            ReTweakResources.RETWEAK_LOGGER.info("METHOD C: {} N: {} D: {}", className, name, desc);
        }

        return super.visitMethod(
                access,
                newName,
                newDesc,
                signature,
                exceptions
        );
    }

    private static final class DeobfuscationFieldVisitor extends FieldVisitor {

        public DeobfuscationFieldVisitor(final FieldVisitor fv) {
            super(
                    ASMHelper.ASM_VERSION,
                    fv
            );
        }

    }

    private static final class DeobfusctionMethodVisitor extends MethodVisitor {

        public DeobfusctionMethodVisitor(final MethodVisitor mv) {
            super(
                    ASMHelper.ASM_VERSION,
                    mv
            );
        }

        @Override
        public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
            super.visitFieldInsn(opcode, owner, name, desc);
        }

        @Override
        public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }

    }

}
