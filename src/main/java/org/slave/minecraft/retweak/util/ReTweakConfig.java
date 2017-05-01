package org.slave.minecraft.retweak.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakConfig.Config.GameVersionConfig.ConfigEntry.Category;
import org.slave.minecraft.retweak.util.ReTweakConfig.Config.GameVersionConfig.ConfigEntry.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by Master on 4/24/2016 at 11:32 PM.
 *
 * @author Master
 */
public final class ReTweakConfig {

    private final File file;
    private final Gson gson;
    private Config config;

    public ReTweakConfig(final File file) {
        this.file = file;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(
                Config.class,
                new Config.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                Config.class,
                new Config.Serializer()
        );

        gsonBuilder.registerTypeAdapter(
                Config.GameVersionConfig.class,
                new Config.GameVersionConfig.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                Config.GameVersionConfig.class,
                new Config.GameVersionConfig.Serializer()
        );

        gsonBuilder.registerTypeAdapter(
                Config.GameVersionConfig.ConfigEntry.class,
                new Config.GameVersionConfig.ConfigEntry.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                Config.GameVersionConfig.ConfigEntry.class,
                new Config.GameVersionConfig.ConfigEntry.Serializer()
        );

        gsonBuilder.registerTypeAdapter(
                Config.GameVersionConfig.ConfigEntry.Value.class,
                new Config.GameVersionConfig.ConfigEntry.Value.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                Config.GameVersionConfig.ConfigEntry.Value.class,
                new Config.GameVersionConfig.ConfigEntry.Value.Serializer()
        );

        gson = gsonBuilder.create();
    }

