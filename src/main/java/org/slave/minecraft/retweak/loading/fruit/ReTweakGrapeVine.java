package org.slave.minecraft.retweak.loading.fruit;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    public List<ReTweakGrape> getMods() {
        return mods;
    }

    public void setMods(List<ReTweakGrape> grapes) {
        this.mods = grapes;
    }

    public void addMod(ReTweakGrape grape) {
        if (mods != null) mods.add(grape);
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
                    "version",
                    new JsonPrimitive(src.getVersion())
            );
            jsonElement.add(
                    "mods",
                    mods
            );
            return jsonElement;
        }

    }

}
