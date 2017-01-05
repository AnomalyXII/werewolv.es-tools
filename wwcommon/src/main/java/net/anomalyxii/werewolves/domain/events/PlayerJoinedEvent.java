package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerJoinedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public PlayerJoinedEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.PLAYER_JOINED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] -> %s has joined the game", getTime(), getPlayer().getName());
    }

}
