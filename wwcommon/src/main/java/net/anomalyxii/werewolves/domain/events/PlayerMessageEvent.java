package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.Vitality;

import java.time.OffsetDateTime;
import java.util.Calendar;

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

    protected PlayerMessageEvent(Player player, OffsetDateTime timestamp, EventType eventType, String message) {
        super(player, timestamp, eventType);
        this.message = message;
    }

    // ******************************
    // Getters
    // ******************************

    public String getMessage() {
        return message;
    }

    public Vitality getVitalityVisibility() { return null; }

    public Alignment getAlignmentVisibility() {
        return null;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("%s %s", super.toString(), message);
    }

}
