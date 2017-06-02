package org.slave.minecraft.retweak.loading.mod;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.base.Throwables;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ILanguageAdapter;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.Metadata;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.ProxyInjector;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.ASMDataTable.ASMData;
import cpw.mods.fml.common.discovery.ModCandidate;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;
import org.apache.logging.log4j.Level;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.cert.Certificate;
import java.util.Arrays;
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

    private static final String INFO_MOD_SYSTEM = "ReTweak";

    private static final String INFO_MODID = "modid";
    private static final String INFO_NAME = "name";
    private static final String INFO_USE_METADATA = "useMetadata";
    private static final String INFO_ACCEPTED_MINECRAFT_VERSIONS = "acceptedMinecraftVersions";

    private static final String MOD_LANGUAGE_JAVA = "java";
    private static final String MOD_LANGUAGE_SCALA = "scala";

    private final GameVersion gameVersion;
    private final String modClassName;
    private final ModCandidate modCandidate;
    private final Map<String, Object> descriptor;
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
    private EventBus eventBus;
    private boolean overridesMetadata;
    private String modLanguage;
    private ILanguageAdapter languageAdapter;

    private ListMultimap<Class<? extends FMLEvent>, Method> eventMethods = ArrayListMultimap.create();

    public ReTweakModContainer(final GameVersion gameVersion, final String modClassName, final ModCandidate modCandidate, final Map<String, Object> descriptor) {
        this.gameVersion = gameVersion;
        this.modClassName = modClassName;
        this.modCandidate = modCandidate;
        this.descriptor = descriptor;
        this.source = modCandidate.getModContainer();

        modLanguage = (String)descriptor.get("modLanguage");
        languageAdapter = ReTweakModContainer.createLanguageAdapter(
            modLanguage,
            (String)descriptor.get("modLanguageAdapter")
        );
    }

    @Override
    public String getModId() {
        return (String)descriptor.get(ReTweakModContainer.INFO_MODID);
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
        modMetadata = mc.getMetadataForId(
            getModId(),
            descriptor
        );

        if (descriptor.containsKey(ReTweakModContainer.INFO_USE_METADATA)) {
            overridesMetadata = !(boolean)descriptor.get(ReTweakModContainer.INFO_USE_METADATA);
        }

        if (overridesMetadata || !modMetadata.useDependencyInformation) {
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
        if (Strings.isNullOrEmpty(modMetadata.name)) {
//            FMLLog.log(getModId(), Level.INFO,"Mod %s is missing the required element 'name'. Substituting %s", getModId(), getModId());//TODO
            modMetadata.name = getModId();
        }
        internalVersion = (String)descriptor.get("version");
        /*//TODO
        if (Strings.isNullOrEmpty(internalVersion)) {
            Properties versionProps = searchForVersionProperties();
            if (versionProps != null) {
                internalVersion = versionProps.getProperty(getModId()+".version");
                FMLLog.log(getModId(), Level.DEBUG, "Found version %s for mod %s in version.properties, using", internalVersion, getModId());
            }
        }
        */
        if (Strings.isNullOrEmpty(internalVersion) && !Strings.isNullOrEmpty(modMetadata.version)) {
//            FMLLog.log(getModId(), Level.WARN, "Mod %s is missing the required element 'version' and a version.properties file could not be found. Falling back to metadata version %s", getModId(), modMetadata.version);//TODO
            internalVersion = modMetadata.version;
        }
        if (Strings.isNullOrEmpty(internalVersion)) {
//            FMLLog.log(getModId(), Level.WARN, "Mod %s is missing the required element 'version' and no fallback can be found. Substituting '1.0'.", getModId());//TODO
            modMetadata.version = internalVersion = "1.0";
        }

        String mcVersionString = (String)descriptor.get(ReTweakModContainer.INFO_ACCEPTED_MINECRAFT_VERSIONS);
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
        if (enabled) {
            eventBus = bus;
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
    }

    @Override
    public Disableable canBeDisabled() {
        return disableability;
    }

    @Override
    public String getGuiClassName() {
        return (String)descriptor.get("guiFactory");
    }

    @Override
    public List<String> getOwnedPackages() {
        return modCandidate.getContainedPackages();
    }

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    @Subscribe
    public void constructMod(@SuppressWarnings("unused") final FMLConstructionEvent fmlConstructionEvent) {
        try {
            ReTweakClassLoader reTweakClassLoader = ReTweakClassLoader.getReTweakClassLoader(gameVersion);
            reTweakClassLoader.addFile(source);

            Class<?> modClass = Class.forName(
                modClassName,
                true,
                reTweakClassLoader
            );

            Method factoryMethod = gatherAnnotations(modClass);
            if (!modLanguage.equals("java")) {
                ReTweakResources.RETWEAK_LOGGER.warn(
                    "Custom languages are not yet supported!"
                );
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Mod ID: \"{}\", Mod language: \"{}\", File path: \"{}\"",
                    getModId(),
                    modLanguage,
                    source.getAbsolutePath()
                );
                return;
            }
            modInstance = getLanguageAdapter().getNewInstance(
                null,//TODO RIP custom language adapter support
                modClass,
                reTweakClassLoader,
                factoryMethod
            );
            /*//TODO
            NetworkRegistry.INSTANCE.register(
                this,
                clazz,
                (String)(descriptor.containsKey("acceptableRemoteVersions") ? descriptor.get("acceptableRemoteVersions") : null),
                event.getASMHarvestedData()
            );
            */
            ProxyInjector.inject(
                this,
                fmlConstructionEvent.getASMHarvestedData(),
                FMLCommonHandler.instance().getSide(),
                getLanguageAdapter()
            );
            processFieldAnnotations(
                fmlConstructionEvent.getASMHarvestedData()
            );

            //TODO
            ReTweakResources.RETWEAK_LOGGER.debug("");
        } catch(Throwable throwable) {
            ReTweakResources.RETWEAK_LOGGER.error(
                String.format(
                    "Failed to construct mod \"%s\"!",
                    getModId()
                ),
                throwable
            );
            ReTweakResources.RETWEAK_LOGGER.debug(
                "Mod ID: \"{}\", File path: \"{}\"",
                getModId(),
                source != null ? source.getAbsolutePath() : "NO FOUND FILE PATH! THIS IS CONSIDERED AN ERROR!"
            );
        }
    }

    @Subscribe
    public void handleModStateEvent(final FMLStateEvent fmlStateEvent) {
        if (eventMethods.containsKey(fmlStateEvent.getClass())) {
            try {
                for(Method method : eventMethods.get(fmlStateEvent.getClass())) {
                    ReflectionHelper.invokeMethod(
                        method,
                        modInstance,
                        new Object[] {
                            fmlStateEvent
                        }
                    );
                }
            } catch(Throwable e) {
                ReTweakResources.RETWEAK_LOGGER.error(
                    "Couldn't handle mod state event!",
                    e
                );
                ReTweakResources.RETWEAK_LOGGER.debug(
                    "Mod ID: \"{}\", File path: \"{}\"",
                    getModId(),
                    source.getAbsolutePath()
                );
            }
        }
    }

    private ILanguageAdapter getLanguageAdapter() {
        return languageAdapter;
    }

    private void processFieldAnnotations(ASMDataTable asmDataTable) throws Exception {
        SetMultimap<String, ASMData> annotations = asmDataTable.getAnnotationsFor(this);

        //TODO
        parseSimpleFieldAnnotation(
            annotations,
            Instance.class.getName(),
            ModContainer::getMod
        );
        parseSimpleFieldAnnotation(
            annotations,
            Metadata.class.getName(),
            ModContainer::getMetadata
        );
    }

    private void parseSimpleFieldAnnotation(final SetMultimap<String, ASMData> annotations, final String annotationClassName, final Function<ModContainer, Object> retreiver) throws IllegalAccessException {
        String[] annName = annotationClassName.split("\\.");
        String annotationName = annName[annName.length - 1];
        for(ASMData targets : annotations.get(annotationClassName)) {
            String targetMod = (String) targets.getAnnotationInfo().get("value");
            Field field = null;
            Object injectedMod = null;
            ModContainer modContainer = this;
            boolean isStatic = false;
            Class<?> modClass = modInstance.getClass();
            if (!Strings.isNullOrEmpty(targetMod)) {
                if (Loader.isModLoaded(targetMod)) {
                    modContainer = Loader.instance().getIndexedModList().get(targetMod);
                } else {
                    modContainer = null;
                }
            }
            if (modContainer != null) {
                try {
                    modClass = Class.forName(
                        targets.getClassName(),
                        true,
                        ReTweakClassLoader.getReTweakClassLoader(gameVersion)
                    );
                    field = ReflectionHelper.getField(
                        modClass,
                        targets.getObjectName()
                    );
                    isStatic = Modifier.isStatic(field.getModifiers());
                    injectedMod = retreiver.apply(modContainer);
                } catch (Exception e) {
                    Throwables.propagateIfPossible(e);
                    /*//TODO
                    FMLLog.log(
                        getModId(),
                        Level.WARN,
                        e,
                        "Attempting to load @%s in class %s for %s and failing",
                        annotationName,
                        targets.getClassName(),
                        modContainer.getModId()
                    );
                    */
                }
            }
            if (field != null) {
                Object target = null;
                if (!isStatic) {
                    target = modInstance;
                    if (!modInstance.getClass().equals(modClass)) {
                        /*//TODO
                        FMLLog.log(
                            getModId(),
                            Level.WARN,
                            "Unable to inject @%s in non-static field %s.%s for %s as it is NOT the primary mod instance",
                            annotationName,
                            targets.getClassName(),
                            targets.getObjectName(),
                            modContainer.getModId()
                        );
                        */
                        continue;
                    }
                }
                ReflectionHelper.setFieldValue(
                    field,
                    target,
                    injectedMod
                );
            }
        }
    }

    private Method gatherAnnotations(final Class<?> clazz) throws Exception {
        //TODO
        Method factoryMethod = null;
        for(Method method : clazz.getDeclaredMethods()) {
            for(Annotation annotation : method.getAnnotations()) {
                if (annotation.annotationType().equals(Mod.EventHandler.class)) {
                    if (method.getParameterTypes().length == 1 && FMLEvent.class.isAssignableFrom(method.getParameterTypes()[0])) {
                        method.setAccessible(true);
                        eventMethods.put(
                            (Class<? extends FMLEvent>)method.getParameterTypes()[0],
                            method
                        );
                    } else {
                        FMLLog.log(
                            getModId(),
                            Level.ERROR,
                            "The mod %s appears to have an invalid event annotation %s. This annotation can only apply to methods with recognized event arguments - it will not be called",
                            getModId(),
                            annotation.annotationType().getSimpleName()
                        );
                    }
                } else if (annotation.annotationType().equals(Mod.InstanceFactory.class)) {
                    if (Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 0 && factoryMethod == null) {
                        method.setAccessible(true);
                        factoryMethod = method;
                    } else if (!(Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 0)) {
                        FMLLog.log(
                            getModId(),
                            Level.ERROR,
                            "The InstanceFactory annotation can only apply to a static method, taking zero arguments - it will be ignored on %s(%s)",
                            method.getName(),
                            Arrays.asList(method.getParameterTypes())
                        );
                    } else if (factoryMethod != null) {
                        FMLLog.log(
                            getModId(),
                            Level.ERROR,
                            "The InstanceFactory annotation can only be used once, the application to %s(%s) will be ignored",
                            method.getName(),
                            Arrays.asList(method.getParameterTypes())
                        );
                    }
                }
            }
        }
        return factoryMethod;
    }

    private static ILanguageAdapter createLanguageAdapter(final String languageAdapterName, final String customLanguageAdapter) {
        ILanguageAdapter languageAdapter = null;
        if (languageAdapterName != null) {
            switch(languageAdapterName.toLowerCase()) {
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
