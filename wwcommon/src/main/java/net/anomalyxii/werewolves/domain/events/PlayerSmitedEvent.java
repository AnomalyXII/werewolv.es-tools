package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerSmitedEvent extends AbstractEvent {

    // ******************************
    // Constructors
    // ******************************

    public PlayerSmitedEvent(Player player, Calendar timestamp) {
        super(player, timestamp, EventType.PLAYER_REVIVED);
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] >-(._.)-> %s was smited", getTimestamp(), getPlayer().getName());
    }

}
