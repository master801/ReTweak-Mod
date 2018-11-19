package org.slave.minecraft.retweak.load.mapping._super;

import com.google.common.collect.Lists;

import org.slave.lib.resources.Obfuscation;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mapping.srg.SrgMap;
import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.AliasString;
import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.MapType;
import org.slave.minecraft.retweak.load.mapping.srg.SrgMap.MappedType;

import java.util.EnumMap;
import java.util.List;

/**
 * Created by master on 10/29/18 at 12:48 PM
 *
 * @author master
 */
public final class SuperMap {

    private final EnumMap<Obfuscation, List<SuperType>> internalMap = new EnumMap<>(Obfuscation.class);

    public void addSuperType(final Obfuscation obfuscation, final SuperType superType) {
        if (!internalMap.containsKey(obfuscation)) internalMap.put(obfuscation, Lists.newArrayList());
        internalMap.get(obfuscation).add(superType);
    }

    public void deobfuscate(final SuperMap superMap, final SrgMap srgMap) {
        List<SuperType> obfuscated = internalMap.get(Obfuscation.OBFUSCATED);
        if (obfuscated == null || obfuscated.size() < 1) return;
        for(SuperType obfuscatedSuperType : obfuscated) {
            switch(obfuscatedSuperType.getMapType()) {
                case CL:
                    AliasString obfuscatedSuperName = obfuscatedSuperType.getSuperClassName();
                    MappedType mappedSuperType = srgMap.getMapped(MapType.CL, Obfuscation.OBFUSCATED, obfuscatedSuperName.getValue());

                    if (mappedSuperType != null) {
                        ReTweak.LOGGER_RETWEAK.info("");
                    }
                    break;
                case FD:

                    ReTweak.LOGGER_RETWEAK.info("");
                    break;
                case MD:
                    break;
            }

            ReTweak.LOGGER_RETWEAK.info("");
        }
    }

    private SuperType getClassSuperType(final Obfuscation obfuscation) {
        if (obfuscation == null) return null;
        List<SuperType> superTypeList = internalMap.get(obfuscation);
        if (superTypeList == null) return null;
        SuperType classSuperType = null;
        for(SuperType superType : superTypeList) {
            if (superType.getMapType() == MapType.CL) {
                if (classSuperType == null) {
                    classSuperType = superType;
                } else {
                    throw new NullPointerException("Duplicate CL entry?!");
                }
            }
        }
        return classSuperType;
    }

}
