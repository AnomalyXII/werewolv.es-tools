package net.anomalyxii.werewolves.domain.phases;

import net.anomalyxii.werewolves.domain.Phase;
import net.anomalyxii.werewolves.domain.events.Event;

import java.util.Collections;
import java.util.List;

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

    private boolean complete = false;
    private final List<Event> events;

    // ******************************
    // Constructors
    // ******************************

    public AbstractPhase(List<Event> events) {
        this.events = events;
    }

    public AbstractPhase(List<Event> events, boolean complete) {
        this.complete = complete;
        this.events = events;
    }

    // ******************************
    // Getters
    // ******************************

    @Override
    public boolean isComplete() {
        return complete;
    }

    @Override
    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    // ******************************
    // Setters
    // ******************************

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public void removeEvent(Event event) {
        events.remove(event);
    }

}
