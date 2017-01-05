package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 20/11/2016.
 */
public abstract class AbstractEvent implements Event {

    // ******************************
    // Members
    // ******************************

    private final PlayerInstance player;
    private final OffsetDateTime timestamp;
    //
    private final EventType type;

    // ******************************
    // Constructors
    // ******************************

    public AbstractEvent(PlayerInstance player, OffsetDateTime timestamp, EventType type) {
        this.player = player;
        this.timestamp = timestamp;
        this.type = type;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @Override
    public PlayerInstance getPlayer() {
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
        return String.format("[%tH:%<tM] <%s>", timestamp, player.getName());
    }

}
