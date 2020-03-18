package org.slave.minecraft.retweak.load.mapping;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slave.lib.api.family.ChildParent;
import org.slave.lib.api.family.Parent;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping;
import org.slave.lib.resources.Obfuscation;
import org.slave.minecraft.retweak.ReTweak;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 3/16/2020 at 5:14 PM
 *
 * @author Master
 */
@RequiredArgsConstructor()
@AllArgsConstructor()
public final class SpiderClass implements Parent<SpiderClass>, ChildParent<SpiderClass> {

    @Getter()
    @Setter(AccessLevel.PACKAGE)
    private SpiderClass parent;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private String softParent;

    @Getter
    private final ObfuscateRemapping.ObfuscationMapping.NameMapping name;

    final List<SpiderField> fields = new ArrayList<>();
    final List<SpiderMethod> methods =  new ArrayList<>();

    public SpiderField getSpiderField(final Obfuscation obfuscation, final String name) {
        return getSpiderField(obfuscation, name, null);
    }

    public SpiderField getSpiderField(final Obfuscation obfuscation, final String name, final String desc) {
        if (obfuscation == null || name == null) return null;
        for(SpiderField spiderField : fields) {
            if (spiderField.getName().getName(obfuscation).equals(name) && (desc == null || spiderField.getDesc() == null || spiderField.getDesc().getDesc(obfuscation).equals(desc))) return spiderField;
        }
        return null;
    }

    public SpiderMethod getSpiderMethod(final Obfuscation obfuscation, final String name, final String desc) {
        if (obfuscation == null || name == null || desc == null) return null;
        for(SpiderMethod spiderMethod : methods) {
            if (spiderMethod.getName().getName(obfuscation).equals(name) && spiderMethod.getDesc().getDesc(obfuscation).equals(desc)) return spiderMethod;
        }
        return null;
    }

    void addSpiderField(final SpiderField spiderField) {
        if (spiderField == null) return;
        if (fields.contains(spiderField)) {
            ReTweak.LOGGER_RETWEAK.warn(
                    "Class \"{}\" already contains field \"{}\"!",
                    getName().getName(Obfuscation.OBFUSCATED),
                    spiderField.getName().getName(Obfuscation.OBFUSCATED)
            );
            return;
        }

        fields.add(spiderField);
    }

    void addSpiderMethod(final SpiderMethod spiderMethod) {
        if (spiderMethod == null) return;
        if (methods.contains(spiderMethod)) {
            ReTweak.LOGGER_RETWEAK.warn(
                    "Class \"{}\" already contains method \"{} {}\"!",
                    getName().getName(Obfuscation.OBFUSCATED),
                    spiderMethod.getName().getName(Obfuscation.OBFUSCATED),
                    spiderMethod.getDesc().getDesc(Obfuscation.OBFUSCATED)
            );
            return;
        }

        methods.add(spiderMethod);
    }

}
