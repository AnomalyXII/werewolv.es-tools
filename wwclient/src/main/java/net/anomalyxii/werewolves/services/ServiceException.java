package net.anomalyxii.werewolves.services;

/**
 * An {@link Exception} thrown by a service.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
public class ServiceException extends Exception {

    // *********************************
    // Constructors
    // *********************************

    public ServiceException() {
    }

    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }

}
