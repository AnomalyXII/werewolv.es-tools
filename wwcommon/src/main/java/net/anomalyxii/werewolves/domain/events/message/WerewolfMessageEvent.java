package net.anomalyxii.werewolves.domain.events.message;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.events.message.PlayerMessageEvent;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class WerewolfMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public WerewolfMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.WEREWOLF_MESSAGE, message);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.WEREWOLVES;
    }

}
