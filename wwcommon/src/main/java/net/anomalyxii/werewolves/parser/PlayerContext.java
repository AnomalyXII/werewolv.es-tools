package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.players.*;
import net.anomalyxii.werewolves.domain.players.Character;

import java.net.URI;
import java.util.*;

/**
 * Created by Anomaly on 07/01/2017.
 */
public class PlayerContext {

    // ******************************
    // Members
    // ******************************

    private final Set<Player> players = new LinkedHashSet<>();
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Character> characters = new HashMap<>();

    // Track the User <-> Character links
    private final Map<User, Character> userCharacterMap = new HashMap<>();
    private final Map<Character, User> characterUserMap = new HashMap<>();

    // Track the User <-> Role and Character <-> Vitality links
    //   - we track Character <-> Vitality as it's more reliable in "live" games
    private final Map<User, Role> userRoleMap = new HashMap<>();
    private final Map<Character, Vitality> characterVitalityMap = new HashMap<>();

    // Track the User <-> Character links for "controlling" Users (e.g. PuppetMaster)
    private final Map<User, Character> controlledUserCharacterMap = new HashMap<>();
    private final Map<Character, User> controlledCharacterUserMap = new HashMap<>();

    // ******************************
    // Getters
    // ******************************

    /**
     * Return all the {@link Player} instances
     * (either {@link User} or {@link Character})
     * that are being tracked by this context.
     * <p>
     * This <i>does not</i> include any
     * {@link SpecialPlayer} instances!
     *
     * @return all the {@link Player Players}
     */
    public Collection<Player> allPlayers() {
        return Collections.unmodifiableCollection(players);
    }

    /**
     * Return all the {@link User} instances
     * that are being tracked by this context.
     *
     * @return all the {@link User Users}
     */
    public Collection<User> allUsers() {
        return Collections.unmodifiableCollection(users.values());
    }

    /**
     * Return all the {@link Character} instances
     * that are being tracked by this context.
     *
     * @return all the {@link Character Characters}
     */
    public Collection<Character> allCharacters() {
        return Collections.unmodifiableCollection(characters.values());
    }

    /**
     * Return a {@link Map} that links all the
     * {@link User} instances with the currently
     * assigned {@link Character} instance.
     *
     * @return a {@link Map} of {@link User Users} to {@link Character Characters}
     */
    public Map<User, Character> allUsersWithCharacter() {
        return Collections.unmodifiableMap(userCharacterMap);
    }

    // ******************************
    // PlayerInstance Methods
    // ******************************

    public PlayerInstance instanceFor(Player player) {
        if (player == Player.MODERATOR)
            return PlayerInstance.MODERATOR;

        if (player == Player.ANONYMOUS)
            return PlayerInstance.ANONYMOUS;

        if (player instanceof User)
            return instanceForUser((User) player);
        if (player instanceof Character)
            return instanceForCharacter((Character) player);
        return null;
    }

    public PlayerInstance instanceForUser(User user) {
        Character character = getCharacterFor(user);
        if (character == null)
            return new UserInstance(user);

        return instanceForCharacter(character);
    }

    public PlayerInstance instanceForCharacter(Character character) {
        Vitality vitality = getVitalityForCharacter(character);

        User user = getUserFromCharacter(character);
        if (isCharacterBeingControlled(character)) {
            User controllingUser = getUserControlling(character);
            return new CharacterControlledInstance(character, controllingUser, user, vitality);
        }

        return new CharacterInstance(character, user, vitality);
    }

    // ******************************
    // Player Methods
    // ******************************

    public Player getSpecialPlayer(String name) {
        Player instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("SpecialPlayer '" + name + "' was not found");
    }

    public Player findSpecialPlayer(String name) {
        if (name == null)
            return SpecialPlayer.ANONYMOUS;

        if (Player.MODERATOR.getName().equals(name))
            return Player.MODERATOR;

        return null;
    }

    // ******************************
    // User Methods
    // ******************************

