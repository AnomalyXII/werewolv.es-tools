package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;

import java.time.OffsetDateTime;
import java.util.*;

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

    // Track players
    private final PlayerContext playerContext = new PlayerContext();

    // Track the events
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
        PlayerInstance player = parsePlayerInstance(event);

        // Calculate the event time
        OffsetDateTime timestamp = parseTime(event);

        Day currentDay = days.peekLast();
        List<Event> currentPhase = gameStarted
                                   ? gameFinished
                                     ? postGameEvents
                                     : dayPhase
                                       ? dayGameEvents.get(currentDay)
                                       : nightGameEvents.get(currentDay)
                                   : preGameEvents;

        Event parsedEvent = parseEvent(player, timestamp, event);
        if (parsedEvent != null)
            currentPhase.add(parsedEvent);

    }

    protected abstract Event parseEvent(PlayerInstance player, OffsetDateTime timestamp, Map<String, Object> event);

    // ******************************
    // Build Methods
    // ******************************

    public Game build() {
        Game game = new Game();
        playerContext.allPlayers().forEach(game::addPlayer);

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

    public PlayerContext getPlayerContext() {
        return playerContext;
    }

    public Deque<Day> getDays() {
        return days;
    }

    // ******************************
    // Helper Methods
    // ******************************

    // Common Parsing Functions

    protected abstract PlayerInstance parsePlayerInstance(Map<String, Object> event);

    protected abstract String parseType(Map<String, Object> event);

    protected abstract OffsetDateTime parseTime(Map<String, Object> event);

    protected abstract String parseMessage(Map<String, Object> event);

    protected User getUser(Map<String, Object> event, String field) {
        return getPlayerContext().getUser((String) event.get(field));
    }

    protected PlayerInstance getInstanceForUser(Map<String, Object> event, String field) {
        return getPlayerContext().instanceFor(getUser(event, field));
    }

    protected Character getCharacter(Map<String, Object> event, String field) {
        return getPlayerContext().getCharacter((String) event.get(field));
    }

    protected PlayerInstance getInstanceForCharacter(Map<String, Object> event, String field) {
        return getPlayerContext().instanceFor(getCharacter(event, field));
    }

    // Look-up Players + Characters

    protected PlayerInstance findOrCreatePlayer(String name, String avatarUrl) {
        Player player = isGameStarted()
                        ? playerContext.findOrCreateCharacter(name, avatarUrl)
                        : playerContext.findOrCreateUser(name, avatarUrl);
        return playerContext.instanceFor(player);
    }

    protected PlayerInstance getPlayer(String name) {
        Player player = isGameStarted()
                        ? playerContext.getCharacter(name)
                        : playerContext.getUser(name);
        return playerContext.instanceFor(player);
    }

    protected PlayerInstance findPlayer(String name) {
        Player player = isGameStarted()
                        ? playerContext.findCharacter(name)
                        : playerContext.findUser(name);
        return playerContext.instanceFor(player);
    }

    // Phase Functions

    protected Day startDayPhase() {
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

    protected Day startNightPhase() {
        Day currentDay = days.peekLast();
        if (currentDay == null)
            throw new AssertionError("Should never happen!");

        playerContext.resetControlledCharacters();

        currentDay.getDayPhase().setComplete(true);
        setDayPhase(false);
        return currentDay;
    }

    protected void finishGame(Alignment winningAlignment) {
        Day lastDay = days.peekLast();
        if (isDayPhase())
            lastDay.getDayPhase().setComplete(true);
        else
            lastDay.getNightPhase().setComplete(true);

        setWinningAlignment(winningAlignment);
        setGameFinished(true);
    }

    // Other Helpers

    protected Role getRole(String role) {
        return Role.forString(role);
    }

    protected void assignFinalUsersToCharacters() {
        playerContext.allUsersWithCharacter().forEach((user, character) -> character.setUser(user));
    }

}
