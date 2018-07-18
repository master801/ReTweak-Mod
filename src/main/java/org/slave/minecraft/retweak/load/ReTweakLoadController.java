package org.slave.minecraft.retweak.load;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Multimap;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.LoaderState.ModState;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.event.FMLEvent;
import cpw.mods.fml.common.event.FMLLoadEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.ReTweak;
import org.slave.minecraft.retweak.load.mod.ReTweakModContainer;
import org.slave.minecraft.retweak.load.util.GameVersion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Master on 7/11/2018 at 12:26 PM.
 *
 * @author Master
 */
@SuppressWarnings("ALL")
public final class ReTweakLoadController extends LoadController {

    private static Field fieldLoadController;
    private static Field fieldMasterChannel;
    private static Field fieldModObjectList;
    private static Field fieldModStates;
    private static Field fieldEventChannels;
    private static Field fieldLoaderState;

    private static Method methodSendEventToModContainer;

    private final GameVersion gameVersion;

    public ReTweakLoadController(final GameVersion gameVersion) {
        super(null);
        this.gameVersion = gameVersion;

        EventBus eventBus = new EventBus("ReTweak-" + gameVersion.getVersion());
        eventBus.register(this);

        setMasterChannel(eventBus);
    }

    @Subscribe
    @Override
    public void buildModList(final FMLLoadEvent event) {
        Builder<String, EventBus> eventBus = ImmutableMap.builder();
        for(ReTweakModContainer mod : ReTweakLoader.instance().getMods(gameVersion)) {
            //Create mod logger, and make the EventBus logger a child of it.
            EventBus bus = new EventBus(mod.getModId());
            boolean isActive = mod.registerBus(bus, this);
            if (isActive) {
                getActiveModList().add(mod);
                getModStates().put(mod.getModId(), ModState.UNLOADED);
                eventBus.put(mod.getModId(), bus);
//                FMLCommonHandler.instance().addModToResourcePack(mod);//FIXME
            } else {
                FMLLog.log(mod.getModId(), Level.WARN, "Mod %s has been disabled through configuration", mod.getModId());
                getModStates().put(mod.getModId(), ModState.UNLOADED);
                getModStates().put(mod.getModId(), ModState.DISABLED);
            }
        }

        setEventChannels(eventBus.build());
    }

    @Subscribe
    @Override
    public void propogateStateMessage(final FMLEvent stateEvent) {
        if (stateEvent instanceof FMLPreInitializationEvent) {
            setModObjectList(buildModObjectList());
        }
        for (ModContainer mc : getActiveModList()) {
            sendEventToModContainer(stateEvent, mc);
        }
    }

    @Override
    public void printModStates(final StringBuilder ret) {
        ret.append("\n\tStates:");
        for(ModState state : ModState.values()) {
            ret
                    .append(" '")
                    .append(state.getMarker())
                    .append("' = ")
                    .append(state.toString());
        }

        for(ReTweakModContainer mc : ReTweakLoader.instance().getMods(gameVersion)) {
            ret.append("\n\t");
            for(ModState state : getModStates().get(mc.getModId())) ret.append(state.getMarker());
            ret
                    .append("\t")
                    .append(mc.getModId())
                    .append("{")
                    .append(mc.getVersion())
                    .append("} [")
                    .append(mc.getName())
                    .append("] (")
                    .append(mc.getSource().getName())
                    .append(") ");
        }
    }

    private void sendEventToModContainer(final FMLEvent stateEvent, final ModContainer modContainer) {
        try {
            ReTweakLoadController.methodSendEventToModContainer = ReflectionHelper.getMethod(
                    LoadController.class,
                    "sendEventToModContainer",
                    new Class<?>[] {
                            FMLEvent.class,
                            ModContainer.class
                    }
            );
        } catch (NoSuchMethodException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get method \"sendEventToModContainer\"!",
                    e
            );
            return;
        }

        try {
            ReflectionHelper.invokeMethod(
                    methodSendEventToModContainer,
                    this,
                    new Object[] {
                            stateEvent,
                            modContainer
                    }
            );
        } catch (InvocationTargetException | IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to invoke method \"sendEventToModContainer\"!",
                    e
            );
        }
    }

    private EventBus getMasterChannel() {
        ReTweakLoadController.getFieldMasterChannel();

        try {
            return ReflectionHelper.getFieldValue(ReTweakLoadController.fieldMasterChannel, this);
        } catch(final IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"masterChannel\"!",
                    e
            );
            return null;
        }
    }

    private void setMasterChannel(final EventBus eventBus) {
        ReTweakLoadController.getFieldMasterChannel();

        try {
            ReflectionHelper.setFieldValue(ReTweakLoadController.fieldMasterChannel, this, eventBus);
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to set field \"masterChannel\"!",
                    e
            );
        }
    }

    private Multimap<String, ModState> getModStates() {
        try {
            ReTweakLoadController.fieldModStates = ReflectionHelper.getField(LoadController.class, "modStates");
        } catch (NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"modStates\"!",
                    e
            );
        }

        try {
            return ReflectionHelper.getFieldValue(ReTweakLoadController.fieldModStates, this);
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get value of field \"modStates\"!",
                    e
            );
        }
        return null;
    }

    private void setModObjectList(final BiMap<ModContainer, Object> biMap) {
        try {
            ReTweakLoadController.fieldModObjectList = ReflectionHelper.getField(LoadController.class, "modObjectList");
        } catch (NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"modObjectList\"!",
                    e
            );
        }

        try {
            ReflectionHelper.setFieldValue(ReTweakLoadController.fieldModObjectList, this, biMap);
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to set field \"modObjectList\"!",
                    e
            );
        }
    }

    private void setEventChannels(final ImmutableMap<String, EventBus> build) {
        try {
            fieldEventChannels = ReflectionHelper.getField(LoadController.class, "eventChannels");
        } catch (NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"eventChannels\"!",
                    e
            );
        }

        try {
            ReflectionHelper.setFieldValue(
                    fieldEventChannels,
                    this,
                    build
            );
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to set field \"eventChannels\"!",
                    e
            );
        }
    }

    private void setLoaderState(final LoaderState loaderState) {
        try {
            ReTweakLoadController.fieldLoaderState = ReflectionHelper.getField(LoadController.class, "state");
        } catch (NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get field \"state\"!",
                    e
            );
        }

        try {
            ReflectionHelper.setFieldValue(ReTweakLoadController.fieldLoaderState, this, loaderState);
        } catch (IllegalAccessException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to set field \"state\"!",
                    e
            );
        }
    }

    private static Field getFieldMasterChannel() {
        if (ReTweakLoadController.fieldMasterChannel == null) {
            try {
                return ReTweakLoadController.fieldMasterChannel = ReflectionHelper.getField(LoadController.class, "masterChannel");
            } catch (NoSuchFieldException e) {
                ReTweak.LOGGER_RETWEAK.error(
                        "Could not get field \"masterChannel\"!",
                        e
                );
            }
        }
        return null;
    }

    public static LoadController getLoadController() {
        try {
            if (ReTweakLoadController.fieldLoadController == null) ReTweakLoadController.fieldLoadController = ReflectionHelper.getField(Loader.class, "modController");
            return ReflectionHelper.getFieldValue(ReTweakLoadController.fieldLoadController, Loader.instance());
        } catch (IllegalAccessException | NoSuchFieldException e) {
            ReTweak.LOGGER_RETWEAK.error(
                    "Failed to get Load Controller!",
                    e
            );
            return null;
        }
    }

}
