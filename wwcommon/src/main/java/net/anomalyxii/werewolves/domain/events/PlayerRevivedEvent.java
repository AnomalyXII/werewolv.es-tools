package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerRevivedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public PlayerRevivedEvent(Player player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.PLAYER_REVIVED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] (^_^) %s was revived", getTime(), getPlayer().getName());
    }

}
