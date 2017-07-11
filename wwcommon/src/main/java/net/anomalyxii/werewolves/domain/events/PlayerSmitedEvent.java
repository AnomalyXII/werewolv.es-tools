package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerSmitedEvent extends AbstractEvent {

    private final String reason;

    // ******************************
    // Constructors
    // ******************************

    public PlayerSmitedEvent(PlayerInstance player, OffsetDateTime timestamp, String reason) {
        super(player, timestamp, EventType.PLAYER_REVIVED);
        this.reason = Objects.requireNonNull(reason);
    }

    // ******************************
    // Getters
    // ******************************

    public String getReason() {
        return reason;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt(">-(._.)-> %s was smited", getPlayer().getName());
    }

}
