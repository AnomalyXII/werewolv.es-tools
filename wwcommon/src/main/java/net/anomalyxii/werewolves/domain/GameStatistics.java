package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.Game;

/**
 * Contains computed statistics about a {@link Game}.
 * <p>
 * Created by Anomaly on 17/04/2017.
 */
public interface GameStatistics {

    // ******************************
    // Interface Methods
    // ******************************

    Game getGame();

    boolean isStarted();

    boolean isComplete();

    int getNumberPlayersTotal();

    int getNumberPlayersAlive();

    int getNumberDays();

    int getMostActiveDay();

    int getNumberLinesSpokenVillage();

    int getNumberLinesSpokenTotal();

    // ******************************
    // To String
    // ******************************

    /**
     * Return a {@link String} for these {@code GameStatistics} in
     * a nice and easy to read format.
     *
     * @return the formatted {@link String}
     */
    String toFormattedString();

}
