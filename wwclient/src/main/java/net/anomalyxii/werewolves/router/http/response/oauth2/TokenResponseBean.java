package net.anomalyxii.werewolves.router.http.response.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.http.response.ResponseBean;

/**
 * Response from the {@code /api/oauth2/token} endpoint.
 * <p>
 * Created by Anomaly on 22/11/2016.
 */
public class TokenResponseBean {

    // ******************************
    // Members
    // ******************************

    private String accessToken;
    private String tokenType;
    private int expiresIn;

    // ******************************
    // Constructors
    // ******************************

    public TokenResponseBean() {
    }

    public TokenResponseBean(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @JsonProperty("access_token")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("token_type")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }


    @JsonProperty("expires_in")
    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

}
