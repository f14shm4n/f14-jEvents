package f14.events;

/**
 * Created by f14shm4n on 26.11.2017.
 */

public class BaseEventArgument implements IEventArgument {

    private boolean handled;

    @Override
    public boolean isHandled() {
        return handled;
    }

    @Override
    public void setHandle() {
        handled = true;
    }
}
