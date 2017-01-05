package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class ModeratorMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public ModeratorMessageEvent(OffsetDateTime timestamp, String message) {
        super(Player.MODERATOR, timestamp, EventType.VILLAGE_MESSAGE, message);
    }

}
