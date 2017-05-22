package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.events.Event;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains computed statistics about a {@link Game}.
 * <p>
 * Created by Anomaly on 17/04/2017.
 */
public class CumulativeGameStatistics implements GameStatistics {

    // ******************************
    // Members
    // ******************************

    private final Game game;
    //
    private final AtomicBoolean isStarted = new AtomicBoolean();
    private final AtomicBoolean isComplete = new AtomicBoolean();

    private final AtomicInteger numberPlayersTotal = new AtomicInteger();
    private final AtomicInteger numberPlayersAlive = new AtomicInteger();
    private final AtomicInteger numberDays = new AtomicInteger();
    private final AtomicInteger mostActiveDay = new AtomicInteger();
    private final AtomicInteger numberLinesSpokenVillage = new AtomicInteger();
    private final AtomicInteger numberLinesSpokenTotal = new AtomicInteger();


    // ******************************
    // Constructors
    // ******************************

    public CumulativeGameStatistics(Game game) {
        this.game = game;
    }

    // ******************************
    // Getters
    // ******************************

    public Game getGame() {
        return game;
    }

    public boolean isStarted() {
        return isStarted.get();
    }

    public boolean isComplete() {
        return isComplete.get();
    }

    public int getNumberPlayersTotal() {
        return numberPlayersTotal.get();
    }

    public int getNumberPlayersAlive() {
        return numberPlayersAlive.get();
    }

    public int getNumberDays() {
        return numberDays.get();
    }

    public int getMostActiveDay() {
        return mostActiveDay.get();
    }

    public int getNumberLinesSpokenVillage() {
        return numberLinesSpokenVillage.get();
    }

    public int getNumberLinesSpokenTotal() {
        return numberLinesSpokenTotal.get();
    }

    // ******************************
    // Setters
    // ******************************

    public void start() {
        this.isStarted.compareAndSet(false, true);
    }

    public void complete() {
        this.isComplete.compareAndSet(false, true);
    }

    public void incrementNumberPlayersTotal() {
        this.numberPlayersTotal.incrementAndGet();
    }

    public void decrementNumberPlayersTotal() {
        this.numberPlayersTotal.decrementAndGet();
    }

    public void incrementNumberPlayersAlive() {
        this.numberPlayersAlive.incrementAndGet();
    }

    public void decementNumberPlayersAlive() {
        this.numberPlayersAlive.decrementAndGet();
    }

    public void incrementNumberDays() {
        this.numberDays.incrementAndGet();
    }

    public void setMostActiveDay(int mostActiveDay) {
        this.mostActiveDay.compareAndSet(0, mostActiveDay);
    }

    public void incrementNumberLinesSpokenVillage() {
        this.numberLinesSpokenVillage.incrementAndGet();
    }

    public void incrementNumberLinesSpokenTotal() {
        this.numberLinesSpokenTotal.incrementAndGet();
    }

    // ******************************
    // To String
    // ******************************

    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        List<Object> arguments = new ArrayList<>();

        // Game ID:
        builder.append("[%s]");
        arguments.add(game.getId());

        // Game Status:
        builder.append(" :: ");
        builder.append("Game %s");

        String status = isStarted() ? isComplete() ? "Complete" : "in Progress" : "in Sign-up";
        arguments.add(status);
        if(isComplete()) {
            builder.append(" :: ");
            builder.append("The %s Won");
            arguments.add(game.getWinningAlignment());
        }

        // Players Alive:
        builder.append(" :: ");
        builder.append("%d / %d Players Alive");
        arguments.add(getNumberPlayersAlive());
        arguments.add(getNumberPlayersTotal());

        // Days:
        builder.append(" :: ");
        builder.append("%d Days");
        arguments.add(getNumberDays());

        // Lines Spoken:
        builder.append(" :: ");
        builder.append("%d / %d Lines Spoken to the Village");
        arguments.add(getNumberLinesSpokenVillage());
        arguments.add(getNumberLinesSpokenTotal());

        return String.format(builder.toString(), arguments.toArray(new Object[arguments.size()]));
    }


}
