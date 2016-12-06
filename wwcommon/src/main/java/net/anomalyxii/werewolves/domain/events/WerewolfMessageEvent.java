package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 03/12/2016.
 */
public class WerewolfMessageEvent extends PlayerMessageEvent {

    // ******************************
    // Constructors
    // ******************************

    public WerewolfMessageEvent(Player player, Calendar timestamp, String message) {
        super(player, timestamp, EventType.WEREWOLF_MESSAGE, message);
    }

    // ******************************
    // PlayerMessageEvent Methods
    // ******************************

    @Override
    public Alignment getAlignmentVisibility() {
        return Alignment.WEREWOLVES;
    }

}
