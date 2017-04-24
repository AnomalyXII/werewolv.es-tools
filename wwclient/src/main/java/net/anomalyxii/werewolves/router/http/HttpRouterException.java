package net.anomalyxii.werewolves.router.http;

import net.anomalyxii.werewolves.router.RouterException;

/**
 * Thrown by a {@link HttpRouter} when something goes wrong.
 * <p>
 * Created by Anomaly on 24/04/2017.
 */
public class HttpRouterException extends RouterException {

    // ******************************
    // Members
    // ******************************

    private final int httpStatusCode;
    private final String httpStatusMessage;

    // ******************************
    // Constructors
    // ******************************

    public HttpRouterException(int httpStatusCode, String httpStatusMessage) {
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    public HttpRouterException(String message, int httpStatusCode, String httpStatusMessage) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    public HttpRouterException(String message, Throwable cause, int httpStatusCode, String httpStatusMessage) {
        super(message, cause);
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    public HttpRouterException(Throwable cause, int httpStatusCode, String httpStatusMessage) {
        super(cause);
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    public HttpRouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int httpStatusCode, String httpStatusMessage) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.httpStatusCode = httpStatusCode;
        this.httpStatusMessage = httpStatusMessage;
    }

    // ******************************
    // Getters
    // ******************************

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public String getHttpStatusMessage() {
        return httpStatusMessage;
    }

}
