package net.anomalyxii.werewolves.router.exceptions;

import net.anomalyxii.werewolves.router.RouterRequest;

/**
 * An {@link RouterException} that is thrown if there
 * was an error when serialising a {@link RouterRequest}
 */
public class RequestSerialisationException extends RouterException {

    // ******************************
    // Members
    // ******************************

    private final RouterRequest request;

    // ******************************
    // Constructors
    // ******************************

    public RequestSerialisationException(RouterRequest request) {
        this.request = request;
    }

    public RequestSerialisationException(String message, RouterRequest request) {
        super(message);
        this.request = request;
    }

    public RequestSerialisationException(String message, Throwable cause, RouterRequest request) {
        super(message, cause);
        this.request = request;
    }

    public RequestSerialisationException(Throwable cause, RouterRequest request) {
        super(cause);
        this.request = request;
    }

    public RequestSerialisationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, RouterRequest request) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.request = request;
    }

    // ******************************
    // Getters
    // ******************************

    public RouterRequest getRequest() {
        return request;
    }

}
