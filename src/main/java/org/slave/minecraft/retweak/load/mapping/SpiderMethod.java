package org.slave.minecraft.retweak.load.mapping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.lib.api.family.ChildParent;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping;

/**
 * Created by Master on 3/16/2020 at 5:14 PM
 *
 * @author Master
 */
@RequiredArgsConstructor
public final class SpiderMethod implements ChildParent<SpiderClass> {

    @Getter
    private final SpiderClass parent;

    @Getter
    private final ObfuscateRemapping.ObfuscationMapping.NameMapping name;

    @Getter
    private final ObfuscateRemapping.ObfuscationMapping.DescMapping desc;

}
