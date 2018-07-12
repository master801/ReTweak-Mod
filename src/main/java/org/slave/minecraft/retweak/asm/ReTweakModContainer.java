package org.slave.minecraft.retweak.asm;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slave.minecraft.retweak.load.mod.ReTweakModCandidate;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.io.File;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by master on 2/25/18 at 9:28 PM
 *
 * @author master
 */
@SuppressWarnings("UnstableApiUsage")
@RequiredArgsConstructor
public final class ReTweakModContainer implements ModContainer  {

    private static final String INFO_MOD_SYSTEM = "ReTweak";

    private final ReTweakModContainerEventHandler eventHandler = new ReTweakModContainerEventHandler(this);

    @Getter
    private final GameVersion gameVersion;

    private final String className;
    private final ReTweakModCandidate reTweakModCandidate;
    private final Map<String, Object> modDescriptor;

    @Getter
    private String version;

    @Getter
    private ModMetadata metadata;

    @Getter
    private File source;

    @Getter
    private Certificate signingCertificate;

    @Getter
    private Map<String, String> customModProperties;

    @Getter
    private Object mod;

    private EventBus eventBus;
    private VersionRange minecraftAccepted;
    private boolean enabled;
    private ArtifactVersion processedVersion;
    private Disableable disableability;

    @Override
    public String getModId() {
        return (String)modDescriptor.get(Descriptor.MODID.name);
    }

    @Override
    public String getName() {
        return (String)modDescriptor.get(Descriptor.NAME.name);
    }

    @Override
    public void bindMetadata(final MetadataCollection mc) {
        metadata = mc.getMetadataForId(
                getModId(),
                modDescriptor
        );

        boolean overridesMetadata = false;
        if (modDescriptor.containsKey(Descriptor.USE_METADATA.name)) {
            overridesMetadata = !(boolean)modDescriptor.get(Descriptor.USE_METADATA.name);
        }

        if (overridesMetadata || !metadata.useDependencyInformation) {
            /*
            Set<ArtifactVersion> requirements = Sets.newHashSet();
            List<ArtifactVersion> dependencies = Lists.newArrayList();
            List<ArtifactVersion> dependants = Lists.newArrayList();
            annotationDependencies = (String)descriptor.get("dependencies");
            Loader.instance().computeDependencies(annotationDependencies, requirements, dependencies, dependants);
            dependants.addAll(Loader.instance().getInjectedBefore(getModId()));
            dependencies.addAll(Loader.instance().getInjectedAfter(getModId()));
            modMetadata.requiredMods = requirements;
            modMetadata.dependencies = dependencies;
            modMetadata.dependants = dependants;
            FMLLog.log(getModId(), Level.TRACE, "Parsed dependency info : %s %s %s", requirements, dependencies, dependants);
            */
            //TODO
        } else {
//            FMLLog.log(getModId(), Level.TRACE, "Using mcmod dependency info : %s %s %s", modMetadata.requiredMods, modMetadata.dependencies, modMetadata.dependants);
            //TODO
        }
        if (Strings.isNullOrEmpty(metadata.name)) {
//            FMLLog.log(getModId(), Level.INFO,"Mod %s is missing the required element 'name'. Substituting %s", getModId(), getModId());//TODO
            metadata.name = getModId();
        }
        version = (String)modDescriptor.get("version");
        /*//TODO
        if (Strings.isNullOrEmpty(internalVersion)) {
            Properties versionProps = searchForVersionProperties();
            if (versionProps != null) {
                internalVersion = versionProps.getProperty(getModId()+".version");
                FMLLog.log(getModId(), Level.DEBUG, "Found version %s for mod %s in version.properties, using", internalVersion, getModId());
            }
        }
        */
        if (Strings.isNullOrEmpty(version) && !Strings.isNullOrEmpty(metadata.version)) {
//            FMLLog.log(getModId(), Level.WARN, "Mod %s is missing the required element 'version' and a version.properties file could not be found. Falling back to metadata version %s", getModId(), modMetadata.version);//TODO
            version = metadata.version;
        }
        if (Strings.isNullOrEmpty(version)) {
//            FMLLog.log(getModId(), Level.WARN, "Mod %s is missing the required element 'version' and no fallback can be found. Substituting '1.0'.", getModId());//TODO
            metadata.version = version = "1.0";
        }

        String mcVersionString = (String)modDescriptor.get(Descriptor.ACCEPTED_MINECRAFT_VERSIONS.name);
        if (!Strings.isNullOrEmpty(mcVersionString)) {
            minecraftAccepted = VersionParser.parseRange(mcVersionString);
        } else {
            minecraftAccepted = Loader.instance().getMinecraftModContainer().getStaticVersionRange();
        }
    }

    @Override
    public void setEnabledState(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public Set<ArtifactVersion> getRequirements() {
        return metadata.requiredMods;
    }

    @Override
    public List<ArtifactVersion> getDependencies() {
        return metadata.dependencies;
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        return metadata.dependants;
    }

    @Override
    public String getSortingRules() {
        return null;
    }

    @Override
    public boolean registerBus(final EventBus bus, final LoadController controller) {
        if (enabled) {
            eventBus = bus;
            bus.register(eventHandler);
            return true;
        }
        return false;
    }

    @Override
    public boolean matches(final Object mod) {
        return mod == this.mod;
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
        return metadata.version;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return minecraftAccepted;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return null;//NOOP...?
    }

    @Override
    public Map<String, String> getSharedModDescriptor() {
        Map<String,String> descriptor = Maps.newHashMap();
        descriptor.put(
                "modsystem",
                ReTweakModContainer.INFO_MOD_SYSTEM
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
                metadata.url
        );
        descriptor.put(
                "authors",
                metadata.getAuthorList()
        );
        descriptor.put(
                "description",
                metadata.description
        );
        return descriptor;
    }

    @Override
    public Disableable canBeDisabled() {
        return disableability;
    }

    @Override
    public String getGuiClassName() {
        return (String)modDescriptor.get(Descriptor.GUI_FACTORY.name);
    }

    @Override
    public List<String> getOwnedPackages() {
        return reTweakModCandidate.getContainedPackages();
    }

    @SuppressWarnings("unused")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ReTweakModContainerEventHandler {

        private final ReTweakModContainer reTweakModContainer;

        @Subscribe
        public void constructMod(final FMLConstructionEvent fmlConstructionEvent) {
            //TODO
        }

        @Subscribe
        public void handleModStateEvent(final FMLStateEvent fmlStateEvent) {
            //TODO
        }

    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private enum Descriptor {

        MODID("modid", String.class),

        NAME("name", String.class),

        USE_METADATA("useMetadata", boolean.class),

        ACCEPTED_MINECRAFT_VERSIONS("acceptedMinecraftVersions", String.class),

        GUI_FACTORY("guiFactory", String.class);

        final String name;
        final Class<?> valueClass;

    }

}
