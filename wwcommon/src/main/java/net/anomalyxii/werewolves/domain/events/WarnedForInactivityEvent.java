package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class WarnedForInactivityEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public WarnedForInactivityEvent(Player player, Calendar timestamp) {
        super(player, timestamp, EventType.INACTIVITY_WARNING);
    }
}
