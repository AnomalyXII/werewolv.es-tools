package net.anomalyxii.werewolves.router.http.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.anomalyxii.werewolves.router.http.request.HttpRouterRequest;

import java.net.URI;

/**
 * A {@link net.anomalyxii.werewolves.router.RouterRequest}
 * for logging in to the <code>werewolv.es</code> API
 */
public class LoginRequestBean {

    // ******************************
    // Constructors
    // ******************************

    private String username;
    private String password;


    // ******************************
    // Constructors
    // ******************************

    public LoginRequestBean() {
    }

    public LoginRequestBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @JsonProperty("userName")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
