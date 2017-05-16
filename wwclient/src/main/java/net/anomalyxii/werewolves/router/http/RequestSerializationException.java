package net.anomalyxii.werewolves.router.http;

import net.anomalyxii.werewolves.router.RouterException;

/**
 * An {@link RouterException} that is thrown if there
 * was an error when serializing a {@link Object}.
 * <p>
 * Created by Anomaly on 20/11/2016.
 */
class RequestSerializationException extends RouterException {

    // ******************************
    // Members
    // ******************************

    private final Object object;

    // ******************************
    // Constructors
    // ******************************

    public RequestSerializationException(Object object) {
        this.object = object;
    }

    public RequestSerializationException(String message, Object object) {
        super(message);
        this.object = object;
    }

    public RequestSerializationException(String message, Throwable cause, Object object) {
        super(message, cause);
        this.object = object;
    }

    public RequestSerializationException(Throwable cause, Object object) {
        super(cause);
        this.object = object;
    }

    public RequestSerializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Object object) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.object = object;
    }

    // ******************************
    // Getters
    // ******************************

    public Object getObject() {
        return object;
    }

}
