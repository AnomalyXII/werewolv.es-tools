package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.*;
import net.anomalyxii.werewolves.domain.players.Character;

import java.net.URI;
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

    private final Set<Player> players = new LinkedHashSet<>();
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Character> characters = new HashMap<>();

    // Track the events
    private final List<Event> preGameEvents = new ArrayList<>();
    private final List<Event> postGameEvents = new ArrayList<>();
    private final Deque<Day> days = new ArrayDeque<>();
    private final Map<Day, List<Event>> dayGameEvents = new HashMap<>();
    private final Map<Day, List<Event>> nightGameEvents = new HashMap<>();

    // Track the User <-> Character linkings and similar
    private final Map<User, Character> userCharacterMap = new HashMap<>();
    private final Map<Character, User> characterUserMap = new HashMap<>();
    private final Map<User, Vitality> userVitalityMap = new HashMap<>();
    private final Map<User, Role> userRoleMap = new HashMap<>();

    // ******************************
    // Process Methods
    // ******************************

    public void process(Map<String, Object> event) {

        // Lookup the player
        PlayerInstance player = parsePlayerInstance(event);
        players.add(player.getPlayer());

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

    protected abstract Event parseEvent(PlayerInstance player, Map<String, Object> event);

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

    protected abstract PlayerInstance parsePlayerInstance(Map<String, Object> event);

    protected abstract String parseType(Map<String, Object> event);

    protected abstract OffsetDateTime parseTime(Map<String, Object> event);

    protected abstract String parseMessage(Map<String, Object> event);

    // Look-up Players + Characters

    protected PlayerInstance findOrCreatePlayer(String name, String avatarUrl) {
        return isGameStarted()
               ? findOrCreateCharacter(name, avatarUrl)
               : findOrCreateUser(name, avatarUrl);
    }

    protected PlayerInstance getPlayer(String name) {
        return isGameStarted()
               ? getCharacter(name)
               : getUser(name);
    }

    protected PlayerInstance findPlayer(String name) {
        return isGameStarted()
               ? findCharacter(name)
               : findUser(name);
    }

    protected PlayerInstance getSpecialPlayer(String name) {
        PlayerInstance instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("SpecialPlayer '" + name + "' was not found");
    }

    protected PlayerInstance findSpecialPlayer(String name) {
        if (name == null)
            return SpecialPlayerInstance.ANONYMOUS;

        if (Player.MODERATOR.getName().equals(name))
            return PlayerInstance.MODERATOR;

        return null;
    }

    protected UserInstance getUser(String name) {
        UserInstance instance = findUser(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    protected UserInstance findUser(String name) {
        User user = users.get(name);
        if(user == null)
            return null;

        return new UserInstance(user);
    }

    protected UserInstance findOrCreateUser(String name, String avatarUrl) {
        UserInstance instance = findUser(name);
        if (instance != null)
            return instance;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        User user = new User(name, uri);
        users.put(name, user);
        return new UserInstance(user);
    }

    protected PlayerInstance getUserOrSpecialPlayer(String name) {
        PlayerInstance instance = findUserOrSpecialPlayer(name);
        if (instance != null)
            return instance;

        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    protected PlayerInstance findUserOrSpecialPlayer(String name) {
        PlayerInstance instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;

        return findUser(name);
    }

    protected PlayerInstance findOrCreateUserOrSpecialPlayer(String name, String avatarUrl) {
        PlayerInstance playerInstance = findSpecialPlayer(name);
        if (playerInstance != null)
            return playerInstance;

        return findOrCreateUser(name, avatarUrl);
    }

    protected CharacterInstance getCharacter(String name) {
        CharacterInstance instance = findCharacter(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    protected CharacterInstance findCharacter(String name) {
        Character character = characters.get(name);
        if (character == null)
            return null;

        User user = getUserFromCharacter(character);
        return new CharacterInstance(character, user, Vitality.ALIVE); // Todo: track this!
    }

    protected CharacterInstance findOrCreateCharacter(String name, String avatarUrl) {
        CharacterInstance instance = findCharacter(name);
        if (instance != null)
            return instance;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        Character character = new Character(name, uri);
        characters.put(name, character);

        User user = getUserFromCharacter(character);
        return new CharacterInstance(character, user, Vitality.ALIVE); // If it's new, it must be alive, right??
    }

    protected PlayerInstance getCharacterOrSpecialPlayer(String name) {
        PlayerInstance instance = findCharacterOrSpecialPlayer(name);
        if (instance != null)
            return instance;

        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    protected PlayerInstance findCharacterOrSpecialPlayer(String name) {
        PlayerInstance instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;

        return findCharacter(name);
    }

    protected PlayerInstance findOrCreateCharacterOrSpecialPlayer(String name, String avatarUrl) {
        PlayerInstance playerInstance = findSpecialPlayer(name);
        if (playerInstance != null)
            return playerInstance;

        return findOrCreateCharacter(name, avatarUrl);
    }

    // Look-up User Characters, Roles, Alignments, etc

    /**
     * Look up the {@link Character} assigned
     * to a specific {@link User}.
     *
     * @param user the {@link User} to look up
     * @return the {@link Character} belonging to that {@link User}
     */
    protected Character getCharacterFor(User user) {
        return userCharacterMap.get(user);
    }

    /**
     * Look up the {@link Role} assigned to a
     * specific {@link User}.
     *
     * @param user the {@link User} to look up
     * @return the {@link Role} belonging to that {@link User}
     */
    protected Role getRoleForUser(User user) {
        return userRoleMap.get(user);
    }

    /**
     * Look up the {@link User} who is assigned
     * to a specific {@link Character}.
     *
     * @param character the {@link Character} to look up
     * @return the {@link User} assigned to that {@link Character}
     */
    protected User getUserFromCharacter(Character character) {
        return characterUserMap.get(character);
    }

    /**
     * Map a {@link User} to a {@link Character}
     *
     * @param user the {@link User}
     * @param character the {@link Character}
     */
    protected void assignUserToCharacter(User user, Character character) {
        userCharacterMap.put(user, character);
        characterUserMap.put(character, user);
    }

    protected void swapUserCharacters(User first, User second) {
        Character firstCharacter = getCharacterFor(first);
        Character secondCharacter = getCharacterFor(second);

        assignUserToCharacter(first, secondCharacter);
        assignUserToCharacter(second, firstCharacter);
    }

    protected void swapUserIntoCharacter(User user, Character character) {
        Character currentCharacter = getCharacterFor(user);
        User oldUserForNewCharacter = getUserFromCharacter(character);

        assignUserToCharacter(user, character);
        assignUserToCharacter(oldUserForNewCharacter, currentCharacter);
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
        userCharacterMap.forEach((user, character) -> character.setUser(user));
    }

}
