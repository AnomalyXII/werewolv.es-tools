package net.anomalyxii.werewolves.router.http;

import net.anomalyxii.werewolves.router.RouterException;

/**
 * An {@link RouterException} that is thrown if there
 * was an error when deserializing an {@link Object}.
 * <p>
 * Created by Anomaly on 20/11/2016.
 */
class ResponseDeserializationException extends RouterException {

    // ******************************
    // Constructors
    // ******************************

    public ResponseDeserializationException() {
    }

    public ResponseDeserializationException(String message) {
        super(message);
    }

    public ResponseDeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseDeserializationException(Throwable cause) {
        super(cause);
    }

    public ResponseDeserializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
