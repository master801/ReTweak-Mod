package org.slave.minecraft.retweak.load.mod;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.ILanguageAdapter;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.resources.ASMTable;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.ReTweakClassLoader;
import org.slave.minecraft.retweak.load.util.EventAnnotation;
import org.slave.minecraft.retweak.load.util.GameVersion;
import org.slave.minecraft.retweak.load.util.GameVersion.GameVersionModIdentifier.Identifier;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
public final class ReTweakModContainer implements ModContainer  {

    private static final String INFO_MOD_SYSTEM = "ReTweak";

    private final ReTweakModContainerEventHandler eventHandler = new ReTweakModContainerEventHandler(this);

    @Getter
    private final GameVersion gameVersion;

    private final String className;
    private final ReTweakModCandidate reTweakModCandidate;
    private final Map<String, Object> modDescriptor;
    private final String modLanguage;

    @Getter
    private final File source;

    @Getter
    private String version;

    @Getter
    private ModMetadata metadata;

    @Getter
    private Certificate signingCertificate;

    @Getter
    private Map<String, String> customModProperties;

    @Getter
    private Object mod;

    @Getter
    private ILanguageAdapter languageAdapter;

    private boolean enabled = true;
    private EventBus eventBus;
    private VersionRange minecraftAccepted;
    private ArtifactVersion processedVersion;
    private Disableable disableability;
    private ListMultimap<Class<? extends FMLEvent>, Method> eventMethods = ArrayListMultimap.create();

    public ReTweakModContainer(@NonNull final GameVersion gameVersion, @NonNull final String className, @NonNull final ReTweakModCandidate reTweakModCandidate, @NonNull final ASMTable asmTable, @NonNull final Map<String, Object> modDescriptor) {
        this.gameVersion = gameVersion;
        this.className = className;
        this.reTweakModCandidate = reTweakModCandidate;
        reTweakModCandidate.setASMTable(asmTable);
        this.modDescriptor = modDescriptor;
        this.source = reTweakModCandidate.getModContainer();

        modLanguage = (String)modDescriptor.get("modLanguage");
        String modLanguageAdapter = (String)modDescriptor.get("modLanguageAdapter");

        if (Strings.isNullOrEmpty(modLanguageAdapter)) {
            languageAdapter = "scala".equals(modLanguage) ? new ILanguageAdapter.ScalaAdapter() : new ILanguageAdapter.JavaAdapter();
        } else {
            try {
                languageAdapter = (ILanguageAdapter) Class.forName(modLanguageAdapter, true, gameVersion.getClassLoader()).newInstance();
//                FMLLog.finer("Using custom language adapter %s (type %s) for %s (modid %s)", languageAdapter, modLanguageAdapter, this.className, getModId());
            } catch (Exception ex) {
//                FMLLog.log(Level.ERROR, ex, "Error constructing custom mod language adapter %s (referenced by %s) (modid: %s)", modLanguageAdapter, this.className, getModId());
                throw new LoaderException(ex);
            }
        }
    }

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

        if (Strings.isNullOrEmpty(version)) {
            /*
            Properties versionProps = searchForVersionProperties();
            if (versionProps != null) {
                version = versionProps.getProperty(getModId()+".version");
                FMLLog.log(getModId(), Level.DEBUG, "Found version %s for mod %s in version.properties, using", version, getModId());
            }
            */
        }

        if (Strings.isNullOrEmpty(metadata.name)) {
            metadata.name = getModId();
        }

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
        return metadata.printableSortingRules();
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
            processedVersion = new DefaultArtifactVersion(getModId(), getVersion());
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
        descriptor.put("modsystem", ReTweakModContainer.INFO_MOD_SYSTEM);
        descriptor.put("id", getModId());
        descriptor.put("version", getDisplayVersion());
        descriptor.put("name", getName());
        descriptor.put("url", metadata.url);
        descriptor.put("authors", metadata.getAuthorList());
        descriptor.put("description", metadata.description);
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

    public ReTweakModCandidate getReTweakModCandidate() {
        return reTweakModCandidate;
    }

