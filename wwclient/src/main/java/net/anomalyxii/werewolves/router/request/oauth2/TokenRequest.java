package net.anomalyxii.werewolves.router.request.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.anomalyxii.werewolves.router.request.AbstractRequest;
import org.apache.http.client.fluent.Form;

import java.net.URI;

/**
 * A {@link net.anomalyxii.werewolves.router.RouterRequest}
 * for logging in to the <code>werewolv.es</code> API
 */
public class TokenRequest extends AbstractRequest<Form> {

    // ******************************
    // Constructors
    // ******************************

    public TokenRequest(URI uri, String username, String password) {
        super(uri, "/oauth2/token", createForm(username, password));
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    @Override
    public boolean usePost() {
        return true;
    }

    @Override
    public boolean useForm() {
        return true;
    }

    // ******************************
    // Helper Methods
    // ******************************

    private static Form createForm(String username, String password) {

        return Form.form()
                .add("username", username)
                .add("password", password)
                .add("grant_type", "password");

    }

    // ******************************
    // Content Class
    // ******************************

    @JsonSerialize
    public static final class Body {

        // Members

        private final String username;
        private final String password;
        private final String grantType = "password";

        // Constructors

        public Body(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Constructors

        @JsonProperty("username")
        public String getUsername() {
            return username;
        }

        @JsonProperty("password")
        public String getPassword() {
            return password;
        }

        @JsonProperty("grant_type")
        public String getGrantType() {
            return grantType;
        }

    }

}
