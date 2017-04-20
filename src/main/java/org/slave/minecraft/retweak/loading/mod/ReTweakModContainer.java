package org.slave.minecraft.retweak.loading.mod;

import com.google.common.base.Strings;
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
 *     Mostly stolen (or re-implemented) from FML code
 * </p>
 *
 * Created by Master on 5/7/2016 at 6:36 PM.
 *
 * @author Master
 */
public final class ReTweakModContainer {

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



        //START -- CODE FROM FML
        if (info.containsKey("modLanguage")) {
            modLanguage = (String)info.get("modLanguage");
        } else {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Found no mod language for \"{}\"! Defaulting to \"{}\"...",
                    modClass,
                    modLanguage = "java"
            );
        }

        final String languageAdapterType = (String)info.get("modLanguageAdapter");
        if (Strings.isNullOrEmpty(languageAdapterType)) {
            if (modLanguage != null && modLanguage.equals("scala")) {
                languageAdapter = new ILanguageAdapter.ScalaAdapter();
            } else {
                languageAdapter = new ILanguageAdapter.JavaAdapter();
            }
        } else {
            try {
                languageAdapter = (ILanguageAdapter)ReflectionHelper.createFromConstructor(
                        ReflectionHelper.getConstructor(
                                Class.forName(
                                        languageAdapterType,
                                        true,
                                        Loader.instance().getModClassLoader()
                                ),
                                new Class[0]
                        ),
                        new Object[0]
                );
            } catch(IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                        "Caught an exception while creating new language adapter!",
                        e
                );
            }
        }
        if (languageAdapter == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Found no language adapter? Defaulting to \"{}\"...",
                    "java"
            );
            languageAdapter = new ILanguageAdapter.JavaAdapter();
        }
        //END -- CODE FROM FML
    }

    public String getModId() {
        return (String)info.get("modid");
    }

    public String getName() {
        return (String)info.get("name");
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

        final boolean overrideMetadata = info.containsKey("useMetadata") && !(boolean)info.get("useMetadata");
        if (overrideMetadata || !modMetadata.useDependencyInformation) {
            //TODO DEPENDENCY PARSING
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "Dependency parsing is not yet implemented!"
            );
        }
        if (version == null && !StringHelper.isNullOrEmpty(modMetadata.version)) version = modMetadata.version;
        String mcVersion = (String)info.get("acceptedMinecraftVersions");
        if (!StringHelper.isNullOrEmpty(mcVersion)) {
            minecraftAccepted = VersionParser.parseRange(mcVersion);
        } else {
            minecraftAccepted = Loader.instance().getMinecraftModContainer().getStaticVersionRange();
        }
    }

    public void callState(Class<? extends FMLStateEvent> fmlStateEventClass) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        if (instance == null) {
            throw new NullPointerException(
                    "Instance for mod \"" + getModId() + "\" is null!"
            );
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
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0] == fmlStateEventClass) && method.isAnnotationPresent(EventHandler.class)) {
                ReflectionHelper.invokeMethod(
                        method,
                        instance,
                        new Object[] {
                                createStateEvent(fmlStateEventClass)
                        }
                );
                break;
            }
        }
    }

    private FMLStateEvent createStateEvent(Class<? extends FMLStateEvent> fmlStateEventClass) {
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

}
