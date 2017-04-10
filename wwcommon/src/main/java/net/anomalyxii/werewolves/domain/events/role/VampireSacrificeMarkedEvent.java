package net.anomalyxii.werewolves.domain.events.role;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.AbstractTargettedEvent;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/01/2017.
 */
public class VampireSacrificeMarkedEvent extends AbstractTargettedEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public VampireSacrificeMarkedEvent(PlayerInstance player, OffsetDateTime timestamp, PlayerInstance target) {
        super(player, timestamp, EventType.PLAYER_MARKED, target);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("The blood of %s can be used to resurrect your master!", getTarget().getName());
    }

}
