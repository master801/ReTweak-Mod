package org.slave.minecraft.retweak.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slave.minecraft.retweak.asm.transformers.GsonTransformer;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master on 5/4/2016 at 10:44 PM.
 *
 * @author Master
 */
public final class ReTweakModConfig {

    public static final ReTweakModConfig INSTANCE = new ReTweakModConfig();

    private static final File MOD_CONFIG_FILE = new File(
            ReTweakResources.RETWEAK_CONFIG_DIRECTORY,
            "mods.json"
    );
    private static final String JSON_TAG_OBJECT_VERSION = "version";
    private static final String JSON_TAG_OBJECT_MODID = "modid";
    private static final String JSON_TAG_OBJECT_ENABLED = "enabled";
    private static final String JSON_TAG_ARRAY_MODS = "mods";

    private JsonArray jsonArray;

    private final Gson gson;

    private ReTweakModConfig() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gson = gsonBuilder.create();
        GsonTransformer.setIndent(
                gson,
                "    "
        );
    }

    public void update(boolean load) throws IOException {
        if (!ReTweakModConfig.MOD_CONFIG_FILE.exists()) {
            jsonArray = new JsonArray();
            for(GameVersion gameVersion : GameVersion.values()) {
                JsonObject versionObject = createVersionObject(
                        gameVersion,
                        ReTweakLoader.INSTANCE.getReTweakModDiscoverer().getModCandidates(gameVersion)
                );
                jsonArray.add(versionObject);
            }
            save();
            return;
        }

        if (load) {
            if (ReTweakModConfig.MOD_CONFIG_FILE.exists()) {
                jsonArray = gson.fromJson(
                        new FileReader(ReTweakModConfig.MOD_CONFIG_FILE),
                        JsonArray.class
                );
            } else {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Found no config file while loading!? This should not happen!"
                );
                update(false);
                return;
            }
        }

        for(JsonElement jsonElement : jsonArray) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            GameVersion gameVersion = GameVersion.getFromVersion(jsonObject.get(ReTweakModConfig.JSON_TAG_OBJECT_VERSION).getAsString());
            if (gameVersion == null) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Invalid version found in config! Version: \"{}\"",
                        jsonObject.get(ReTweakModConfig.JSON_TAG_OBJECT_VERSION)
                );
                continue;
            }
            JsonArray mods = jsonObject.getAsJsonArray(ReTweakModConfig.JSON_TAG_ARRAY_MODS);
            for(JsonElement element : mods) {
                JsonObject object = element.getAsJsonObject();
                for(ReTweakModCandidate reTweakModCandidate : ReTweakLoader.INSTANCE.getReTweakModDiscoverer().getModCandidates(gameVersion)) {
                    /*
                    for(String modid : reTweakModCandidate.getModClasses()) {
                        if (modid.equals(object.get(ReTweakModConfig.JSON_TAG_OBJECT_MODID).getAsString())) {
                            reTweakModCandidate.setEnabled(object.get(ReTweakModConfig.JSON_TAG_OBJECT_ENABLED).getAsBoolean());
                        }
                    }
                    */

                    //TODO
                }
            }
        }

        if (!load) save();
    }

    private void save() throws IOException {
        FileWriter fileWriter = new FileWriter(ReTweakModConfig.MOD_CONFIG_FILE);
        gson.toJson(
                jsonArray,
                fileWriter
        );
        fileWriter.flush();
        fileWriter.close();
    }

    private JsonObject[] createReTweakModObjects(ReTweakModCandidate reTweakModCandidate) {
        if (reTweakModCandidate == null) return null;
        List<JsonObject> jsonObjects = new ArrayList<>();
        for(String modid : reTweakModCandidate.getModIds()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(
                    ReTweakModConfig.JSON_TAG_OBJECT_MODID,
                    new JsonPrimitive(modid)
            );
            jsonObject.add(
                    ReTweakModConfig.JSON_TAG_OBJECT_ENABLED,
                    new JsonPrimitive(reTweakModCandidate.isEnabled())
            );
            jsonObjects.add(jsonObject);
        }
        return jsonObjects.toArray(new JsonObject[0]);
    }

    private JsonObject createVersionObject(GameVersion gameVersion, List<ReTweakModCandidate> reTweakModCandidates) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(
                ReTweakModConfig.JSON_TAG_OBJECT_VERSION,
                new JsonPrimitive(gameVersion.getVersion())
        );

        JsonArray mods = new JsonArray();

        for(ReTweakModCandidate reTweakModCandidate : reTweakModCandidates) {
            JsonObject[] modObjects = createReTweakModObjects(reTweakModCandidate);
            for(JsonObject modObject : modObjects) mods.add(modObject);
        }

        jsonObject.add(
                ReTweakModConfig.JSON_TAG_ARRAY_MODS,
                mods
        );
        return jsonObject;
    }

}
