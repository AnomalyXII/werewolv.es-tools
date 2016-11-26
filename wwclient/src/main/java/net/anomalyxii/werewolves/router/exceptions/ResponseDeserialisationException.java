package net.anomalyxii.werewolves.router.exceptions;

import net.anomalyxii.werewolves.router.RouterRequest;

/**
 * An {@link RouterException} that is thrown if there
 * was an error when serialising a {@link RouterRequest}
 */
public class ResponseDeserialisationException extends RouterException {

    // ******************************
    // Members
    // ******************************

    private final String responseBody;

    // ******************************
    // Constructors
    // ******************************

    public ResponseDeserialisationException(String responseBody) {
        this.responseBody = responseBody;
    }

    public ResponseDeserialisationException(String message, String responseBody) {
        super(message);
        this.responseBody = responseBody;
    }

    public ResponseDeserialisationException(String message, Throwable cause, String responseBody) {
        super(message, cause);
        this.responseBody = responseBody;
    }

    public ResponseDeserialisationException(Throwable cause, String responseBody) {
        super(cause);
        this.responseBody = responseBody;
    }

    public ResponseDeserialisationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String responseBody) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseBody = responseBody;
    }

    // ******************************
    // Getters
    // ******************************

    public String getResponseBody() {
        return responseBody;
    }

}
