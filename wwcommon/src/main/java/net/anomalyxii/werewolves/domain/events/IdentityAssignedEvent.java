package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 06/01/2017.
 */
public class IdentityAssignedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public IdentityAssignedEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.IDENTITY_ASSIGNED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt(getPlayer(), "My new identity is: %s", getPlayer().getCharacter().getName());
    }

}
