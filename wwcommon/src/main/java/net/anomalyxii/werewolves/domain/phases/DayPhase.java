package net.anomalyxii.werewolves.domain.phases;

import net.anomalyxii.werewolves.domain.events.Event;

import java.util.List;

/**
 * The day phase.
 * <p>
 * Created by Anomaly on 27/11/2016.
 */
public class DayPhase extends AbstractPhase {

    // ******************************
    // Constructors
    // ******************************

    public DayPhase(List<Event> events) {
        super(events);
    }

}
