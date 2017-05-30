package org.slave.minecraft.retweak.loading.mod;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLLoadEvent;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

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
    private final EventBus eventBus;

    ReTweakLoadController(final GameVersion gameVersion) {
        this.gameVersion = gameVersion;
        eventBus = new EventBus(ReTweakLoadController.ID + "_" + gameVersion.getVersion());
        eventBus.register(this);
    }

    public GameVersion getGameVersion() {
        return gameVersion;
    }

    public void distributeStateMessage(final LoaderState loaderState, final Object... objects) {
        if (loaderState.hasEvent()) {
            eventBus.post(
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
