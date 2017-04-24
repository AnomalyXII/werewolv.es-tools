package net.anomalyxii.werewolves.router;

/**
 * An authorization method.
 *
 * Created by Anomaly on 24/04/2017.
 */
public interface Auth {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Get the {@code Authorization} {@link String} that can be
     * sent via a {@code HTTP Header} to authorize a request.
     *
     * @return the {@code Authorization} header content
     */
    String getAuthorizationString();

}
