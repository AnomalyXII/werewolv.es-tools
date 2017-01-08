package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerLynchedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public PlayerLynchedEvent(PlayerInstance player, OffsetDateTime timestamp) {
        super(player, timestamp, EventType.PLAYER_LYNCHED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("(x_x) %s was lynched", getPlayer().getName());
    }

}
