package org.slave.minecraft.retweak.load.mapping;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import okio.Buffer;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping;
import org.slave.lib.resources.ASMTable;
import org.slave.lib.resources.Obfuscation;
import org.slave.lib.util.WrappingDataT;
import org.slave.minecraft.retweak.ReTweak;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 3/16/2020 at 4:22 PM
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SrgMap {

    private final List<SpiderClass> classList = new ArrayList<>();

    public void readCsv(final CsvType csvType, final Reader reader) throws IOException {
        if (csvType == null || reader == null) return;
        CsvListReader csvListReader = new CsvListReader(reader, CsvPreference.STANDARD_PREFERENCE);
        List<String> line;//searge,name,side,desc
        while((line = csvListReader.read()) != null) {
            for(SpiderClass spiderClass : classList) {
                switch(csvType) {
                    case FIELDS:
                        for(SpiderField spiderField : spiderClass.fields) {
                            if (spiderField.getName().getName(Obfuscation.DEOBFUSCATED).equals(line.get(0))) {
                                spiderField.getName().setName(Obfuscation.DEOBFUSCATED, line.get(1));
                            }
                        }
                        break;
                    case METHODS:
                        for(SpiderMethod spiderMethod : spiderClass.methods) {
                            if (spiderMethod.getName().getName(Obfuscation.DEOBFUSCATED).equals(line.get(0))) {
                                spiderMethod.getName().setName(Obfuscation.DEOBFUSCATED, line.get(1));
                            }
                        }
                        break;
                }
            }
        }
    }

    public void readSuper(final Buffer buffer) throws IOException {
        String line;
        while((line = buffer.readUtf8Line()) != null) {
            if (line.startsWith("#")) continue;//Skip comments

            String[] parts = line.split(" ");
            if (parts[0].equals(MapType.SUPER.getHeader())) {
                for(SpiderClass spiderClass : classList) {
                    if(spiderClass.getName().getName(Obfuscation.DEOBFUSCATED).equals(parts[1])) {
                        for(SpiderClass superSpiderClass  : classList) {
                            if (superSpiderClass.getName().getName(Obfuscation.DEOBFUSCATED).equals(parts[2])) {
                                spiderClass.setParent(superSpiderClass);
                                break;
                            }
                        }

                        if (spiderClass.getParent() == null) {
                            spiderClass.setSoftParent(parts[2]);
                        }
                    }
                }
            }
        }
    }

    public void merge(final ASMTable asmTable) {
        for(ASMTable.TableClass tableClass : asmTable.getTableClasses()) {
            boolean conflicting = false;
            for(SpiderClass spiderClass : classList) {
                if (spiderClass.getName().getName(Obfuscation.OBFUSCATED).equals(tableClass.getName()) || spiderClass.getName().getName(Obfuscation.DEOBFUSCATED).equals(tableClass.getName())) {
                    ReTweak.LOGGER_RETWEAK.warn("Class \"{}\" already has a SpiderClass!", tableClass.getName());
                    conflicting = true;
                }
            }
            if (conflicting) continue;
            SpiderClass spiderClass = new SpiderClass(
                    ObfuscateRemapping.ObfuscationMapping.NameMapping.factory()
                            .setName(tableClass.getName())
                            .create()
            );
            spiderClass.setSoftParent(tableClass.getSuperName());

            for(ASMTable.TableClass.TableField tableField : tableClass.getFields()) {
                spiderClass.addSpiderField(
                        new SpiderField(
                                spiderClass,
                                ObfuscateRemapping.ObfuscationMapping.NameMapping.factory()
                                        .setName(tableField.getName())
                                        .create(),
                                ObfuscateRemapping.ObfuscationMapping.DescMapping.factory()
                                        .setDesc(tableField.getDesc())
                                        .create()
                        )
                );
            }

            for(ASMTable.TableClass.TableMethod tableMethod : tableClass.getMethods()) {
                spiderClass.addSpiderMethod(
                        new SpiderMethod(
                                spiderClass,
                                ObfuscateRemapping.ObfuscationMapping.NameMapping.factory()
                                        .setName(tableMethod.getName())
                                        .create(),
                                ObfuscateRemapping.ObfuscationMapping.DescMapping.factory()
                                        .setDesc(tableMethod.getDesc())
                                        .create()
                        )
                );
            }

            classList.add(spiderClass);
        }
    }

    public void sort() {
        for(SpiderClass spiderClass : classList) {
            if(spiderClass.getParent() == null && spiderClass.getSoftParent() == null) {
                ReTweak.LOGGER_RETWEAK.warn("Spider class \"{}\" (\"{}\") does not have a set parent nor \'soft parent\'", spiderClass.getName().getName(Obfuscation.OBFUSCATED), spiderClass.getName().getName(Obfuscation.DEOBFUSCATED));
                continue;
            } else if (spiderClass.getParent() == null && spiderClass.getSoftParent() != null) {
                for(SpiderClass iterating : classList) {
                    if (iterating.getName().getName(Obfuscation.OBFUSCATED).equals(spiderClass.getSoftParent())) {
                        spiderClass.setParent(iterating);
                        break;
                    }
                }
            }
        }
    }

    public static SrgMap loadFromSrgMapping(final Buffer buffer) {
        SrgMap srgMap = new SrgMap();

        String line;
        try {
            while((line = buffer.readUtf8Line()) != null) {
                if (line.startsWith("#")) continue;//Skip comments
                String[] parts = line.split(" ");

                MapType mapType = MapType.getFrom(parts[0]);
                if (mapType != null) {
                    if (mapType == MapType.PACKAGE) continue;//Skip PK

                    int oi, doi;
                    String obfuscatedClass, obfuscatedName;
                    String deobfuscatedClass, deobfuscatedName;
                    SpiderClass spiderClass;
                    switch(mapType) {
                        case CLASS:
                            srgMap.classList.add(
                                    new SpiderClass(
                                            ObfuscateRemapping.ObfuscationMapping.NameMapping.factory()
                                                    .setName(Obfuscation.OBFUSCATED, parts[1])
                                                    .setName(Obfuscation.DEOBFUSCATED, parts[2])
                                                    .create()
                                    )
                            );
                            break;
                        case FIELD:
                            oi = parts[1].lastIndexOf('/');
                            obfuscatedClass = parts[1].substring(0, oi);
                            obfuscatedName = parts[1].substring(oi + 1);

                            doi = parts[2].lastIndexOf('/');
                            deobfuscatedClass = parts[2].substring(0, doi);
                            deobfuscatedName = parts[2].substring(doi + 1);


                            spiderClass = srgMap.getSpiderClass(Obfuscation.OBFUSCATED, obfuscatedClass);
                            if (spiderClass == null) {
                                ReTweak.LOGGER_RETWEAK.error("Spider class not found?!");
                                continue;
                            }

                            spiderClass.addSpiderField(
                                    new SpiderField(
                                            spiderClass,
                                            ObfuscateRemapping.ObfuscationMapping.NameMapping.factory()
                                                    .setName(Obfuscation.OBFUSCATED, obfuscatedName)
                                                    .setName(Obfuscation.DEOBFUSCATED, deobfuscatedName)
                                                    .create()
                                    )
                            );
                            break;
                        case METHOD:
                            oi = parts[1].lastIndexOf('/');
                            obfuscatedClass = parts[1].substring(0, oi);
                            obfuscatedName = parts[1].substring(oi + 1);

                            doi = parts[3].lastIndexOf('/');
                            deobfuscatedClass = parts[3].substring(0, doi);
                            deobfuscatedName = parts[3].substring(doi + 1);


                            spiderClass = srgMap.getSpiderClass(Obfuscation.OBFUSCATED, obfuscatedClass);
                            if (spiderClass == null) {
                                ReTweak.LOGGER_RETWEAK.error("Spider class not found?!");
                                continue;
                            }

                            SpiderMethod spiderMethod = new SpiderMethod(
                                    spiderClass,
                                    ObfuscateRemapping.ObfuscationMapping.NameMapping.factory()
                                            .setName(Obfuscation.OBFUSCATED, obfuscatedName)
                                            .setName(Obfuscation.DEOBFUSCATED, deobfuscatedName)
                                            .create(),
                                    ObfuscateRemapping.ObfuscationMapping.DescMapping.factory()
                                            .setDesc(Obfuscation.OBFUSCATED, parts[2])
                                            .setDesc(Obfuscation.DEOBFUSCATED, parts[4])
                                            .create()
                            );

                            spiderClass.addSpiderMethod(spiderMethod);
                            break;
                    }
                } else {
                    ReTweak.LOGGER_RETWEAK.error("Unknown map type for line \"{}\"!", line);
                }
            }
        } catch (EOFException e) {
            e.printStackTrace();
        }

        return srgMap;
    }

    public SpiderClass getSpiderClass(final Obfuscation obfuscation, final String obfuscatedClass) {
        if (obfuscation == null || obfuscatedClass == null) return null;
        for(SpiderClass spiderClass : classList) {
            if (spiderClass.getName().getName(obfuscation).equals(obfuscatedClass)) return spiderClass;
        }
        return null;
    }

    public WrappingDataT.WrappingDataT2<SpiderClass, SpiderField> getSpiderClassField(final Obfuscation obfuscation, final String className, final String name, final String desc) {
        if (className == null || name == null) return null;
        SpiderClass spiderClass = getSpiderClass(obfuscation, className);
        if (spiderClass != null) {
            SpiderField spiderField;
            while((spiderField = spiderClass.getSpiderField(obfuscation, name, desc)) == null) {
                spiderClass = spiderClass.getParent();
                if (spiderClass == null) break;
            }
            if (spiderClass == null) return null;
            return new WrappingDataT.WrappingDataT2<>(spiderClass, spiderField);
        }
        return null;
    }

    public WrappingDataT.WrappingDataT2<SpiderClass, SpiderMethod> getSpiderClassMethod(final Obfuscation obfuscation, final String className, final String name, final String desc) {
        if (className == null || name == null || desc == null) return null;
        SpiderClass spiderClass = getSpiderClass(obfuscation, className);
        if (spiderClass != null) {
            SpiderMethod spiderMethod;
            while((spiderMethod = spiderClass.getSpiderMethod(obfuscation, name, desc)) == null) {
                spiderClass = spiderClass.getParent();
                if (spiderClass == null) break;
            }
            if (spiderClass == null) return null;
            return new WrappingDataT.WrappingDataT2<>(spiderClass, spiderMethod);
        }
        return null;
    }

    @RequiredArgsConstructor
    public enum MapType {

        PACKAGE("PK:", 2),

        CLASS("CL:", 2),

        FIELD("FD:", 2),

        METHOD("MD:", 4),

        SUPER("SUPER:", 2);

        public static final MapType[] VALUES = MapType.values();

        @Getter
        private final String header;

        @Getter
        private final int args;

        public static MapType getFrom(final String from) {
            for(MapType mapType : VALUES) {
                if (mapType.header.equals(from)) return mapType;
            }
            return null;
        }

    }

    public enum CsvType {

        FIELDS,

        METHODS;

    }

}
