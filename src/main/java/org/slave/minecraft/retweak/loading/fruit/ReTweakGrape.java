package org.slave.minecraft.retweak.loading.fruit;

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

/**
 * Created by Master801 on 4/9/2016 at 9:43 PM.
 *
 * @author Master801
 */
public final class ReTweakGrape implements Serializable {

    private static final long serialVersionUID = -1008949968042133996L;

    private String name;
    private boolean enable;

    public ReTweakGrape() {
    }

    public ReTweakGrape(final String name, final boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public boolean isEnabled() {
        return enable;
    }

    public static final class ReTweakGrapeSerializer implements JsonSerializer<ReTweakGrape> {

        @Override
        public JsonElement serialize(final ReTweakGrape src, final Type typeOfSrc, final JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(
                    ReTweakPeach.JSON_MEMBER_NAME_NAME,
                    new JsonPrimitive(src.getName())
            );
            jsonObject.add(
                    ReTweakPeach.JSON_MEMBER_NAME_ENABLE,
                    new JsonPrimitive(src.isEnabled())
            );
            return jsonObject;
        }

    }

    public static final class ReTweakGrapeDeserializer implements JsonDeserializer<ReTweakGrape> {

        @Override
        public ReTweakGrape deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
            JsonObject grapeObject = json.getAsJsonObject();
            ReTweakGrape grape = new ReTweakGrape();
            grape.name = grapeObject.getAsJsonObject(ReTweakPeach.JSON_MEMBER_NAME_NAME).getAsString();
            grape.enable = grapeObject.getAsJsonObject(ReTweakPeach.JSON_MEMBER_NAME_ENABLE).getAsBoolean();
            return grape;
        }

    }

}
