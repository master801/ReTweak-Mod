package org.slave.minecraft.retweak.load.mapping.srg;

import com.google.common.collect.Lists;

import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationField;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationField.FactoryObfuscationField;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.DescMapping;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.NameMapping;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMethod;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMethod.FactoryObfuscationMethod;
import org.slave.lib.resources.Obfuscation;
import org.slave.minecraft.retweak.ReTweak;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import okio.Buffer;

/**
 * Created by master on 10/26/18 at 8:55 PM
 *
 * @author master
 */
public final class SrgMap {

    private final EnumMap<MapType, List<MappedType>> lines = new EnumMap<>(MapType.class);

    public SrgMap() {
        for (MapType mapType : MapType.VALUES) {
            lines.put(mapType, Lists.newArrayList());
        }
    }

    public void readFromLine(final String line) {
        if (line == null || line.isEmpty()) return;
        MapType mapType = MapType.getTypeFromLine(line);
        if (mapType == null) {
            if (!line.toUpperCase().startsWith("PK: ")) {//Ignore PK
                ReTweak.LOGGER_RETWEAK.warn("No type found for line \"{}\"!", line);
            }
            return;
        }

        MappedType mappedType = new MappedType(mapType);
        mappedType.readFromLine(line);

        List<MappedType> mappedTypes = lines.get(mapType);
        if (!mappedTypes.contains(mappedType)) {
            mappedTypes.add(mappedType);
        }
    }

    public void readCsv(final CsvType csvType, final Reader reader) throws IOException {
        CsvListReader csvListReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);

        boolean skip = true;//Skip first value - This is the header
        List<String[]> aliasList = new ArrayList<>();

        List<String> read;
        while((read = csvListReader.read()) != null) {
            if (skip) {
                skip = false;
                continue;
            }
            switch(csvType) {
                case FIELDS:
                    aliasList.add(read.toArray(new String[0]));
                    break;
                case METHODS:
                    aliasList.add(read.toArray(new String[0]));
                    break;
            }
        }

