package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class VampireMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public VampireMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.VAMPIRE_MESSAGE, message);
    }

    // ******************************
    // PlayerMessageEvent Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.VAMPIRE;
    }

}
