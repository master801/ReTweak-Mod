package org.slave.minecraft.retweak.loading;

import com.github.pwittchen.kirai.library.Kirai;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.InstanceFactory;
import cpw.mods.fml.common.SidedProxy;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Created by Master801 on 4/10/2016 at 9:43 PM.
 *
 * @author Master801
 */
public final class ReTweakStateHandler {

    private ReTweakStateHandler() {
        throw new IllegalStateException();
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoadControllerTransformer}
     * {@link cpw.mods.fml.common.LoadController#transition(cpw.mods.fml.common.LoaderState, boolean)}
     *
     * @param currentState Such a fragile existence...
     */
    public static void step(LoadController loadController, LoaderState currentState, LoaderState wantedState) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        if (loadController == null || currentState == null || wantedState == null) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                    "An error occurred while stepping (variables for {} are null)! The ASM hacks may have failed?",
                    ReTweakStateHandler.class.getName()
            );
            return;
        }
        switch(currentState) {
            case CONSTRUCTING:
                ReTweakStateHandler.constructing(loadController);
                break;
            case PREINITIALIZATION:
                ReTweakModController.preInitialization();
                break;
            case INITIALIZATION:
                ReTweakModController.initialization();
                break;
            case POSTINITIALIZATION:
                ReTweakModController.postInitialization();
                break;
            case SERVER_ABOUT_TO_START:
                ReTweakModController.serverAboutToStart();
                break;
            case SERVER_STARTING:
                ReTweakModController.serverStarting();
                break;
            case SERVER_STARTED:
                ReTweakModController.serverStarted();
                break;
            case SERVER_STOPPING:
                ReTweakModController.serverStopping();
                break;
            case SERVER_STOPPED:
                ReTweakModController.serverStopped();
                break;
            case LOADING:
            case AVAILABLE:
                //NOOP
                break;
            case NOINIT:
            case ERRORED:
                //NOOP
                break;
        }
    }

    private static void constructing(LoadController loadController) {
        FMLCommonHandler.instance().registerCrashCallable(new ICrashCallable() {

            @Override
            public String getLabel() {
                return "ReTweak-Mod";
            }

            @Override
            public String call() throws Exception {
                return "ReTweak is loaded, this crash may have been caused by it.";
            }

        });

        for(GameVersion gameVersion : GameVersion.values()) {
            ReTweakModContainer[] reTweakModContainers = ReTweakLoader.INSTANCE.getReTweakModContainers(gameVersion);
            for(ReTweakModContainer reTweakModContainer : reTweakModContainers) {
                if (!reTweakModContainer.isEnabled()) {
                    ReTweakResources.RETWEAK_LOGGER.info(
                            "Mod {} has been disabled, not loading..."
                    );
                    continue;
                }
                ReTweakClassLoader.getInstance().addFile(reTweakModContainer.getReTweakModCandidate().getFile());

                //Find mcmod.info for mod
                try {
                    JarFile jarFile = ReTweakClassLoader.getInstance().findJarFileForCandidate(reTweakModContainer.getReTweakModCandidate());
                    ZipEntry entry = jarFile.getEntry("mcmod.info");
                    if (entry != null) {
                        InputStream is = jarFile.getInputStream(entry);
                        reTweakModContainer.bindMetadata(MetadataCollection.from(
                                is,
                                jarFile.getName()
                        ));
                        is.close();
                    }
                    jarFile.close();
                } catch(IOException e) {
                    ReTweakResources.RETWEAK_LOGGER.warn(
                            Kirai.from(
                                    "Caught exception while getting \"{name}\" from file \"{file_path}\"!"
                            ).put(
                                    "name",
                                    "mcmod.info"
                            ).put(
                                    "file_path",
                                    reTweakModContainer.getReTweakModCandidate().getFile().getPath()
                            ).format().toString(),
                            e
                    );
                }

                for(TableClass tableClass : reTweakModContainer.getReTweakModCandidate().getModClasses()) {
                    try {
                        Class<?> modClass = Class.forName(
                                tableClass.getName().replace(
                                        '/',
                                        '.'
                                ),
                                true,
                                ReTweakClassLoader.getInstance()
                        );

                        //Somewhat stolen from FML
                        Method factoryMethod = null;
                        for(Method method : modClass.getDeclaredMethods()) {
                            if (method.isAnnotationPresent(InstanceFactory.class)) {
                                if (factoryMethod == null) {
                                    if (Modifier.isStatic(method.getModifiers()) && method.getParameterTypes().length == 0) {
                                        method.setAccessible(true);
                                        factoryMethod = method;
                                    } else {
                                        ReTweakResources.RETWEAK_LOGGER.warn(
                                                "Instance factory method for mod \"{}\" is not static and/or may have parameters!",
                                                reTweakModContainer.getModid()
                                        );
                                    }
                                } else {
                                    ReTweakResources.RETWEAK_LOGGER.warn(
                                            "Found duplicate instance factory methods! Duplicate name: \"{}\"",
                                            method.getName()
                                    );
                                    break;
                                }
                            }
                        }

                        if (modClass.isAnnotationPresent(Mod.class)) {//Find mod annotation
                            Object classInstance;
                            try {
                                classInstance = reTweakModContainer.getLanguageAdapter().getNewInstance(
                                        null,//No FML mod container...
                                        modClass,
                                        Loader.instance().getModClassLoader(),
                                        factoryMethod
                                );
                            } catch(Exception e) {
                                reTweakModContainer.setEnabled(false);
                                ReTweakResources.RETWEAK_LOGGER.warn(
                                        "Mod \"{}\" has been disabled due to incorrectly creating a new instance of class \"{}\"",
                                        reTweakModContainer.getModid(),
                                        reTweakModContainer.getModClass()
                                );
                                return;
                            }
                            reTweakModContainer.setInstance(classInstance);
                        } else {
                            ReTweakResources.RETWEAK_LOGGER.warn(
                                    "Mod \"{}\" cannot load due to it not being an actual mod? File path: {}",
                                    reTweakModContainer.getModid(),
                                    reTweakModContainer.getReTweakModCandidate().getFile().getPath()
                            );
                            return;
                        }

                        for(Field field : modClass.getDeclaredFields()) {
                            field.setAccessible(true);

                            if (field.isAnnotationPresent(Instance.class)) {//Set instance
                                if (!Modifier.isStatic(field.getModifiers())) {
                                    reTweakModContainer.setEnabled(false);
                                    ReTweakResources.RETWEAK_LOGGER.error(
                                            "Mod \"{}\" has been disabled due to the \"{}\" field not being static.",
                                            Instance.class.getCanonicalName(),
                                            reTweakModContainer.getModClass()
                                    );
                                    return;
                                }
                                Instance instance = field.getAnnotation(Instance.class);
                                if (!instance.value().equals(reTweakModContainer.getModid())) {
                                    throw new UnsupportedOperationException("Instance annotation value must be the mod's modid! Injecting other mods' instances is not yet supported!");
                                }
                                try {
                                    ReflectionHelper.setFieldValue(
                                            field,
                                            null,
                                            reTweakModContainer.getInstance()
                                    );
                                } catch(IllegalAccessException e) {
                                    reTweakModContainer.setEnabled(false);
                                    ReTweakResources.RETWEAK_LOGGER.warn(
                                            "Mod \"{}\" has been disabled due to being unable to set the instance of the class, to the field with the \"{}\" annotation.",
                                            reTweakModContainer.getModid(),
                                            Instance.class.getCanonicalName()
                                    );
                                    return;
                                }
                            } else if (field.isAnnotationPresent(SidedProxy.class)) {//Set proxy
                                SidedProxy sidedProxy = field.getAnnotation(SidedProxy.class);
                                if (!Modifier.isStatic(field.getModifiers())) {
                                    reTweakModContainer.setEnabled(false);
                                    ReTweakResources.RETWEAK_LOGGER.error(
                                            "Mod \"{}\" has been disabled due to the \"{}\" field not being static.",
                                            SidedProxy.class.getCanonicalName(),
                                            reTweakModContainer.getModClass()
                                    );
                                    return;
                                }
                                if (!StringHelper.isNullOrEmpty(sidedProxy.modId())) throw new UnsupportedOperationException("Modids for SidedProxies are not yet supported!");
                                String proxyClassName = FMLCommonHandler.instance().getSide().isClient() ? sidedProxy.clientSide() : sidedProxy.serverSide();
                                try {
                                    Class<?> proxyClass = Class.forName(
                                            proxyClassName,
                                            true,
                                            ReTweakClassLoader.getInstance()
                                    );

                                    try {
                                        ReflectionHelper.setFieldValue(
                                                field,
                                                null,
                                                ReflectionHelper.createFromConstructor(
                                                        ReflectionHelper.getConstructor(
                                                                proxyClass,
                                                                new Class<?>[0]
                                                        ),
                                                        new Object[0]
                                                )
                                        );
                                    } catch(NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                                        reTweakModContainer.setEnabled(false);
                                        ReTweakResources.RETWEAK_LOGGER.warn(
                                                "Mod \"{}\" has been disabled due to being unable to set the proxy instance to the \"{}\" field",
                                                reTweakModContainer.getModid(),
                                                SidedProxy.class.getCanonicalName()
                                        );
                                        return;
                                    }
                                } catch(ClassNotFoundException e) {
                                    reTweakModContainer.setEnabled(false);
                                    ReTweakResources.RETWEAK_LOGGER.error(
                                            "Mod \"{}\" has been disabled due to the proxy class \"{}\" being unable to be found.",
                                            reTweakModContainer.getModClass(),
                                            proxyClassName
                                    );
                                    return;
                                }
                            }
                        }
                    } catch(ClassNotFoundException e) {
                        ReTweakResources.RETWEAK_LOGGER.error(
                                "Failed to load a mod class!",
                                e
                        );
                    }
                }
            }
        }
    }

}
