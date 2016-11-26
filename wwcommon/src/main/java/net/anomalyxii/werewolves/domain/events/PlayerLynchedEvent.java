package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.players.Character;

import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerLynchedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public PlayerLynchedEvent(Player player, Calendar timestamp) {
        super(player, timestamp, EventType.PLAYER_LYNCHED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] (x_x) %s was lynched", getTimestamp(), getPlayer().getName());
    }

}
