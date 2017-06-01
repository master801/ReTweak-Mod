package org.slave.minecraft.retweak.loading.mod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.FMLModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.lang.reflect.Field;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *     Mostly stolen (or re-implemented) from FML<br/>
 *     {@link cpw.mods.fml.common.ModContainer}
 *     {@link cpw.mods.fml.common.FMLModContainer}
 * </p>
 *
 * Created by Master on 5/7/2016 at 6:36 PM.
 *
 * @author Master
 */
public final class ReTweakModContainer implements ModContainer {

    private static final String INFO_MODID = "modid";
    private static final String INFO_NAME = "name";
    private static final String INFO_USE_METADATA = "useMetadata";
    private static final String INFO_ACCEPTED_MINECRAFT_VERSIONS = "acceptedMinecraftVersions";

    private static final String MOD_LANGUAGE_JAVA = "java";
    private static final String MOD_LANGUAGE_SCALA = "scala";

    private final String modClassName;
    private final ModCandidate modCandidate;
    private final Map<String, String> descriptor;
    private final File source;
    private DefaultArtifactVersion processedVersion;
    private VersionRange minecraftAccepted;
    private Certificate certificate;
    private Map<String, String> customModProperties;
    private Disableable disableability;
    private ModMetadata modMetadata;
    private String internalVersion;
    private boolean enabled = true;
    private Object modInstance;

    public ReTweakModContainer(final String modClassName, final ModCandidate modCandidate, final Map<String, String> descriptor) {
        this.modClassName = modClassName;
        this.modCandidate = modCandidate;
        this.descriptor = descriptor;
        this.source = modCandidate.getModContainer();
    }

    @Override
    public String getModId() {
        return descriptor.get(ReTweakModContainer.INFO_MODID);
    }

    @Override
    public String getName() {
        return modMetadata.name;
    }

    @Override
    public String getVersion() {
        return internalVersion;
    }

    @Override
    public File getSource() {
        return source;
    }

    @Override
    public ModMetadata getMetadata() {
        return modMetadata;
    }

    @Override
    public void bindMetadata(final MetadataCollection mc) {
        //TODO
    }

    @Override
    public void setEnabledState(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Set<ArtifactVersion> getRequirements() {
        return modMetadata.requiredMods;
    }

    @Override
    public List<ArtifactVersion> getDependencies() {
        return modMetadata.dependencies;
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        return modMetadata.dependants;
    }

    @Override
    public String getSortingRules() {
        return null;//TODO
    }

    @Override
    public boolean registerBus(final EventBus bus, final LoadController controller) {
        //NOOP
        return false;
    }

    public boolean registerBus(final EventBus bus, final ReTweakLoadController reTweakLoadController) {
        if (isEnabled()) {
            setEventBus(
                bus
            );
            bus.register(this);
            return true;
        }
        return false;
    }

    @Override
    public boolean matches(final Object mod) {
        return mod == modInstance;
    }

    @Override
    public Object getMod() {
        return modInstance;
    }

    @Override
    public ArtifactVersion getProcessedVersion() {
        if (processedVersion == null) {
            processedVersion = new DefaultArtifactVersion(
                getModId(),
                getVersion()
            );
        }
        return processedVersion;
    }

    @Override
    public boolean isImmutable() {
        return false;
    }

    @Override
    public String getDisplayVersion() {
        return modMetadata.version;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return minecraftAccepted;
    }

    @Override
    public Certificate getSigningCertificate() {
        return certificate;
    }

    @Override
    public Map<String, String> getCustomModProperties() {
        return customModProperties;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return null;//TODO
    }

    @Override
    public Map<String, String> getSharedModDescriptor() {
        /*
        Map<String,String> descriptor = Maps.newHashMap();
        descriptor.put(
            "modsystem",
            "FML"
        );
        descriptor.put(
            "id",
            getModId()
        );
        descriptor.put(
            "version",
            getDisplayVersion()
        );
        descriptor.put(
            "name",
            getName()
        );
        descriptor.put(
            "url",
            modMetadata.url
        );
        descriptor.put(
            "authors",
            modMetadata.getAuthorList()
        );
        descriptor.put(
            "description",
            modMetadata.description
        );
        return descriptor;
        */
        return null;//TODO
    }

    @Override
    public Disableable canBeDisabled() {
        return disableability;
    }

    @Override
    public String getGuiClassName() {
        return descriptor.get("guiFactory");
    }

    @Override
    public List<String> getOwnedPackages() {
        return modCandidate.getContainedPackages();
    }

    private boolean isEnabled() {
        try {
            Field field = ReflectionHelper.getField(
                FMLModContainer.class,
                "enabled"
            );
            return ReflectionHelper.getFieldValue(
                field,
                modCandidate
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                String.format(
                    "Caught exception while getting field \"%s\" in class \"%s\"!",

                    "enabled",
                    FMLModContainer.class.getCanonicalName()
                ),
                e
            );
            return false;
        }
    }

    private EventBus getEventBus() {
        try {
            Field field = ReflectionHelper.getField(
                FMLModContainer.class,
                "eventBus"
            );
            return ReflectionHelper.getFieldValue(
                field,
                modCandidate
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                String.format(
                    "Caught exception while getting field \"%s\" in class \"%s\"!",

                    "eventBus",
                    FMLModContainer.class.getCanonicalName()
                ),
                e
            );
            return null;
        }
    }

    private void setEventBus(final EventBus eventBus) {
        try {
            Field field = ReflectionHelper.getField(
                FMLModContainer.class,
                "eventBus"
            );
            ReflectionHelper.setFieldValue(
                field,
                modCandidate,
                eventBus
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                String.format(
                    "Caught exception while setting field \"%s\" in class \"%s\"!",

                    "eventBus",
                    FMLModContainer.class.getCanonicalName()
                ),
                e
            );
        }
    }

    @Subscribe
    public void constructMod(final FMLConstructionEvent fmlConstructionEvent) {
        //TODO
        ReTweakResources.RETWEAK_LOGGER.debug("");
    }

    @Subscribe
    public void handleModStateEvent(final FMLStateEvent fmlStateEvent) {
        //TODO
        ReTweakResources.RETWEAK_LOGGER.debug("");
    }

    /*
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
    */

}
