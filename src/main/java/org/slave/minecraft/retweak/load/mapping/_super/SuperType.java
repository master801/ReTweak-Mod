package org.slave.minecraft.retweak.load.mapping._super;

import com.google.common.collect.Lists;

import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.AliasString;
import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.ArgType;
import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.MapType;

import java.util.EnumMap;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by master on 11/7/18 at 1:04 PM
 *
 * @author master
 */
@RequiredArgsConstructor(access = AccessLevel.PUBLIC)
public final class SuperType {

    @Getter
    private final MapType mapType;

    private final EnumMap<ArgType, AliasString[]> argType = new EnumMap<>(ArgType.class);
    private List<SuperExtra> extras;

    public SuperType setClassName(final AliasString aliasString) {
        if (mapType != MapType.CL || aliasString == null) return this;
        argType.put(
                ArgType.CLASS_NAME,
                new AliasString[] {
                        aliasString
                }
        );
        return this;
    }

    public SuperType setSuperClassName(final AliasString aliasString) {
        if (mapType != MapType.CL || aliasString == null) return this;
        argType.put(
                ArgType.SUPER_CLASS_NAME,
                new AliasString[] {
                        aliasString
                }
        );
        return this;
    }

    public SuperType setInterfaces(final AliasString[] aliasStrings) {
        if (mapType != MapType.CL || aliasStrings == null || aliasStrings.length < 1) return this;
        argType.put(
                ArgType.INTERFACE,
                aliasStrings
        );
        return this;
    }

    public SuperType setName(final AliasString aliasString) {
        if ((mapType != MapType.FD && mapType != MapType.MD) || aliasString == null) return this;
        argType.put(
                ArgType.NAME,
                new AliasString[] {
                        aliasString
                }
        );
        return this;
    }

    public SuperType setDesc(final AliasString aliasString) {
        if ((mapType != MapType.FD && mapType != MapType.MD) || aliasString == null) return this;
        argType.put(
                ArgType.DESC,
                new AliasString[] {
                        aliasString
                }
        );
        return this;
    }

    public SuperType setSignature(final AliasString aliasString) {
        if ((mapType != MapType.FD && mapType != MapType.MD) || aliasString == null) return this;
        argType.put(
                ArgType.SIGNATURE,
                new AliasString[] {
                        aliasString
                }
        );
        return this;
    }

    public SuperType addExtra(final int opcode, final AliasString type) {
        if (mapType != MapType.MD || opcode == -1 || type == null) return this;
        if (extras == null) extras = Lists.newArrayList();
        extras.add(
                new SuperExtra(opcode, type)
        );
        return this;
    }

    public SuperType addExtra(final int opcode, final AliasString owner, final AliasString name, final AliasString desc) {
        if (mapType != MapType.MD || opcode == -1 || owner == null || name == null || desc == null) return this;
        if (extras == null) extras = Lists.newArrayList();
        extras.add(
                new SuperExtra(opcode, owner, name, desc)
        );
        return this;
    }

    public AliasString getClassName() {
        if (mapType != MapType.CL) return AliasString.ALIAS_STRING_EMPTY;
        return argType.get(ArgType.CLASS_NAME)[0];
    }

    public AliasString getSuperClassName() {
        if (mapType != MapType.CL) return AliasString.ALIAS_STRING_EMPTY;
        return argType.get(ArgType.SUPER_CLASS_NAME)[0];
    }

    public AliasString[] getInterfaceNames() {
        if (mapType != MapType.CL) return new AliasString[0];
        return argType.get(ArgType.INTERFACE);
    }

}
