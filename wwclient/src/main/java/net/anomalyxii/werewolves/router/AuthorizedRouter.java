package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.router.exceptions.RouterException;

/**
 * A {@link Router} that requires some form of authentication
 * or authorization before it can be used.
 * <p>
 * Created by Anomaly on 16/04/2017.
 */
public interface AuthorizedRouter extends Router {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Authenticate to the service
     *
     * @param username the username to use for authentication
     * @param password the password to use for authentication
     * @return {@literal true} if authentication was successful, {@literal false} otherwise
     * @throws RouterException if anything goes wrong during authentication
     */
    boolean login(String username, String password) throws RouterException;


    /**
     * Authenticate to the service using the OAuth2 mechanism
     *
     * @param username the username to use for authentication
     * @param password the password to use for authentication
     * @return {@literal true} if authentication was successful, {@literal false} otherwise
     * @throws RouterException if anything goes wrong during authentication
     */
    boolean oauth(String username, String password) throws RouterException;

    // ******************************
    // Default Methods
    // ******************************

}
