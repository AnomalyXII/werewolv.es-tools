package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class VillageMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public VillageMessageEvent(Player player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.VILLAGE_MESSAGE, message);
    }

}
