package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.SpecialPlayer;
import net.anomalyxii.werewolves.domain.players.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Anomaly on 20/11/2016.
 */
public class Game {

    // ******************************
    // Members
    // ******************************

    private final List<User> users = new ArrayList<>();
    private final List<Character> characters = new ArrayList<>();
    private final List<Player> specialPlayers = new ArrayList<>();

    private final List<Event> preGameEvents = new ArrayList<>();
    private final List<Event> postGameEvents = new ArrayList<>();

    private final List<Day> days = new ArrayList<>();

    // ******************************
    // Constructors
    // ******************************

    public Game() {
    }

    // ******************************
    // Getters
    // ******************************

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

    public List<Character> getCharacters() {
        return Collections.unmodifiableList(characters);
    }

    public List<Event> getPreGameEvents() {
        return Collections.unmodifiableList(preGameEvents);
    }

    public List<Event> getPostGameEvents() {
        return Collections.unmodifiableList(postGameEvents);
    }

    public List<Day> getDays() {
        return Collections.unmodifiableList(days);
    }

    public Day getDay(int dayNo) {
        return days.get(dayNo - 1);
    }

    // ******************************
    // Setters
    // ******************************

    public void addPlayer(Player player) {
        if(player instanceof User)
            addUser((User) player);
        else if(player instanceof Character)
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
