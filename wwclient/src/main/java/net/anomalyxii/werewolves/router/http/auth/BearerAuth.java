package net.anomalyxii.werewolves.router.http.auth;

import net.anomalyxii.werewolves.router.Auth;

/**
 * A {@code Bearer} {@link Auth}.
 * <p>
 * Created by Anomaly on 24/04/2017.
 */
public class BearerAuth implements Auth {

    // ******************************
    // Members
    // ******************************

    private final String token;

    // ******************************
    // Constructors
    // ******************************

    public BearerAuth(String token) {
        this.token = token;
    }

    // ******************************
    // Getters
    // ******************************

    /**
     * Get the {@code Bearer} token.
     *
     * @return the {@code Bearer} token
     */
    public String getToken() {
        return token;
    }

    // ******************************
    // Auth Methods
    // ******************************

    @Override
    public String getAuthorizationString() {
        return String.format("Bearer %s", token);
    }

}
