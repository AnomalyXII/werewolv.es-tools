package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.events.Event;

import java.util.Collections;
import java.util.List;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class Day {

    // ******************************
    // Members
    // ******************************

    private final List<Event> events;

    // ******************************
    // Constructors
    // ******************************

    public Day(List<Event> events) {
        this.events = events;
    }

    // ******************************
    // Getters
    // ******************************

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

}
