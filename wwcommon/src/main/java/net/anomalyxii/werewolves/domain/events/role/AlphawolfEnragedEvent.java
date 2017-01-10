package net.anomalyxii.werewolves.domain.events.role;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.AbstractEvent;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 10/01/2017.
 */
public class AlphawolfEnragedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public AlphawolfEnragedEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.ROLE_ABILITY_STARTED);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.WEREWOLVES;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("%s is enraged", getPlayer().getName());
    }

}
