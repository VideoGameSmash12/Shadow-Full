/*
 * Copyright (c) Shadow client, 0x150, Saturn5VFive 2022. All rights reserved.
 */

package net.shadow.client.helper.event;

import net.shadow.client.helper.event.events.base.Event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Events {

    static final Map<EventType, List<Consumer<Event>>> HANDLERS = new HashMap<>();

    public static void registerEventHandler(EventType event, Consumer<Event> handler) {
        if (!HANDLERS.containsKey(event)) {
            HANDLERS.put(event, new ArrayList<>());
        }
        HANDLERS.get(event).add(handler);
    }

    public static boolean fireEvent(EventType event, Event argument) {
        if (HANDLERS.containsKey(event)) {
            for (Consumer<Event> handler : HANDLERS.get(event)) {
                handler.accept(argument);
            }
        }
        return argument.isCancelled();
    }
}
