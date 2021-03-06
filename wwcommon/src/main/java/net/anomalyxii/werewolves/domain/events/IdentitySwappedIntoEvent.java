package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 07/01/2017.
 */
public class IdentitySwappedIntoEvent extends AbstractTargettedEvent {

    // ******************************
    // Constructors
    // ******************************

    public IdentitySwappedIntoEvent(PlayerInstance player, OffsetDateTime timestamp, PlayerInstance target) {
        super(player, timestamp, EventType.IDENTITY_ASSIGNED, target);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("~~ %s swapped into %s", getPlayer().getName(), getTarget().getName());
    }

}
