package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.SpecialPlayer;
import net.anomalyxii.werewolves.domain.players.SpecialPlayerInstance;
import net.anomalyxii.werewolves.domain.players.User;

/**
 * A snapshot of the state of a {@link Player} at a single
 * point in a {@link Game}.
 * <p>
 * Created by Anomaly on 05/01/2017.
 */
public interface PlayerInstance {

    PlayerInstance MODERATOR = new SpecialPlayerInstance(Player.MODERATOR);
    PlayerInstance ANONYMOUS = new SpecialPlayerInstance(Player.ANONYMOUS);

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Get the {@link Player} that corresponds to this instance.
     * <p>
     * For pre-game events, this is likely to be a {@link User},
     * for in-game events it's almost always going to be a
     * {@link Character} and for post-game events it might be a
     * mixture.
     *
     * @return the {@link Player}
     */
    Player getPlayer();

    /**
     * Get the {@link Character} that corresponds to this instance,
     * if appropriate.
     *
     * @return the {@link User}, or {@literal null} if no character is associated
     */
    Character getCharacter();

    /**
     * Get the underlying {@link User} that corresponds to this
     * instance, if known.
     *
     * @return the {@link User}, or {@literal null} if not known
     */
    User getUser();

    /**
     * Determine whether this {@link Player} is joined to the game.
     *
     * @return {@link true} if the {@link Player} has joined the game, {@literal false} otherwise
     */
    boolean isPlayerInGame();

    /**
     * Determine the {@link Alignment} of this {@link Player}.
     *
     * @return the {@link Alignment} of the player, or {@literal null} if not applicable
     */
    Alignment getAlignment();

    /**
     * Determine whether, at this moment in the game, this
     * {@link Player} is alive or dead.
     *
     * @return the {@link Vitality} of the player, or {@literal null} if not applicable
     */
    Vitality getVitality();

    // ******************************
    // Default Methods
    // ******************************

    /**
     * Get the name of the {@link Player}.
     *
     * @return the display name of the player
     */
    default String getName() {
        return getPlayer().getName();
    }

    /**
     * Get the name of the {@link Player}, potentially with special
     * formatting applied.
     *
     * @return the formatted display name of the player
     */
    default String getFormattedName() {
        return getPlayer().getName();
    }

    /**
     * Convenience method for determining whether the
     * {@link Player} is currently alive.
     *
     * @return {@literal true} if the player is alive, {@literal false} otherwise
     */
    default boolean isAlive() {
        return getVitality() == Vitality.ALIVE;
    }

    /**
     * Convenience method for determining whether the
     * {@link Player} is currently dead.
     *
     * @return {@literal true} if the player is dead, {@literal false} otherwise
     */
    default boolean isDead() {
        return getVitality() == Vitality.DEAD;
    }

}
