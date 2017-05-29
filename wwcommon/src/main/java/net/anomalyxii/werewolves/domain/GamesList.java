package net.anomalyxii.werewolves.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holds lists of all <code>active</code>
 * and <code>pending</code> game IDs.
 *
 * Created by Anomaly on 25/11/2016.
 */
public class GamesList {

    // ******************************
    // Members
    // ******************************

    private final List<String> active = new ArrayList<>();
    private final List<String> pending = new ArrayList<>();
    private final List<String> completed = new ArrayList<>();

    // ******************************
    // Constructors
    // ******************************

    public GamesList(List<String> active, List<String> pending) {
        this(active, pending, Collections.emptyList());
    }

    public GamesList(List<String> active, List<String> pending, List<String> completed) {
        this.active.addAll(active);
        this.pending.addAll(pending);
        this.completed.addAll(completed);
    }

    // ******************************
    // Getters
    // ******************************

    /**
     * Retrieve a {@link List} of all {@code active game IDs}. An
     * <i>active</i> {@link Game} is one that has started and can
     * no longer be joined by new players.
     *
     * @return a {@link List} of Game IDs
     */
    public List<String> getActiveGameIds() {
        return Collections.unmodifiableList(active);
    }

    /**
     * Retrieve a {@link List} of all {@code pending game IDs}.
     * A <i>pending</i> {@link Game} is one that has not yet
     * started and thus can be joined by new players.
     *
     * @return a {@link List} of Game IDs
     */
    public List<String> getPendingGameIds() {
        return Collections.unmodifiableList(pending);
    }

    /**
     * Retrieve a {@link List} of all {@code completed game IDs}.
     *
     * <b>This field isn't (currently) exposed by the API; it is
     * intended to be used by clients that can return archived
     * {@link GamesList}.</b>
     *
     * @return a {@link List} of Game IDs
     */
    public List<String> getCompletedGameIds() {
        return Collections.unmodifiableList(completed);
    }

}
