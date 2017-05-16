package net.anomalyxii.werewolves.router.http.response;

import java.util.Collections;
import java.util.Map;

/**
 * A basic response from the API
 *
 * Created by Anomaly on 15/05/2017.
 */
public class ResponseBean {

    // ******************************
    // Members
    // ******************************

    private String message;
    private Map<String, Object> modelState = Collections.emptyMap();

    // ******************************
    // Getters & Setters
    // ******************************

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getModelState() {
        return modelState;
    }

    public void setModelState(Map<String, Object> modelState) {
        this.modelState = modelState;
    }

}
