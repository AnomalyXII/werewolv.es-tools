package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 06/01/2017.
 */
public class MasonMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public MasonMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.MASON_MESSAGE, message);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    protected char getChatStatus() {
        return 'M'; // Todo: do we need to add a specific Alignment in for this?
    }

}
