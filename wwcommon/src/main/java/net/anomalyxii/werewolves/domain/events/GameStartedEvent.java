package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class GameStartedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public GameStartedEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.GAME_STARTED);
    }

}
