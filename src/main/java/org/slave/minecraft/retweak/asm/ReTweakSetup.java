package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import okio.Buffer;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationClass;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationClass.FactoryObfuscationClass;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationField;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationField.FactoryObfuscationField;
import org.slave.lib.obfuscate_mapping.ObfuscateRemapping.ObfuscationMapping.NameMapping;
import org.slave.lib.resources.Obfuscation;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakClassLoader;
import org.slave.minecraft.retweak.load.ReTweakLoader;
import org.slave.minecraft.retweak.load.mapping.SrgMap;
import org.slave.minecraft.retweak.load.mapping.SrgMap.AliasString;
import org.slave.minecraft.retweak.load.mapping.SrgMap.CsvType;
import org.slave.minecraft.retweak.load.mapping.SrgMap.MapType;
import org.slave.minecraft.retweak.load.mapping.SrgMap.MappedType;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by master on 2/25/18 at 9:27 PM
 *
 * @author master
 */
public final class ReTweakSetup implements IFMLCallHook {

    private LaunchClassLoader launchClassLoader;

    @Override
    public void injectData(final Map<String, Object> data) {
        launchClassLoader = (LaunchClassLoader)data.get("classLoader");
    }

    @SuppressWarnings("unchecked")
    @Override
    public Void call() throws Exception {
        //<editor-fold desc="Populate class loaders">
        //Yes, I know using reflection on my own classes is dumb, but the field is private for a reason - to signify that it should not be modified (although we're doing it anyway...)
        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakClassLoader reTweakClassLoader = new ReTweakClassLoader(gameVersion, launchClassLoader);
            reTweakClassLoader.addExclusion("org.slave.minecraft.retweak.load.asm.tweak.migrate.");

            Field field = ReTweakClassLoader.class.getDeclaredField("INSTANCES");
            field.setAccessible(true);

            Map<GameVersion, ReTweakClassLoader> instances = (Map<GameVersion, ReTweakClassLoader>)field.get(null);
            instances.put(gameVersion, reTweakClassLoader);

            field.setAccessible(false);
        }
        //</editor-fold>

        loadMappings();
        constructClassMappings();
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadMappings() throws IOException {
        Map<GameVersion, SrgMap> mappings;

        try {
            Field fieldMappings = ReTweakLoader.class.getDeclaredField("mappings");
            fieldMappings.setAccessible(true);
            mappings = (Map<GameVersion, SrgMap>)fieldMappings.get(ReTweakLoader.instance());
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get mappings field from ReTweakLoader?! This really should not happen!!"
            );
            return;
        }

