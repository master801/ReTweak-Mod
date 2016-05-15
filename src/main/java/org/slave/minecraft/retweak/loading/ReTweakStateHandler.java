package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
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

                        if (modClass.isAnnotationPresent(Mod.class)) {
                            Object classInstance;
                            try {
                                classInstance = ReflectionHelper.createFromConstructor(
                                        ReflectionHelper.getConstructor(
                                                modClass,
                                                new Class<?>[0]
                                        ),
                                        new Object[0]
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
                            reTweakModContainer.setInstance(classInstance);
                        }

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
