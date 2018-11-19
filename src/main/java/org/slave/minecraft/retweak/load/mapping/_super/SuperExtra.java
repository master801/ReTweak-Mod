package org.slave.minecraft.retweak.load.mapping._super;

import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.AliasString;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Created by master on 11/7/18 at 1:36 PM
 *
 * @author master
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SuperExtra {

    private final int opcode;

    private AliasString owner, name, desc;
    private AliasString type;

    public SuperExtra(final int opcode, final AliasString owner, final AliasString name, final AliasString desc) {
        this(opcode);
        this.owner = owner;
        this.name = name;
        this.desc = desc;
    }

    public SuperExtra(final int opcode, final AliasString type) {
        this(opcode);
        this.type = type;
    }

}
