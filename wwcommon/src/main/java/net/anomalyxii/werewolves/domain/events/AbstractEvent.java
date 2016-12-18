package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Created by Anomaly on 20/11/2016.
 */
public abstract class AbstractEvent implements Event {

    // ******************************
    // Members
    // ******************************

    private final Player player;
    private final OffsetDateTime timestamp;
    //
    private final EventType type;

    // ******************************
    // Constructors
    // ******************************

    public AbstractEvent(Player player, OffsetDateTime timestamp, EventType type) {
        this.player = player;
        this.timestamp = timestamp;
        this.type = type;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public OffsetDateTime getTime() {
        return timestamp;
    }

    @Override
    public EventType getType() {
        return type;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] <%s>", timestamp, player.getFormattedName());
    }

}
