package net.anomalyxii.werewolves.router.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.response.AbstractResponse;

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

        private String accessToken;
        private String tokenType;
        private int expiresIn;

        // Constructors

        public Body() {
        }

        // Getters & Setters

        @JsonProperty("accessToken")
        public String getAccessToken() {
            return accessToken;
        }

        @JsonProperty("tokenType")
        public String getTokenType() {
            return tokenType;
        }

        @JsonProperty("expiresIn")
        public int getExpiresIn() {
            return expiresIn;
        }


        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public void setExpiresIn(int expiresIn) {
            this.expiresIn = expiresIn;
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
