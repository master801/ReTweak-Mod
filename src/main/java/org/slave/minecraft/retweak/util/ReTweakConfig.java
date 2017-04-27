package org.slave.minecraft.retweak.util;

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
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakConfig.ConfigEntry.Category;
import org.slave.minecraft.retweak.util.ReTweakConfig.ConfigEntry.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Master on 4/24/2016 at 11:32 PM.
 *
 * @author Master
 */
public final class ReTweakConfig {

    private static final List<Class<?>> VALID_TYPES = Arrays.asList(
            //Numbers
            byte.class,
            short.class,
            int.class,
            float.class,
            double.class,

            //Json
            JsonElement.class,
            JsonObject.class,
            JsonArray.class,

            //Etc
            boolean.class
    );

    private final File file;

    private final JsonObject main;
    private final JsonArray versions;

    public ReTweakConfig(final File file) {
        this.file = file;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();

        gsonBuilder.registerTypeAdapter(
                ConfigEntry.class,
                new ConfigEntry.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                ConfigEntry.class,
                new ConfigEntry.Serializer()
        );

        gsonBuilder.registerTypeAdapter(
                Category.class,
                new Category.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                Category.class,
                new Category.Serializer()
        );

        gsonBuilder.registerTypeAdapter(
                Value.class,
                new Value.Deserializer()
        );
        gsonBuilder.registerTypeAdapter(
                Value.class,
                new Value.Serializer()
        );

        //TODO

        main = new JsonObject();
        versions = new JsonArray();

        for(GameVersion gameVersion : GameVersion.values()) {
            JsonObject gameVersionObject = new JsonObject();

            gameVersionObject.add(
                    "name",
                    new JsonPrimitive(gameVersion.getVersion())
            );

            JsonObject category = new JsonObject();
            gameVersionObject.add(
                    "category",
                    category
            );


            versions.add(gameVersionObject);
        }

        main.add(
                "versions",
                versions
        );
    }

    public void load() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        //TODO
        fileInputStream.close();
    }

    public void save() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        //TODO
        fileOutputStream.close();
    }

    public static final class ConfigEntry {

        private final String name;
        private final Object value;

        ConfigEntry(final Value value, final Object valueObject) {
            this.name = value.name;
            this.value = valueObject;
        }

        ConfigEntry(final String name, final Object value) {
            this.name = name;
            this.value = value;
        }

        enum Category {

            BOOLEAN(
                    "boolean",
                    new ConfigEntry[] {
                            new ConfigEntry(
                                    Value.ENABLE_GAME_VERSION,
                                    true
                            )
                    }
            );

            final String name;
            final ConfigEntry[] configValues;

            Category(final String name, final ConfigEntry[] configValues) {
                this.name = name;
                this.configValues = configValues;
            }

            public static final class Serializer implements JsonSerializer<Category> {

                private Serializer() {
                    final Object _INTERNAL_USAGE_ONLY = null;
                }

                @Override
                public JsonElement serialize(final Category src, final Type typeOfSrc, final JsonSerializationContext context) {
                    //TODO
                    return null;
                }

            }

            public static final class Deserializer implements JsonDeserializer<Category> {

                Deserializer() {
                    final Object _INTERNAL_USAGE_ONLY = null;
                }

                @Override
                public Category deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                    //TODO
                    return null;
                }

            }

        }

        enum Value {

            ENABLE_GAME_VERSION(
                    "enable",
                    boolean.class
            );

            final String name;
            final Class<?> valueType;

            Value(final String name, final Class<?> valueType) {
                this.name = name;
                if (!ReTweakConfig.VALID_TYPES.contains(valueType)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Type \"%s\" for Key enum is invalid!",
                                    valueType.getCanonicalName()
                            )
                    );
                } else {
                    this.valueType = valueType;
                }
            }

            public static final class Serializer implements JsonSerializer<Value> {

                private Serializer() {
                    final Object _INTERNAL_USAGE_ONLY = null;
                }

                @Override
                public JsonElement serialize(final Value src, final Type typeOfSrc, final JsonSerializationContext context) {
                    //TODO
                    return null;
                }

            }

            public static final class Deserializer implements JsonDeserializer<Value> {

                Deserializer() {
                    final Object _INTERNAL_USAGE_ONLY = null;
                }

                @Override
                public Value deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                    //TODO
                    return null;
                }

            }

        }

        public static final class Serializer implements JsonSerializer<ConfigEntry> {

            private Serializer() {
                final Object _INTERNAL_USAGE_ONLY = null;
            }

            @Override
            public JsonElement serialize(final ConfigEntry src, final Type typeOfSrc, final JsonSerializationContext context) {
                //TODO
                return null;
            }

        }

        public static final class Deserializer implements JsonDeserializer<ConfigEntry> {

            Deserializer() {
                final Object _INTERNAL_USAGE_ONLY = null;
            }

            @Override
            public ConfigEntry deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
                //TODO
                return null;
            }

        }

    }

}
