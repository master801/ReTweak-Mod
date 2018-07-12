package org.slave.minecraft.retweak.load;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;

import java.lang.reflect.Field;

/**
 * Created by Master on 7/11/2018 at 12:26 PM.
 *
 * @author Master
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public final class ReTweakLoadController {

    private static Field fieldLoadController;

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController.MethodVisitorTransition}
     */
    public void transition(final LoaderState desiredState, final boolean forceState) {
        switch(desiredState) {
            case NOINIT:
                break;
            case LOADING:
                break;
            case CONSTRUCTING:
                break;
            case PREINITIALIZATION:
                break;
            case INITIALIZATION:
                break;
            case POSTINITIALIZATION:
                break;
            case AVAILABLE:
                break;
            case SERVER_ABOUT_TO_START:
                break;
            case SERVER_STARTING:
                break;
            case SERVER_STARTED:
                break;
            case SERVER_STOPPING:
                break;
            case SERVER_STOPPED:
                break;
            case ERRORED:
                break;
        }
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController.MethodVisitorDistributeStateMessage}
     */
    public void distributeStateMessage(final LoaderState state, final Object... eventData) {
        switch(state) {
            case LOADING:
                break;
            case CONSTRUCTING:
                break;
            case PREINITIALIZATION:
                break;
            case INITIALIZATION:
                break;
            case POSTINITIALIZATION:
                break;
            case AVAILABLE:
                break;
            case SERVER_ABOUT_TO_START:
                break;
            case SERVER_STARTING:
                break;
            case SERVER_STARTED:
                break;
            case SERVER_STOPPING:
                break;
            case SERVER_STOPPED:
                break;
            case ERRORED:
                break;
        }
    }

    /**
     * {@link org.slave.minecraft.retweak.asm.transformers.TransformerLoadController.MethodVisitorDistributeStateEvent}
     */
    public void distributeStateMessage(final Class<?> eventClass) {
    }

    public static ReTweakLoadController instance() {
        return ReTweakLoader.instance().getReTweakLoadController();
    }

    public static LoadController getLoadController() {
        try {
            if (ReTweakLoadController.fieldLoadController == null) ReTweakLoadController.fieldLoadController = ReflectionHelper.getField(Loader.class, "modController");
            return ReflectionHelper.getFieldValue(
                    ReTweakLoadController.fieldLoadController,
                    Loader.instance()
            );
        } catch (IllegalAccessException | NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get Load Controller!",
                    e
            );
            return null;
        }
    }

}
