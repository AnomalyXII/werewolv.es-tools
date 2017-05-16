package net.anomalyxii.werewolves.router;

import org.apache.http.entity.ContentType;

import java.util.List;

/**
 * Created by Anomaly on 23/11/2016.
 */
public class UnsupportedContentTypeException extends RouterException {

    // ******************************
    // Members
    // ******************************

    private final List<ContentType> supportedTypes;
    private final List<ContentType> unsupportedTypes;

    // ******************************
    // Constructors
    // ******************************

    public UnsupportedContentTypeException(List<ContentType> supportedTypes, List<ContentType> unsupportedTypes) {
        this.supportedTypes = supportedTypes;
        this.unsupportedTypes = unsupportedTypes;
    }

    public UnsupportedContentTypeException(String message, List<ContentType> supportedTypes, List<ContentType> unsupportedTypes) {
        super(message);
        this.supportedTypes = supportedTypes;
        this.unsupportedTypes = unsupportedTypes;
    }

    public UnsupportedContentTypeException(String message, Throwable cause, List<ContentType> supportedTypes, List<ContentType> unsupportedTypes) {
        super(message, cause);
        this.supportedTypes = supportedTypes;
        this.unsupportedTypes = unsupportedTypes;
    }

    public UnsupportedContentTypeException(Throwable cause, List<ContentType> supportedTypes, List<ContentType> unsupportedTypes) {
        super(cause);
        this.supportedTypes = supportedTypes;
        this.unsupportedTypes = unsupportedTypes;
    }

    public UnsupportedContentTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, List<ContentType> supportedTypes, List<ContentType> unsupportedTypes) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.supportedTypes = supportedTypes;
        this.unsupportedTypes = unsupportedTypes;
    }

}