    public User getUser(String name) {
        User instance = findUser(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    public User findUser(String name) {
        User user = users.get(name);
        if (user != null)
            return user;

        return null;
    }

    public User findOrCreateUser(String name, String avatarUrl) {
        User instance = findUser(name);
        if (instance != null)
            return instance;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        User user = new User(name, uri);
        players.add(user);
        users.put(name, user);
        return user;
    }

    public Player getUserOrSpecialPlayer(String name) {
        Player instance = findUserOrSpecialPlayer(name);
        if (instance != null)
            return instance;

        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    public Player findUserOrSpecialPlayer(String name) {
        Player instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;

        return findUser(name);
    }

    public Player findOrCreateUserOrSpecialPlayer(String name, String avatarUrl) {
        Player player = findSpecialPlayer(name);
        if (player != null)
            return player;

        return findOrCreateUser(name, avatarUrl);
    }

    // ******************************
    // Character Methods
    // ******************************

    public Character getCharacter(String name) {
        Character instance = findCharacter(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    public Character findCharacter(String name) {
        Character character = characters.get(name);
        if (character != null)
            return character;

        return null;
    }

    public Character findOrCreateCharacter(String name, String avatarUrl) {
        Character instance = findCharacter(name);
        if (instance != null)
            return instance;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        Character character = new Character(name, uri);
        players.add(character);
        characters.put(name, character);
        return character;
    }

    public Player getCharacterOrSpecialPlayer(String name) {
        Player instance = findCharacterOrSpecialPlayer(name);
        if (instance != null)
            return instance;

        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    public Player findCharacterOrSpecialPlayer(String name) {
        Player instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;

        return findCharacter(name);
    }

    public Player findOrCreateCharacterOrSpecialPlayer(String name, String avatarUrl) {
        Player player = findSpecialPlayer(name);
        if (player != null)
            return player;

        return findOrCreateCharacter(name, avatarUrl);
    }

    // ******************************
    // Look-up Methods
    // ******************************

    /**
     * Look up the {@link Character} assigned
     * to a specific {@link User}.
     *
     * @param user the {@link User} to look up
     * @return the {@link Character} belonging to that {@link User}
     */
    public Character getCharacterFor(User user) {
        return userCharacterMap.get(user);
    }

    /**
     * Look up the {@link User} who is assigned
     * to a specific {@link Character}.
     *
     * @param character the {@link Character} to look up
     * @return the {@link User} assigned to that {@link Character}
     */
    public User getUserFromCharacter(Character character) {
        return characterUserMap.get(character);
    }

    /**
     * Look up the {@link Role} assigned to a
     * specific {@link User}.
     *
     * @param user the {@link User} to look up
     * @return the {@link Role} belonging to that {@link User}
     */
    public Role getRoleForUser(User user) {
        return userRoleMap.get(user);
    }

    /**
     * Look up the {@link Vitality} assigned to a
     * specific {@link User}.
     *
     * @param user the {@link User} to look up
     * @return the {@link Vitality} of that {@link User}
     */
    public Vitality getVitalityForUser(User user) {
        Character character = getCharacterFor(user);
        if (character == null)
            return Vitality.ALIVE; // ? Maybe??

        return characterVitalityMap.get(character);
    }

    /**
     * Look up the {@link Vitality} assigned to a
     * specific {@link Character}.
     *
     * @param character the {@link Character} to look up
     * @return the {@link Vitality} of that {@link User}
     */
    public Vitality getVitalityForCharacter(Character character) {
        // Default to ALIVE if we're not tracking?
        return characterVitalityMap.getOrDefault(character, Vitality.ALIVE);
    }

    /**
     * Map a {@link User} to a {@link Character}.
     *
     * @param user      the {@link User}
     * @param character the {@link Character}
     */
    public void assignCharacterToUser(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        assignCharacterToUser$(user, character);
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users}.
     *
     * @param first  the first {@link User}
     * @param second the second {@link User}
     */
    public void swapUserCharacters(User first, User second) {
        if (first == null || second == null)
            throw new IllegalArgumentException("User cannot be null");

        Character firstCharacter = getCharacterFor(first);
        Character secondCharacter = getCharacterFor(second);

        assignCharacterToUser$(first, secondCharacter);
        assignCharacterToUser$(second, firstCharacter);
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users} by swapping a given
     * {@link User} into a given {@link Character}.
     *
     * @param user      the {@link User} to swap
     * @param character the {@link Character} to become
     */
    public void swapUserIntoCharacter(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        Character firstCharacter = getCharacterFor(user);
        User secondUser = getUserFromCharacter(character);

        assignCharacterToUser$(user, character);
        assignCharacterToUser$(secondUser, firstCharacter);
    }

    /**
     * Map a {@link User} to a {@link Role}.
     *
     * @param user the {@link User}
     * @param role the {@link Role}
     */
    public void assignRoleToUser(User user, Role role) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (role == null)
            throw new IllegalArgumentException("Role cannot be null");

        userRoleMap.put(user, role);
    }

    /**
     * Map a {@link User} to a {@link Vitality}.
     *
     * @param user     the {@link User}
     * @param vitality the {@link Vitality}
     */
    public void assignVitalityToUser(User user, Vitality vitality) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");

        Character character = getCharacterFor(user);
        assignVitalityToCharacter(character, vitality);
    }

    /**
     * Map a {@link Character} to a {@link Vitality}.
     *
     * @param character the {@link Character}
     * @param vitality  the {@link Vitality}
     */
    public void assignVitalityToCharacter(Character character, Vitality vitality) {
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");
        if (vitality == null)
            throw new IllegalArgumentException("Vitality cannot be null");

        characterVitalityMap.put(character, vitality);
    }

    /**
     * Get the {@link Character} that a given
     * {@link User} is currently controlling.
     *
     * @param user the {@link User}
     * @return the {@link Character}, or {@literal null} if the {@link User} is not controlling anyone
     */
    public Character getControlledCharacterFor(User user) {
        return controlledUserCharacterMap.get(user);
    }

    /**
     * Get the {@link User} that a given
     * {@link Character} is currently
     * being controlled by.
     *
     * @param character the {@link Character} who is being controlled
     * @return the {@link User}, or {@literal null} if the {@link Character} is not being controlled
     */
    public User getUserControlling(Character character) {
        return controlledCharacterUserMap.get(character);
    }

    /**
     * Map a {@link User} to a {@link Character}
     * temporarily. This will be reset every day
     * at night fall.
     *
     * @param user      the {@link User} who will be controlling the target
     * @param character the {@link Character} who will be being controlled
     */
    public void assignControlOfCharacterToUser(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        // Reset first
        if (controlledUserCharacterMap.containsKey(user)) {
            Character previouslyControlledCharacter = controlledUserCharacterMap.remove(user);
            controlledCharacterUserMap.remove(previouslyControlledCharacter);
        }

        Character originalUserCharacter = getCharacterFor(user);
        if (!character.equals(originalUserCharacter)) {
            controlledUserCharacterMap.put(user, character);
            controlledCharacterUserMap.put(character, user);
        }
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users}.
     * <p>
     * This effect will only persist until nightfall,
     * at which point both users will return to their
     * original identities.
     *
     * @param controller the {@link User} who will be controlling the target
     * @param target     the {@link User} who will be controlled
     */
    public void assignControlOfUserToUser(User controller, User target) {
        if (controller == null || target == null)
            throw new IllegalArgumentException("User cannot be null");

        Character character = getCharacterFor(target);
        assignControlOfCharacterToUser(controller, character);
    }

    /**
     * Reset all the temporary swaps.
     */
    public void resetControlledCharacters() {
        controlledUserCharacterMap.clear();
        controlledCharacterUserMap.clear();
    }

    // ******************************
    // Public Helper Methods
    // ******************************

    /**
     * Check if the given {@link User}
     * has taken control of a different
     * {@link Character}.
     *
     * @param user the {@link User} to check
     * @return {@literal true} if the {@link User} has been temporarily swapped, {@literal false} otherwise
     */
    public boolean isUserControlling(User user) {
        return controlledUserCharacterMap.containsKey(user);
    }

    /**
     * Check if the given {@link Character}
     * has been taken control of by another
     * {@link User}.
     *
     * @param character the {@link Character} to check
     * @return {@literal true} if the {@link Character} has been temporarily swapped, {@literal false} otherwise
     */
    public boolean isCharacterBeingControlled(Character character) {
        return controlledCharacterUserMap.containsKey(character);
    }

    // ******************************
    // Protected Helper Methods
    // ******************************

    /**
     * Map a {@link User} to a {@link Character}.
     * <p>
     * One or both of <code>user</code> and
     * <code>character</code> may be null.
     *
     * @param user      the {@link User}
     * @param character the {@link Character}
     */
    protected void assignCharacterToUser$(User user, Character character) {
        userCharacterMap.put(user, character);
        characterUserMap.put(character, user);
    }

}
