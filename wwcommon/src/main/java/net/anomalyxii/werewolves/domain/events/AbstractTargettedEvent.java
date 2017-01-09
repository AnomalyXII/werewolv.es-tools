package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 08/01/2017.
 */
public abstract class AbstractTargettedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final PlayerInstance target;

    // ******************************
    // Constructors
    // ******************************

    public AbstractTargettedEvent(PlayerInstance player, OffsetDateTime timestamp, EventType type, PlayerInstance target) {
        super(player, timestamp, type);
        this.target = target;
    }

    // ******************************
    // Constructors
    // ******************************

    public PlayerInstance getTarget() {
        return target;
    }

}
