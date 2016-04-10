package org.slave.minecraft.retweak.loading;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slave.lib.helpers.FileHelper;
import org.slave.minecraft.retweak.loading.fruit.ReTweakGrapeVine;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Master801 on 3/21/2016 at 12:39 PM.
 *
 * @author Master801
 */
@SuppressWarnings("unchecked")
public final class ReTweakCereal {

    public static final File RETWEAK_CONFIG_DIRECTORY = new File("config", ReTweakResources.RETWEAK_DIRECTORY.getName());
    public static final File RETWEAK_CONFIG_FILE = new File(ReTweakCereal.RETWEAK_CONFIG_DIRECTORY, "mods.json");

    public static final ReTweakCereal INSTANCE = new ReTweakCereal();
    private final JsonFactory jsonFactory = new JsonFactory();

    private ReTweakCereal() {
    }

    public void update() throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile().exists() || !ReTweakCereal.RETWEAK_CONFIG_FILE.exists()) create();

        //TODO
    }

    public void modify(final SupportedGameVersion supportedGameVersion, final List<ReTweakModContainer> reTweakModContainers) throws IOException {
        if (!ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile().exists() || !ReTweakCereal.RETWEAK_CONFIG_FILE.exists()) create();

        //TODO
    }

    private void create() throws IOException {
        FileHelper.createDirectory(ReTweakCereal.RETWEAK_CONFIG_FILE.getParentFile());
        FileOutputStream fileOutputStream = new FileOutputStream(ReTweakCereal.RETWEAK_CONFIG_FILE);

        JsonGenerator jsonGenerator = jsonFactory.createGenerator(fileOutputStream).useDefaultPrettyPrinter();
        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<ReTweakGrapeVine> grapeVines = new ArrayList<>();
        for(SupportedGameVersion supportedGameVersion : SupportedGameVersion.values()) {
            grapeVines.add(new ReTweakGrapeVine(supportedGameVersion.getVersion()));
        }
        objectMapper.writeValue(
                jsonGenerator,
                grapeVines
        );

        jsonGenerator.flush();
        jsonGenerator.close();

        fileOutputStream.flush();
        fileOutputStream.close();
    }

}
