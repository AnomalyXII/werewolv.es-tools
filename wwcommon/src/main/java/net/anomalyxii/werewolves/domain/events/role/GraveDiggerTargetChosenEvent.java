package net.anomalyxii.werewolves.domain.events.role;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.AbstractTargettedEvent;
import net.anomalyxii.werewolves.domain.events.Event;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 10/01/2017.
 */
public class GraveDiggerTargetChosenEvent extends AbstractTargettedEvent {

    // ******************************
    // Constructors
    // ******************************

    public GraveDiggerTargetChosenEvent(PlayerInstance player, OffsetDateTime timestamp, PlayerInstance target) {
        super(player, timestamp, EventType.ROLE_ABILITY_SELECTED, target);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("%s will investigate the grave of %s", getPlayer().getName(), getTarget().getName());
    }

}
