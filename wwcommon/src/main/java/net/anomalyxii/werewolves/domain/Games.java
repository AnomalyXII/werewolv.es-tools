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
public class Games {

    // ******************************
    // Members
    // ******************************

    private final List<String> active = new ArrayList<>();
    private final List<String> pending = new ArrayList<>();

    // ******************************
    // Constructors
    // ******************************

    public Games(List<String> active, List<String> pending) {
        this.active.addAll(active);
        this.pending.addAll(pending);
    }

    // ******************************
    // Getters
    // ******************************

    /**
     * Retrieve a {@link List} of all <code>active</code>
     * game IDs. An <code>active</code> game is one that
     * has started and can no longer be joined by new
     * players.
     *
     * @return a {@link List} of Game IDs
     */
    public List<String> getActiveGameIDs() {
        return Collections.unmodifiableList(active);
    }

    /**
     * Retrieve a {@link List} of all <code>pending</code>
     * game IDs. A <code>pending</code> game is one that
     * has not yet started and thus can be joined by new
     * players.
     *
     * @return a {@link List} of Game IDs
     */
    public List<String> getPendingGameIDs() {
        return Collections.unmodifiableList(pending);
    }

}
