package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.resources.ASMTable.TableClass;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

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
     *
     * @param currentState Such a fragile existence...
     */
    public static void step(LoadController loadController, LoaderState currentState, LoaderState wantedState) {
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
            case INITIALIZATION:
            case POSTINITIALIZATION:
                //NOOP
                break;
            case SERVER_ABOUT_TO_START:
            case SERVER_STARTING:
            case SERVER_STARTED:
            case SERVER_STOPPING:
            case SERVER_STOPPED:
                //Unsupported -- NOOP
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

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.LoadControllerTransformer}
     *
     * Sends a state event to mods
     *
     * @param loadController Load controller instance
     * @param fmlStateEvent State event to send to mods
     */
    public static void sendStateEvent(LoadController loadController, FMLStateEvent fmlStateEvent) {
        if (fmlStateEvent.getClass() == FMLPreInitializationEvent.class) {
            ReTweakModController.preInitialization((FMLPreInitializationEvent)fmlStateEvent);
        } else if (fmlStateEvent.getClass() == FMLInitializationEvent.class) {
            ReTweakModController.initialization((FMLInitializationEvent)fmlStateEvent);
        } else if (fmlStateEvent.getClass() == FMLPostInitializationEvent.class) {
            ReTweakModController.postInitialization((FMLPostInitializationEvent)fmlStateEvent);
        }

        ReTweakResources.RETWEAK_LOGGER.debug(
                "Sent state event {}",
                fmlStateEvent
        );
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

                        for(Field field : modClass.getDeclaredFields()) {
                            field.setAccessible(true);
                            if (field.isAnnotationPresent(Instance.class)) {
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
                                Object classInstance;
                                try {
                                    classInstance = ReflectionHelper.createFromConstructor(
                                            ReflectionHelper.getConstructor(
                                                    modClass,
                                                    null
                                            ),
                                            null
                                    );
                                } catch(InvocationTargetException | InstantiationException | NoSuchMethodException | IllegalAccessException e) {
                                    reTweakModContainer.setEnabled(false);
                                    ReTweakResources.RETWEAK_LOGGER.warn(
                                            "Mod \"{}\" has been disabled due to incorrectly creating a new instance of class \"{}\"",
                                            reTweakModContainer.getModid(),
                                            reTweakModContainer.getModClass()
                                    );
                                    return;
                                }
                                try {
                                    ReflectionHelper.setFieldValue(
                                            field,
                                            null,
                                            classInstance
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
                                reTweakModContainer.setInstance(classInstance);
                            } else if (field.isAnnotationPresent(SidedProxy.class)) {
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
                                        Object proxyInstance = ReflectionHelper.createFromConstructor(
                                                ReflectionHelper.getConstructor(
                                                        proxyClass,
                                                        null
                                                ),
                                                null
                                        );
                                        ReflectionHelper.setFieldValue(
                                                field,
                                                null,
                                                proxyInstance
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
                                } catch(NoClassDefFoundError | ClassNotFoundException e) {
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
                    } catch(NoClassDefFoundError | ClassNotFoundException e) {
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
