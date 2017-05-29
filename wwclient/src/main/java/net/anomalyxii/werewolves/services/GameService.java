package net.anomalyxii.werewolves.services;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.GamesList;
// import net.anomalyxii.werewolves.wwesbot.spring.domain.GameStatistics;

/**
 * A service for interacting with {@code werewolv.es}
 * {@link Game Games}.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
public interface GameService {

    // *********************************
    // Interface Methods
    // *********************************

    /**
     * Check whether a {@link Game} with the specified {@code ID}
     * exists in the context of this {@code GameService}.
     *
     * @param id the {@link Game} {@code ID}
     * @return {@literal true} if the {@link Game} exists; {@literal false} otherwise
     */
    boolean doesGameExist(String id);

    /**
     * Returns a {@code GamesList} instance containing the {@code IDs}
     * of pending games (i.e. are in sign-up) and active games
     * (i.e. are currently in progress).
     *
     * @return a {@link GamesList )
     * @throws ServiceException if something goes wrong accessing the API
     */
    GamesList getGameIds() throws ServiceException;

    /**
     * Return the {@link Game} with the specified {@code ID}.
     *
     * @param id the {@link Game} {@code ID}
     * @return the {@link Game}
     * @throws ServiceException if something goes wrong accessing the API
     */
    Game getGame(String id) throws ServiceException;

    /**
     * Return the {@link GameStatistics} for the {@link Game} with
     * the specified {@code ID}.
     *
     * @param id the {@link Game} {@code ID}
     * @return the {@link GameStatistics}
     * @throws ServiceException if something goes wrong accessing the API
     */
    GameStatistics getGameStatistics(String id) throws ServiceException;

}
