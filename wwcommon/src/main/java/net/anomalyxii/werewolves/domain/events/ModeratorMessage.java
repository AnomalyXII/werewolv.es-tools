package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class ModeratorMessage extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public ModeratorMessage(Calendar timestamp, String message) {
        super(Player.MODERATOR, timestamp, EventType.VILLAGE_MESSAGE, message);
    }

}
