package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

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

    public WarnedForInactivityEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.INACTIVITY_WARNING);
    }
}
