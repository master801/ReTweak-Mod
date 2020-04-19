package org.slave.minecraft.retweak.load.asm.tweak.specific._1_5_2;

import com.google.common.collect.Maps;
import net.minecraft.entity.EntityLivingBase;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.asm.tweak.specific.SpecificTweaker;

import java.util.Map;

/**
 * Created by Master on 3/19/2020 at 2:25 PM
 *
 * @author Master
 */
public final class SpecificTweaker152 implements SpecificTweaker {

    private final Map<String, String> mapItemMigration = Maps.newHashMap(), mapBlockMigration = Maps.newHashMap();

    public SpecificTweaker152() {
        mapItemMigration.put("sugar", "sugar");
        mapItemMigration.put("stick", "stick");
        mapItemMigration.put("coal", "coal");
        mapItemMigration.put("flintAndSteel", "flint_and_steel");
        mapItemMigration.put("porkRaw", "porkchop");
        mapItemMigration.put("porkchop", "cooked_porkchop");
        mapItemMigration.put("beefRaw", "beef");
        mapItemMigration.put("beefCooked", "cooked_beef");
        mapItemMigration.put("chickenRaw", "chicken");
        mapItemMigration.put("chickenCooked", "cooked_chicken");
        mapItemMigration.put("fishRaw", "fish");
        mapItemMigration.put("fishCooked", "cooked_fished");
        mapItemMigration.put("dyePowder", "dye");
        mapItemMigration.put("book", "book");
        mapItemMigration.put("sign", "sign");
        mapItemMigration.put("boat", "boat");
        mapItemMigration.put("potato", "potato");
        mapItemMigration.put("bakedPotato", "baked_potato");
        mapItemMigration.put("bucketLava", "lava_bucket");
        mapItemMigration.put("paper", "paper");
        mapItemMigration.put("bowlEmpty", "bowl");
        mapItemMigration.put("plantRed", "red_flower");
        mapItemMigration.put("plantYellow", "yellow_flower");
        mapItemMigration.put("cloth", "wool");
        mapItemMigration.put("lightStoneDust", "glowstone_dust");
        mapItemMigration.put("spiderEye", "spider_eye");
        mapItemMigration.put("poisonousPotato", "poisonous_potato");
        mapItemMigration.put("seeds", "wheat_seeds");
        mapItemMigration.put("wheat", "wheat");
        mapItemMigration.put("silk", "string");

        mapBlockMigration.put("dirt", "dirt");
        mapBlockMigration.put("cobblestone", "cobblestone");
        mapBlockMigration.put("wood", "log");
        mapBlockMigration.put("planks", "planks");
        mapBlockMigration.put("bookShelf", "bookshelf");
        mapBlockMigration.put("stairsWoodOak", "oak_stairs");
        mapBlockMigration.put("stairsWoodSpruce", "spruce_stairs");
        mapBlockMigration.put("stairsWoodBirch", "birch_stairs");
        mapBlockMigration.put("stairsWoodJungle", "jungle_stairs");
        mapBlockMigration.put("fence", "fence");
        mapBlockMigration.put("fenceGate", "fence_gate");
        mapBlockMigration.put("ladder", "ladder");
        mapBlockMigration.put("doorWood", "wooden_door");
        mapBlockMigration.put("trapdoor", "trapdoor");
        mapBlockMigration.put("sapling", "sapling");
        mapBlockMigration.put("plantRed", "red_flower");
        mapBlockMigration.put("plantYellow", "yellow_flower");
        mapBlockMigration.put("cloth", "wool");
    }

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
                    if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked local variable from \"{} {} {}\" to \"{} {} {}\"", name, desc, index, name, newDesc, index);
                    super.visitLocalVariable(name, newDesc, signature, start, end, index);
                }

            };
        }
        return new MethodVisitor(api, mv) {

            @Override
            public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
                String newOwner = owner, newName = name, newDesc = desc;
                if (opcode == Opcodes.GETSTATIC) {
                    if (owner.equals("net/minecraft/item/Item") && mapItemMigration.containsKey(name) && desc.equals("Lnet/minecraft/item/Item;")) {
                        newOwner = "net/minecraft/init/Items";
                        newName = mapItemMigration.get(name);
                    } else if (owner.equals("net/minecraft/block/Block") && mapBlockMigration.containsKey(name)) {
                        newOwner = "net/minecraft/init/Blocks";
                        newName = mapBlockMigration.get(name);
                    } else if (owner.equals("net/minecraftforge/common/MinecraftForge") && name.equals("EVENT_BUS") && desc.equals("Lcpw/mods/fml/common/eventhandler/EventBus;")) {
                        newOwner = "org/slave/minecraft/retweak/load/fml/EventBusHandler";
                        newDesc = "Lorg/slave/minecraft/retweak/load/fml/EventBusHandler;";
                    }
                }

                if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(desc)) {
                    if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked field insn from \"{}/{} {}\" to \"{}/{} {}\"", owner, name, desc, newOwner, newName, newDesc);
                }
                super.visitFieldInsn(opcode, newOwner, newName, newDesc);
            }

            @Override
            public void visitMethodInsn(final int opcode, final String owner, final String name, final String desc, final boolean itf) {
                String newOwner = owner, newName = name, newDesc = desc;
                if (owner.equals("net/minecraftforge/event/EventBus") && name.equals("register") && desc.equals("(Ljava/lang/Object;)V")) {
                    newOwner = "org/slave/minecraft/retweak/load/fml/EventBusHandler";
                }
                if (opcode == Opcodes.INVOKESPECIAL && owner.equals("net/minecraft/block/BlockFlower") && name.equals("updateTick") && desc.equals("(Lnet/minecraft/world/World;IIILjava/util/Random;)V")) {
                    newOwner = "org/slave/minecraft/retweak/load/asm/tweak/migrate/_1_5_2/Migrate152BlockBush";
                }
                if (!newOwner.equals(owner) || !newName.equals(name) || !newDesc.equals(desc)) {
                    if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Tweaked method insn from \"{} {}{}\" to \"{} {}{}\"", owner, name, desc, newOwner, newName, newDesc);
                }
                super.visitMethodInsn(opcode, newOwner, newName, newDesc, itf);
            }

        };
    }

    @Override
    public FieldVisitor getFieldVisitor(final int api, final FieldVisitor fv, final String owner, final String name, final String desc) {
        if (api < 0 || fv == null || name == null || desc == null) return null;
        return fv;
    }

}
