package net.anomalyxii.werewolves.domain.events.message;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.Event;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class VampireMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public VampireMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, Event.EventType.VAMPIRE_MESSAGE, message);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.VAMPIRES;
    }

}
