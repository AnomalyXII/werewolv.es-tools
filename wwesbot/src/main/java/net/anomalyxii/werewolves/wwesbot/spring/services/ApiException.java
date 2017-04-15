package net.anomalyxii.werewolves.wwesbot.spring.services;

/**
 * An {@link Exception} thrown by the {@link ApiService}.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
public class ApiException extends Exception {

    // *********************************
    // Constructors
    // *********************************

    public ApiException() {
    }

    public ApiException(String s) {
        super(s);
    }

    public ApiException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ApiException(Throwable throwable) {
        super(throwable);
    }

    public ApiException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
