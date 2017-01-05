package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class VillageMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public VillageMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.VILLAGE_MESSAGE, message);
    }

}