        for(GameVersion gameVersion : GameVersion.VALUES) {
            File mappingDir = new File(ReTweak.FILE_DIRECTORY_RETWEAK_MAPPINGS, gameVersion.getVersion());

            ReTweak.LOGGER_RETWEAK.info(
                    "Searching for {} mappings...",
                    gameVersion.getVersion()
            );

            File notchMcpSrg = new File(mappingDir, "notch-mcp.srg");
            File packagedSrg = new File(mappingDir, "packaged.srg");

            SrgMap srgMap;

            FileInputStream srgFS;
            if (notchMcpSrg.exists()) {
                srgFS = new FileInputStream(notchMcpSrg);
                ReTweak.LOGGER_RETWEAK.info(
                        "Found \"notch-mcp.srg\" mapping at \"{}\"",
                        notchMcpSrg.getPath()
                );
            } else if (packagedSrg.exists()) {
                srgFS = new FileInputStream(packagedSrg);
                ReTweak.LOGGER_RETWEAK.info(
                        "Found \"packaged.srg\" mapping at \"{}\"",
                        packagedSrg.getPath()
                );
            } else {
                ReTweak.LOGGER_RETWEAK.warn(
                        "Found no mapping for game version: {}",
                        gameVersion.getVersion()
                );
                continue;
            }

            BufferedInputStream srgBIS = new BufferedInputStream(srgFS);

            Buffer buffer = new Buffer();
            buffer.readFrom(srgFS);

            srgMap = SrgMap.loadFromMapping(buffer);

            buffer.clear();
            buffer.close();

            srgBIS.close();
            srgFS.close();

            if (!notchMcpSrg.exists() && packagedSrg.exists()) {//Make sure notch-mcp.srg does not exist when checking for packaged.srg - We don't want to load fields.csv and methods.csv for packaged.srg
                File fieldsCsv = new File(mappingDir, "fields.csv");
                File methodsCsv = new File(mappingDir, "methods.csv");

                if (!fieldsCsv.exists() || !methodsCsv.exists()) {
                    ReTweakASM.LOGGER_RETWEAK_ASM.warn(
                            "Csv files \"{}\" and \"{}\" do not exist when mapping \"{}\" exists!",
                            fieldsCsv.getPath(),
                            methodsCsv.getPath(),

                            packagedSrg.getPath()
                    );
                    continue;
                }

                FileInputStream fieldsFIS = new FileInputStream(fieldsCsv);
                InputStreamReader fieldsISR = new InputStreamReader(fieldsFIS);
                srgMap.readCsv(CsvType.FIELDS, fieldsISR);
                fieldsISR.close();
                fieldsFIS.close();

                FileInputStream methodsFIS = new FileInputStream(methodsCsv);
                InputStreamReader methodsISR = new InputStreamReader(methodsFIS);
                srgMap.readCsv(CsvType.METHODS, methodsISR);
                methodsISR.close();
                methodsFIS.close();
            }

            mappings.put(gameVersion, srgMap);

            ReTweak.LOGGER_RETWEAK.info(
                    "Loaded mappings for game version: {}",
                    gameVersion.getVersion()
            );
        }
    }

    private void constructClassMappings() throws Exception {
        for(GameVersion gameVersion : GameVersion.VALUES) {
            List<ObfuscationClass> obfuscationClassList = new ArrayList<>();

            SrgMap srgMap = ReTweakLoader.instance().getMapping(gameVersion);
            if (srgMap == null) continue;

            List<MappedType> clList = srgMap.getMapped(MapType.CL);
            for(MappedType cl : clList) {
                AliasString obfuscatedName = cl.getClassName(Obfuscation.OBFUSCATED);
                AliasString deobfuscatedName = cl.getClassName(Obfuscation.DEOBFUSCATED);
                if (obfuscatedName == null || deobfuscatedName == null) continue;

                FactoryObfuscationClass factoryObfuscationClass = ObfuscationClass.factory()
                        .setName(
                                NameMapping.factory()
                                        .setName(Obfuscation.OBFUSCATED, obfuscatedName.getValue())
                                        .setName(Obfuscation.DEOBFUSCATED, deobfuscatedName.getValue())
                                        .create()
                        );

                List<MappedType> fdList = srgMap.getMapped(MapType.FD);
                for(MappedType fd : fdList) {
                    AliasString className = fd.getClassName(Obfuscation.DEOBFUSCATED);
                    if (className != null && className.getValue().equals(deobfuscatedName.getValue())) {
                        AliasString fdNameObf = fd.getName(Obfuscation.OBFUSCATED);
                        AliasString fdNameDeobf = fd.getName(Obfuscation.DEOBFUSCATED);
                        if (fdNameObf == null || fdNameDeobf == null) continue;

                        FactoryObfuscationField factoryObfuscationField = ObfuscationField.factory();
                        factoryObfuscationField.setName(
                                NameMapping.factory()
                                        .setName(Obfuscation.OBFUSCATED, fdNameObf.getValue())
                                        .setName(Obfuscation.DEOBFUSCATED, fdNameObf.getAlias())
                                        .create()
                        );

                        ObfuscationField obfuscationField = factoryObfuscationField.create();
                        factoryObfuscationClass.addField(obfuscationField);
                    }
                }

                ObfuscationClass obfuscationClass = factoryObfuscationClass.create();

                obfuscationClassList.add(obfuscationClass);
            }
        }
    }

}
