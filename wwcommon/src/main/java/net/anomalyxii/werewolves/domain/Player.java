package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.players.SpecialPlayer;

import java.net.URI;

/**
 * A player (actual or anonymised).
 *
 * Created by Anomaly on 26/11/2016.
 */
public interface Player {

    Player MODERATOR = new SpecialPlayer("Moderator", null);

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Get the name of the {@link Player}.
     *
     * @return the display name of the player
     */
    String getName();

    /**
     * Get a {@link URI} that points to
     * the avatar associated with this
     * {@link Player}.
     *
     * @return a {@link URI} to the avatar image
     */
    URI getAvatarURI();

    // ******************************
    // Default Methods
    // ******************************

    /**
     * Get the name of the {@link Player},
     * potentially with special formatting
     * applied.
     *
     * @return the formatted display name of the player
     */
    default String getFormattedName() {
        return getName();
    }

}
