package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.players.Character;

import java.time.OffsetDateTime;

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

    public PlayerNominationEvent(PlayerInstance player, OffsetDateTime timestamp, Character target) {
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
                             getTime(),
                             getPlayer().getName(),
                             getTarget().getName());
    }

}
