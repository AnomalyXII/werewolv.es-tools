package net.anomalyxii.werewolves.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
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

    public List<String> getActiveGameIDs() {
        return Collections.unmodifiableList(active);
    }

    public List<String> getPendingGameIDs() {
        return Collections.unmodifiableList(pending);
    }

}
