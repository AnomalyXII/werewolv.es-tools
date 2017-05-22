package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.players.User;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains computed statistics about a {@link User}.
 *
 * Created by Anomaly on 22/05/2017.
 */
public interface UserStatistics {

    // ******************************
    // Interface Methods
    // ******************************

    User getUser();

    int getNumberGamesPlayed();

    int getNumberGamesWon();

    int getNumberGamesPlayedForAlignment(Alignment alignment);

    int getNumberGamesWonForAlignment(Alignment alignment);

    // ******************************
    // To String
    // ******************************

    /**
     * Return a {@link String} for these {@code UserStatistics} in
     * a nice and easy to read format.
     *
     * @return the formatted {@link String}
     */
    String toFormattedString();


}
