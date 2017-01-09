package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.players.Character;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerNominationEvent extends AbstractTargettedEvent {

    // ******************************
    // Constructors
    // ******************************

    public PlayerNominationEvent(PlayerInstance player, OffsetDateTime timestamp, PlayerInstance target) {
        super(player, timestamp, EventType.NOMINATION, target);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("%s votes to lynch %s", getPlayer().getName(), getTarget().getName());
    }

}
