package f14.events;

import f14.common.CollectionsUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by f14shm4n on 02.12.2017.
 */
public class EventPool<T extends IEventHandler<E>, E extends IEventArgument> {

    private final ArrayList<EventHolder<T>> eventHolders;

    public EventPool() {
        eventHolders = new ArrayList<>();
    }

    public synchronized EventHolder add(T handler) {
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

    private boolean containsHandler(T handler) {
        return CollectionsUtil.any(eventHolders, o -> o.isSame(handler));
    }

    public synchronized void remove(T handler) {
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

    public void notify(E arg) {
        List<EventHolder> toNotify;

        synchronized (this) {
            toNotify = new ArrayList<>();
            CollectionsUtil.forEach(eventHolders, toNotify::add);
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
