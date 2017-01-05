package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by Anomaly on 05/01/2017.
 */
public abstract class GameContext {

    // ******************************
    // Members
    // ******************************

    private boolean gameStarted = false;
    private boolean gameFinished = false;
    private boolean dayPhase = false;
    private Alignment winner = null;

    private final Set<Player> players = new LinkedHashSet<>();
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Character> characters = new HashMap<>();

    private final List<Event> preGameEvents = new ArrayList<>();
    private final List<Event> postGameEvents = new ArrayList<>();
    private final Deque<Day> days = new ArrayDeque<>();
    private final Map<Day, List<Event>> dayGameEvents = new HashMap<>();
    private final Map<Day, List<Event>> nightGameEvents = new HashMap<>();

    // ******************************
    // Process Methods
    // ******************************

    public void process(Map<String, Object> event) {

        // Lookup the player
        Player player = parsePlayer(event);
        players.add(player);

        Day currentDay = days.peekLast();
        List<Event> currentPhase = gameStarted
                                   ? gameFinished
                                     ? postGameEvents
                                     : dayPhase
                                       ? dayGameEvents.get(currentDay)
                                       : nightGameEvents.get(currentDay)
                                   : preGameEvents;

        Event parsedEvent = parseEvent(player, event);
        if (parsedEvent != null)
            currentPhase.add(parsedEvent);

    }

    protected abstract Event parseEvent(Player player, Map<String, Object> event);

    // ******************************
    // Build Methods
    // ******************************

    public Game build() {
        Game game = new Game();
        players.forEach(game::addPlayer);

        preGameEvents.forEach(game::addPreGameEvent);
        days.forEach(game::addDay);
        postGameEvents.forEach(game::addPostGameEvent);

        game.setWinningAlignment(winner);
        return game;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    protected boolean isGameStarted() {
        return gameStarted;
    }

    protected void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    protected boolean isGameFinished() {
        return gameFinished;
    }

    protected void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    protected boolean isDayPhase() {
        return dayPhase;
    }

    protected void setDayPhase(boolean dayPhase) {
        this.dayPhase = dayPhase;
    }

    public Alignment getWinningAlignment() {
        return winner;
    }

    public void setWinningAlignment(Alignment winner) {
        this.winner = winner;
    }

    // Collection Getters


    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(players);
    }

    public Map<String, User> getUsers() {
        return Collections.unmodifiableMap(users);
    }

    public Map<String, Character> getCharacters() {
        return Collections.unmodifiableMap(characters);
    }

    public Deque<Day> getDays() {
        return days;
    }

    // ******************************
    // Helper Methods
    // ******************************

    // Common Parsing Functions

    protected abstract Player parsePlayer(Map<String, Object> event);

    protected abstract String parseType(Map<String, Object> event);

    protected abstract OffsetDateTime parseTime(Map<String, Object> event);

    protected abstract String parseMessage(Map<String, Object> event);

    // Look-up Players + Characters

    protected Player findOrCreatePlayer(String name, String avatarUrl) {

        if (isGameStarted())
            return findOrCreateCharacter(name, avatarUrl);
        else
            return findOrCreateUser(name, avatarUrl);

    }

    protected User getUser(String name) {
        User user = findUser(name);
        if (user != null)
            return user;
        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    protected User findUser(String name) {
        return users.get(name);
    }

    protected User findOrCreateUser(String name, String avatarUrl) {
        User user = findUser(name);
        if (user != null)
            return user;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        user = new User(name, uri);
        users.put(name, user);
        return user;
    }

    protected Character getCharacter(String name) {
        Character character = findCharacter(name);
        if (character != null)
            return character;
        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    protected Character findCharacter(String name) {
        return characters.get(name);
    }

    protected Character findOrCreateCharacter(String name, String avatarUrl) {
        Character character = findCharacter(name);
        if (character != null)
            return character;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        character = new Character(name, uri);
        characters.put(name, character);
        return character;
    }

    // Look-up Roles and Alignments

    protected Role getRole(String role) {
        return Role.forString(role);
    }

    // Day and Night Functions

    protected Day startNewDay() {
        Day lastDay = days.peekLast();
        if (lastDay != null)
            lastDay.getNightPhase().setComplete(true);

        List<Event> newDayPhase = new ArrayList<>();
        List<Event> newNightPhase = new ArrayList<>();
        Day currentDay = new Day(new DayPhase(newDayPhase), new NightPhase(newNightPhase));
        dayGameEvents.put(currentDay, newDayPhase);
        nightGameEvents.put(currentDay, newNightPhase);
        getDays().addLast(currentDay);

        setDayPhase(true);
        return currentDay;
    }

}
