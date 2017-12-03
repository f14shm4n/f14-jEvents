package f14.events.impl;

import f14.events.BaseEventArgument;

/**
 * Created by f14shm4n on 03.12.2017.
 */
public class ExceptionEventArg extends BaseEventArgument {
    private Exception exception;
    private String message;

    public ExceptionEventArg(Exception exception) {
        this(exception, null);
    }

    public ExceptionEventArg(Exception exception, String message) {
        this.exception = exception;
        this.message = message;
    }

    public Exception getException() {
        return exception;
    }

    public String getMessage() {
        return message;
    }
}
