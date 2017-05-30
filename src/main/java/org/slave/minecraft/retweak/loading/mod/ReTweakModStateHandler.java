package org.slave.minecraft.retweak.loading.mod;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Master on 4/26/2016 at 3:26 PM.
 *
 * @author Master
 */
public final class ReTweakModStateHandler {

    private static final String STATE_NAME_PREINITIALIZATION = "pre-initialization";
    private static final String STATE_NAME_INITIALIZATION = "initialization";
    private static final String STATE_NAME_POSTINITIALIZATION = "post-initialization";
    private static final String STATE_NAME_SERVER_ABOUT_TO_START = "server about to start";
    private static final String STATE_NAME_SERVER_STARTING = "server starting";
    private static final String STATE_NAME_SERVER_STARTED = "server started";
    private static final String STATE_NAME_SERVER_STOPPING = "server stopping";
    private static final String STATE_NAME_SERVER_STOPPED = "server stopped";

    private ReTweakModStateHandler() {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        throw new IllegalStateException();
    }

    static void preInitialization() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_PREINITIALIZATION,
                FMLPreInitializationEvent.class
        );
    }

    static void initialization() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_INITIALIZATION,
                FMLInitializationEvent.class
        );
    }

    static void postInitialization() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_POSTINITIALIZATION,
                FMLPostInitializationEvent.class
        );
    }

    static void serverAboutToStart() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_SERVER_ABOUT_TO_START,
                FMLServerAboutToStartEvent.class
        );
    }

    static void serverStarting() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_SERVER_STARTING,
                FMLServerStartingEvent.class
        );
    }

    static void serverStarted() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_SERVER_STARTED,
                FMLServerStartedEvent.class
        );
    }

    static void serverStopping() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_SERVER_STOPPING,
                FMLServerStoppingEvent.class
        );
    }

    static void serverStopped() throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY_ = null;
        ReTweakModStateHandler.callState(
                ReTweakModStateHandler.STATE_NAME_SERVER_STOPPED,
                FMLServerStoppedEvent.class
        );
    }

    private static void callState(final String stateName, final Class<? extends FMLStateEvent> fmlStateEventClass) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        final Object _RETWEAK_INTERNAL_USAGE_ONLY = null;
        //TODO


        ReTweakResources.RETWEAK_LOGGER.debug("");
    }

}
