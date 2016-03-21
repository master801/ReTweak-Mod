package org.slave.minecraft.retweak.loading;

import com.google.gson.stream.JsonWriter;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.FileHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Master801 on 3/21/2016 at 12:39 PM.
 *
 * @author Master801
 */
public final class ReTweakCereal {

    public static final File RETWEAK_CONFIG_DIRECTORY = new File("config", ReTweakResources.RETWEAK_DIRECTORY.getName());
    public static final File RETWEAK_CONFIG_FILE = new File(ReTweakCereal.RETWEAK_CONFIG_DIRECTORY, "mods.json");

    public static final ReTweakCereal INSTANCE = new ReTweakCereal();

    private ReTweakCereal() {
    }

    public void writeReTweakModConfigs(SupportedGameVersion supportedGameVersion, ReTweakModContainer[] retweakModContainers) throws IOException {
        if (supportedGameVersion == null || ArrayHelper.isNullOrEmpty(retweakModContainers)) return;

        if (!ReTweakCereal.RETWEAK_CONFIG_DIRECTORY.exists()) FileHelper.createDirectory(ReTweakCereal.RETWEAK_CONFIG_DIRECTORY);

        FileWriter fileWriter = new FileWriter(ReTweakCereal.RETWEAK_CONFIG_FILE);
        JsonWriter jsonWriter = new JsonWriter(fileWriter);


//        jsonWriter.beginArray();
        jsonWriter.beginObject();
        jsonWriter.beginObject().name("version").value(supportedGameVersion.getDirectoryName()).endObject();

        jsonWriter.beginArray().name("mods");
        for(ReTweakModContainer reTweakModContainer : retweakModContainers) {
            jsonWriter.beginObject().name("modid").value(reTweakModContainer.getModid()).endObject();
            jsonWriter.beginObject().name("enabled").value(Boolean.TRUE).endObject();
        }
        jsonWriter.endArray();

//        jsonWriter.endArray();
        jsonWriter.endObject();


        fileWriter.flush();
        jsonWriter.flush();

        fileWriter.close();
        jsonWriter.close();
    }

}
