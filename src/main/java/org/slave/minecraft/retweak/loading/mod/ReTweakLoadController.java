package org.slave.minecraft.retweak.loading.mod;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.BiMap;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLLoadEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import cpw.mods.fml.common.functions.ArtifactVersionNameFunction;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import org.apache.logging.log4j.ThreadContext;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;
import org.slave.minecraft.retweak.util.ReTweakResources;
import org.slf4j.MarkerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

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

    private final ReTweakLoader reTweakLoader;
    private final GameVersion gameVersion;
    private final EventBus channel;

    private LoadController fmlLoadController;//Cache

    private ImmutableMap<String, EventBus> modChannels;
    private Multimap<String, ModState> modStates = ArrayListMultimap.create();
    private List<ModContainer> activeModList = Lists.newArrayList();
    private ModContainer activeContainer;
    private BiMap<ModContainer, Object> modObjectList;

    private LoaderState loaderState;

    ReTweakLoadController(final ReTweakLoader reTweakLoader, final GameVersion gameVersion) {
        this.reTweakLoader = reTweakLoader;
        this.gameVersion = gameVersion;
        channel = new EventBus(ReTweakLoadController.ID + "_" + gameVersion.getVersion());
        channel.register(this);
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
        if (loaderState == null || !loaderState.hasEvent()) return;

        channel.post(
            loaderState.getEvent(objects)
        );
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void buildModList(final FMLLoadEvent fmlLoadEvent) {
        Builder<String, EventBus> eventBus = ImmutableMap.builder();
        for(ModContainer mod : reTweakLoader.getModList(gameVersion)) {
            EventBus bus = new EventBus(mod.getModId());
            boolean isActive = mod.registerBus(//Load Controller is a dummy
                bus,
                getFMLLoadController()
            );
            if (isActive) {
                activeModList.add(mod);
                modStates.put(
                    mod.getModId(),
                    ModState.UNLOADED
                );
                eventBus.put(
                    mod.getModId(),
                    bus
                );
//                FMLCommonHandler.instance().addModToResourcePack(mod);//TODO
            } else {
//                FMLLog.log(mod.getModId(), Level.WARN, "Mod %s has been disabled through configuration", mod.getModId());//TODO
                modStates.put(
                    mod.getModId(),
                    ModState.UNLOADED
                );
                modStates.put(
                    mod.getModId(),
                    ModState.DISABLED
                );
            }
        }

        modChannels = eventBus.build();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void propogateStateMessage(final FMLEvent fmlEvent) {
        if (fmlEvent instanceof FMLPreInitializationEvent) {
            modObjectList = buildModObjectList();
        }
        for(ModContainer mc : activeModList) {
            sendEventToModContainer(
                fmlEvent,
                mc
            );
        }
    }

    private void sendEventToModContainer(final FMLEvent stateEvent, final ModContainer mc) {
        String modId = mc.getModId();
        Collection<String> requirements =  Collections2.transform(
            mc.getRequirements(),
            new ArtifactVersionNameFunction()
        );
        for(ArtifactVersion av : mc.getDependencies()) {
            if (av.getLabel()!= null && requirements.contains(av.getLabel()) && modStates.containsEntry(av.getLabel(), ModState.ERRORED)) {
//                FMLLog.log(modId, Level.ERROR, "Skipping event %s and marking errored mod %s since required dependency %s has errored", stateEvent.getEventType(), modId, av.getLabel());
                modStates.put(modId, ModState.ERRORED);
                return;
            }
        }
        activeContainer = mc;
        stateEvent.applyModContainer(activeContainer);
        ThreadContext.put(
            "mod",
            modId
        );

        ReTweakResources.RETWEAK_LOGGER.debug(
            "Sending event \"{}\" to mod \"{}\"",
            stateEvent.getEventType(),
            modId
        );

        modChannels.get(
            modId
        ).post(
            stateEvent
        );

        ReTweakResources.RETWEAK_LOGGER.debug(
            "Sent event \"{}\" to mod \"{}\"",
            stateEvent.getEventType(),
            modId
        );

        ThreadContext.remove("mod");
        activeContainer = null;
        if (stateEvent instanceof FMLStateEvent) {
//            if (!errors.containsKey(modId)) {
                modStates.put(
                    modId,
                    ((FMLStateEvent)stateEvent).getModState()
                );
//            } else {
//                modStates.put(modId, ModState.ERRORED);
//            }
        }
    }

    public ImmutableBiMap<ModContainer, Object> buildModObjectList() {
        ImmutableBiMap.Builder<ModContainer, Object> builder = ImmutableBiMap.builder();
        for(ModContainer mc : activeModList) {
            if (!mc.isImmutable() && mc.getMod()!= null) {
                builder.put(
                    mc,
                    mc.getMod()
                );
            }
        }
        return builder.build();
    }

    private LoadController getFMLLoadController() {
        if (fmlLoadController == null) {
            try {
                Field fieldLoadController = ReflectionHelper.getField(
                    LoadController.class,
                    "modController"
                );
                fmlLoadController = ReflectionHelper.getFieldValue(
                    fieldLoadController,
                    Loader.instance()
                );
            } catch(Exception e) {
                ReTweakResources.RETWEAK_LOGGER.debug(
                    MarkerFactory.getMarker("WARN"),

                    "Failed to get instance of \"{}\"!",
                    LoadController.class.getCanonicalName()
                );
            }
        }
        return fmlLoadController;
    }

    LoaderState getLoaderState() {
        return loaderState;
    }

}
