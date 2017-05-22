package net.anomalyxii.werewolves.services;

import net.anomalyxii.werewolves.domain.UserStatistics;
import net.anomalyxii.werewolves.domain.players.User;

/**
 * A service for interacting with {@code werewolv.es}
 * {@link User users}.
 * <p>
 * Created by Anomaly on 24/04/2017.
 */
public interface UserService {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Return the {@link UserStatistics} for the {@link User} with
     * the specified {@code ID}.
     *
     * @param id the {@link User} {@code ID}
     * @return the {@link UserStatistics}
     * @throws ServiceException if something goes wrong accessing the API
     */
    UserStatistics getUserStatistics(String id) throws ServiceException;

}
