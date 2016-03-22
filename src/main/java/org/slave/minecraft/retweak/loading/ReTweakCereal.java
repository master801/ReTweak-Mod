package org.slave.minecraft.retweak.loading;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.FileHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Master801 on 3/21/2016 at 12:39 PM.
 *
 * @author Master801
 */
@SuppressWarnings("unchecked")
public final class ReTweakCereal {

    public static final File RETWEAK_CONFIG_DIRECTORY = new File("config", ReTweakResources.RETWEAK_DIRECTORY.getName());
    public static final File RETWEAK_CONFIG_FILE = new File(ReTweakCereal.RETWEAK_CONFIG_DIRECTORY, "mods.json");

    public static final ReTweakCereal INSTANCE = new ReTweakCereal();

    private ReTweakCereal() {
    }

    public JSONArray initReTweakModConfig() throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_DIRECTORY.exists()) FileHelper.createDirectory(ReTweakCereal.RETWEAK_CONFIG_DIRECTORY);
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.exists()) {
            FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);

            JSONArray jsonArray = new JSONArray();
            for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) add(jsonArray, supportedGameVersion);
            jsonArray.writeJSONString(fileWriter);

            fileWriter.flush();
            fileWriter.close();

            return jsonArray;
        } else {
            return sanityChecks();
        }
    }

    public void readReTweakModConfig() throws IOException {
        initReTweakModConfig();
        //TODO?
    }

    public void writeReTweakModConfig(SupportedGameVersion supportedGameVersion, ReTweakModContainer[] reTweakModContainers) throws IOException {
        if (supportedGameVersion == null || ArrayHelper.isNullOrEmpty(reTweakModContainers)) return;
        JSONArray jsonArray = initReTweakModConfig();

        for(Object object : jsonArray) {
            JSONObject gameVersion = (JSONObject)object;
            if (gameVersion.get(Key.VERSION.toString()).equals(supportedGameVersion.getDirectoryName())) {
                JSONArray mods = (JSONArray)gameVersion.get(Key.MODS.toString());

                for(ReTweakModContainer reTweakModContainer : reTweakModContainers) {
                    boolean exists = false;
                    for(Object modObject : mods) {
                        exists = ((JSONObject)modObject).get(Key.MODID.toString()).equals(reTweakModContainer.getModid());
                        if (exists) break;
                    }
                    if (exists) {
                        ReTweakResources.RETWEAK_LOGGER.debug("ReTweak's config file already contains mod \"{}\"! Skipping it...", reTweakModContainer.getModid());
                        continue;
                    }
                    add(gameVersion, reTweakModContainer);
                }
            }
        }

        FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);
        jsonArray.writeJSONString(fileWriter);
        fileWriter.flush();
        fileWriter.close();
    }

    public void enable(ReTweakModContainer reTweakModContainer, Boolean enable) {
        if (reTweakModContainer == null) return;
        if (enable == null) enable = Boolean.TRUE;
        //TODO
    }

    private void add(JSONObject supportedGameVersion, ReTweakModContainer reTweakModContainer) throws IOException {
        if (supportedGameVersion.get(Key.VERSION.toString()) == null) return;
        JSONArray mods = (JSONArray)supportedGameVersion.get(Key.MODS.toString());
        if (mods == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Invalid ReTweak \"mods.json\" config file!"
            );
            return;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Key.ENABLE.toString(), Boolean.TRUE);
        jsonObject.put(Key.MODID.toString(), reTweakModContainer.getModid());
        mods.add(jsonObject);
    }

    private void remove(JSONObject supportedGameVersion, ReTweakModContainer reTweakModContainer) throws IOException {
        if (supportedGameVersion.get(Key.VERSION.toString()) == null) return;
        JSONArray mods = (JSONArray)supportedGameVersion.get(Key.MODS.toString());
        if (mods == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Invalid ReTweak \"mods.json\" config file!"
            );
            return;
        }
        for(Object object : mods) {
            if (object instanceof JSONObject) {
                String modid = (String)((JSONObject)object).get("modid");
                if (!StringHelper.isNullOrEmpty(modid) && modid.equals(reTweakModContainer.getModid())) {
                    mods.remove(object);
                    break;
                }
            }
        }
    }

    private void add(JSONArray mainArray, SupportedGameVersion supportedGameVersion) throws IOException {
        JSONObject jsonObject = new JSONObject();

        jsonObject.put(Key.VERSION.toString(), supportedGameVersion.getDirectoryName());
        jsonObject.put(Key.MODS.toString(), new JSONArray());

        mainArray.add(jsonObject);
    }

    private void remove(JSONArray mainArray, SupportedGameVersion supportedGameVersion) throws IOException {
        Iterator iterator = mainArray.iterator();
        while(iterator.hasNext()) {
            JSONObject next = (JSONObject)iterator.next();

            String version = (String)next.get(Key.VERSION.toString());
            if (!StringHelper.isNullOrEmpty(version) && version.equals(supportedGameVersion.getDirectoryName())) {
                iterator.remove();
                break;
            }

        }
    }

    private void enable(JSONObject reTweakModContainer, boolean enable) {
        if (reTweakModContainer.get(Key.MODID.toString()) == null) return;
        reTweakModContainer.put(Key.ENABLE.toString(), enable);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private JSONArray sanityChecks() throws IOException {
        FileReader fileReader = new FileReader(ReTweakCereal.RETWEAK_CONFIG_FILE);

        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray)new JSONParser().parse(fileReader);
        } catch(ParseException e) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "ReTweak's \"mods.json\" file is invalid!"
            );
        } catch(ClassCastException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Excepted an array but got something else!\nInvalid \"mods.json\" file!\nDeleting..."
            );
            fileReader.close();
            ReTweakCereal.RETWEAK_CONFIG_FILE.delete();
            return null;
        }
        if (jsonArray == null) return null;

        for(Object object : jsonArray) {
            if (!(object instanceof JSONObject)) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "Expected an object but got something else!\nInvalid \"mods.json\" file!\nDeleting..."
                );
                fileReader.close();
                ReTweakCereal.RETWEAK_CONFIG_FILE.delete();
                return null;
            }

            JSONObject jsonObject = (JSONObject)object;

            Object version = jsonObject.get(Key.VERSION.toString());
            Object mods = jsonObject.get(Key.MODS.toString());

            if (version == null || !(version instanceof String)) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "No key \"{}\" found for object \"{}\"!\nInvalid \"mods.json\" file!\nDeleting...",
                        Key.VERSION,
                        jsonObject
                );
                fileReader.close();
                ReTweakCereal.RETWEAK_CONFIG_FILE.delete();
                return null;
            } else {
                boolean exists = false;
                for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
                    if (supportedGameVersion.getDirectoryName().equals(version)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    ReTweakResources.RETWEAK_LOGGER.error(
                            "Version \"{}\" is not supported!",
                            version
                    );
                    fileReader.close();
                    return null;
                }
            }
            if (mods == null || !(mods instanceof JSONArray)) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "No key \"{}\" found for object \"{}\"!\nInvalid \"mods.json\" file!\nDeleting...",
                        Key.MODS,
                        jsonObject
                );
                fileReader.close();
                ReTweakCereal.RETWEAK_CONFIG_FILE.delete();
                return null;
            } else {
                JSONArray modsArray = (JSONArray)mods;
                Iterator iterator = modsArray.iterator();
                while(iterator.hasNext()) {
                    Object object_ = iterator.next();

                    if (object_ == null || !(object_ instanceof JSONObject)) {
                        ReTweakResources.RETWEAK_LOGGER.warn(
                                "Invalid entry! Entry: {}",
                                object_
                        );
                        continue;
                    }

                    JSONObject mod = (JSONObject)object_;

                    Object modid = mod.get(Key.MODID.toString());
                    Object enable = mod.get(Key.ENABLE.toString());

                    if (modid == null) {
                        fileReader.close();
                        iterator.remove();//Remove this invalid entry

                        //Rewrite it
                        FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);
                        jsonArray.writeJSONString(fileWriter);
                        fileWriter.flush();
                        fileWriter.close();

                        return null;
                    }

                    if (enable == null) mod.put(Key.ENABLE.toString(), Boolean.TRUE);//Write true if read as an invalid
                }
            }
        }

        fileReader.close();

        return jsonArray;
    }

    private enum Key {

        ENABLE("enable"),

        VERSION("version"),

        MODID("modid"),

        MODS("mods");

        private final String key;

        Key(final String key) {
            this.key = key;
        }

        @Override
        public String toString() {
            return key;
        }

    }

}
