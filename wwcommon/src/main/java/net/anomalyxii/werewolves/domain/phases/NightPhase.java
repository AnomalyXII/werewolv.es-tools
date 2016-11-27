package net.anomalyxii.werewolves.domain.phases;

import net.anomalyxii.werewolves.domain.events.Event;

import java.util.List;

/**
 * The night phase.
 *
 * Created by Anomaly on 27/11/2016.
 */
public class NightPhase extends AbstractPhase {

    // ******************************
    // Constructors
    // ******************************

    public NightPhase(List<Event> events) {
        super(events);
    }

    public NightPhase(List<Event> events, boolean complete) {
        super(events, complete);
    }

}
