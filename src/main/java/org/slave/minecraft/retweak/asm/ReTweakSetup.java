package org.slave.minecraft.retweak.asm;

import cpw.mods.fml.relauncher.IFMLCallHook;
import net.minecraft.launchwrapper.LaunchClassLoader;
import okio.Buffer;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakClassLoader;
import org.slave.minecraft.retweak.load.mapping.SrgMap;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
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
        return null;
    }

    @SuppressWarnings("unchecked")
    private void loadMappings() throws IOException {
        for(GameVersion gameVersion : GameVersion.VALUES) {
            File mappingDir = new File(ReTweak.FILE_DIRECTORY_RETWEAK_MAPPINGS, gameVersion.getVersion());

            ReTweak.LOGGER_RETWEAK.info("Searching for {} mappings...", gameVersion.getVersion());

            if (gameVersion.isDisabled()) {
                ReTweak.LOGGER_RETWEAK.info("Game version is disabled. Not searching for mappings...");
                continue;
            }

            File notchMcpSrg = new File(mappingDir, "notch-mcp.srg");
            File packagedSrg = new File(mappingDir, "packaged.srg");
            File _super = new File(mappingDir, String.format("MC-%s.super", gameVersion.getVersion()));

            SrgMap srgMap;

            FileInputStream srgFS;
            if (notchMcpSrg.exists()) {
                srgFS = new FileInputStream(notchMcpSrg);
                ReTweak.LOGGER_RETWEAK.info("Found mapping \"notch-mcp.srg\" in path \"{}\"", notchMcpSrg.getPath());
            } else if (packagedSrg.exists()) {
                srgFS = new FileInputStream(packagedSrg);
                ReTweak.LOGGER_RETWEAK.info("Found mapping \"packaged.srg\" in path \"{}\"", packagedSrg.getPath());
            } else {
                ReTweak.LOGGER_RETWEAK.warn("Found no mapping for game version: {}", gameVersion.getVersion());
                continue;
            }

            try(Buffer buffer = new Buffer()) {
                buffer.readFrom(srgFS);
                srgMap = SrgMap.loadFromSrgMapping(buffer);
                buffer.clear();
            }

            if (!notchMcpSrg.exists() && packagedSrg.exists()) {//Make sure notch-mcp.srg does not exist when checking for packaged.srg - We don't want to load fields.csv and methods.csv for notch-mcp.srg
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

                try(FileInputStream fieldsFIS = new FileInputStream(fieldsCsv)) {
                    try(InputStreamReader fieldsISR = new InputStreamReader(fieldsFIS)) {
                        try {
                            srgMap.readCsv(SrgMap.CsvType.FIELDS, fieldsISR);
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                try(FileInputStream methodsFIS = new FileInputStream(methodsCsv)) {
                    try(InputStreamReader methodsISR = new InputStreamReader(methodsFIS)) {
                        try {
                            srgMap.readCsv(SrgMap.CsvType.METHODS, methodsISR);
                        } catch(IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            if (_super.exists()) {
                ReTweak.LOGGER_RETWEAK.info("Found super mapping \"{}\" in path \"{}\"", _super.getName(), _super.getPath());
                try(FileInputStream superFIS = new FileInputStream(_super)) {
                    try(Buffer superBuffer = new Buffer()) {
                        superBuffer.readFrom(superFIS);
                        srgMap.readSuper(superBuffer);
                    }
                }
            } else {
                ReTweak.LOGGER_RETWEAK.error("Super mapping \"{}\" was not found. No mappings will be loaded for this game version ({}) due to this.", _super.getPath(), gameVersion.getVersion());
                continue;
            }

            srgMap.sort();

            try {
                Field field = GameVersion.class.getDeclaredField("srgMap");
                field.setAccessible(true);
                field.set(gameVersion, srgMap);
            } catch(NoSuchFieldException e) {
                ReTweak.LOGGER_RETWEAK.error("Failed to find field \"srgMap\" in class \"" + GameVersion.class.getName() + "\"!", e);
            } catch (IllegalAccessException e) {
                ReTweak.LOGGER_RETWEAK.error("Could not access field \"srgMap\" in class \"" + GameVersion.class.getName() + "\"!?", e);
            }

            ReTweak.LOGGER_RETWEAK.info("Loaded mappings for game version: {}", gameVersion.getVersion());
        }
    }

}
