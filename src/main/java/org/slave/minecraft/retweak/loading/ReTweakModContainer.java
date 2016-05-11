package org.slave.minecraft.retweak.loading;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLStateEvent;
import org.slave.minecraft.retweak.resources.ReTweakResources;

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

    public void callState(FMLStateEvent fmlStateEvent) {
        ReTweakResources.RETWEAK_LOGGER.debug(
                "Calling state \"{}\" for mod \"{}\"",
                fmlStateEvent.getClass().getSimpleName(),
                getModid()
        );
        if (!isEnabled()) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Attemped to call state class \"{}\" when disabled?! This should not happen!",
                    fmlStateEvent.getClass().getCanonicalName()
            );
            return;
        }
        try {
            Class<?> _modClass = Class.forName(
                    modClass,
                    true,
                    ReTweakClassLoader.getInstance()
            );

            for(Method method : _modClass.getDeclaredMethods()) {
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes != null && parameterTypes.length == 1 && parameterTypes[0] == fmlStateEvent.getClass() && method.isAnnotationPresent(EventHandler.class)) {
                    try {
                        method.invoke(
                                instance,
                                fmlStateEvent
                        );
                    } catch(IllegalAccessException | InvocationTargetException e) {
                        ReTweakResources.RETWEAK_LOGGER.error(
                                "Something happened that should not have happened!",
                                e
                        );
                    }
                    break;
                }
            }

        } catch(ClassNotFoundException e) {
            ReTweakResources.RETWEAK_LOGGER.error(
                    "Couldn't find mod class?",
                    e
            );
        }
    }

}
