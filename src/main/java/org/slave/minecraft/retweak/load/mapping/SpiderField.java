package org.slave.minecraft.retweak.load.mapping;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slave.lib.api.family.ChildParent;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping;

/**
 * Created by Master on 3/16/2020 at 5:15 PM
 *
 * @author Master
 */
@AllArgsConstructor
@RequiredArgsConstructor
public final class SpiderField implements ChildParent<SpiderClass> {

    @Getter
    private final SpiderClass parent;

    @Getter
    private final ObfuscateRemapping.ObfuscationMapping.NameMapping name;

    @Getter
    @Setter(AccessLevel.PACKAGE)
    private ObfuscateRemapping.ObfuscationMapping.DescMapping desc;

}
