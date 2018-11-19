package org.slave.minecraft.retweak.load.fml;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import cpw.mods.fml.common.eventhandler.IEventListener;

/**
 * Created by master on 11/19/18 at 12:01 AM
 *
 * @author master
 */
public final class EventBusHandler {

    private ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners = new ConcurrentHashMap<Object, ArrayList<IEventListener>>();

    public void register(final Object object) {
        if (object == null || listeners.containsKey(object)) return;

        //TODO
    }

}
