package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.router.exceptions.RouterException;

/**
 * Created by Anomaly on 05/01/2017.
 */
public interface Router {

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


    /**
     * Retrieve a list of Game IDs for any active or pending games
     *
     * @return An instance of {@link Games} containing all active and pending
     * @throws RouterException if anything goes wrong retrieving the list of games
     */
    Games games() throws RouterException;

    /**
     * Retrieve the details of a {@link Game}
     *
     * @param id the id of the {@link Game}
     * @return an instance of a {@link Game} containing all the associated events
     * @throws RouterException if anything goes wrong retrieving the game
     */
    Game game(String id) throws RouterException;

}
