package org.slave.minecraft.retweak.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.slave.lib.helpers.FileHelper;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrape;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrape.ReTweakGrapeDeserializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrape.ReTweakGrapeSerializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine.ReTweakGrapeVineDeserializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine.ReTweakGrapeVineSerializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakPeach;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Master on 4/24/2016 at 10:09 AM.
 *
 * @author Master
 */
public final class ReTweakMilk {

    private static final File CONFIG_FILE = new File(
            ReTweakResources.RETWEAK_CONFIG_DIRECTORY,
            "mods.json"
    );

    public static final ReTweakMilk INSTANCE = new ReTweakMilk();

    private final Gson gson;

    private JsonArray config = null;

    private ReTweakMilk() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(
                ReTweakGrapeVine.class,
                new ReTweakGrapeVineSerializer()
        );
        gsonBuilder.registerTypeAdapter(
                ReTweakGrapeVine.class,
                new ReTweakGrapeVineDeserializer()
        );
        gsonBuilder.registerTypeAdapter(
                ReTweakGrape.class,
                new ReTweakGrapeSerializer()
        );
        gsonBuilder.registerTypeAdapter(
                ReTweakGrape.class,
                new ReTweakGrapeDeserializer()
        );

        gson = gsonBuilder.create();
    }

    public void update(boolean load) throws IOException {
        if (config == null) cleanCreation();
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            JsonArray mods = getMods(supportedGameVersion);
            if (mods == null) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "Could not find \"{}\" array for version \"{}\", in JSON config?",
                        ReTweakPeach.JSON_MEMBER_NAME_MODS,
                        supportedGameVersion.getVersion()
                );
                continue;
            }
            for(ReTweakModContainer reTweakModContainer : ReTweakLoader.INSTANCE.getModContainers(supportedGameVersion)) {
                mods.add(gson.toJsonTree(
                        reTweakModContainer.toGrape(),
                        ReTweakGrape.class
                ));
            }
        }

        if (!load || !ReTweakMilk.CONFIG_FILE.getParentFile().exists()) {
            if (!ReTweakMilk.CONFIG_FILE.getParentFile().exists())
                FileHelper.createDirectory(ReTweakMilk.CONFIG_FILE.getParentFile());
            FileWriter fileWriter = new FileWriter(ReTweakMilk.CONFIG_FILE);
            gson.toJson(
                    config,
                    fileWriter
            );
            fileWriter.flush();
            fileWriter.close();
        }
    }

    private JsonObject getVineObject(SupportedGameVersion supportedGameVersion) {
        for(JsonElement jsonElement : config) {
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = (JsonObject)jsonElement;
                if (jsonObject.get(ReTweakPeach.JSON_MEMBER_NAME_VERSION).getAsString().equals(supportedGameVersion.getVersion())) {
                    return jsonObject;
                }
            }
        }
        return null;
    }

    private JsonArray getMods(SupportedGameVersion supportedGameVersion) {
        JsonObject get = getVineObject(supportedGameVersion);
        if (get == null) return null;
        return get.get(ReTweakPeach.JSON_MEMBER_NAME_MODS).getAsJsonArray();
    }

    private void cleanCreation() {
        config = new JsonArray();
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(
                    ReTweakPeach.JSON_MEMBER_NAME_VERSION,
                    new JsonPrimitive(supportedGameVersion.getVersion())
            );
            jsonObject.add(
                    ReTweakPeach.JSON_MEMBER_NAME_MODS,
                    new JsonArray()
            );
            config.add(jsonObject);
        }
    }

}
