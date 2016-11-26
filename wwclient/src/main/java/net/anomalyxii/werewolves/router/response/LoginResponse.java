package net.anomalyxii.werewolves.router.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.exceptions.ResponseDeserialisationException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Anomaly on 22/11/2016.
 */
public class LoginResponse extends AbstractResponse<LoginResponse.Body> {

    // ******************************
    // Constructors
    // ******************************

    public LoginResponse(int statusCode, Body content) {
        super(statusCode, content);
    }

    // ******************************
    // Content Class
    // ******************************

    public static final class Body extends AbstractResponse.DefaultBody {

        // Members

        private String token;

        // Constructors

        public Body() {
        }

        public Body(String token) {
            this.token = token;
        }

        // Getters & Setters

        @JsonProperty("Token")
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

    // ******************************
    // Static Helper Methods
    // ******************************

    public static DeserialisationCallback<LoginResponse> deserialisation() {
        return (response, objectMapper) -> {
            // Construct the response object
            return new LoginResponse(getStatusCode(response), deserialise(response, objectMapper, Body.class));
        };
    }

}
