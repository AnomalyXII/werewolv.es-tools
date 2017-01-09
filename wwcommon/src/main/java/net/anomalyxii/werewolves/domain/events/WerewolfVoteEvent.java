package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class WerewolfVoteEvent extends AbstractTargettedEvent {

    // ******************************
    // Constructors
    // ******************************

    public WerewolfVoteEvent(PlayerInstance player, OffsetDateTime timestamp, PlayerInstance target) {
        super(player, timestamp, EventType.WEREWOLF_VOTE, target);
    }

    // ******************************
    // PlayerMessageEvent Methods
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
        return fmt("%s votes to kill %s", getPlayer().getName(), getTarget().getName());
    }

}