    public void load() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);

        config = gson.fromJson(
                inputStreamReader,
                Config.class
        );

        inputStreamReader.close();
        fileInputStream.close();
    }

    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);

        gson.toJson(
                config,
                outputStreamWriter
        );

        outputStreamWriter.close();
        fileOutputStream.close();
    }

    public static final class Config {

        private List<GameVersionConfig> gameVersionConfigs;

        Config() {
        }

        private void init() {
            gameVersionConfigs = new ArrayList<>();
            for(GameVersion gameVersion : GameVersion.values()) {
                gameVersionConfigs.add(
                        new GameVersionConfig(gameVersion)
                );
            }
        }

        public static final class GameVersionConfig {

            private GameVersion gameVersion;
            private Map<Category, List<ConfigEntry>> categories;

            GameVersionConfig(final GameVersion gameVersion) {
                this.gameVersion = gameVersion;
            }

            private void init() {
                categories = new HashMap<>();
                for(Category category : Category.CATEGORIES) {
                    List<ConfigEntry> configEntries = new ArrayList<>();

                    configEntries.add(
                            new ConfigEntry(
                                    Value.ENABLE_GAME_VERSION,
                                    true
                            )
                    );

                    categories.put(
                            category,
                            configEntries
                    );
                }
            }

            public static final class Deserializer implements JsonDeserializer<GameVersionConfig> {

                private Deserializer() {
                    final Object _INTERNAL_USAGE_ONLY = null;
                }

                @Override
                public GameVersionConfig deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                    GameVersion gameVersion = context.deserialize(
                            json.getAsJsonObject().get("version"),
                            GameVersion.class
                    );
                    if (gameVersion == null) return null;

                    GameVersionConfig gameVersionConfig = new GameVersionConfig(gameVersion);

                    JsonArray categories = json.getAsJsonObject().get("category").getAsJsonArray();
                    gameVersionConfig.categories = new HashMap<>();
                    for(JsonElement jsonElement : categories) {
                        JsonObject categoryWrapper = jsonElement.getAsJsonObject();

                        Category category = Category.getFromName(categoryWrapper.get("name").getAsString());
                        List<ConfigEntry> configEntries = new ArrayList<>();

                        for(JsonElement value : categoryWrapper.getAsJsonArray("values")) {
                            configEntries.add(
                                    context.deserialize(
                                            value,
                                            ConfigEntry.class
                                    )
                            );
                        }

                        gameVersionConfig.categories.put(
                                category,
                                configEntries
                        );
                    }

                    return gameVersionConfig;
                }

            }

            public static final class Serializer implements JsonSerializer<GameVersionConfig> {

                private Serializer() {
                    final Object _INTERNAL_USAGE_ONLY = null;
                }

                @Override
                public JsonElement serialize(final GameVersionConfig src, final Type typeOfSrc, final JsonSerializationContext context) {
                    JsonObject jsonObject = new JsonObject();


                    jsonObject.add(
                            "version",
                            context.serialize(
                                    src.gameVersion,
                                    GameVersion.class
                            )
                    );

                    JsonArray category = new JsonArray();

                    for(Entry<Category, List<ConfigEntry>> entries : src.categories.entrySet()) {
                        JsonObject categoryWrapper = new JsonObject();


                        categoryWrapper.add(
                                "name",
                                new JsonPrimitive(entries.getKey().name)
                        );

                        JsonArray values = new JsonArray();
                        for(ConfigEntry configEntry : entries.getValue()) {
                            values.add(
                                    context.serialize(
                                            configEntry,
                                            ConfigEntry.class
                                    )
                            );
                        }


                        categoryWrapper.add(
                                "values",
                                values
                        );

                        category.add(
                                categoryWrapper
                        );
                    }

                    jsonObject.add(
                            "category",
                            category
                    );


                    return jsonObject;
                }

            }

            public static final class ConfigEntry {

                private Value value;
                private Object valueObject;

                ConfigEntry(final Value value, final Object valueObject) {
                    this.value = value;
                    this.valueObject = valueObject;
                }

                public static final class Category {

                    public static final Category[] CATEGORIES = new Category[] {
                            Category.BOOLEAN
                    };

                    public static final Category BOOLEAN = new Category(
                            "boolean"
                    );

                    private final String name;

                    private Category(final String name) {
                        this.name = name;
                    }

                    public static Category getFromName(final String name) {
                        if (StringHelper.isNullOrEmpty(name)) return null;
                        for(Category category : Category.CATEGORIES) {
                            if (category.name.equals(name)) return category;
                        }
                        return null;
                    }

                }

                public static final class Value {

                    public static final Value ENABLE_GAME_VERSION = new Value(
                            "enable"
                    );

                    private final String name;

                    private Value(final String name) {
                        this.name = name;
                    }

                    public static final class Deserializer implements JsonDeserializer<Value> {

                        private Deserializer() {
                            final Object _INTERNAL_USAGE_ONLY = null;
                        }

                        @Override
                        public Value deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                            return new Value(
                                    json.getAsJsonObject().getAsJsonPrimitive("name").getAsString()
                            );
                        }

                    }

                    public static final class Serializer implements JsonSerializer<Value> {

                        private Serializer() {
                            final Object _INTERNAL_USAGE_ONLY = null;
                        }

                        @Override
                        public JsonElement serialize(final Value src, final Type typeOfSrc, final JsonSerializationContext context) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.add(
                                    "name",
                                    new JsonPrimitive(src.name)
                            );
                            return jsonObject;
                        }

                    }

                }

                public static final class Deserializer implements JsonDeserializer<ConfigEntry> {

                    private Deserializer() {
                        final Object _INTERNAL_USAGE_ONLY = null;
                    }

                    @Override
                    public ConfigEntry deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                        Value value;
                        Object valueObject = null;

                        JsonObject jsonObject = json.getAsJsonObject();

                        value = context.deserialize(
                                jsonObject.get("name"),
                                Value.class
                        );

                        if (jsonObject.has("value")) {
                            JsonElement jsonElement = jsonObject.get("value");
                            if(jsonElement.isJsonPrimitive()) {
                                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                                if(jsonPrimitive.isNumber()) {
                                    valueObject = jsonPrimitive.getAsNumber();
                                } else if(jsonPrimitive.isString()) {
                                    valueObject = jsonPrimitive.getAsString();
                                } else if(jsonPrimitive.isBoolean()) {
                                    valueObject = jsonPrimitive.getAsBoolean();
                                }
                            } else if(jsonElement.isJsonObject()) {
                                //TODO
                            } else if(jsonElement.isJsonArray()) {
                                //TODO
                            }
                        }
                        return new ConfigEntry(
                                value,
                                valueObject
                        );
                    }

                }

                public static final class Serializer implements JsonSerializer<ConfigEntry> {

                    private Serializer() {
                        final Object _INTERNAL_USAGE_ONLY = null;
                    }

                    @Override
                    public JsonElement serialize(final ConfigEntry src, final Type typeOfSrc, final JsonSerializationContext context) {
                        JsonObject jsonObject = new JsonObject();

                        jsonObject.add(
                                "name",
                                context.serialize(
                                        src.value,
                                        Value.class
                                )
                        );

                        JsonElement value = null;
                        if (src.valueObject != null) {
                            if (src.valueObject instanceof Number) {
                                value = new JsonPrimitive((Number)src.valueObject);
                            } else if (src.valueObject instanceof String) {
                                value = new JsonPrimitive((String)src.valueObject);
                            } else if (src.valueObject instanceof Boolean) {
                                value = new JsonPrimitive((Boolean)src.valueObject);
                            } else if (src.valueObject instanceof JsonObject) {
                                //TODO
                            } else if (src.valueObject instanceof JsonArray) {
                                //TODO
                            }
                        }
                        jsonObject.add(
                                "value",
                                value
                        );

                        return jsonObject;
                    }

                }

            }

        }

        public static final class Deserializer implements JsonDeserializer<Config> {

            private Deserializer() {
                final Object _INTERNAL_USAGE_ONLY = null;
            }

            @Override
            public Config deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                Config config = new Config();

                JsonArray gameVersionConfigs = json.getAsJsonObject().getAsJsonArray("versions");
                if (gameVersionConfigs != null) {
                    config.gameVersionConfigs = new ArrayList<>();
                    for(JsonElement gameVersionConfig : gameVersionConfigs) {
                        config.gameVersionConfigs.add(
                                context.deserialize(
                                        gameVersionConfig,
                                        GameVersionConfig.class
                                )
                        );
                    }
                } else {
                    config.init();
                }

                return config;
            }

        }

        public static final class Serializer implements JsonSerializer<Config> {

            private Serializer() {
                final Object _INTERNAL_USAGE_ONLY = null;
            }

            @Override
            public JsonElement serialize(final Config src, final Type typeOfSrc, final JsonSerializationContext context) {
                JsonObject jsonObject = new JsonObject();

                JsonArray gameVersionConfigs = new JsonArray();
                for(GameVersionConfig gameVersionConfig : src.gameVersionConfigs) {
                    gameVersionConfigs.add(
                            context.serialize(
                                    gameVersionConfig,
                                    GameVersionConfig.class
                            )
                    );
                }
                jsonObject.add(
                        "versions",
                        gameVersionConfigs
                );

                return jsonObject;
            }

        }

    }

}
