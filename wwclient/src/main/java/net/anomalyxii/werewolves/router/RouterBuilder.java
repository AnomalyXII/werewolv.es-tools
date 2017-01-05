package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.router.exceptions.RouterException;

/**
 * Created by Anomaly on 27/11/2016.
 */
public interface RouterBuilder {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Return a {@link Router} built using the
     * supplied <code>token</code>.
     *
     * @param token the oauth token for authentication
     * @return a {@link Router}
     * @throws RouterException if something goes wrong
     */
    Router forToken(String token) throws RouterException;

    /**
     * Return a {@link Router} built using the
     * supplied <code>username</code> and
     * <code>password</code>.
     *
     * @param username the username for authentication
     * @param password the password for authentication
     * @return a {@link Router}
     * @throws RouterException if something goes wrong
     */
    Router forCredentials(String username, String password) throws RouterException;

    /**
     * Return a {@link Router} built to
     * retrieve games from a local store.
     *
     * @return a {@link Router}
     * @throws RouterException if something goes wrong
     */
    Router forLocalGame() throws RouterException;

    /**
     * Return a {@link Router} built to
     * retrieve archived games from a local store
     * using the given username to filter visible
     * events
     *
     * @return a {@link Router}
     * @throws RouterException if something goes wrong
     */
    Router forArchivedGame(String username) throws RouterException;

}
