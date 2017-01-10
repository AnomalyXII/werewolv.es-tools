package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class CovenMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public CovenMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.COVEN_MESSAGE, message);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.COVEN;
    }

}
