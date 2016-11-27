package net.anomalyxii.werewolves.domain;

import java.net.URI;

/**
 * A player (actual or anonymised).
 *
 * Created by Anomaly on 26/11/2016.
 */
public interface Player {

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

}
