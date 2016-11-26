package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerMessageEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final String message;

    // ******************************
    // Constructors
    // ******************************

    public PlayerMessageEvent(Player player, Calendar timestamp, String message) {
        super(player, timestamp, EventType.VILLAGE_MESSAGE);
        this.message = message;
    }

    // ******************************
    // Getters
    // ******************************

    public String getMessage() {
        return message;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), message);
    }

}
