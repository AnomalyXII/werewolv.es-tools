package net.anomalyxii.werewolves.wwesbot.spring.services;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.wwesbot.spring.domain.GameStatistics;

/**
 * Created by Anomaly on 13/05/2017.
 */
public interface ApiService {

    // ******************************
    // Interface Methods
    // ******************************

    GamesList getGameIDs() throws ApiException;

    Game getGame(String id) throws ApiException;

    GameStatistics getGameStatistics(String id) throws ApiException;

    // ******************************
    // Default Methods
    // ******************************

}
