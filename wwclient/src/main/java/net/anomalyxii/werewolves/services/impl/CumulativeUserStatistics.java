package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.UserStatistics;
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
public class CumulativeUserStatistics implements UserStatistics {

    // ******************************
    // Members
    // ******************************

    private final User user;
    //
    private final AtomicInteger gamesPlayed = new AtomicInteger();
    private final AtomicInteger gamesWon = new AtomicInteger();
    private final Map<Alignment, AtomicInteger> gamesPlayedPerAlignment = new EnumMap<>(Alignment.class);
    private final Map<Alignment, AtomicInteger> gamesWonPerAlignment = new EnumMap<>(Alignment.class);

    // ******************************
    // Constructors
    // ******************************

    public CumulativeUserStatistics(User user) {
        this.user = user;
    }

    // ******************************
    // Getters
    // ******************************

    public User getUser() {
        return user;
    }

    public int getNumberGamesPlayed() {
        return gamesPlayed.get();
    }

    public int getNumberGamesWon() {
        return gamesWon.get();
    }

    @Override
    public int getNumberGamesPlayedForAlignment(Alignment alignment) {
        return gamesPlayedPerAlignment.getOrDefault(alignment, new AtomicInteger()).get();
    }

    @Override
    public int getNumberGamesWonForAlignment(Alignment alignment) {
        return gamesWonPerAlignment.getOrDefault(alignment, new AtomicInteger()).get();
    }

    // ******************************
    // Setters
    // ******************************

    public void incrementNumberGamesPlayed() {
        this.gamesPlayed.incrementAndGet();
    }

    public void incrementNumberGamesPlayednForAlignment(Alignment alignment) {
        this.gamesPlayedPerAlignment.computeIfAbsent(alignment, a -> new AtomicInteger()).incrementAndGet();
    }

    public void incrementNumberGamesWon() {
        this.gamesWon.incrementAndGet();
    }

    public void incrementNumberGamesWonForAlignment(Alignment alignment) {
        this.gamesWonPerAlignment.computeIfAbsent(alignment, a -> new AtomicInteger()).incrementAndGet();
    }

    // ******************************
    // To String
    // ******************************

    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        List<Object> arguments = new ArrayList<>();

        // Game ID:
        builder.append("[%s]");
        arguments.add(user.getName());

        // Game Status:
        builder.append(" :: ");
        builder.append("Game Played = %d (%d, %d, %d, %d, %d)");
        arguments.add(getNumberGamesPlayed());
        arguments.add(getNumberGamesPlayedForAlignment(Alignment.VILLAGE));
        arguments.add(getNumberGamesPlayedForAlignment(Alignment.WEREWOLVES));
        arguments.add(getNumberGamesPlayedForAlignment(Alignment.COVEN));
        arguments.add(getNumberGamesPlayedForAlignment(Alignment.VAMPIRES));
        arguments.add(getNumberGamesPlayedForAlignment(Alignment.DEMONS));

        // Players Alive:
        builder.append(" :: ");
        builder.append("Game Won = %d (%d, %d, %d, %d, %d)");
        arguments.add(getNumberGamesWon());
        arguments.add(getNumberGamesWonForAlignment(Alignment.VILLAGE));
        arguments.add(getNumberGamesWonForAlignment(Alignment.WEREWOLVES));
        arguments.add(getNumberGamesWonForAlignment(Alignment.COVEN));
        arguments.add(getNumberGamesWonForAlignment(Alignment.VAMPIRES));
        arguments.add(getNumberGamesWonForAlignment(Alignment.DEMONS));

        return String.format(builder.toString(), arguments.toArray(new Object[arguments.size()]));
    }


}
