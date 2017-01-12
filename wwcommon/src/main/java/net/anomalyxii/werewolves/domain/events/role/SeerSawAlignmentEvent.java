package net.anomalyxii.werewolves.domain.events.role;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.AbstractTargettedEvent;
import net.anomalyxii.werewolves.domain.events.AbstractVisitedEvent;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 11/01/2017.
 */
public class SeerSawAlignmentEvent extends AbstractTargettedEvent {

    // ******************************
    // Private Members
    // ******************************

    private final Alignment alignment;

    // ******************************
    // Constructors
    // ******************************

    public SeerSawAlignmentEvent(PlayerInstance player,
                                 OffsetDateTime timestamp,
                                 PlayerInstance target,
                                 Alignment alignment) {
        super(player, timestamp, EventType.ROLE_ABILITY_USED, target);
        this.alignment = alignment;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("%s saw that %s belongs to the %s", getPlayer().getName(), getTarget().getName(), alignment);
    }

}
