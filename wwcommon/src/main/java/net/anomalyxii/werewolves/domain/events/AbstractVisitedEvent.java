package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 08/01/2017.
 */
public abstract class AbstractVisitedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final PlayerInstance visitor;
    private final PlayerInstance target;

    // ******************************
    // Constructors
    // ******************************

    public AbstractVisitedEvent(PlayerInstance player,
                                OffsetDateTime timestamp,
                                EventType type,
                                PlayerInstance visitor,
                                PlayerInstance target) {
        super(player, timestamp, type);
        this.visitor = visitor;
        this.target = target;
    }

    // ******************************
    // Getters
    // ******************************

    public PlayerInstance getVisitor() {
        return visitor;
    }

    public PlayerInstance getTarget() {
        return target;
    }

}
