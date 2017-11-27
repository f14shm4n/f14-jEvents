package f14.events;

/**
 * Created by f14shm4n on 15.11.2017.
 */

/**
 * Represent the base signature for the event argument for the event handler.
 */
public interface IEventArgument {
    /**
     * Determine whether this argument was handled or not.
     *
     * @return true if this arg is handled; false otherwise.
     */
    boolean isHandled();

    /**
     * Marks current arg as handled.
     */
    void setHandle();
}
