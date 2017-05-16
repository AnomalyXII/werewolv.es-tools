package net.anomalyxii.werewolves.router.http.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.anomalyxii.werewolves.router.http.request.HttpRouterRequest;
import net.anomalyxii.werewolves.router.RouterRequest;

import java.net.URI;

/**
 * A {@link RouterRequest}
 * for logging in to the <code>werewolv.es</code> API
 */
public class RegisterRequestBean {

    // ******************************
    // Members
    // ******************************

    private String username;
    private String email;
    private String password;

    // ******************************
    // Constructors
    // ******************************

    public RegisterRequestBean() {
    }

    public RegisterRequestBean(String username, String email, String password) {
        this.username = username;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("confirmPassword")
    public String getConfirmPassword() {
        return password;
    }

    public void setConfirmPassword(String confirmPassword) {
        // No-op?
    }

}
