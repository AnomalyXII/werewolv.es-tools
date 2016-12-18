package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class GameStartedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public GameStartedEvent(Player player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.GAME_STARTED);
    }

}
