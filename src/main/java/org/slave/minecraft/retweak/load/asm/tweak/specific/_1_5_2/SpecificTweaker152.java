package org.slave.minecraft.retweak.load.asm.tweak.specific._1_5_2;

import net.minecraft.entity.EntityLivingBase;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.load.asm.tweak.specific.SpecificTweaker;

/**
 * Created by Master on 3/19/2020 at 2:25 PM
 *
 * @author Master
 */
public final class SpecificTweaker152 implements SpecificTweaker {

    @Override
    public MethodVisitor getMethodVisitor(final int api, final MethodVisitor mv, final String owner, final String name, final String desc) {
        if (api < 0 || mv == null || name == null || desc == null) return null;
        if (name.equals("onBlockPlacedBy") && desc.equals("(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;)V")) {
            return new MethodVisitor(api, mv) {

                @Override
                public void visitLocalVariable(final String name, final String desc, final String signature, final Label start, final Label end, final int index) {
                    String newDesc = desc;
                    if (desc.equals("Lnet/minecraft/entity/EntityLiving;") && index == 5) {//If vanilla method, desc will always be EntityLiving and will always be index 5
                        newDesc = Type.getDescriptor(EntityLivingBase.class);
                    }
                    super.visitLocalVariable(name, newDesc, signature, start, end, index);
                }

            };
        }
        return mv;
    }

    @Override
    public FieldVisitor getFieldVisitor(final int api, final FieldVisitor fv, final String owner, final String name, final String desc) {
        return fv;
    }

}
