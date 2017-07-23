package net.anomalyxii.werewolves.domain.events.role;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.AbstractVisitedEvent;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 11/01/2017.
 */
public class InsomniacSawVisitEvent extends AbstractVisitedEvent {

    // ******************************
    // Constructors
    // ******************************

    public InsomniacSawVisitEvent(PlayerInstance player,
                                  OffsetDateTime timestamp,
                                  PlayerInstance visitor,
                                  PlayerInstance target) {
        super(player, timestamp, EventType.ROLE_ABILITY_USED, visitor, target);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("%s saw they were visited by %s", getPlayer().getName(), getVisitor().getName());

    }

}
