package net.anomalyxii.werewolves.router.request.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.anomalyxii.werewolves.router.request.AbstractRequest;

import java.net.URI;

/**
 * A {@link net.anomalyxii.werewolves.router.RouterRequest}
 * for logging in to the <code>werewolv.es</code> API
 */
public class RegisterRequest extends AbstractRequest<RegisterRequest.Body> {

    // ******************************
    // Constructors
    // ******************************

    public RegisterRequest(URI uri, String username, String email, String password) {
        super(uri, "/api/Account/Register", new Body(username, email, password));
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
        private final String email;
        private final String password;

        // Constructors

        public Body(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }


        // Constructors

        @JsonProperty("userName")
        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        @JsonProperty("password")
        public String getPassword() {
            return password;
        }

        @JsonProperty("confirmPassword") // Assume this is validated client-side?
        public String getConfirmPassword() {
            return password;
        }

    }

}
