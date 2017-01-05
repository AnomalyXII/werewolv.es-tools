package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/11/2016.
 */
public abstract class PlayerMessageEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final String message;

    // ******************************
    // Constructors
    // ******************************

    protected PlayerMessageEvent(PlayerInstance player, OffsetDateTime timestamp, EventType eventType, String message) {
        super(player, timestamp, eventType);
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
