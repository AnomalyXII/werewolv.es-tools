package net.anomalyxii.werewolves.services;

/**
 * Created by Anomaly on 15/05/2017.
 */
public class UncheckedServiceException extends RuntimeException {

    // ******************************
    // Members
    // ******************************

    private final ServiceException cause;

    // ******************************
    // Constructors
    // ******************************

    public UncheckedServiceException(String message, ServiceException cause) {
        super(message, cause);
        this.cause = cause;
    }

    public UncheckedServiceException(ServiceException cause) {
        super(cause);
        this.cause = cause;
    }

    public UncheckedServiceException(String message, ServiceException cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.cause = cause;
    }

    // ******************************
    // Getters
    // ******************************

    @Override
    public ServiceException getCause() {
        return cause;
    }

}
