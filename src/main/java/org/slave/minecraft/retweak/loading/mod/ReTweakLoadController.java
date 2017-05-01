package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.Multimap;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLLoadEvent;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;

/**
 * Created by Master on 4/27/2017 at 1:07 AM.
 *
 * @author Master
 */
public final class ReTweakLoadController extends LoadController {

    private final GameVersion gameVersion;
    private final ReTweakLoader reTweakLoader;

    public ReTweakLoadController(final GameVersion gameVersion, final ReTweakLoader reTweakLoader) {
        super(null);
        this.gameVersion = gameVersion;
        this.reTweakLoader = reTweakLoader;
    }

    @Subscribe
    public void buildModList(final FMLLoadEvent event) {
        super.buildModList(event);
    }

    @Override
    public void printModStates(final StringBuilder ret) {
        Multimap<String, ModState> modStateMap = getModStateMap();

        if (modStateMap == null) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "ReTweak's mod state map is null!"
            );
            return;
        }

        ret.append(
                "\n\tStates:"
        );
        for (ModState state : ModState.values()) {
            ret.append(
                    " '"
            ).append(
                    state.getMarker()
            ).append(
                    "' = "
            ).append(
                    state.toString()
            );
        }

        for(ModContainer mc : reTweakLoader.getModList(gameVersion)){
            ret.append(
                    "\n\t"
            );
            for(ModState state : modStateMap.get(mc.getModId())) {
                ret.append(
                        state.getMarker()
                );
            }

            ret.append(
                    "\t"
            ).append(
                    mc.getModId()
            ).append(
                    "{"
            ).append(
                    mc.getVersion()
            ).append(
                    "} ["
            ).append(
                    mc.getName()
            ).append(
                    "] ("
            ).append(
                    mc.getSource().getName()
            ).append(
                    ") "
            );
        }
    }

    private Multimap<String, ModState> getModStateMap() {//Hacky
        try {
            return ReflectionHelper.getFieldValue(
                    ReflectionHelper.getField(
                            LoadController.class,
                            "modStates"
                    ),
                    this
            );
        } catch(NoSuchFieldException | IllegalAccessException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    String.format(
                            "Failed to get mod states field in \"%s\"!",
                            getClass().getCanonicalName()
                    ),
                    e
            );
            return null;
        }
    }

}
