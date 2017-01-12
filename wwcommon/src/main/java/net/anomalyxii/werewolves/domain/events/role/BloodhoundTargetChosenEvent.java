package net.anomalyxii.werewolves.domain.events.role;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.AbstractTargettedEvent;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class BloodhoundTargetChosenEvent extends AbstractTargettedEvent {

    // ******************************
    // Constructors
    // ******************************

    public BloodhoundTargetChosenEvent(PlayerInstance player, OffsetDateTime timestamp, PlayerInstance target) {
        super(player, timestamp, EventType.ROLE_ABILITY_SELECTED, target);
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
        return fmt("%s will track %s", getPlayer().getName(), getTarget().getName());
    }

}
