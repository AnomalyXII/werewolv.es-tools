package net.anomalyxii.werewolves.domain.events.message;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.Event;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class VillageMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public VillageMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, Event.EventType.VILLAGE_MESSAGE, message);
    }

}
