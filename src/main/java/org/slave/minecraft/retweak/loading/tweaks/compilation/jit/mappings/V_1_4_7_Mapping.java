package org.slave.minecraft.retweak.loading.tweaks.compilation.jit.mappings;

import com.github.pwittchen.kirai.library.Kirai;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InnerClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.slave.lib.helpers.ASMHelper;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.handlers.TextureHandler;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Master on 4/27/2016 at 3:33 PM.
 *
 * @author Master
 */
final class V_1_4_7_Mapping extends Mapping {

    private final EnumMap<_Type, HashMap<Holder, Holder>> types = new EnumMap<>(_Type.class);

    V_1_4_7_Mapping() {
        super(GameVersion.V_1_4_7);
        //<editor-fold desc="Automation">
        for(_Type type : _Type.values()) {
            types.put(
                    type,
                    new HashMap<Holder, Holder>()
            );
        }
        //</editor-fold>

        //<editor-fold desc="ANNOTATION">
        final HashMap<Holder, Holder> annotationMap = types.get(_Type.ANNOTATION);

        //<editor-fold desc="CLASS">
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_NETWORK_MOD_DESC
                ),
                null
        );
        //</editor-fold>

        //<editor-fold desc="METHOD">
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_PRE_INIT_DESC
                ),
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Type.getDescriptor(EventHandler.class)
                )
        );
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_INIT_DESC
                ),
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Type.getDescriptor(EventHandler.class)
                )
        );
        annotationMap.put(
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Mapping.ANNOTATION_POST_INIT_DESC
                ),
                new Holder(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        Type.getDescriptor(EventHandler.class)
                )
        );
        //</editor-fold>

        //</editor-fold>

        //<editor-fold desc="CLASS">
        //</editor-fold>

        //<editor-fold desc="INNER CLASS">
        final HashMap<Holder, Holder> innerClassMap = types.get(_Type.INNER_CLASS);
        innerClassMap.put(
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mapping.ANNOTATION_PRE_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_PRE_INIT_DESC.indexOf('$')
                        ),
                        Mapping.ANNOTATION_PRE_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_PRE_INIT_DESC.length() - 1
                        ),
                        Mapping.ANNOTATION_PRE_INIT_DESC.substring(
                                Mapping.ANNOTATION_PRE_INIT_DESC.lastIndexOf('$') + 1,
                                Mapping.ANNOTATION_PRE_INIT_DESC.length() - 1
                        )
                ),
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mod.class.getCanonicalName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getSimpleName()
                )
        );
        innerClassMap.put(
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mapping.ANNOTATION_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_INIT_DESC.indexOf('$')
                        ),
                        Mapping.ANNOTATION_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_INIT_DESC.length() - 1
                        ),
                        Mapping.ANNOTATION_INIT_DESC.substring(
                                Mapping.ANNOTATION_INIT_DESC.lastIndexOf('$') + 1,
                                Mapping.ANNOTATION_INIT_DESC.length() - 1
                        )
                ),
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mod.class.getCanonicalName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getSimpleName()
                )
        );
        innerClassMap.put(
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mapping.ANNOTATION_POST_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_POST_INIT_DESC.indexOf('$')
                        ),
                        Mapping.ANNOTATION_POST_INIT_DESC.substring(
                                1,
                                Mapping.ANNOTATION_POST_INIT_DESC.length() - 1
                        ),
                        Mapping.ANNOTATION_POST_INIT_DESC.substring(
                                Mapping.ANNOTATION_POST_INIT_DESC.lastIndexOf('$') + 1,
                                Mapping.ANNOTATION_POST_INIT_DESC.length() - 1
                        )
                ),
                new Holder(
                        _Type.INNER_CLASS,
                        Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_ANNOTATION + Opcodes.ACC_ABSTRACT + Opcodes.ACC_INTERFACE,
                        Mod.class.getCanonicalName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getName().replace(
                                '.',
                                '/'
                        ),
                        EventHandler.class.getSimpleName()
                )
        );
        //</editor-fold>

        //<editor-fold desc="FIELD">
        final HashMap<Holder, Holder> fieldMap = types.get(_Type.FIELD);
        //</editor-fold>

        //<editor-fold desc="METHOD">
        final HashMap<Holder, Holder> methodMap = types.get(_Type.METHOD);
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PROTECTED,
                        null,
                        "func_77041_b",
                        "(Lnet/minecraft/entity/EntityLiving;F)V"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PROTECTED,
                        null,
                        "func_77041_b",
                        "(Lnet/minecraft/entity/EntityLivingBase;F)V"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_77644_a",
                        "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/entity/EntityLiving;)Z"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_77644_a",
                        "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/entity/EntityLivingBase;)Z"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PROTECTED,
                        null,
                        "func_77044_a",
                        "(Lnet/minecraft/entity/EntityLiving;F)F"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PROTECTED,
                        null,
                        "func_77040_d",
                        "(Lnet/minecraft/entity/EntityLivingBase;F)F"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_77031_a",
                        "(Lnet/minecraft/entity/EntityLiving;DDDFF)V"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_76986_a",
                        "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_78086_a",
                        "(Lnet/minecraft/entity/EntityLiving;FFF)V"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_78086_a",
                        "(Lnet/minecraft/entity/EntityLivingBase;FFF)V"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PROTECTED,
                        null,
                        "func_77037_a",
                        "(Lnet/minecraft/entity/EntityLiving;)F"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PROTECTED,
                        null,
                        "func_77037_a",
                        "(Lnet/minecraft/entity/EntityLivingBase;)F"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;I)Z"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;F)Z"
                )
        );
        methodMap.put(
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_70667_aM",
                        "()I"
                ),
                new Holder(
                        _Type.METHOD,
                        Opcodes.ACC_PUBLIC,
                        null,
                        "func_110138_aP",
                        "()I"
                )
        );
        //</editor-fold>

        //<editor-fold desc="FIELD INSN">
        final HashMap<Holder, Holder> fieldInsnMap = types.get(_Type.FIELD_INSN);
        //<editor-fold desc="ITEMS">
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77683_K",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        new AbstractInsnNode[] {
                                new FieldInsnNode(
                                        Opcodes.GETSTATIC,
                                        "net/minecraft/init/Blocks",
                                        "field_150321_G",
                                        "Lnet/minecraft/block/Block;"
                                ),
                                new MethodInsnNode(
                                        Opcodes.INVOKESTATIC,
                                        "net/minecraft/item/Item",
                                        "func_150898_a",
                                        "(Lnet/minecraft/block/Block;)Lnet/minecraft/item/Item;",
                                        false
                                )
                        }
                )
        );

        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77737_bm",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151078_bh",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77770_aF",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151116_aA",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77770_aF",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151116_aA",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77703_o",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151042_j",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77669_D",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151055_y",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77686_W",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151027_R",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77685_T",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151015_O",
                        "Lnet/minecraft/item/Item;"
                )
        );

        types.get(_Type.FIELD_INSN).put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        null,
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        null,
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77804_ap",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151145_ak",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77767_aC",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151137_ax",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77725_bx",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151064_bs",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77722_bw",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151065_br",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77756_aW",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151100_aR",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_82796_bJ",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151162_bE",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77746_aZ",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151105_aU",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77771_aG",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151117_aB",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77747_aY",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151102_aT",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_82791_bT",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151158_bO",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77714_s",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151038_n",
                        "Lnet/minecraft/item/Item;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "field_77754_aU",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "field_151115_aP",
                        "Lnet/minecraft/item/Item;"
                )
        );


        /*
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/item/Item",
                        "",
                        "Lnet/minecraft/item/Item;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Items",
                        "",
                        "Lnet/minecraft/item/Item;"
                )
        );
        */


        //</editor-fold>

        //<editor-fold desc="BLOCKS">
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/block/Block",
                        "field_72002_bp",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150411_aY",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/block/Block",
                        "field_72061_ba",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150423_aK",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/block/Block",
                        "field_71956_V",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150320_F",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/block/Block",
                        "field_71963_Z",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150331_J",
                        "Lnet/minecraft/block/Block;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/block/Block",
                        "field_71987_y",
                        "Lnet/minecraft/block/Block;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        Opcodes.GETSTATIC,
                        "net/minecraft/init/Blocks",
                        "field_150345_g",
                        "Lnet/minecraft/block/Block;"
                )
        );
        //</editor-fold>

        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        -1,
                        "net/minecraft/block/material/Material",
                        "field_76244_g",
                        "Lnet/minecraft/block/material/Material;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        -1,
                        "net/minecraft/block/material/Material",
                        "field_151586_h",
                        "Lnet/minecraft/block/material/Material;"
                )
        );
        fieldInsnMap.put(
                new Holder(
                        _Type.FIELD_INSN,
                        -1,
                        "net/minecraft/block/Block",
                        "field_72018_cp",
                        "Lnet/minecraft/block/material/Material;"
                ),
                new Holder(
                        _Type.FIELD_INSN,
                        -1,
                        "net/minecraft/block/Block",
                        "field_149764_J",
                        "Lnet/minecraft/block/material/Material;"
                )
        );
        //</editor-fold>

        //<editor-fold desc="METHOD INSN">
        final HashMap<Holder, Holder> methodInsnMap = types.get(_Type.METHOD_INSN);
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        -1,
                        "net/minecraft/item/crafting/FurnaceRecipes",
                        "func_77600_a",
                        "(ILnet/minecraft/item/ItemStack;F)V"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        -1,
                        "net/minecraft/item/crafting/FurnaceRecipes",
                        "func_151396_a",
                        "(Lnet/minecraft/item/Item;Lnet/minecraft/item/ItemStack;F)V"
                )
        );
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKESPECIAL,
                        "net/minecraft/client/renderer/entity/RenderLiving",
                        "func_77031_a",
                        "(Lnet/minecraft/entity/EntityLiving;DDDFF)V"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKESPECIAL,
                        "net/minecraft/client/renderer/entity/RenderLiving",
                        "func_76986_a",
                        "(Lnet/minecraft/entity/EntityLiving;DDDFF)V"
                )
        );
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKESPECIAL,
                        "net/minecraft/client/renderer/entity/RenderLiving",
                        "func_77031_a",
                        "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKESPECIAL,
                        "net/minecraft/client/renderer/entity/RenderLiving",
                        "func_76986_a",
                        "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V"
                )
        );
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/entity/EntityLiving",
                        "func_71038_i",
                        "()V"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/entity/EntityLivingBase",
                        "func_71038_i",
                        "()V"
                )
        );
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/entity/Entity",
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;I)Z"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/entity/Entity",
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;F)Z"
                )
        );
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/entity/player/EntityPlayer",
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;I)Z"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKEVIRTUAL,
                        "net/minecraft/entity/player/EntityPlayer",
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;F)Z"
                )
        );
        methodInsnMap.put(
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKESPECIAL,
                        "net/minecraft/entity/EntityLiving",
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;I)Z"
                ),
                new Holder(
                        _Type.METHOD_INSN,
                        Opcodes.INVOKESPECIAL,
                        "net/minecraft/entity/EntityLivingBase",
                        "func_70097_a",
                        "(Lnet/minecraft/util/DamageSource;F)Z"
                )
        );
        //</editor-fold>
    }

    @Override
    protected boolean _class(final String className, final ClassNode classNode) {
        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING CLASS: {} {}",
                    classNode.name,
                    classNode.superName
            );
        }
        if (classNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = classNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        annotationNode.desc
                );
                if (holders != null) {
                    for(Holder holder : holders) {
                        if (holder == null) {//Remove annotation
                            annotationNodeIterator.remove();
                            if (ReTweakResources.DEBUG_MESSAGES) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed annotation \"{}\" from class {}",
                                        annotationNode.desc,
                                        classNode.name
                                );
                            }
                            continue;
                        }
                        holder.set(annotationNode);
                    }
                }
            }
        }
        if (classNode.innerClasses != null) {
            Iterator<InnerClassNode> innerClassNodeIterator = classNode.innerClasses.iterator();
            while(innerClassNodeIterator.hasNext()) {
                InnerClassNode innerClassNode = innerClassNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.INNER_CLASS,
                        innerClassNode.access,
                        innerClassNode.outerName,
                        innerClassNode.name,
                        innerClassNode.innerName
                );
                if (holders != null) {
                    for(Holder holder : holders) {
                        if (holder == null) {
                            innerClassNodeIterator.remove();
                            if (ReTweakResources.DEBUG_MESSAGES) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed inner-class \"{}\" from class {}",
                                        innerClassNode.name,
                                        classNode.name
                                );
                            }
                            continue;
                        }
                        holder.set(innerClassNode);
                    }
                }
                //TODO
            }
        }

        Holder[] holders = getHolders(
                _Type.CLASS,
                -1,
                null,
                classNode.name,
                null
        );
        if (holders != null) {
            for(Holder holder : holders) {
                if (holder == null) continue;
                holder.set(classNode);
            }
        }
        return false;
    }

    @Override
    protected void postClass(final String className, final ClassNode classNode) {
    }

    @Override
    protected boolean field(final String className, final FieldNode fieldNode) {
        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING FIELD: {} {}",
                    fieldNode.name,
                    fieldNode.desc
            );
        }

        if (fieldNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = fieldNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        annotationNode.desc
                );
                if (holders != null) {
                    for(Holder holder : holders) {
                        if (holder == null) {//Remove annotation
                            annotationNodeIterator.remove();
                            if (ReTweakResources.DEBUG_MESSAGES) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed annotation \"{}\" from field {}",
                                        annotationNode.desc,
                                        ASMHelper.toString(fieldNode)
                                );
                            }
                            continue;
                        }
                        holder.set(annotationNode);
                    }
                }
            }
        }

        Holder[] holders = getHolders(
                _Type.FIELD,
                fieldNode.access,
                null,
                fieldNode.name,
                fieldNode.desc
        );
        if (holders != null) {
            for(Holder holder : holders) {
                if (holder == null) continue;
                holder.set(fieldNode);
            }
        }
        return false;
    }

    @Override
    protected boolean method(final String className, final MethodNode methodNode) {
        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING METHOD: {}{}",
                    methodNode.name,
                    methodNode.desc
            );
        }

        if (methodNode.visibleAnnotations != null) {
            Iterator<AnnotationNode> annotationNodeIterator = methodNode.visibleAnnotations.iterator();
            while(annotationNodeIterator.hasNext()) {
                AnnotationNode annotationNode = annotationNodeIterator.next();
                Holder[] holders = getHolders(
                        _Type.ANNOTATION,
                        -1,
                        null,
                        null,
                        annotationNode.desc
                );
                if (holders != null) {
                    for(Holder holder : holders) {
                        if (holder == null) {
                            annotationNodeIterator.remove();
                            if (ReTweakResources.DEBUG_MESSAGES) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Removed annotation \"{}\" from method {}",
                                        annotationNode.desc,
                                        ASMHelper.toString(methodNode)
                                );
                            }
                            continue;
                        }
                        holder.set(annotationNode);
                    }
                }
            }
        }

        Holder[] holders = getHolders(
                _Type.METHOD,
                methodNode.access,
                null,
                methodNode.name,
                methodNode.desc
        );
        if (holders != null) {
            for(Holder holder : holders) {
                if (holder == null) continue;
                holder.set(methodNode);
            }
        }
        return false;
    }

    @Override
    protected void postMethodNode(final ClassNode classNode, final int index, final MethodNode methodNode) {
        if (methodNode.instructions != null) {
            Iterator<AbstractInsnNode> abstractInsnNodeIterator = methodNode.instructions.iterator();
            int tickHandler = -1;
            int loadTexture_func_76985_a = -1;

            int i = 0;
            while(abstractInsnNodeIterator.hasNext()) {
                AbstractInsnNode abstractInsnNode = abstractInsnNodeIterator.next();
                if (abstractInsnNode instanceof FieldInsnNode) {
                    FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                } else if (abstractInsnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                    switch(methodInsnNode.owner) {
                        case "cpw/mods/fml/common/registry/TickRegistry":
                            switch(methodInsnNode.getOpcode()) {
                                case Opcodes.INVOKESTATIC:
                                    if (methodInsnNode.name.equals("registerTickHandler") && methodInsnNode.desc.equals("(Lcpw/mods/fml/common/ITickHandler;Lcpw/mods/fml/relauncher/Side;)V") && tickHandler == -1) {
                                        tickHandler = i;
                                    }
                                    break;
                            }
                            break;
                        case "net/minecraftforge/common/Configuration":
                            methodInsnNode.owner = "net/minecraftforge/common/config/Configuration";
                            break;
                        case "net/minecraftforge/common/Property":
                            methodInsnNode.owner = "net/minecraftforge/common/config/Property";
                            break;
                        case "net/minecraft/client/renderer/entity/Render":
                            if (methodInsnNode.name.equals("func_76985_a") && methodInsnNode.desc.equals("(Ljava/lang/String;)V") && loadTexture_func_76985_a == -1) {
                                loadTexture_func_76985_a = i;
                            }
                            break;
                        case "net/minecraft/world/World":
                            switch(methodInsnNode.getOpcode()) {
                                case Opcodes.INVOKEVIRTUAL:
                                    if (methodInsnNode.name.equals("func_72798_a") && methodInsnNode.desc.equals("(III)I")) {//getBlockId
                                        AbstractInsnNode next = methodInsnNode.getNext();
                                        if (next instanceof IntInsnNode && next.getOpcode() == Opcodes.ISTORE) {
                                            //TODO Rename every reference of this insn node that uses an int, rename to use object
                                            //Old desc = (III)I
                                            //New desc = (III)Lnet/minecraft/block/Block;
                                        }
                                    }
                                    break;
                            }
                            break;
                    }
                } else if (abstractInsnNode instanceof TypeInsnNode) {
                    TypeInsnNode typeInsnNode = (TypeInsnNode)abstractInsnNode;
                }
                i++;
            }

            if (tickHandler != -1) {
                int cache = tickHandler - 1;
                try {
                    while(methodNode.instructions.get(cache) != null) {
                        AbstractInsnNode abstractInsnNode = methodNode.instructions.get(cache);
                        if (abstractInsnNode instanceof TypeInsnNode) {
                            TypeInsnNode typeInsnNode = (TypeInsnNode)abstractInsnNode;
                            if (typeInsnNode.getOpcode() == Opcodes.NEW) break;
                        }
                        cache--;
                    }
                } catch (IndexOutOfBoundsException e) {
                    ReTweakResources.RETWEAK_LOGGER.error(
                            methodNode.name + methodNode.desc + " - Index: " + cache,
                            e
                    );
                    cache = tickHandler - 1;
                }

                if (cache != (tickHandler - 1)) {
                    int buffer = tickHandler;
                    while(buffer > cache - 1) {
                        methodNode.instructions.remove(methodNode.instructions.get(buffer));
                        buffer--;
                    }
                }
            }

            if (loadTexture_func_76985_a != -1) {
                MethodInsnNode methodInsnNode = (MethodInsnNode)methodNode.instructions.get(loadTexture_func_76985_a);
                methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                methodInsnNode.owner = TextureHandler.class.getCanonicalName().replace(
                        '.',
                        '/'
                );
                methodInsnNode.name = "bindTexture";
                methodInsnNode.desc = "(Ljava/lang/Class;Ljava/lang/String;)V";

                methodNode.instructions.insert(
                        methodNode.instructions.get(loadTexture_func_76985_a - 2),//ALOAD 0
                        new MethodInsnNode(
                                Opcodes.INVOKEVIRTUAL,
                                "java/lang/Object",
                                "getClass",
                                "()Ljava/lang/Class;",
                                false
                        )
                );
            }
        }

        switch(classNode.superName) {
            case "net/minecraft/item/Item":
                if (methodNode.name.equals("<init>") && methodNode.instructions != null) {
                    int methodInsnNodeIndex = -1;

                    for(int i = 0; i < methodNode.instructions.size(); ++i) {
                        AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
                        if (abstractInsnNode instanceof MethodInsnNode) {
                            methodInsnNodeIndex = i;
                            break;
                        }
                    }

                    if (methodInsnNodeIndex != -1) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode)methodNode.instructions.get(methodInsnNodeIndex);
                        if (methodInsnNode != null && methodInsnNode.desc.equals("(I)V")) {
                            final String originalDesc = methodInsnNode.desc;
                            methodInsnNode.desc = "()V";
                            methodNode.instructions.remove(methodInsnNode.getPrevious());
                            if (ReTweakResources.DEBUG_MESSAGES) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Remapped desc of super-constructor call (at index {}) from method \"{}{}\" in class \"{}\", from \"{}\" to \"{}\"",
                                        methodInsnNodeIndex,
                                        methodNode.name,
                                        methodNode.desc,
                                        classNode.name,
                                        originalDesc,
                                        methodInsnNode.desc
                                );
                            }
                        }
                    }
                }
                break;
            case "net/minecraft/item/ItemFood":
                if (methodNode.name.equals("<init>") && methodNode.instructions != null) {
                    int methodInsnNodeIndex = -1;

                    for(int i = 0; i < methodNode.instructions.size(); ++i) {
                        AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
                        if (abstractInsnNode instanceof MethodInsnNode) {
                            methodInsnNodeIndex = i;
                            break;
                        }
                    }

                    if (methodInsnNodeIndex != -1) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode)methodNode.instructions.get(methodInsnNodeIndex);
                        if (methodInsnNode != null && methodInsnNode.desc.equals("(IIZ)V")) {
                            final String originalDesc = methodInsnNode.desc;
                            methodInsnNode.desc = "(IZ)V";
                            methodNode.instructions.remove(
                                    methodInsnNode.getPrevious().getPrevious().getPrevious()//ILOAD 1
                            );
                            if (ReTweakResources.DEBUG_MESSAGES) {
                                ReTweakResources.RETWEAK_LOGGER.info(
                                        "Remapped desc of super-constructor call (at index {}) from method \"{}{}\" in class \"{}\", from \"{}\" to \"{}\"",
                                        methodInsnNodeIndex,
                                        methodNode.name,
                                        methodNode.desc,
                                        classNode.name,
                                        originalDesc,
                                        methodInsnNode.desc
                                );
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected boolean fieldInsn(final String className, final int index, final InsnList insnList, final FieldInsnNode fieldInsnNode) {
        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING FIELD INSN: {}/{} {}",
                    fieldInsnNode.owner,
                    fieldInsnNode.name,
                    fieldInsnNode.desc
            );
        }

        //<editor-fold desc="Unsupported fields">
        String[][] fields = new String[0][];//No fields are yet unsupported...
        for(String[] field : fields) {
            if (ArrayHelper.isNullOrEmpty(field)) continue;
            checkFieldInsn(
                    field[0],
                    field[1],
                    field[2],
                    fieldInsnNode
            );
        }
        //</editor-fold>

        Holder[] holders = getHolders(
                _Type.FIELD_INSN,
                fieldInsnNode.getOpcode(),
                fieldInsnNode.owner,
                fieldInsnNode.name,
                fieldInsnNode.desc
        );
        if (holders != null) {
            for(Holder holder : holders) {
                holder.set(
                        fieldInsnNode,
                        insnList
                );
            }
        }
        if (fieldInsnNode.getOpcode() == Opcodes.PUTSTATIC && fieldInsnNode.owner.equals("net/minecraft/client/renderer/ChestItemRenderHelper") && fieldInsnNode.name.equals("field_78545_a") && fieldInsnNode.desc.equals("Lnet/minecraft/client/renderer/ChestItemRenderHelper;")) {
            if (ReTweakResources.DEBUG_MESSAGES) {
                ReTweakResources.RETWEAK_LOGGER.info(
                        "Removed field insn node \"{}\" at index {} from class \"{}\"",
                        ASMHelper.toString(fieldInsnNode),
                        index,
                        className
                );
            }
            return true;
        }
        if (fieldInsnNode.owner.equals("net/minecraft/item/Item")) {
            if (fieldInsnNode.desc.equals("I") && fieldInsnNode.getPrevious() instanceof FieldInsnNode) {//Get item id from item
                FieldInsnNode fi = (FieldInsnNode)fieldInsnNode.getPrevious();
                if (fi.desc.equals("L" + fieldInsnNode.owner + ";")) {
                    holders = getHolders(
                            _Type.FIELD_INSN,
                            fi.getOpcode(),
                            fi.owner,
                            fi.name,
                            fi.desc
                    );
                    if (holders != null) {
                        for(Holder holder : holders) holder.set(fi);
                    }
                    return true;//Remove item id instruction
                }
            }
        }
        switch(fieldInsnNode.getOpcode()) {
            case Opcodes.GETFIELD:
                switch(fieldInsnNode.owner) {
                    case "net/minecraft/item/Item":
                        if (fieldInsnNode.name.equals("field_77779_bT") && fieldInsnNode.desc.equals("I")) {
                            return true;//Remove item id call
                        }
                        break;
                }
                break;
        }
        return false;
    }

    @Override
    protected boolean methodInsn(final String className, final int index, final InsnList insnList, final MethodInsnNode methodInsnNode) {
        if (ReTweakResources.DEBUG_MESSAGES) {
            ReTweakResources.RETWEAK_LOGGER.info(
                    "MAPPING METHOD INSN: {}/{}{}",
                    methodInsnNode.owner,
                    methodInsnNode.name,
                    methodInsnNode.desc
            );
        }

        //<editor-fold desc="Unsupported method invokes">
        if (ASMHelper.isInvokeOpcode(methodInsnNode.getOpcode())) {
            String[][] methods = new String[][] {
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "applyModContainer",
                            "(Lcpw/mods/fml/common/ModContainer;)V"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "getModMetadata",
                            "()Lcpw/mods/fml/common/ModMetadata;"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "getAsmData",
                            "()Lcpw/mods/fml/common/discovery/ASMDataTable;"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPreInitializationEvent",
                            "getModLog",
                            "()Lorg/apache/logging/log4j/Logger;"
                    },
                    new String[] {
                            "cpw/mods/fml/common/event/FMLPostInitializationEvent",
                            "buildSoftDependProxy",
                            "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/Object;",
                    }
            };

            for(String[] method : methods) {
                if (ArrayHelper.isNullOrEmpty(method)) continue;
                checkMethodInsn(
                        method[0],
                        method[1],
                        method[2],
                        methodInsnNode
                );
            }
        }
        //</editor-fold>

        Holder[] holders = getHolders(
                _Type.METHOD_INSN,
                methodInsnNode.getOpcode(),
                methodInsnNode.owner,
                methodInsnNode.name,
                methodInsnNode.desc
        );
        if (holders != null) {
            for(Holder holder : holders) {
                holder.set(
                        methodInsnNode,
                        insnList
                );
            }
        }
        switch(methodInsnNode.getOpcode()) {
            case Opcodes.INVOKESTATIC:
                switch(methodInsnNode.owner) {
                    case "cpw/mods/fml/common/registry/GameRegistry":
                        if (methodInsnNode.name.equals("addSmelting") && methodInsnNode.desc.equals("(ILnet/minecraft/item/ItemStack;F)V")) {
                            if (methodInsnNode.getPrevious().getPrevious().getPrevious().getPrevious() instanceof FieldInsnNode) {
                                FieldInsnNode fieldInsnNode = (FieldInsnNode)methodInsnNode.getPrevious().getPrevious().getPrevious().getPrevious();
                                switch(fieldInsnNode.desc) {
                                    case "L" + "net/minecraft/item/Item" + ";": {
                                        final String originalDesc = methodInsnNode.desc;
                                        methodInsnNode.desc =
                                                "(" +
                                                        ("L" + "net/minecraft/item/Item" + ";") +
                                                        "Lnet/minecraft/item/ItemStack;F)V";
                                        if (ReTweakResources.DEBUG_MESSAGES) {
                                            ReTweakResources.RETWEAK_LOGGER.info(
                                                    "Remapped desc of method insn \"{}\" from \"{}\" to \"{}\"",
                                                    ASMHelper.toString(methodInsnNode),
                                                    originalDesc,
                                                    methodInsnNode.desc
                                            );
                                        }
                                        break;
                                    }
                                    case "L" + "net/minecraft/block/Block" + ";": {
                                        final String originalDesc = methodInsnNode.desc;
                                        methodInsnNode.desc =
                                                "(" +
                                                        ("L" + "net/minecraft/block/Block" + ";") +
                                                        "Lnet/minecraft/item/ItemStack;F)V";
                                        if (ReTweakResources.DEBUG_MESSAGES) {
                                            ReTweakResources.RETWEAK_LOGGER.info(
                                                    "Remapped desc of method insn \"{}\" from \"{}\" to \"{}\"",
                                                    ASMHelper.toString(methodInsnNode),
                                                    originalDesc,
                                                    methodInsnNode.desc
                                            );
                                        }
                                        break;
                                    }
                                    case "L" + "net/minecraft/item/ItemStack" + ";": {
                                        final String originalDesc = methodInsnNode.desc;
                                        methodInsnNode.desc =
                                                "(" +
                                                        ("L" + "net/minecraft/item/ItemStack" + ";") +
                                                        "Lnet/minecraft/item/ItemStack;F)V";
                                        if (ReTweakResources.DEBUG_MESSAGES) {
                                            ReTweakResources.RETWEAK_LOGGER.info(
                                                    "Remapped desc of method insn \"{}\" from \"{}\" to \"{}\"",
                                                    ASMHelper.toString(methodInsnNode),
                                                    originalDesc,
                                                    methodInsnNode.desc
                                            );
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case "net/minecraftforge/client/MinecraftForgeClient":
                        if (methodInsnNode.name.equals("preloadTexture") && methodInsnNode.desc.equals("(Ljava/lang/String;)V")) {
                            return true;//Remove method insn call
                        }
                        break;
                }
                break;
            case Opcodes.INVOKEVIRTUAL:
                switch(methodInsnNode.owner) {
                    case "net/minecraft/item/Item":


                        //TODO Should not be removing setIconIndex methods
                        if (methodInsnNode.name.equals("func_77665_c") && methodInsnNode.desc.equals("(I)Lnet/minecraft/item/Item;")) {
                            insnList.remove(methodInsnNode.getPrevious());
                            return true;
                        } else if (methodInsnNode.name.equals("func_77652_b") && methodInsnNode.desc.equals("(II)Lnet/minecraft/item/Item;")) {
                            insnList.remove(methodInsnNode.getPrevious().getPrevious());
                            insnList.remove(methodInsnNode.getPrevious());
                            return true;
                        }


                        break;
                }
                break;
            default:
                break;
        }
        if (methodInsnNode.desc.contains("Lnet/minecraftforge/common/Property;")) {
            //I don't even care right now
            methodInsnNode.desc = methodInsnNode.desc.replace(
                    "Lnet/minecraftforge/common/Property;",
                    "Lnet/minecraftforge/common/config/Property;"
            );
        }
        if (methodInsnNode.desc.contains("Lnet/minecraft/entity/EntityLiving;")) {
            //I don't even care right now
            methodInsnNode.desc = methodInsnNode.desc.replace(
                    "Lnet/minecraft/entity/EntityLiving;",
                    "Lnet/minecraft/entity/EntityLivingBase;"
            );
        }
        return false;
    }

    @Override
    protected boolean intInsn(final String className, final int index, final InsnList insnList, final IntInsnNode intInsnNode) {
        return false;
    }

    @Override
    protected boolean ldcInsn(final String className, final int index, final InsnList insnList, final LdcInsnNode ldcInsnNode) {
        if (ldcInsnNode.getNext() instanceof MethodInsnNode) {
            MethodInsnNode methodInsnNodeNext = (MethodInsnNode)ldcInsnNode.getNext();
            if (methodInsnNodeNext.getOpcode() == Opcodes.INVOKESTATIC) {
                if (methodInsnNodeNext.owner.equals("net/minecraftforge/client/MinecraftForgeClient")) {
                    if (methodInsnNodeNext.name.equals("preloadTexture") && methodInsnNodeNext.desc.equals("(Ljava/lang/String;)V")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected boolean typeInsn(final String className, final int index, final InsnList insnList, final TypeInsnNode typeInsnNode) {
        switch(typeInsnNode.getOpcode()) {
            case Opcodes.NEW:
                if (typeInsnNode.desc.equals("net/minecraftforge/common/Configuration")) {
                    typeInsnNode.desc = "net/minecraftforge/common/config/Configuration";
                }
                break;
        }
        return false;
    }

    @Override
    public boolean ignoreField(final int opcode, final String owner, final String name, final String desc) {
        if (opcode < 0 || StringHelper.isNullOrEmpty(owner) || StringHelper.isNullOrEmpty(name) || StringHelper.isNullOrEmpty(desc)) return false;
        switch(owner) {
            default:
                if (name.equals("field_40331_g") && desc.equals("F")) return true;
                if (name.equals("field_40332_n") && desc.equals("F")) return true;
                if (name.equals("field_752_b") && desc.equals("F")) return true;
                if (name.equals("field_757_d") && desc.equals("F")) return true;
                if (name.equals("field_756_e") && desc.equals("F")) return true;
                break;
        }
        return false;
    }

    @Override
    public boolean ignoreMethod(final int opcode, final String owner, final String name, final String desc) {
        if (opcode == -1 || StringHelper.isNullOrEmpty(owner) || StringHelper.isNullOrEmpty(name) || StringHelper.isNullOrEmpty(desc)) return false;
        switch(owner) {
            default:
                break;
        }
        return false;
    }

    private void checkMethodInsn(final String owner, final String name, final String desc, final MethodInsnNode methodInsnNode) throws UnsupportedOperationException {
        if (methodInsnNode.owner.equals(owner) && methodInsnNode.name.equals(name) && methodInsnNode.desc.equals(desc)) {
            throw new UnsupportedOperationException(
                    Kirai.from(
                            "Mod cannot invoke method \"{class}/{name}{desc}\"!"
                    ).put(
                            "class",
                            methodInsnNode.owner
                    ).put(
                            "name",
                            methodInsnNode.name
                    ).put(
                            "desc",
                            methodInsnNode.desc
                    ).format().toString()
            );
        }
    }

    private void checkFieldInsn(final String owner, final String name, final String desc, final FieldInsnNode fieldInsnNode) {
        if (fieldInsnNode.owner.equals(owner) && fieldInsnNode.name.equals(name) && fieldInsnNode.desc.equals(desc)) {
            throw new UnsupportedOperationException(
                    Kirai.from(
                            "Mod cannot get or set field \"{class}/{name}{desc}\"!"
                    ).put(
                            "class",
                            fieldInsnNode.owner
                    ).put(
                            "name",
                            fieldInsnNode.name
                    ).put(
                            "desc",
                            fieldInsnNode.desc
                    ).format().toString()
            );
        }
    }

    private Holder[] getHolders(final _Type type, final int opcode, final String owner, final String name, final String desc) {
        if (type == null || (StringHelper.isNullOrEmpty(owner) && (type != _Type.CLASS && type != _Type.ANNOTATION && type != _Type.METHOD && type != _Type.FIELD)) || (StringHelper.isNullOrEmpty(desc) && (type != _Type.CLASS && type != _Type.ANNOTATION))) return null;
        final List<Holder> holders = new ArrayList<>();
        for(Holder holder : types.get(type).keySet()) {
            boolean flag_1, flag_2, flag_3, flag_4;
            switch(type) {
                case ANNOTATION:
                    flag_1 = holder.getOpcode() == -1 && opcode == -1;
                    flag_2 = StringHelper.isNullOrEmpty(holder.getOwner()) && StringHelper.isNullOrEmpty(owner);
                    flag_3 = StringHelper.isNullOrEmpty(holder.getName()) && StringHelper.isNullOrEmpty(name);
                    flag_4 = !StringHelper.isNullOrEmpty(holder.getDesc()) && holder.getDesc().equals(desc);
                    if (flag_1 && flag_2 && flag_3 && flag_4) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
                case CLASS:
                    flag_1 = holder.getName().equals(name);
                    if (flag_1) {
                        return new Holder[] {
                                holder
                        };
                    }
                    break;
                case INNER_CLASS:
                    flag_1 = holder.getOpcode() == opcode;
                    flag_2 = !StringHelper.isNullOrEmpty(holder.getOwner()) && holder.getOwner().equals(owner);
                    flag_3 = !StringHelper.isNullOrEmpty(holder.getName()) && holder.getName().equals(name);
                    flag_4 = !StringHelper.isNullOrEmpty(holder.getDesc()) && holder.getDesc().equals(desc);
                    if (flag_1 && flag_2 && flag_3 && flag_4) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
                case FIELD:
                case METHOD:
                    flag_1 = (opcode == -1 && holder.getOpcode() == -1) || holder.getOpcode() == opcode;
                    flag_2 = name == null || holder.getName().equals(name);
                    flag_3 = holder.getDesc().equals(desc);
                    if (flag_1 && flag_2 && flag_3) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
                case FIELD_INSN:
                case METHOD_INSN:
                    flag_1 = holder.getOwner() == null || holder.getOwner().equals(owner);
                    flag_2 = ((opcode == -1 || holder.getOpcode() == -1) || holder.getOpcode() == opcode);
                    flag_3 = ((name == null || holder.getName() == null) || name.equals(holder.getName()));
                    flag_4 = desc.equals(holder.getDesc());
                    if (flag_1 && flag_2 && flag_3 && flag_4) {
                        holders.add(types.get(type).get(holder));
                    }
                    break;
            }
        }
        if (holders.isEmpty()) return null;
        return holders.toArray(new Holder[holders.size()]);
    }

}
