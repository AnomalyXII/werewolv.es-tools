package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.players.Character;

import java.util.Calendar;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class PlayerNominationEvent extends AbstractEvent {

    // ******************************
    // Members
    // ******************************

    private final Character target;

    // ******************************
    // Constructors
    // ******************************

    public PlayerNominationEvent(Player player, Calendar timestamp, Character target) {
        super(player, timestamp, EventType.NOMINATION);
        this.target = target;
    }

    // ******************************
    // Getters
    // ******************************

    public Character getTarget() {
        return target;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return String.format("[%tH:%<tM] %s votes to lynch %s",
                getTimestamp(),
                getPlayer().getName(),
                getTarget().getName());
    }

}
