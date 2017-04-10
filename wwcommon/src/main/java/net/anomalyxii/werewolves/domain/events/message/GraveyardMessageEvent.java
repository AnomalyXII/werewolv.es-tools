package net.anomalyxii.werewolves.domain.events.message;

import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class GraveyardMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public GraveyardMessageEvent(PlayerInstance player, OffsetDateTime timestamp, String message) {
        super(player, timestamp, EventType.WEREWOLF_MESSAGE, message);
    }

    // ******************************
    // Event Methods
    // ******************************

    @Override
    public Vitality getVitalityVisibility() {
        return Vitality.DEAD;
    }

    // ******************************
    // To String
    // ******************************

    @Override
    public String toString() {
        return fmt("(%s) %s", getPlayer().getName(), getMessage());
    }

    @Override
    protected char getChatStatus() {
        return 'G';
    }
}
