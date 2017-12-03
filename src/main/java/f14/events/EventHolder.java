package f14.events;

/**
 * Created by f14shm4n on 26.11.2017.
 */

public class EventHolder<T extends IEventHandler> {

    private T handler;
    private ExceptionHandler errorHandler;
    private ErrorStrategy errorStrategy = ErrorStrategy.NONE;

    protected EventHolder(T handler) {
        this.handler = handler;
    }

    public EventHolder error(ExceptionHandler errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public EventHolder errorStrategy(ErrorStrategy strategy) {
        this.errorStrategy = strategy;
        return this;
    }

    public <T extends IEventHandler> boolean canHandle(Class<T> requiredHandlerClass) {
        return requiredHandlerClass.isInstance(handler);
    }

    public boolean isSame(IEventHandler handler) {
        return this.handler.equals(handler);
    }

    public ErrorStrategy getErrorStrategy() {
        return this.errorStrategy;
    }

    public boolean execute(IEventArgument arg) {
        try {
            handler.handle(arg);
        } catch (Exception e) {
            if (errorHandler != null) {
                errorHandler.run(e);
            }
            return false;
        }
        return true;
    }
}
