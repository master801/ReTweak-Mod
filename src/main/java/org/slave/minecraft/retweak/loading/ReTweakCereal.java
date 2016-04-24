package org.slave.minecraft.retweak.loading;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slave.lib.helpers.FileHelper;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrape;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrape.ReTweakGrapeDeserializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrape.ReTweakGrapeSerializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine.ReTweakGrapeVineDeserializer;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine.ReTweakGrapeVineSerializer;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.IOException;

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

    private void create() throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile().exists()) FileHelper.createDirectory(ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile());
    }

}
