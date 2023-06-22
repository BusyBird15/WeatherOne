package com.google.appinventor.components.runtime;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventDispatcher {
    private static final boolean DEBUG = false;
    private static final Map<HandlesEventDispatching, EventRegistry> mapDispatchDelegateToEventRegistry = new HashMap();

    private static final class EventClosure {
        /* access modifiers changed from: private */
        public final String componentId;
        /* access modifiers changed from: private */
        public final String eventName;

        private EventClosure(String componentId2, String eventName2) {
            this.componentId = componentId2;
            this.eventName = eventName2;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            EventClosure that = (EventClosure) o;
            if (!this.componentId.equals(that.componentId)) {
                return false;
            }
            if (!this.eventName.equals(that.eventName)) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            return (this.eventName.hashCode() * 31) + this.componentId.hashCode();
        }
    }

    private static final class EventRegistry {
        private final HandlesEventDispatching dispatchDelegate;
        /* access modifiers changed from: private */
        public final HashMap<String, Set<EventClosure>> eventClosuresMap = new HashMap<>();

        EventRegistry(HandlesEventDispatching dispatchDelegate2) {
            this.dispatchDelegate = dispatchDelegate2;
        }
    }

    private EventDispatcher() {
    }

    private static EventRegistry getEventRegistry(HandlesEventDispatching dispatchDelegate) {
        EventRegistry er = mapDispatchDelegateToEventRegistry.get(dispatchDelegate);
        if (er != null) {
            return er;
        }
        EventRegistry er2 = new EventRegistry(dispatchDelegate);
        mapDispatchDelegateToEventRegistry.put(dispatchDelegate, er2);
        return er2;
    }

    private static EventRegistry removeEventRegistry(HandlesEventDispatching dispatchDelegate) {
        return mapDispatchDelegateToEventRegistry.remove(dispatchDelegate);
    }

    public static void registerEventForDelegation(HandlesEventDispatching dispatchDelegate, String componentId, String eventName) {
        EventRegistry er = getEventRegistry(dispatchDelegate);
        Set<EventClosure> eventClosures = (Set) er.eventClosuresMap.get(eventName);
        if (eventClosures == null) {
            eventClosures = new HashSet<>();
            er.eventClosuresMap.put(eventName, eventClosures);
        }
        eventClosures.add(new EventClosure(componentId, eventName));
    }

    public static void unregisterEventForDelegation(HandlesEventDispatching dispatchDelegate, String componentId, String eventName) {
        Set<EventClosure> eventClosures = (Set) getEventRegistry(dispatchDelegate).eventClosuresMap.get(eventName);
        if (eventClosures != null && !eventClosures.isEmpty()) {
            Set<EventClosure> toDelete = new HashSet<>();
            for (EventClosure eventClosure : eventClosures) {
                if (eventClosure.componentId.equals(componentId)) {
                    toDelete.add(eventClosure);
                }
            }
            for (EventClosure eventClosure2 : toDelete) {
                eventClosures.remove(eventClosure2);
            }
        }
    }

    public static void unregisterAllEventsForDelegation() {
        for (EventRegistry er : mapDispatchDelegateToEventRegistry.values()) {
            er.eventClosuresMap.clear();
        }
    }

    public static void removeDispatchDelegate(HandlesEventDispatching dispatchDelegate) {
        EventRegistry er = removeEventRegistry(dispatchDelegate);
        if (er != null) {
            er.eventClosuresMap.clear();
        }
    }

    public static boolean dispatchEvent(Component component, String eventName, Object... args) {
        Object[] args2 = OptionHelper.optionListsFromValues(component, eventName, args);
        boolean dispatched = false;
        HandlesEventDispatching dispatchDelegate = component.getDispatchDelegate();
        if (dispatchDelegate.canDispatchEvent(component, eventName)) {
            Set<EventClosure> eventClosures = (Set) getEventRegistry(dispatchDelegate).eventClosuresMap.get(eventName);
            if (eventClosures != null && eventClosures.size() > 0) {
                dispatched = delegateDispatchEvent(dispatchDelegate, eventClosures, component, args2);
            }
            dispatchDelegate.dispatchGenericEvent(component, eventName, !dispatched, args2);
        }
        return dispatched;
    }

    private static boolean delegateDispatchEvent(HandlesEventDispatching dispatchDelegate, Set<EventClosure> eventClosures, Component component, Object... args) {
        boolean dispatched = false;
        for (EventClosure eventClosure : eventClosures) {
            if (dispatchDelegate.dispatchEvent(component, eventClosure.componentId, eventClosure.eventName, args)) {
                dispatched = true;
            }
        }
        return dispatched;
    }

    public static String makeFullEventName(String componentId, String eventName) {
        return componentId + '$' + eventName;
    }
}
