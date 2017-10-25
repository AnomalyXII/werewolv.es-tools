package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A game of <code>werewolv.es</code>.
 * <p>
 * A game is made up of a series of
 * {@link Day days} themselves comprising
 * of a number of {@link Event events}.
 * <p>
 * Created by Anomaly on 20/11/2016.
 */
public class Game {

    // ******************************
    // Members
    // ******************************

    private final String id;
    private final List<User> users = new ArrayList<>();
    private final List<Character> characters = new ArrayList<>();
    private final List<Player> specialPlayers = new ArrayList<>();

    private final List<Event> preGameEvents = new ArrayList<>();
    private final List<Event> postGameEvents = new ArrayList<>();

    private final List<Day> days = new ArrayList<>();

    private Alignment winningAlignment = null;

    // ******************************
    // Constructors
    // ******************************

    public Game(String id) {
        this.id = id;
    }

    // ******************************
    // Getters
    // ******************************

    /**
     * Get the {@code ID} of this {@code Game}.
     *
     * @return the {@code ID}
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieve a {@link List} of all the real identities of
     * {@link User Users} who are in the game.
     *
     * @return a {@link List} of {@link User Users}
     */
    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    /**
     * Retrieve a {@link List} of all anonymised identities of
     * {@link Character Characters} who are in the game.
     *
     * @return a {@link List} of {@link Character Characters}
     */
    public List<Character> getCharacters() {
        return Collections.unmodifiableList(characters);
    }

    /**
     * Get all the {@link Event Events} that occurred before the
     * {@code Game} officially began.
     *
     * @return a {@link List} of pre-game {@link Event Events}
     */
    public List<Event> getPreGameEvents() {
        return Collections.unmodifiableList(preGameEvents);
    }

    /**
     * Get all the {@link Event Events} that occurred after the
     * {@code Game} officially ended.
     *
     * @return a {@link List} of post-game {@link Event Events}
     */
    public List<Event> getPostGameEvents() {
        return Collections.unmodifiableList(postGameEvents);
    }

    /**
     * Get all the {@link Day Days} that occurred during the
     * start and end of the {@code Game}.
     *
     * @return a {@link List} of {@link Day Days}
     */
    public List<Day> getDays() {
        return Collections.unmodifiableList(days);
    }

    /**
     * Get the {@link Day} using a 0-based index.
     *
     * @param dayNo the day number, starting from 0
     * @return the {@link Day}
     */
    public Day getDay(int dayNo) {
        return days.get(dayNo);
    }

    /**
     * Get the {@link Day} using a 1-based index.
     *
     * @param visualDayNo the day number, starting from 1
     * @return the {@link Day}
     */
    public Day getDayFor(int visualDayNo) {
        return getDay(visualDayNo - 1);
    }

    /**
     * Get the final {@link Day}.
     * <p>
     * If the game hasn't finished, this method will return
     * {@literal null}.
     *
     * @return the final {@link Day}
     */
    public Day getLastDay() {
        if (!isGameEnded())
            return null;

        return days.get(days.size() - 1);
    }

    /**
     * Get the effectively final {@link Day} - that is, the last
     * {@link Day} that actually has meaningful
     * {@link Event Events}.
     * <p>
     * Under normal circumstances, if the game ended at night-fall,
     * i.e. after a player has been lynched, then will be the actual
     * {@link #getLastDay() last day} - otherwise this will be the
     * previous day.
     * <p>
     * If the game hasn't finished, this method will return
     * {@literal null}.
     *
     * @return the final {@link Day}
     */
    public Day getEffectiveLastDay() {
        if (!isGameEnded())
            return null;

        int lastDayIndex = days.size() - 1;
        Day lastDay = days.get(lastDayIndex);

        // Game ended when the Day started, so go back another day...
        if (!lastDay.getDayPhase().isComplete())
            lastDay = days.get(lastDayIndex - 1);

        return lastDay;
    }

    /**
     * Determine whether the {@code Game} has ended.
     *
     * @return {@literal true} if the game is over; {@literal false} otherwise
     */
    public boolean isGameEnded() {
        return winningAlignment != null;
    }

    /**
     * Retrieve the {@link Alignment} of the team who won this
     * game. Will return {@literal null} if the {@code Game} is
     * still in progress.
     *
     * @return the winning {@link Alignment}, or {@literal null}
     */
    public Alignment getWinningAlignment() {
        return winningAlignment;
    }

    // ******************************
    // Setters
    // ******************************

    public void addPlayer(Player player) {
        if (player instanceof User)
            addUser((User) player);
        else if (player instanceof Character)
            addCharacter((Character) player);
        else
            specialPlayers.add(player);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void addPreGameEvent(Event event) {
        preGameEvents.add(event);
    }

    public void addPostGameEvent(Event event) {
        postGameEvents.add(event);
    }

    public void addDay(Day day) {
        days.add(day);
    }

    public void setWinningAlignment(Alignment winningAlignment) {
        this.winningAlignment = winningAlignment;
    }

    // ******************************
    // Constants
    // ******************************

    public enum GameStatus {

        PENDING,
        IN_PROGRESS,
        COMPLETE,

        // End of constants
        ;

    }

}