        List<MappedType> mappedTypes;
        switch(csvType) {
            case FIELDS:
                mappedTypes = lines.get(MapType.FD);

                for(String[] aliasArray : aliasList) {
                    String alias = aliasArray[1];//mcp
                    for(MappedType mappedType : mappedTypes) {
                        AliasString name = mappedType.getName(Obfuscation.DEOBFUSCATED);
                        if (name == null) continue;
                        if (name.getValue().equals(aliasArray[0])) name.setAlias(alias);
                    }
                }
                break;
            case METHODS:
                mappedTypes = lines.get(MapType.MD);

                for(String[] aliasArray : aliasList) {
                    String alias = aliasArray[1];//mcp
                    for(MappedType mappedType : mappedTypes) {
                        AliasString name = mappedType.getName(Obfuscation.DEOBFUSCATED);
                        if (name == null) continue;
                        if (name.getValue().equals(aliasArray[0])) name.setAlias(alias);
                    }
                }
                break;
        }
    }

    public List<MappedType> getMappedList(final MapType argType) {
        if (argType == null) return null;
        return lines.get(argType);
    }

    public MappedType getMapped(final MapType mapType, final Obfuscation obfuscation, final String name) {
        return getMapped(mapType, obfuscation, name, null);
    }

    public MappedType getMapped(final MapType mapType, final Obfuscation obfuscation, final String name, final String desc) {
        if (mapType == null || obfuscation == null || name == null) return null;

        List<MappedType> mappedTypeList = lines.get(mapType);
        for(MappedType mappedType : mappedTypeList) {
            AliasString aliasClass = mappedType.getClassName(obfuscation);
            AliasString aliasName = mappedType.getName(obfuscation);
            AliasString aliasDesc = mappedType.getDesc(obfuscation);

            switch(mapType) {
                case CL:
                    if (aliasClass.getValue().equals(name)) return mappedType;
                    break;
                case FD:
                case MD:
                    if (aliasName.getValue().equals(name)) {
                        if (desc == null) return null;
                        if (aliasDesc.getValue().equals(desc)) {
                            return mappedType;
                        }
                    }
                    break;
            }
        }
        return null;
    }

    /**
     * notch-mcp.srg/packaged.srg
     */
    public static SrgMap loadFromMapping(final Buffer buffer) throws IOException {
        SrgMap srgMap = new SrgMap();

        //<editor-fold desc="Read">
        String line;
        while((line = buffer.readUtf8Line()) != null) {
            srgMap.readFromLine(line);
        }
        //</editor-fold>

        return srgMap;
    }

    public static final class MappedType {

        private final MapType mapType;
        private final EnumMap<Obfuscation, EnumMap<ArgType, AliasString>> args = new EnumMap<>(Obfuscation.class);

        public MappedType(final MapType mapType) {
            this.mapType = mapType;
        }

        public void readFromLine(final String line) {
            String trimmedLine = line.substring(4);//Trim "XX: " from the line

            String[] split = trimmedLine.split(" ", mapType.getArgs());
            if (split.length != mapType.getArgs()) {
                throw new IllegalStateException(
                        String.format(
                                "Contents of split line does not match mapType args! Expected: %d, Got: %d",
                                mapType.getArgs(),
                                split.length
                        )
                );
            }

            EnumMap<ArgType, AliasString> obfArgMap = new EnumMap<>(ArgType.class);
            EnumMap<ArgType, AliasString> deobfArgMap = new EnumMap<>(ArgType.class);

            String[] cut;

            String combinedObf;
            String obfClassName;
            String obfName;

            String combinedDeobf;
            String deobfClassName;
            String deobfName;
            switch(mapType) {
                case CL:
                    //<editor-fold desc="Obf">
                    obfArgMap.put(ArgType.CLASS_NAME, new AliasString(split[0]));
                    //</editor-fold>

                    //<editor-fold desc="Deobf">
                    deobfArgMap.put(ArgType.CLASS_NAME, new AliasString(split[1]));
                    //</editor-fold>
                    break;
                case FD:
                    //<editor-fold desc="Obf">
                    combinedObf = split[0];

                    cut = MappedType.cut(combinedObf);
                    obfClassName = cut[0];
                    obfName = cut[1];

                    obfArgMap.put(ArgType.CLASS_NAME, new AliasString(obfClassName));
                    obfArgMap.put(ArgType.NAME, new AliasString(obfName));
                    //</editor-fold>

                    //<editor-fold desc="Deobf">
                    combinedDeobf = split[1];

                    cut = MappedType.cut(combinedDeobf);
                    deobfClassName = cut[0];
                    deobfName = cut[1];

                    deobfArgMap.put(ArgType.CLASS_NAME, new AliasString(deobfClassName));
                    deobfArgMap.put(ArgType.NAME, new AliasString(deobfName));//FD
                    //</editor-fold>
                    break;
                case MD:
                    //<editor-fold desc="Obf">
                    combinedObf = split[0];

                    cut = MappedType.cut(combinedObf);
                    obfClassName = cut[0];
                    obfName = cut[1];

                    obfArgMap.put(ArgType.CLASS_NAME, new AliasString(obfClassName));
                    obfArgMap.put(ArgType.NAME, new AliasString(obfName));
                    obfArgMap.put(ArgType.DESC, new AliasString(split[1]));
                    //</editor-fold>

                    //<editor-fold desc="Deobf">
                    combinedDeobf = split[2];

                    cut = MappedType.cut(combinedDeobf);
                    deobfClassName = cut[0];
                    deobfName = cut[1];

                    deobfArgMap.put(ArgType.CLASS_NAME, new AliasString(deobfClassName));
                    deobfArgMap.put(ArgType.NAME, new AliasString(deobfName));
                    deobfArgMap.put(ArgType.DESC, new AliasString(split[3]));
                    //</editor-fold>
                    break;
            }

            args.put(Obfuscation.OBFUSCATED, obfArgMap);
            args.put(Obfuscation.DEOBFUSCATED, deobfArgMap);
        }

        public AliasString getClassName(final Obfuscation obfuscation) {
            if (obfuscation == null || !args.containsKey(obfuscation)) return AliasString.ALIAS_STRING_EMPTY;
            return args.get(obfuscation).get(ArgType.CLASS_NAME);
        }

        public AliasString getName(final Obfuscation obfuscation) {
            if (obfuscation == null || !args.containsKey(obfuscation)) return AliasString.ALIAS_STRING_EMPTY;
            return args.get(obfuscation).get(ArgType.NAME);
        }

        /**
         * Used only for MD
         */
        public AliasString getDesc(final Obfuscation obfuscation) {
            if (obfuscation == null || !args.containsKey(obfuscation) || mapType != MapType.MD) return AliasString.ALIAS_STRING_EMPTY;
            return args.get(obfuscation).get(ArgType.DESC);
        }

        public ObfuscationField getAsObfuscationField(final boolean useAlias) {
            if (mapType != MapType.FD) return null;
            FactoryObfuscationField factoryObfuscationField = ObfuscationField.factory();

            factoryObfuscationField.setName(
                    NameMapping.factory()
                            .setName(Obfuscation.OBFUSCATED, getName(Obfuscation.OBFUSCATED).getValue())
                            .setName(Obfuscation.DEOBFUSCATED, useAlias && getName(Obfuscation.DEOBFUSCATED).getAlias() != null ? getName(Obfuscation.DEOBFUSCATED).getAlias() : getName(Obfuscation.DEOBFUSCATED).getValue())
                            .create()
            );

            return factoryObfuscationField.create();
        }

        public ObfuscationMethod getAsObfuscationMethod(final boolean useAlias) {
            if (mapType != MapType.MD) return null;
            FactoryObfuscationMethod factoryObfuscationMethod = ObfuscationMethod.factory();

            factoryObfuscationMethod.setName(
                    NameMapping.factory()
                            .setName(Obfuscation.OBFUSCATED, getName(Obfuscation.OBFUSCATED).getValue())
                            .setName(Obfuscation.DEOBFUSCATED, useAlias && getName(Obfuscation.DEOBFUSCATED).getAlias() != null ? getName(Obfuscation.DEOBFUSCATED).getAlias() : getName(Obfuscation.DEOBFUSCATED).getValue())
                            .create()
            );
            factoryObfuscationMethod.setDesc(
                    DescMapping.factory()
                            .setDesc(Obfuscation.OBFUSCATED, getDesc(Obfuscation.OBFUSCATED).getValue())
                            .setDesc(Obfuscation.DEOBFUSCATED, getDesc(Obfuscation.DEOBFUSCATED).getValue())
                            .create()
            );

            return factoryObfuscationMethod.create();
        }

        private static String[] cut(final String toCut) {
            if (toCut == null || toCut.lastIndexOf('/') == -1) return null;
            int last_index = toCut.lastIndexOf('/');
            String[] cut = new String[2];
            cut[0] = toCut.substring(0, last_index);
            cut[1] = toCut.substring(last_index + 1);
            return cut;
        }

    }

    @RequiredArgsConstructor
    public static final class AliasString {

        public static final AliasString ALIAS_STRING_EMPTY = new AliasString("");

        @Getter
        private final String value;

        @Setter
        @Getter
        private String alias;

        public static AliasString[] fromArray(final String[] strings) {
            if (strings == null || strings.length < 1) return null;
            AliasString[] aliasStrings = new AliasString[strings.length];
            for (int i = 0; i < strings.length; i++) {
                String string = strings[i];
                if (string == null) continue;
                aliasStrings[i] = string.isEmpty() ? AliasString.ALIAS_STRING_EMPTY : new AliasString(string);
            }
            return aliasStrings;
        }

    }

    @RequiredArgsConstructor
    public enum MapType {

        CL("CL: ", 2),

        FD("FD: ", 2),

        MD("MD: ", 4);

        public static final MapType[] VALUES = MapType.values();

        @Getter
        private final String header;

        @Getter
        private final int args;

        public static MapType getTypeFromLine(final String line) {
            if (line == null || line.isEmpty()) return null;
            String lineHeader = line.substring(0, 4);
            for (MapType mapType : MapType.VALUES) {
                if (lineHeader.equals(mapType.getHeader())) return mapType;
            }
            return null;
        }

    }

    public enum ArgType {

        CLASS_NAME,

        SUPER_CLASS_NAME,

        INTERFACE,

        NAME,

        DESC,

        SIGNATURE;

    }

    public enum CsvType {

        FIELDS,

        METHODS

    }

}
