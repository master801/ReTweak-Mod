package org.slave.minecraft.retweak.load.fml;

import cpw.mods.fml.common.eventhandler.IEventListener;
import org.slave.minecraft.retweak.ReTweak;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by master on 11/19/18 at 12:01 AM
 *
 * @author master
 */
public final class EventBusHandler {

    public static final EventBusHandler EVENT_BUS = new EventBusHandler();

    private ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = new ConcurrentHashMap<Object, ArrayList<IEventListener>>();

    public void register(final Object object) {
        if (object == null || listeners.containsKey(object)) return;

        ReTweak.LOGGER_RETWEAK.debug("");

        //TODO
    }

}
