package f14.events;

import java.util.ArrayList;
import java.util.List;

import f14.common.collections.CollectionsUtil;

/**
 * Created by f14shm4n on 15.11.2017.
 */

public class EventManager {

    private final ArrayList<EventHolder> eventHolders;

    public EventManager() {
        eventHolders = new ArrayList<>();
    }

    public synchronized <T extends IEventHandler, E extends IEventArgument> EventHolder add(T handler) {
        if (handler == null) {
            throw new NullPointerException();
        }

        EventHolder<T> holder;

        if (!containsHandler(handler)) {
            holder = new EventHolder<>(handler);
            eventHolders.add(holder);
            return holder;
        } else {
            return null;
        }
    }

    private boolean containsHandler(IEventHandler handler) {
        return CollectionsUtil.any(eventHolders, o -> o.isSame(handler));
    }

    public synchronized void remove(IEventHandler handler) {
        for (EventHolder holder : eventHolders) {
            if (holder.isSame(handler)) {
                eventHolders.remove(holder);
                break;
            }
        }
    }

    public synchronized void remove(EventHolder holder) {
        eventHolders.remove(holder);
    }

    public synchronized void clear() {
        eventHolders.clear();
    }

    public synchronized int size() {
        return eventHolders.size();
    }

    public <T extends IEventHandler<E>, E extends IEventArgument> void notify(Class<T> listenerClass, E arg) {
        List<EventHolder> toNotify;

        synchronized (this) {
            toNotify = new ArrayList<>();
            CollectionsUtil.forEach(eventHolders, o -> {
                if (o.canHandle(listenerClass)) {
                    toNotify.add(o);
                }
            });
        }

        CollectionsUtil.forEach(toNotify, h -> {
            boolean success = h.execute(arg);
            if (!success) {
                switch (h.getErrorStrategy()) {
                    case REMOVE:
                        remove(h);
                        break;
                }
            }
        });
    }

}
