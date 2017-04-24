package net.anomalyxii.werewolves.router.http.request.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.RouterRequest;

/**
 * A request bean for {@code /oauth2/token/}
 */
public class TokenRequestBean {

    private String username;
    private String password;
    private String grantType;

    // ******************************
    // Constructors
    // ******************************

    public TokenRequestBean() {
    }

    public TokenRequestBean(String username, String password) {
        this(username, password, "password");
    }

    public TokenRequestBean(String username, String password, String grantType) {
        this.username = username;
        this.password = password;
        this.grantType = grantType;
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("grant_type")
    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

}
