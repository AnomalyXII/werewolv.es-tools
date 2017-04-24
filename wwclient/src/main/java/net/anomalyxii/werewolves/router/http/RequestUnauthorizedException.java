package net.anomalyxii.werewolves.router.http;

/**
 * A {@link HttpRouterException} for when the client is not
 * authorized to access a resource.
 * <p>
 * Created by Anomaly on 24/04/2017.
 */
public class RequestUnauthorizedException extends HttpRouterException {

    private static final int STATUS_CODE = 401;
    private static final String STATUS_MESSAGE = "UNAUTHORIZED";

    // ******************************
    // Constructors
    // ******************************

    public RequestUnauthorizedException() {
        super(STATUS_CODE, STATUS_MESSAGE);
    }

    public RequestUnauthorizedException(String message) {
        super(message, STATUS_CODE, STATUS_MESSAGE);
    }

    public RequestUnauthorizedException(String message, Throwable cause) {
        super(message, cause, STATUS_CODE, STATUS_MESSAGE);
    }

    public RequestUnauthorizedException(Throwable cause) {
        super(cause, STATUS_CODE, STATUS_MESSAGE);
    }

    public RequestUnauthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace, STATUS_CODE, STATUS_MESSAGE);
    }

}
