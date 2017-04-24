package net.anomalyxii.werewolves.router.http.response.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.http.response.ResponseBean;

/**
 * Response from the {@code /api/Account/Login} endpoint.
 *
 * Created by Anomaly on 22/11/2016.
 */
public class LoginResponseBean {

    // ******************************
    // Members
    // ******************************

    private String accessToken;
    private String tokenType;
    private int expiresIn;

    // ******************************
    // Constructors
    // ******************************

    public LoginResponseBean() {
    }

    public LoginResponseBean(String accessToken, String tokenType, int expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("expiresIn")
    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

}
