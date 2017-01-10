package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;
import net.anomalyxii.werewolves.domain.players.User;

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

    /**
     * Get the {@link PlayerInstance} that
     * triggered this event.
     *
     * @return the {@link PlayerInstance}
     */
    PlayerInstance getPlayer();

    /**
     * Get the underlying {@link Player}
     * that triggered this event.
     *
     * @return the {@link Player}
     */
    default Player getActualPlayer() {
        return getPlayer().getPlayer();
    }

    /**
     * Get the {@link OffsetDateTime} that
     * this event was triggered at.
     *
     * @return the {@link OffsetDateTime}
     */
    OffsetDateTime getTime();

    /**
     * Get the {@link EventType} of this
     * event.
     *
     * @return the {@link EventType}
     */
    EventType getType();

    // ******************************
    // Default Methods
    // ******************************

    default Vitality getVitalityVisibility() { return null; }

    default Alignment getAlignmentVisibility() {
        return null;
    }

    default boolean isVisibleToUser(User user) {
        return true;
    }

    // ******************************
    // Constants
    // ******************************

    /**
     * Enum for each event type
     */
    enum EventType {

        GAME_STARTED,
        IDENTITY_ASSIGNED,
        ROLE_ASSIGNED,
        ROLE_ABILITY_STARTED,
        ROLE_ABILITY_SELECTED,
        ROLE_ABILITY_USED,

        PLAYER_JOINED,
        PLAYER_LEFT,

        PLAYER_KILLED,
        PLAYER_LYNCHED,
        PLAYER_REVIVED,

        INACTIVITY_WARNING,

        NOMINATION,
        WEREWOLF_VOTE,

        COVEN_MESSAGE,
        WEREWOLF_MESSAGE,
        VAMPIRE_MESSAGE,
        MASON_MESSAGE,
        VILLAGE_MESSAGE,

        // End of constants
        ;

    }

}
