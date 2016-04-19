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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *     JSON config file
 * </p>
 *
 * Created by Master801 on 3/21/2016 at 12:39 PM.
 *
 * @author Master801
 */
@SuppressWarnings("unchecked")
public final class ReTweakCereal {

    public static final File RETWEAK_CONFIG_DIRECTORY = new File(
            "config",
            ReTweakResources.RETWEAK_DIRECTORY.getName()
    );
    public static final File RETWEAK_CONFIG_FILE = new File(
            ReTweakCereal.RETWEAK_CONFIG_DIRECTORY,
            "mods.json"
    );

    public static final ReTweakCereal INSTANCE = new ReTweakCereal();

    private final Gson gson;

    private ReTweakCereal() {
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

    public void update() throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.exists()) create();

        boolean broken = false, save = false;
        FileReader fileReader = new FileReader(ReTweakCereal.RETWEAK_CONFIG_FILE);
        JsonArray jsonArray = gson.fromJson(
                fileReader,
                JsonArray.class
        );
        for(JsonElement supportedGameVersionElement : jsonArray) {
            JsonObject supportedGameVersionObject = supportedGameVersionElement.getAsJsonObject();

            JsonElement version = supportedGameVersionObject.get(ReTweakPeach.JSON_MEMBER_NAME_VERSION);
            JsonArray mods = supportedGameVersionObject.getAsJsonArray(ReTweakPeach.JSON_MEMBER_NAME_MODS);

            SupportedGameVersion supportedGameVersion = SupportedGameVersion.getFromVersion(version.getAsString());
            if (supportedGameVersion == null) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                        "\"{}\" is not a supported game version!",
                        version.getAsString()
                );
                broken = true;
                save = false;
                break;
            }

            List<ReTweakModContainer> reTweakModContainers = ReTweakLoader.INSTANCE.getModContainers(supportedGameVersion);

            for(ReTweakModContainer reTweakModContainer : reTweakModContainers) {
                for(JsonElement element : mods) {
                    JsonObject mod = element.getAsJsonObject();
                    if (mod.get(ReTweakPeach.JSON_MEMBER_NAME_NAME).getAsString().equals(reTweakModContainer.getModid())) {
                        if (mod.get(ReTweakPeach.JSON_MEMBER_NAME_ENABLE).getAsBoolean() != reTweakModContainer.isEnabled()) {
                            mod.add(
                                    ReTweakPeach.JSON_MEMBER_NAME_ENABLE,
                                    new JsonPrimitive(reTweakModContainer.isEnabled())
                            );
                            save = true;
                            break;
                        }
                    }
                }
            }
        }
        fileReader.close();

        if (broken) {
            int i = 0;
            String newName;
            while(!ReTweakCereal.RETWEAK_CONFIG_FILE.renameTo(new File(
                    ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile(),
                    newName = (ReTweakCereal.RETWEAK_CONFIG_FILE.getName() + "." + i + "broken")
            ))) {
                i++;
            }

            ReTweakResources.RETWEAK_LOGGER.info(
                    "ReTweak's config file was marked as broken, renamed to \"{}\". Please either fix or delete the config file.",
                    newName
            );
            save = false;
        }

        if (save) {
            FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);
            gson.toJson(
                    jsonArray,
                    fileWriter
            );
            fileWriter.flush();
            fileWriter.close();
        }
    }

    public void modify(final SupportedGameVersion supportedGameVersion, final List<ReTweakModContainer> reTweakModContainers) throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.exists()) create();

        boolean save = false, containsVersion = false;
        FileReader fileReader = new FileReader(ReTweakCereal.RETWEAK_CONFIG_FILE);
        JsonArray jsonArray = gson.fromJson(
                fileReader,
                JsonArray.class
        );

        for(JsonElement supportedGameVersionObject : jsonArray) {
            JsonObject jsonObject = supportedGameVersionObject.getAsJsonObject();

            JsonElement versionObject = jsonObject.get(ReTweakPeach.JSON_MEMBER_NAME_VERSION);
            JsonArray modsObject = jsonObject.get(ReTweakPeach.JSON_MEMBER_NAME_MODS).getAsJsonArray();
            if (versionObject.getAsString().equals(supportedGameVersion.getVersion())) {
                containsVersion = true;
                for(ReTweakModContainer reTweakModContainer : reTweakModContainers) {

                    JsonObject modObject = null;
                    for(JsonElement modElement : modsObject) {
                        if (modElement.getAsJsonObject().get(ReTweakPeach.JSON_MEMBER_NAME_NAME).getAsString().equals(reTweakModContainer.getModid())) {
                            modObject = modElement.getAsJsonObject();
                            break;
                        }
                    }

                    if (modObject == null) {
                        modsObject.add(gson.toJsonTree(
                                reTweakModContainer.toGrape(),
                                ReTweakGrape.class
                        ));
                        save = true;
                    } else {
                        if (modObject.get(ReTweakPeach.JSON_MEMBER_NAME_ENABLE).getAsBoolean() != reTweakModContainer.isEnabled()) {
                            modObject.add(
                                    ReTweakPeach.JSON_MEMBER_NAME_ENABLE,
                                    new JsonPrimitive(reTweakModContainer.isEnabled())
                            );
                            save = true;
                        }
                    }
                }
                break;
            }
        }
        fileReader.close();

        if (save || !containsVersion) {
            FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);
            if (!containsVersion) {
                jsonArray.add(gson.toJsonTree(
                        new ReTweakGrapeVine(supportedGameVersion.getVersion()),
                        ReTweakGrapeVine.class
                ));
            }
            gson.toJson(
                    jsonArray,
                    fileWriter
            );
            fileWriter.flush();
            fileWriter.close();
        }
        if (!containsVersion) {
            //Modify again
            modify(
                    supportedGameVersion,
                    reTweakModContainers
            );
        }
    }

    private void create() throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile().exists()) FileHelper.createDirectory(ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile());
        FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);

        JsonArray jsonArray = new JsonArray();
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            ReTweakGrapeVine grapeVine = new ReTweakGrapeVine(supportedGameVersion.getVersion());
            JsonElement grapeVineElement = gson.toJsonTree(
                    grapeVine,
                    ReTweakGrapeVine.class
            );
            jsonArray.add(grapeVineElement);
        }
        gson.toJson(
                jsonArray,
                fileWriter
        );

        fileWriter.flush();
        fileWriter.close();
    }

}
