package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.events.Event;

import java.util.List;

/**
 * A phase (day / night) of a day.
 *
 * Created by Anomaly on 27/11/2016.
 */
public interface Phase {

    // ******************************
    // Interface Methods
    // ******************************

    boolean isComplete();

    List<Event> getEvents();

}
