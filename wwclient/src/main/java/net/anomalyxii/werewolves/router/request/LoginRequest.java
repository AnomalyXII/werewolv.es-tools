package net.anomalyxii.werewolves.router.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.net.URI;

/**
 * A {@link net.anomalyxii.werewolves.router.RouterRequest}
 * for logging in to the <code>werewolv.es</code> API
 */
public class LoginRequest extends AbstractRequest<LoginRequest.Body> {

    // ******************************
    // Constructors
    // ******************************

    public LoginRequest(URI host, String path, String username, String password) {
        super(host, path, new Body(username, password));
    }

    public LoginRequest(URI uri, String username, String password) {
        super(uri, new Body(username, password));
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    @Override
    public boolean usePost() {
        return true;
    }

    // ******************************
    // Content Class
    // ******************************

    @JsonSerialize
    public static final class Body {

        // Members

        private final String username;
        private final String password;

        // Constructors

        public Body(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Constructors

        @JsonProperty("UserName")
        public String getUsername() {
            return username;
        }

        @JsonProperty("Password")
        public String getPassword() {
            return password;
        }

    }

}
