package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ILanguageAdapter;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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

    private final String modClass;
    private String version;
    private final ReTweakModCandidate reTweakModCandidate;
    private boolean enabled = true;
    private ModMetadata modMetadata;
    private VersionRange minecraftAccepted;
    private final Map<String, Object> info;
    private final String modLanguage;
    private ILanguageAdapter languageAdapter;

    private Object instance;

    public ReTweakModContainer(final String modClass, final ReTweakModCandidate reTweakModCandidate, final Map<String, Object> info) {
        this.modClass = modClass;
        this.reTweakModCandidate = reTweakModCandidate;
        this.info = info;

        String modLanguage = (String)info.get("modLanguage");
        String modLanguageAdapter = (String)info.get("modLanguageAdapter");

        languageAdapter = ReTweakModContainer.createLanguageAdapter(
                modLanguage,
                modLanguageAdapter
        );
        if (modLanguage == null) {
            this.modLanguage = ReTweakModContainer.MOD_LANGUAGE_JAVA;
        } else {
            this.modLanguage = modLanguage;
        }
    }

    public String getModId() {
        return (String)info.get(ReTweakModContainer.INFO_MODID);
    }

    public String getName() {
        return (String)info.get(ReTweakModContainer.INFO_NAME);
    }

    public String getVersion() {
        return version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public ModMetadata getModMetadata() {
        return modMetadata;
    }

    public ILanguageAdapter getLanguageAdapter() {
        return languageAdapter;
    }

    ReTweakModCandidate getReTweakModCandidate() {
        return reTweakModCandidate;
    }

    void setInstance(Object instance) {
        this.instance = instance;
    }

    Object getInstance() {
        return instance;
    }

    String getModClass() {
        return modClass;
    }

    /**
     * {@link cpw.mods.fml.common.FMLModContainer#bindMetadata(cpw.mods.fml.common.MetadataCollection)}
     */
    void bindMetadata(MetadataCollection metadataCollection) {
        if (metadataCollection == null) throw new NullPointerException();
        try {
            this.modMetadata = metadataCollection.getMetadataForId(
                    getModId(),
                    info
            );
        } catch(NullPointerException e) {
            //Does not contain modid
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "File \"{}\" does not contain modid \"{}\"!",
                    "mcmod.info",
                    getModId()
            );
            return;
        }

        boolean overrideMetadata = info.containsKey(ReTweakModContainer.INFO_USE_METADATA) && !(boolean)info.get(ReTweakModContainer.INFO_USE_METADATA);
        if (overrideMetadata || !modMetadata.useDependencyInformation) {
            //TODO DEPENDENCY PARSING
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Dependency parsing is not yet implemented!"
            );
        }
        if (version == null && !StringHelper.isNullOrEmpty(modMetadata.version)) version = modMetadata.version;
        String mcVersion = (String)info.get(ReTweakModContainer.INFO_ACCEPTED_MINECRAFT_VERSIONS);
        if (!StringHelper.isNullOrEmpty(mcVersion)) {
            minecraftAccepted = VersionParser.parseRange(mcVersion);
        } else {
            minecraftAccepted = Loader.instance().getMinecraftModContainer().getStaticVersionRange();
        }
    }

    public void callState(final Class<? extends FMLStateEvent> fmlStateEventClass) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        if (instance == null) {
            throw new NullPointerException(
                    "Instance for mod \"" + getModId() + "\" is null!"
            );
        }
        if (fmlStateEventClass == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Tried to call a null state for mod \"{}\"!",
                    getModId()
            );
            return;
        }

        ReTweakResources.RETWEAK_LOGGER.debug(
                "Calling state \"{}\" for mod \"{}\"",
                fmlStateEventClass.getSimpleName(),
                getModId()
        );
        if (!isEnabled()) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Attempted to call state class \"{}\" when disabled?! This should not happen!",
                    fmlStateEventClass.getCanonicalName()
            );
            return;
        }
        Class<?> _modClass = Class.forName(
                modClass,
                true,
                ReTweakClassLoader.getClassLoader(getReTweakModCandidate().getGameVersion())
        );

        for(Method method : _modClass.getDeclaredMethods()) {
            Class<?>[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0] == fmlStateEventClass) && method.isAnnotationPresent(EventHandler.class)) {
                try {
                    ReflectionHelper.invokeMethod(
                            method,
                            instance,
                            new Object[] {
                                    createStateEvent(fmlStateEventClass)
                            }
                    );
                } catch(InvocationTargetException e) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            "Caught an exception while creating state event \"" + fmlStateEventClass.getSimpleName() + "\"!",
                            e
                    );
                }
                break;
            }
        }
    }

    private FMLStateEvent createStateEvent(final Class<? extends FMLStateEvent> fmlStateEventClass) {
        Object[] parameters;
        if (fmlStateEventClass == FMLPreInitializationEvent.class) {
            parameters = new Object[] {
                    null,
                    ReTweakResources.RETWEAK_CONFIG_DIRECTORY
            };
        } else if (fmlStateEventClass == FMLServerAboutToStartEvent.class || fmlStateEventClass == FMLServerStartingEvent.class) {
            parameters = new Object[] {
                    FMLCommonHandler.instance().getMinecraftServerInstance()
            };
        } else {
            parameters = new Object[0];
        }

        try {
            FMLStateEvent fmlStateEvent = ReflectionHelper.createFromConstructor(
                    ReflectionHelper.getConstructor(
                            fmlStateEventClass,
                            new Class<?>[] {
                                Object[].class
                            }
                    ),
                    new Object[] {
                            parameters
                    }
            );
            //TODO Find out what is wrong with the pre-initialization event arguments (wrong num of arguments?); and find out how to hack sound from (audio) files in (fake sounds.json file?)
            if (fmlStateEventClass == FMLPreInitializationEvent.class) {
                ReflectionHelper.setFieldValue(
                        ReflectionHelper.getField(
                                FMLPreInitializationEvent.class,
                                "sourceFile"
                        ),
                        fmlStateEvent,
                        getReTweakModCandidate().getSource()
                );
                ReflectionHelper.setFieldValue(
                        ReflectionHelper.getField(
                                FMLPreInitializationEvent.class,
                                "suggestedConfigFile"
                        ),
                        fmlStateEvent,
                        new File(
                                ReTweakResources.RETWEAK_CONFIG_DIRECTORY,
                                getModId() + ".cfg"
                        )
                );
            }
            return fmlStateEvent;
        } catch(Exception e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Failed to create new instance of state event \"" + fmlStateEventClass.getCanonicalName() + "\"!",
                    e
            );
        }
        return null;
    }

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
