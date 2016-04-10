package org.slave.minecraft.retweak.loading.fruit;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 4/9/2016 at 9:41 PM.
 *
 * @author Master801
 */
public final class ReTweakGrapeVine implements Serializable {

    private static final long serialVersionUID = -5453174058209040688L;

    private String version;
    private List<ReTweakGrape> mods;

    public ReTweakGrapeVine() {
    }

    public ReTweakGrapeVine(final String version) {
        this.version = version;
        mods = new ArrayList<>();
    }

    public String getVersion() {
        return version;
    }

    public Iterable<ReTweakGrape> getMods() {
        return mods;
    }

    public static final class ReTweakGrapeVineSerializer implements JsonSerializer<ReTweakGrapeVine> {

        @Override
        public JsonElement serialize(final ReTweakGrapeVine src, final Type typeOfSrc, final JsonSerializationContext context) {
            JsonObject jsonElement = new JsonObject();

            JsonArray mods = new JsonArray();
            if (src.getMods() != null) {
                for(ReTweakGrape grape : src.getMods()) {
                    mods.add(context.serialize(
                            grape,
                            ReTweakGrape.class
                    ));
                }
            }

            jsonElement.add(
                    ReTweakPeach.JSON_MEMBER_NAME_VERSION,
                    new JsonPrimitive(src.getVersion())
            );
            jsonElement.add(
                    ReTweakPeach.JSON_MEMBER_NAME_MODS,
                    mods
            );
            return jsonElement;
        }

    }

    public static final class ReTweakGrapeVineDeserializer implements JsonDeserializer<ReTweakGrapeVine> {

        @Override
        public ReTweakGrapeVine deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            JsonObject modObject = json.getAsJsonObject();
            JsonObject version = modObject.getAsJsonObject(ReTweakPeach.JSON_MEMBER_NAME_VERSION);
            JsonArray mods = modObject.getAsJsonArray(ReTweakPeach.JSON_MEMBER_NAME_MODS);

            ReTweakGrapeVine grapeVine = new ReTweakGrapeVine();
            grapeVine.version = version.getAsString();
            grapeVine.mods = new ArrayList<>();
            for(JsonElement jsonElement : mods) {
                ReTweakGrape grape = context.deserialize(
                        jsonElement.getAsJsonObject(),
                        ReTweakGrapeVine.class
                );
                grapeVine.mods.add(grape);
            }
            return grapeVine;
        }

    }

}
