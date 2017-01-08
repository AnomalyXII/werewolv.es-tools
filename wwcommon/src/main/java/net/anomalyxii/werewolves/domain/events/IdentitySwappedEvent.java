package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 07/01/2017.
 */
public class IdentitySwappedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public IdentitySwappedEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.IDENTITY_ASSIGNED);
    }

}
