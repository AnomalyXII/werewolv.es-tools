package net.anomalyxii.werewolves.domain.events.message;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class SpectatorMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public SpectatorMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.VILLAGE_MESSAGE, message);
    }

}
