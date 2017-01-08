package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class ModeratorMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public ModeratorMessageEvent(OffsetDateTime timestamp, String message) {
        super(PlayerInstance.MODERATOR, timestamp, EventType.VILLAGE_MESSAGE, message);
    }

    // ******************************
    // Constructors
    // ******************************

    @Override
    protected char getVitalityStatus() {
        return '!'; // Special for Moderator Message!
    }

}
