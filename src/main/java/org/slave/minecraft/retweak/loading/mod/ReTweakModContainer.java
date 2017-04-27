package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.ILanguageAdapter;
import cpw.mods.fml.common.Loader;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 *     Mostly stolen (or re-implemented) from FML<br/>
 *     {@link cpw.mods.fml.common.ModContainer}
 * </p>
 *
 * Created by Master on 5/7/2016 at 6:36 PM.
 *
 * @author Master
 */
public final class ReTweakModContainer {

    private static final String INFO_MODID = "modid";
    private static final String INFO_NAME = "name";
    private static final String INFO_USE_METADATA = "useMetadata";
    private static final String INFO_ACCEPTED_MINECRAFT_VERSIONS = "acceptedMinecraftVersions";

    private static final String MOD_LANGUAGE_JAVA = "java";
    private static final String MOD_LANGUAGE_SCALA = "scala";

    public ReTweakModContainer() {
        //TODO
    }

    //TODO

    private static ILanguageAdapter createLanguageAdapter(final String name, final String customLanguageAdapter) {
        ILanguageAdapter languageAdapter = null;
        if (name != null) {
            switch(name.toLowerCase()) {
                case ReTweakModContainer.MOD_LANGUAGE_JAVA:
                    languageAdapter = new ILanguageAdapter.JavaAdapter();
                    break;
                case ReTweakModContainer.MOD_LANGUAGE_SCALA:
                    languageAdapter = new ILanguageAdapter.ScalaAdapter();
                    break;
            }
        } else if (customLanguageAdapter != null) {
            try {
                languageAdapter = (ILanguageAdapter)ReflectionHelper.createFromConstructor(
                        ReflectionHelper.getConstructor(
                                Class.forName(
                                        customLanguageAdapter,
                                        true,
                                        Loader.instance().getModClassLoader()//We may need to use "our" classloader to create the custom language adapter...
                                ),
                                new Class[0]
                        ),
                        new Object[0]
                );
            } catch (IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "Caught an exception while creating a new language adapter!",
                        e
                );
            }
        }
        if (languageAdapter == null) languageAdapter = new ILanguageAdapter.JavaAdapter();
        return languageAdapter;
    }

}
