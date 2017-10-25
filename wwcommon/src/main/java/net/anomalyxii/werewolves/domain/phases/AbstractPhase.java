package net.anomalyxii.werewolves.domain.phases;

import net.anomalyxii.werewolves.domain.Phase;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.players.CharacterInstance;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Base class for {@link DayPhase}
 * and {@link NightPhase}
 * <p>
 * Created by Anomaly on 27/11/2016.
 */
public abstract class AbstractPhase implements Phase {

    // ******************************
    // Members
    // ******************************

    private OffsetDateTime startTime;
    private boolean complete = false;
    private final List<Event> events;
    private final Set<CharacterInstance> characterInstancesAtStart = new HashSet<>();

    // ******************************
    // Constructors
    // ******************************

    public AbstractPhase(List<Event> events) {
        this.events = events;
    }

    // ******************************
    // Getters
    // ******************************

    @Override
    public OffsetDateTime getStartTime() {
        return startTime;
    }

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    @Override
    public Set<CharacterInstance> getCharacterInstanceAtStartOfPhase() {
        return Collections.unmodifiableSet(characterInstancesAtStart);
    }

    // ******************************
    // Setters
    // ******************************

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

    public void setCharacterInstancesAtStart(Collection<CharacterInstance> instances) {
        characterInstancesAtStart.addAll(instances);
    }

    public void setCharacterInstanceAtStart(CharacterInstance instance) {
        characterInstancesAtStart.add(instance);
    }

}