    /**
     * @param clazz
     * @return Instance factory {@link cpw.mods.fml.common.Mod.InstanceFactory}
     */
    private Method gatherAnnotations(final Class<?> clazz) {
        Method factoryMethod = null;
        for(Method method : clazz.getDeclaredMethods()) {
            for(Annotation annotation : method.getAnnotations()) {
                for(EventAnnotation eventAnnotation : gameVersion.getEventAnnotations()) {
                    if (eventAnnotation.equals(annotation)) {
                        eventMethods.put((Class<? extends FMLEvent>)method.getParameterTypes()[0], method);
                        if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Found event method \"{}( {} )\" in class \"{}\"", method.getName(), Joiner.on(", ").join(method.getParameterTypes()), clazz.getName());
                    } else if (gameVersion.getInstanceFactoryAnnotation() != null && gameVersion.getInstanceFactoryAnnotation().equals(annotation)) {
                        factoryMethod = method;
                    }
                }
            }
        }
        return factoryMethod;
    }

    @SuppressWarnings("unused")
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ReTweakModContainerEventHandler {

        private final ReTweakModContainer reTweakModContainer;

        @Subscribe
        public void constructMod(final FMLConstructionEvent fmlConstructionEvent) {
            try {
                ReTweakClassLoader reTweakClassLoader = reTweakModContainer.getGameVersion().getClassLoader();
                reTweakClassLoader.addFile(reTweakModContainer.getSource());
                reTweakClassLoader.clearNegativeCacheFor(reTweakModContainer.reTweakModCandidate.getClassList());

                Class<?> modClass = Class.forName(reTweakModContainer.className, true, reTweakClassLoader);
                Method factoryMethod = reTweakModContainer.gatherAnnotations(modClass);

                reTweakModContainer.mod = reTweakModContainer.getLanguageAdapter().getNewInstance(null, modClass, reTweakClassLoader, factoryMethod);

                EventAnnotation sidedProxyAnnotation = reTweakModContainer.getGameVersion().getSidedProxyAnnotation();
                if (sidedProxyAnnotation != null) {
                    injectProxy(reTweakModContainer, fmlConstructionEvent.getASMHarvestedData(), sidedProxyAnnotation);
                }
            } catch(Throwable e) {
                ReTweak.LOGGER_RETWEAK.error(
                        "Failed to construct mod!",
                        e
                );
            }
        }

        @Subscribe
        public void handleModStateEvent(final FMLStateEvent fmlStateEvent) {
            Class<? extends FMLStateEvent> fmlStateEventClass = fmlStateEvent.getClass();

            List<Method> eventMethods = Lists.newArrayList();
            if (reTweakModContainer.eventMethods.containsKey(fmlStateEvent.getClass())) {
                List<Method> methods = reTweakModContainer.eventMethods.get(fmlStateEvent.getClass());
                eventMethods.addAll(methods);
            }

            for(Method eventMethod : eventMethods) {
                try {
                    ReflectionHelper.invokeMethod(
                            eventMethod,
                            reTweakModContainer.mod,
                            new Object[] {
                                    fmlStateEvent
                            }
                    );
                    if (ReTweak.DEBUG) ReTweak.LOGGER_RETWEAK.info("Invoked mod event method \"{}({})\" for mod \"{}\"", eventMethod.getName(), Joiner.on(", ").join(eventMethod.getParameterTypes()), reTweakModContainer.getName());
                } catch (IllegalAccessException | InvocationTargetException e) {
                    ReTweak.LOGGER_RETWEAK.error(
                            "Failed to invoke a mod method!",
                            e
                    );
                }
            }
        }

        /**
         * {@link cpw.mods.fml.common.ProxyInjector}
         */
        private static void injectProxy(final ReTweakModContainer reTweakModContainer, final ASMDataTable asmHarvestedData, final EventAnnotation sidedProxyAnnotation) {
            if (reTweakModContainer == null || asmHarvestedData == null || sidedProxyAnnotation == null) return;
            if (sidedProxyAnnotation.getIdentifier() == Identifier.ANNOTATION) {
                Set<ASMData> proxies = asmHarvestedData.getAnnotationsFor(reTweakModContainer).get(sidedProxyAnnotation.getName());

                //TODO
                ReTweak.LOGGER_RETWEAK.info("TODO: INJECT PROXY");
            }
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
