package net.anomalyxii.werewolves.router.http;

import net.anomalyxii.werewolves.router.RouterException;

import java.util.Map;

/**
 * Created by Anomaly on 16/05/2017.
 */
public class UnhappyHttpResponseException extends RouterException {

    // ******************************
    // Members
    // ******************************

    private final int code;
    private final String message;
    private final Map<String, Object> modelState;

    // ******************************
    // Constructors
    // ******************************

    public UnhappyHttpResponseException(int code, String message, Map<String, Object> modelState) {
        this.code = code;
        this.message = message;
        this.modelState = modelState;
    }

    // ******************************
    // Getters
    // ******************************

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Map<String, Object> getModelState() {
        return modelState;
    }

}
