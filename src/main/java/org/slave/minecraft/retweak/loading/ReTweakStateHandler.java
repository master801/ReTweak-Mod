package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ICrashCallable;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState;
import org.slave.minecraft.retweak.loading.capsule.GameVersion;
import org.slave.minecraft.retweak.resources.ReTweakResources;

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
                ReTweakModController.preInitialization();
                break;
            case INITIALIZATION:
                ReTweakModController.initialization();
                break;
            case POSTINITIALIZATION:
                ReTweakModController.postInitialization();
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
                for(String modClassName : reTweakModContainer.getReTweakModCandidate().getModClasses()) {
                    modClassName = modClassName.replace(
                            '/',
                            '.'
                    ).substring(
                            0,
                            modClassName.indexOf(".class")
                    );

                    try {
                        Class.forName(
                                modClassName,
                                true,
                                ReTweakClassLoader.getInstance()
                        );
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
