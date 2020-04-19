package org.slave.minecraft.retweak.load.asm.tweak.clazz;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import org.slave.lib.helpers.IterableHelper;
import org.slave.lib.resources.Obfuscation;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationField.MigrationField;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.BuilderMigrationMethod.MigrationMethod;
import org.slave.minecraft.retweak.load.asm.tweak.clazz.BuilderMigrationClass.MigrationClass;
import org.slave.minecraft.retweak.load.mapping.SpiderClass;
import org.slave.minecraft.retweak.load.mapping.SpiderMethod;
import org.slave.minecraft.retweak.load.mapping.SrgMap;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.util.List;
import java.util.Map;

/**
 * Created by Master on 7/12/2018 at 8:48 PM.
 *
 * @author Master
 */
public abstract class TweakClass {

    @Getter
    private final GameVersion gameVersion;

    private final Map<String, MigrationClass> migrationClasses = Maps.newHashMap();
    private final List<BuilderSuperMigration.SuperMigration> superMigrationList = Lists.newArrayList();

    @Getter
    private final List<String> classesToTweak = Lists.newArrayList();

    protected TweakClass(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
    }

    protected final void addMigrationClass(final MigrationClass migrationClass) {
        if (migrationClass == null || migrationClasses.containsValue(migrationClass)) return;
        migrationClasses.put(migrationClass.getFrom().replace('/', '.'), migrationClass);
    }

    public final boolean hasMigrationClass(final String name, final boolean actuallyMigrates) {
        boolean flag;
        MigrationClass migrationClass = migrationClasses.get(name);
        if (flag = (migrationClass != null)) {
            if (actuallyMigrates) flag = !migrationClass.getFrom().equals(migrationClass.getTo().getName().replace('.', '/'));
        }
        return flag;
    }

    public final MigrationClass getMigrationClass(final String name) {
        return migrationClasses.get(name.replace('/', '.'));
    }

    public final void addSuperMigration(final BuilderSuperMigration.SuperMigration superMigration) {
        if (superMigration == null) return;
        if (!superMigrationList.contains(superMigration)) superMigrationList.add(superMigration);
    }

    public final BuilderSuperMigration.SuperMigration getSuperMigration(final String from) {
        if (from == null) return null;
        String newFrom = from.replace('.', '/');
        for(BuilderSuperMigration.SuperMigration superMigration : superMigrationList) {
            if (superMigration.getFrom().equals(newFrom)) return superMigration;
        }
        return null;
    }

    public final MigrationMethod getMigrationMethod(final SrgMap srgMap, final Obfuscation obfuscation, final List<String> interfaces, final String className, final String name, final String desc) {
        if (className == null || name == null || desc == null) return null;
        if (!IterableHelper.isNullOrEmpty(interfaces)) {
            for(String iface : interfaces) {
                SpiderClass spiderClass_iface = srgMap.getSpiderClass(obfuscation, iface);//Super class and interfaces are already deobfuscated before visiting fields and methods
                if (spiderClass_iface == null) continue;

                SpiderMethod spiderMethod = null;
                if (!spiderClass_iface.getInterfaces().isEmpty()) {//Search interfaces first
                    for(SpiderClass spiderClassInterface : spiderClass_iface.getInterfaces()) {
                        spiderMethod = spiderClassInterface.getSpiderMethod(obfuscation, name, desc);
                        if (spiderMethod != null) break;
                    }
                }
                if (spiderMethod == null) {
                    while ((spiderMethod = spiderClass_iface.getSpiderMethod(obfuscation, name, desc)) == null) {
                        spiderClass_iface = spiderClass_iface.getParent();
                        if (spiderClass_iface == null) break;
                    }
                }

                if (spiderClass_iface != null) {
                    MigrationClass migrationClass = migrationClasses.get(spiderClass_iface.getName().getName(obfuscation).replace('/', '.'));
                    if (migrationClass != null) return migrationClass.getMethod(spiderMethod.getName().getName(obfuscation), spiderMethod.getDesc().getDesc(obfuscation));
                }
                return null;
            }
        }

        SpiderClass spiderClass = srgMap.getSpiderClass(obfuscation, className);
        if (spiderClass != null) {
            SpiderMethod spiderMethod;
            while((spiderMethod = spiderClass.getSpiderMethod(obfuscation, name, desc)) == null) {
                spiderClass = spiderClass.getParent();
                if (spiderClass == null) break;
            }

            if (spiderMethod != null) {
                if ((spiderMethod.getName().getName(Obfuscation.OBFUSCATED).equals(name) && spiderMethod.getName().getName(Obfuscation.DEOBFUSCATED).equals(name)) && (spiderMethod.getDesc().getDesc(Obfuscation.OBFUSCATED).equals(desc) && spiderMethod.getDesc().getDesc(Obfuscation.DEOBFUSCATED).equals(desc))) {
                    return getMigrationMethod(srgMap, obfuscation, interfaces, spiderClass.getSoftParent(), name, desc);
                }

                MigrationClass migrationClass = migrationClasses.get(spiderClass.getName().getName(obfuscation).replace('/', '.'));
                if (migrationClass != null) return migrationClass.getMethod(spiderMethod.getName().getName(obfuscation), spiderMethod.getDesc().getDesc(obfuscation));
                return null;
            }
            return null;
        }
        return null;
    }

    public final MigrationMethod getMigrationMethod(final String className, final String name, final String desc) {
        return getMigrationMethod(className, name, desc, false);
    }

    public final MigrationMethod getMigrationMethod(final String className, final String name, final String desc, final boolean ignoreOwner) {
        if ((!ignoreOwner && className == null) || name == null || desc == null) return null;
        if (!ignoreOwner) {
            MigrationClass migrationClass = migrationClasses.get(className.replace('/', '.'));
            if (migrationClass != null) return migrationClass.getMethod(name, desc);
        } else {
            for(MigrationClass migrationClass : migrationClasses.values()) {
                MigrationMethod mm = migrationClass.getMethod(name, desc);
                if (mm != null) return mm;
            }
        }
        return null;
    }

    public final MigrationField getMigrationField(final String name, final String desc) {
        //TODO
        return null;
    }

}
