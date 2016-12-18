package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerLeftEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public PlayerLeftEvent(Player player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.PLAYER_LEFT);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] <- %s has left the game", getTime(), getPlayer().getName());
    }

}
