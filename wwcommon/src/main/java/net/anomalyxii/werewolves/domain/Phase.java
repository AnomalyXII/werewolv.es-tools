package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.players.CharacterInstance;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

/**
 * A phase (day / night) of a day.
 * <p>
 * Created by Anomaly on 27/11/2016.
 */
public interface Phase {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Get the time that this {@code Phase} started.
     *
     * @return the {@link OffsetDateTime}
     */
    OffsetDateTime getStartTime();

    /**
     * Whether the phase is considered "complete", i.e. if a new
     * phase has superseded it.
     *
     * @return {@literal true} if the phase is complete; {@literal false} otherwise
     */
    boolean isComplete();

    /**
     * Get a {@link List} containing all the {@link Event Events}
     * that took place, in chronological order, during this phaase.
     *
     * @return a {@link List} of {@link Event Events}
     */
    List<Event> getEvents();

    /**
     * Get a {@link Set} containing a {@link CharacterInstance}
     * representing the state of each player in the game at the
     * start of this {@code Phase}.
     */
    Set<CharacterInstance> getCharacterInstanceAtStartOfPhase();

}
