package org.slave.minecraft.retweak.loading.capsule.versions;

import com.google.common.collect.ImmutableList;
import cpw.mods.fml.common.eventhandler.EventBus;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Property;
import org.objectweb.asm.Type;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.FieldEntryBuilder;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassInfoBuilder.ClassInfo;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.aah;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.abw;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.acf;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.amw;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.aoe;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.aqz;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.ark;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.bje;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.by;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.cpw.mods.fml.common.network.NetworkMod;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.ms;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.net.minecraftforge.common.Configuration;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.ni;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.of;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.uf;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.wh;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.wp;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.ww;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.xo;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.xv;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.xx;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.yc;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.ye;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.yp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 4/19/2017 at 7:37 PM.
 *
 * @author Master
 */
final class ClassHolderWrapper_1_6_4 extends ClassHolderWrapper {

    static final ClassHolderWrapper INSTANCE = new ClassHolderWrapper_1_6_4();

    private final ClassHolder classHolder;

    private ClassHolderWrapper_1_6_4() {
        List<ClassInfo> list = new ArrayList<>();
        //<editor-fold desc="Minecraft">
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "aah"
                ).setTo(
                    aah.class
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("abw").setTo(abw.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("acf").setTo(acf.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "akc"
                ).setTo(
                    Material.class
                ).addFieldMapping(
                    FieldEntryBuilder.instance().setObfuscatedName(
                        "d"
                    ).setDeobfuscatedNameThroughField(
                        Material.class,
                        Material.wood
                    ).setFromDescType(
                        Type.getType("Lakc;")
                    ).build()
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                        "amw"
                ).setTo(
                    amw.class
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "aoe"
                ).setTo(
                    aoe.class
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("aqz").setTo(aqz.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("ark").setTo(ark.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("bje").setTo(bje.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("by").setTo(by.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("ms").setTo(ms.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("ni").setTo(ni.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("of").setTo(of.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("uf").setTo(uf.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("wh").setTo(wh.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("wp").setTo(wp.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("ww").setTo(ww.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("xo").setTo(xo.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("xv").setTo(xv.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("xx").setTo(xx.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("yc").setTo(yc.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom("ye").setTo(ye.class).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "yp"
                ).setTo(
                    yp.class
                ).build()
            ).build()
        );
        //</editor-fold>
        //<editor-fold desc="Override">
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "net.minecraftforge.common.Configuration"
                ).setTo(
                    Configuration.class
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance(
            ).setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "net.minecraftforge.common.Property"
                ).setTo(
                    Property.class
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance(
            ).setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "cpw.mods.fml.common.network.NetworkMod"
                ).setTo(
                    NetworkMod.class
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    MinecraftForge.class.getName()
                ).addFieldMapping(
                    FieldEntryBuilder.instance().setObfuscatedName(
                        "EVENT_BUS"
                    ).setDeobfuscatedNameThroughField(
                        MinecraftForge.class,
                        MinecraftForge.EVENT_BUS
                    ).setFromDescType(
                        Type.getType("Lnet/minecraftforge/event/EventBus;")
                    ).setToDescType(
                        Type.getType(EventBus.class)
                    ).build()
                ).build()
            ).build()
        );
        list.add(
            ClassInfoBuilder.instance().setClassEntry(
                ClassEntryBuilder.instance().setFrom(
                    "net.minecraftforge.event.EventBus"
                ).setTo(
                    EventBus.class
                ).build()
            ).build()
        );
        //</editor-fold>

        this.classHolder = new ClassHolder(
            ImmutableList.copyOf(list)
        );
    }

    @Override
    ClassHolder getClassHolder() {
        return classHolder;
    }

}
