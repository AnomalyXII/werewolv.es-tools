package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.phases.AbstractPhase;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.CharacterInstance;
import net.anomalyxii.werewolves.domain.players.User;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Tracks the information needed to reconstruct a {@link Game}
 * whilst parsing {@link Event Events} from the API.
 * <p>
 * Created by Anomaly on 05/01/2017.
 */
public abstract class GameContext {

    // ******************************
    // Members
    // ******************************

    private final String id;
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
    // Constructors
    // ******************************

    public GameContext(String id) {
        this.id = id;
    }

    // ******************************
    // Process Methods
    // ******************************

    public void process(Map<String, Object> event) {
        PlayerInstance player = parsePlayerInstance(event); // Lookup the player
        OffsetDateTime timestamp = parseTime(event); // Calculate the event time

        Event parsedEvent = parseEvent(player, timestamp, event);
        if (parsedEvent != null)
            getCurrentPhaseEventList().add(parsedEvent);
    }

    protected abstract Event parseEvent(PlayerInstance player, OffsetDateTime timestamp, Map<String, Object> event);

    // ******************************
    // Build Methods
    // ******************************

    public Game build() {
        Game game = new Game(id);
        playerContext.allPlayers().forEach(game::addPlayer);

        preGameEvents.forEach(game::addPreGameEvent);
        days.forEach(game::addDay);
        postGameEvents.forEach(game::addPostGameEvent);

        game.setWinningAlignment(winner);
        finalisePlayerIdentities();
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

    /*
     * Get the Event List for the current "Phase".
     *
     * In this context, "Phase" might be:
     *      -> preGameEvents
     *      -> postGameEvents
     *      -> dayPhase
     *      -> nightPhase
     */
    protected List<Event> getCurrentPhaseEventList() {
        if (!gameStarted)
            return preGameEvents;
        if (gameFinished)
            return postGameEvents;

        if (dayPhase)
            return dayGameEvents.get(days.peekLast());
        else
            return nightGameEvents.get(days.peekLast());
    }


    protected Day startDayPhase(int dayNumber, OffsetDateTime time) {
        Day lastDay = days.peekLast();
        if (Objects.nonNull(lastDay))
            lastDay.getNightPhase().setComplete(true);

        List<Event> newDayPhase = new ArrayList<>();
        DayPhase dayPhase = new DayPhase(newDayPhase);
        dayPhase.setStartTime(time);

        List<Event> newNightPhase = new ArrayList<>();
        NightPhase nightPhase = new NightPhase(newNightPhase);

        Day currentDay = new Day(dayNumber, dayPhase, nightPhase);

        dayGameEvents.put(currentDay, newDayPhase);
        nightGameEvents.put(currentDay, newNightPhase);
        getDays().addLast(currentDay);

        setCurrentCharacterInstances(dayPhase);
        setDayPhase(true);
        return currentDay;
    }

    protected Day startNightPhase(OffsetDateTime time) {
        Day currentDay = days.peekLast();
        if (Objects.isNull(currentDay)) {
            // Very rarely, games might start in the night phase...
            //   ... so immediately start and then end the day
            currentDay = startDayPhase(1, time);
        }

        playerContext.resetControlledCharacters();

        DayPhase dayPhase = currentDay.getDayPhase();
        dayPhase.setComplete(true);

        NightPhase nightPhase = currentDay.getNightPhase();
        nightPhase.setStartTime(time);
        setCurrentCharacterInstances(nightPhase);

        setDayPhase(false);
        return currentDay;
    }

    protected void setCurrentCharacterInstances(AbstractPhase phase) {
        PlayerContext playerContext = getPlayerContext();
        playerContext.allCharacters().stream()
                .map(playerContext::instanceForCharacter)
                .forEach(phase::setCharacterInstanceAtStart);
    }

    protected void finishGame(Alignment winningAlignment) {
        setWinningAlignment(winningAlignment);
        setGameFinished(true);
    }

    // Other Helpers

    protected Role getRole(String role) {
        return Role.forString(role);
    }

    protected void finalisePlayerIdentities() {
        playerContext.allUsers().forEach(user -> {
            Character character = playerContext.getCharacterFor(user);
            if (Objects.isNull(character))
                return;

            character.setUser(user);

            Role role = playerContext.getRoleForUser(user);
            character.setRole(role);
        });
    }

}
