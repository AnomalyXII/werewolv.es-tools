package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;

import java.util.Collections;
import java.util.List;

/**
 * A day cycle.
 * <p>
 * One day cycle comprises of two phases:
 * a <code>day</code> phases and a
 * <code>night</code> phases.
 * <p>
 * During the day phases the villagers will
 * discuss who they believe the werewolves
 * to be, hopefully ending in a lynch.
 * <p>
 * During the night phases any characters
 * who have a special role can use their
 * ability, the outcome of which will be
 * revealed at the end of this phases.
 * <p>
 * Created by Anomaly on 26/11/2016.
 */
public class Day {

    // ******************************
    // Members
    // ******************************

    private final DayPhase dayPhase;
    private final NightPhase nightPhase;

    // ******************************
    // Constructors
    // ******************************

    public Day(DayPhase dayPhase, NightPhase nightPhase) {
        this.dayPhase = dayPhase;
        this.nightPhase = nightPhase;
    }

    // ******************************
    // Getters
    // ******************************

    public DayPhase getDayPhase() {
        return dayPhase;
    }

    public NightPhase getNightPhase() {
        return nightPhase;
    }

}
