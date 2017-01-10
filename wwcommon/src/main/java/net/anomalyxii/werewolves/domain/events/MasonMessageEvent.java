package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 06/01/2017.
 */
public class MasonMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public MasonMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.MASON_MESSAGE, message);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return null; // Todo: work out how to make this apply to only certain players!
    }


    // ******************************
    // To String
    // ******************************

    @Override
    protected char getChatStatus() {
        return 'M'; // Todo: do we need to add a specific Alignment in for this?
    }

}
