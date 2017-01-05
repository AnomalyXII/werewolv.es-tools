package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;

import java.time.OffsetDateTime;

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

    PlayerInstance getPlayer();

    OffsetDateTime getTime();

    EventType getType();

    // ******************************
    // Default Methods
    // ******************************

    default Vitality getVitalityVisibility() { return null; }

    default Alignment getAlignmentVisibility() {
        return null;
    }

    // ******************************
    // Constants
    // ******************************

    /**
     *
     */
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
        VAMPIRE_MESSAGE,
        VILLAGE_MESSAGE,

        // End of constants
        ;

    }


}
