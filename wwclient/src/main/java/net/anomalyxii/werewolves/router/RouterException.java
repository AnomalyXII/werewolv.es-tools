package net.anomalyxii.werewolves.router;

/**
 * Thrown by a {@link Router} when something goes wrong.
 * <p>
 * Created by Anomaly on 22/11/2016.
 */
public class RouterException extends Exception {

    // ******************************
    // Members
    // ******************************

    // None?

    // ******************************
    // Constructors
    // ******************************

    public RouterException() {
    }

    public RouterException(String message) {
        super(message);
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterException(Throwable cause) {
        super(cause);
    }

    public RouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
