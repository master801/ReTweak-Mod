package org.slave.minecraft.retweak.loading.mod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLLoadEvent;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 *     {@link cpw.mods.fml.common.LoadController}
 * </p>
 *
 * Created by Master on 5/30/2017 at 9:21 AM.
 *
 * @author Master
 */
public final class ReTweakLoadController {

    private static final String ID = "ReTweakMainChannel";

    private final GameVersion gameVersion;
    private final EventBus channel;

    ReTweakLoadController(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
        channel = new EventBus(ReTweakLoadController.ID + "_" + gameVersion.getVersion());
        channel.register(this);
    }

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    public void distributeStateMessage(final Class<?> customEvent) {
        if (customEvent == null) return;

        Object instance;
        try {
            Constructor<?> constructor = ReflectionHelper.getConstructor(
                customEvent,
                new Class<?>[0]
            );
            instance = ReflectionHelper.createFromConstructor(
                constructor,
                new Object[0]
            );
        } catch(NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            ReTweakResources.RETWEAK_LOGGER.warn(
                String.format(
                    "Failed to create a new instance of event class \"%s\"!",
                    customEvent.getCanonicalName()
                ),
                e
            );
            return;
        }
        if (instance != null) {
            channel.post(
                instance
            );
        }
    }

    public void distributeStateMessage(final LoaderState loaderState, final Object... objects) {
        if (loaderState.hasEvent()) {
            channel.post(
                loaderState.getEvent(objects)
            );
        }
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void buildModList(final FMLLoadEvent fmlLoadEvent) {
        //TODO
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void propogateStateMessage(final FMLEvent fmlEvent) {
        //TODO
    }

}
