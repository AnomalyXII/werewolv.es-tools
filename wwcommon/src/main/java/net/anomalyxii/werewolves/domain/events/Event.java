package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.util.Calendar;

/**
 * Created by Anomaly on 20/11/2016.
 */
public interface Event {

    // ******************************
    // Interface Methods
    // ******************************

    Player getPlayer();

    Calendar getTimestamp();

    EventType getType();

    // ******************************
    // Constnts
    // ******************************

    enum EventType {

        GAME_STARTED,

        PLAYER_JOINED,
        PLAYER_LEFT,

        PLAYER_KILLED,
        PLAYER_LYNCHED,
        PLAYER_REVIVED,

        NOMINATION,

        VILLAGE_MESSAGE,

        // End of constants
        ;

    }


}
