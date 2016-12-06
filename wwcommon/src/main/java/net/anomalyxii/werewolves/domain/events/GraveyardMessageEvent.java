package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.Vitality;

import java.util.Calendar;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class GraveyardMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public GraveyardMessageEvent(Player player, Calendar timestamp, String message) {
        super(player, timestamp, EventType.WEREWOLF_MESSAGE, message);
    }

    // ******************************
    // PlayerMessageEvent Methods
    // ******************************

    @Override
    public Vitality getVitalityVisibility() {
        return Vitality.DEAD;
    }

}
