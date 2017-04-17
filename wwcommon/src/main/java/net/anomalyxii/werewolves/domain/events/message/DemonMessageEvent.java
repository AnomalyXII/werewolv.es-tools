package net.anomalyxii.werewolves.domain.events.message;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class DemonMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public DemonMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.VAMPIRE_MESSAGE, message);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.DEMONS;
    }

}