package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.exceptions.RouterException;

/**
 * A gateway for interacting with the {@code werewolv.es API}.
 * <p>
 * Created by Anomaly on 05/01/2017.
 */
public interface Router extends AutoCloseable {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Retrieve a list of Game IDs for any active or pending games
     *
     * @return An instance of {@link GamesList} containing all active and pending
     * @throws RouterException if anything goes wrong retrieving the list of games
     */
    GamesList games() throws RouterException;

    /**
     * Retrieve the details of a {@link Game}
     *
     * @param id the id of the {@link Game}
     * @return an instance of a {@link Game} containing all the associated events
     * @throws RouterException if anything goes wrong retrieving the game
     */
    Game game(String id) throws RouterException;

    // ******************************
    // Default Methods
    // ******************************

    @Override
    default void close() throws Exception {
        // Default: no-op
    }

}
