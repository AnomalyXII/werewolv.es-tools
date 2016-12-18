package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Player;

import java.time.OffsetDateTime;
import java.util.Calendar;

/**
 * Generic specification of an event from
 * the <code>werewolv.es</code> API
 *
 * Created by Anomaly on 20/11/2016.
 */
public interface Event {

    // ******************************
    // Interface Methods
    // ******************************

    Player getPlayer();

    OffsetDateTime getTime();

    EventType getType();

    // ******************************
    // Constnts
    // ******************************

    enum EventType {

        GAME_STARTED,
        ROLE_ASSIGNED,

        PLAYER_JOINED,
        PLAYER_LEFT,

        PLAYER_KILLED,
        PLAYER_LYNCHED,
        PLAYER_REVIVED,

        INACTIVITY_WARNING,

        NOMINATION,

        COVEN_MESSAGE,
        WEREWOLF_MESSAGE,
        VILLAGE_MESSAGE,

        // End of constants
        ;

    }


}
