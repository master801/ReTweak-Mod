package org.slave.minecraft.retweak.loading.capsule.versions;

import com.google.common.collect.ImmutableList;
import net.minecraftforge.common.config.Property;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder;
import org.slave.minecraft.retweak.loading.capsule.versions.ClassHolder.ClassEntryBuilder.ClassEntry;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.aah;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.abw;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.acf;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter._1_6_4.akc;
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

    private final ClassHolder classHolder;

    ClassHolderWrapper_1_6_4() {
        List<ClassEntry> list = new ArrayList<>();
        //<editor-fold desc="Minecraft">
        list.add(
            new ClassEntryBuilder().setFrom("aah").setTo(aah.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("abw").setTo(abw.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("acf").setTo(acf.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("akc").setTo(akc.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("amw").setTo(amw.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("aoe").setTo(aoe.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("aqz").setTo(aqz.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("ark").setTo(ark.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("bje").setTo(bje.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("by").setTo(by.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("ms").setTo(ms.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("ni").setTo(ni.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("of").setTo(of.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("uf").setTo(uf.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("wh").setTo(wh.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("wp").setTo(wp.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("ww").setTo(ww.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("xo").setTo(xo.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("xv").setTo(xv.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("xx").setTo(xx.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("yc").setTo(yc.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("ye").setTo(ye.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("yp").setTo(yp.class).build()
        );
        //</editor-fold>
        //<editor-fold desc="Override">
        list.add(
            new ClassEntryBuilder().setFrom("net.minecraftforge.common.Configuration").setTo(Configuration.class).build()

        );
        list.add(
            new ClassEntryBuilder().setFrom("net.minecraftforge.common.Property").setTo(Property.class).build()
        );
        list.add(
            new ClassEntryBuilder().setFrom("cpw.mods.fml.common.network.NetworkMod").setTo(NetworkMod.class).build()
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
