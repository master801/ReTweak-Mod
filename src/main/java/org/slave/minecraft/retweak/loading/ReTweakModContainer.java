package org.slave.minecraft.retweak.loading;

import com.github.pwittchen.kirai.library.Kirai;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLStateEvent;
import org.slave.lib.helpers.ReflectionHelper;
import org.slave.minecraft.retweak.resources.ReTweakResources;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Master on 5/7/2016 at 6:36 PM.
 *
 * @author Master
 */
public final class ReTweakModContainer {

    private final String modClass;
    private final String modid;
    private String name;
    private String version;
    private final ReTweakModCandidate reTweakModCandidate;
    private boolean enabled = true;

    private Object instance;

    public ReTweakModContainer(final String modClass, final String modid, final String name, final String version, final ReTweakModCandidate reTweakModCandidate) {
        this.modClass = modClass;
        this.modid = modid;
        this.name = name;
        this.version = version;
        this.reTweakModCandidate = reTweakModCandidate;
    }

    public String getModid() {
        return modid;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    ReTweakModCandidate getReTweakModCandidate() {
        return reTweakModCandidate;
    }

    void setInstance(Object instance) {
        this.instance = instance;
    }

    Object getInstance() {
        return instance;
    }

    String getModClass() {
        return modClass;
    }

    public void callState(Class<? extends FMLStateEvent> fmlStateEventClass) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        if (instance == null) {
            throw new NullPointerException(
                    Kirai.from(
                            "Instance for mod \"{modid}\" is null!"
                    ).put(
                            "modid",
                            getModid()
                    ).format().toString()
            );
        }

        ReTweakResources.RETWEAK_LOGGER.debug(
                "Calling state \"{}\" for mod \"{}\"",
                fmlStateEventClass.getSimpleName(),
                getModid()
        );
        if (!isEnabled()) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Attempted to call state class \"{}\" when disabled?! This should not happen!",
                    fmlStateEventClass.getCanonicalName()
            );
            return;
        }
        Class<?> _modClass = Class.forName(
                modClass,
                true,
                ReTweakClassLoader.getInstance()
        );

        for(Method method : _modClass.getDeclaredMethods()) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if ((parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0] == fmlStateEventClass) && method.isAnnotationPresent(EventHandler.class)) {
                ReflectionHelper.invokeMethod(
                        method,
                        instance,
                        new Object[] {
                                createStateEvent(fmlStateEventClass)
                        }
                );
                break;
            }
        }

    }

    private FMLStateEvent createStateEvent(Class<? extends FMLStateEvent> fmlStateEventClass) {
        final Class<?>[] parameterClasses = new Class<?>[] {
                Object[].class
        };
        Object[] parameters;

        if (fmlStateEventClass == FMLPreInitializationEvent.class) {
            parameters = new Object[] {
                    null,
                    ReTweakResources.RETWEAK_CONFIG_DIRECTORY
            };
        } else if (fmlStateEventClass == FMLServerAboutToStartEvent.class || fmlStateEventClass == FMLServerStartingEvent.class) {
            parameters = new Object[] {
                    FMLCommonHandler.instance().getMinecraftServerInstance()
            };
        } else {
            parameters = new Object[] {
                    new Object[0]
            };
        }

        try {
            FMLStateEvent fmlStateEvent = ReflectionHelper.createFromConstructor(
                    ReflectionHelper.getConstructor(
                            fmlStateEventClass,
                            parameterClasses
                    ),
                    parameters
            );
            if (fmlStateEventClass == FMLPreInitializationEvent.class) {
                ReflectionHelper.setFieldValue(
                        ReflectionHelper.getField(
                                FMLPreInitializationEvent.class,
                                "sourceFile"
                        ),
                        fmlStateEvent,
                        getReTweakModCandidate().getFile()
                );
                ReflectionHelper.setFieldValue(
                        ReflectionHelper.getField(
                                FMLPreInitializationEvent.class,
                                "suggestedConfigFile"
                        ),
                        fmlStateEvent,
                        new File(
                                ReTweakResources.RETWEAK_CONFIG_DIRECTORY,
                                getModid() + ".cfg"
                        )
                );
            }
            return fmlStateEvent;
        } catch(Exception e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    Kirai.from(
                            "Failed to create new instance of state event \"{state_event}\"!"
                    ).put(
                            "state_event",
                            fmlStateEventClass.getCanonicalName()
                    ).format().toString(),
                    e
            );
        }
        return null;
    }

}
