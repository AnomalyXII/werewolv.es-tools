package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerJoinedEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public PlayerJoinedEvent(Player player, Calendar timestamp) {
        super(player, timestamp, EventType.PLAYER_JOINED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] -> %s has joined the game", getTimestamp(), getPlayer().getName());
    }

}
