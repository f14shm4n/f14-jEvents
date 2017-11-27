package f14.events;

/**
 * Created by f14shm4n on 15.11.2017.
 */

public interface IEventHandler<E extends IEventArgument> {
    void handle(E arg);
}
