package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerKilledEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public PlayerKilledEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.PLAYER_KILLED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("%s [%tH:%<tM] (x_x) %s was killed", getStatusString(), getTime(), getPlayer().getName());
    }

}
