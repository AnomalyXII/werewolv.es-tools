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
    private final Map<User, Character> temporaryUserCharacterMap = new HashMap<>();
    private final Map<Character, User> characterUserMap = new HashMap<>();
    private final Map<Character, User> temporaryCharacterUserMap = new HashMap<>();

    // Track the User <-> Role and Character <-> Vitality links
    //   - we track Character <-> Vitality as it's more reliable in "live" games
    private final Map<User, Role> userRoleMap = new HashMap<>();
    private final Map<Character, Vitality> characterVitalityMap = new HashMap<>();

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

    protected PlayerInstance instanceFor(Player player) {
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

    protected PlayerInstance instanceForUser(User user) {
        Character character = getCharacterFor(user);
        if (character == null)
            return new UserInstance(user);

        return instanceForCharacter(character);
    }

    protected PlayerInstance instanceForCharacter(Character character) {
        Vitality vitality = getVitalityForCharacter(character);

        User user = getUserFromCharacter(character);
        if(isUserTemporarilySwapped(user)) {
            User originalUser = characterUserMap.get(character);
            return new CharacterControlledInstance(character, user, originalUser, vitality);
        }
        return new CharacterInstance(character, user, vitality);
    }

    // ******************************
    // Player Methods
    // ******************************

    protected Player getSpecialPlayer(String name) {
        Player instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("SpecialPlayer '" + name + "' was not found");
    }

    protected Player findSpecialPlayer(String name) {
        if (name == null)
            return SpecialPlayer.ANONYMOUS;

        if (Player.MODERATOR.getName().equals(name))
            return Player.MODERATOR;

        return null;
    }

    // ******************************
    // User Methods
    // ******************************

    protected User getUser(String name) {
        User instance = findUser(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    protected User findUser(String name) {
        User user = users.get(name);
        if (user != null)
            return user;

        return null;
    }

    protected User findOrCreateUser(String name, String avatarUrl) {
        User instance = findUser(name);
        if (instance != null)
            return instance;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        User user = new User(name, uri);
        players.add(user);
        users.put(name, user);
        return user;
    }

    protected Player getUserOrSpecialPlayer(String name) {
        Player instance = findUserOrSpecialPlayer(name);
        if (instance != null)
            return instance;

        throw new IllegalArgumentException("User '" + name + "' was not found");
    }

    protected Player findUserOrSpecialPlayer(String name) {
        Player instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;

        return findUser(name);
    }

    protected Player findOrCreateUserOrSpecialPlayer(String name, String avatarUrl) {
        Player player = findSpecialPlayer(name);
        if (player != null)
            return player;

        return findOrCreateUser(name, avatarUrl);
    }

    // ******************************
    // Character Methods
    // ******************************

    protected Character getCharacter(String name) {
        Character instance = findCharacter(name);
        if (instance != null)
            return instance;
        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    protected Character findCharacter(String name) {
        Character character = characters.get(name);
        if (character != null)
            return character;

        return null;
    }

    protected Character findOrCreateCharacter(String name, String avatarUrl) {
        Character instance = findCharacter(name);
        if (instance != null)
            return instance;

        URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

        Character character = new Character(name, uri);
        players.add(character);
        characters.put(name, character);
        return character;
    }

    protected Player getCharacterOrSpecialPlayer(String name) {
        Player instance = findCharacterOrSpecialPlayer(name);
        if (instance != null)
            return instance;

        throw new IllegalArgumentException("Character '" + name + "' was not found");
    }

    protected Player findCharacterOrSpecialPlayer(String name) {
        Player instance = findSpecialPlayer(name);
        if (instance != null)
            return instance;

        return findCharacter(name);
    }

    protected Player findOrCreateCharacterOrSpecialPlayer(String name, String avatarUrl) {
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
    protected Character getCharacterFor(User user) {
        if (temporaryUserCharacterMap.containsKey(user))
            return temporaryUserCharacterMap.get(user);
        return userCharacterMap.get(user);
    }

    /**
     * Look up the {@link User} who is assigned
     * to a specific {@link Character}.
     *
     * @param character the {@link Character} to look up
     * @return the {@link User} assigned to that {@link Character}
     */
    protected User getUserFromCharacter(Character character) {
        if (temporaryCharacterUserMap.containsKey(character))
            return temporaryCharacterUserMap.get(character);
        return characterUserMap.get(character);
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
     * Look up the {@link Vitality} assigned to a
     * specific {@link User}.
     *
     * @param user the {@link User} to look up
     * @return the {@link Vitality} of that {@link User}
     */
    protected Vitality getVitalityForUser(User user) {
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
    protected Vitality getVitalityForCharacter(Character character) {
        // Default to ALIVE if we're not tracking?
        return characterVitalityMap.getOrDefault(character, Vitality.ALIVE);
    }

    /**
     * Map a {@link User} to a {@link Character}.
     *
     * @param user      the {@link User}
     * @param character the {@link Character}
     */
    protected void assignCharacterToUser(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        userCharacterMap.put(user, character);
        characterUserMap.put(character, user);
    }

    /**
     * Map a {@link User} to a {@link Character}
     * temporarily. This will be reset every day
     * at night fall.
     *
     * @param user      the {@link User}
     * @param character the {@link Character}
     */
    protected void assignCharacterToUserTemporarily(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        if(userCharacterMap.containsKey(user)) {
            Character originalCharacter = userCharacterMap.get(user);
            if(character.equals(originalCharacter)) {
                temporaryUserCharacterMap.remove(user);
                temporaryCharacterUserMap.remove(character);
                return;
            }
        }

        temporaryUserCharacterMap.put(user, character);
        temporaryCharacterUserMap.put(character, user);
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users}.
     *
     * @param first  the first {@link User}
     * @param second the second {@link User}
     */
    protected void swapUserCharacters(User first, User second) {
        if (first == null || second == null)
            throw new IllegalArgumentException("User cannot be null");

        Character firstCharacter = getCharacterFor(first);
        Character secondCharacter = getCharacterFor(second);

        assignCharacterToUser(first, secondCharacter);
        assignCharacterToUser(second, firstCharacter);
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users} by swapping a given
     * {@link User} into a given {@link Character}.
     *
     * @param user      the {@link User} to swap
     * @param character the {@link Character} to become
     */
    protected void swapUserIntoCharacter(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        Character currentCharacter = getCharacterFor(user);
        User oldUserForNewCharacter = getUserFromCharacter(character);

        assignCharacterToUser(user, character);
        assignCharacterToUser(oldUserForNewCharacter, currentCharacter);
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users}.
     * <p>
     * This effect will only persist until nightfall,
     * at which point both users will return to their
     * original identities.
     *
     * @param first  the first {@link User}
     * @param second the second {@link User}
     */
    protected void swapUserCharactersTemporarily(User first, User second) {
        if (first == null || second == null)
            throw new IllegalArgumentException("User cannot be null");

        Character firstCharacter = getCharacterFor(first);
        Character secondCharacter = getCharacterFor(second);
        assignCharacterToUserTemporarily(first, secondCharacter);
        assignCharacterToUserTemporarily(second, firstCharacter);
    }

    /**
     * Swap the assigned identity ({@link Character})
     * of two {@link User Users} by swapping a given
     * {@link User} into a given {@link Character}.
     * <p>
     * This effect will only persist until nightfall,
     * at which point both users will return to their
     * original identities.
     *
     * @param user      the {@link User} to swap
     * @param character the {@link Character} to become
     */
    protected void swapUserIntoCharacterTemporarily(User user, Character character) {
        if (user == null)
            throw new IllegalArgumentException("User cannot be null");
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");

        Character currentCharacter = getCharacterFor(user);
        User oldUserForNewCharacter = getUserFromCharacter(character);

        assignCharacterToUserTemporarily(user, character);
        assignCharacterToUserTemporarily(oldUserForNewCharacter, currentCharacter);
    }

    /**
     * Map a {@link User} to a {@link Role}.
     *
     * @param user the {@link User}
     * @param role the {@link Role}
     */
    protected void assignRoleToUser(User user, Role role) {
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
    protected void assignVitalityToUser(User user, Vitality vitality) {
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
    protected void assignVitalityToCharacter(Character character, Vitality vitality) {
        if (character == null)
            throw new IllegalArgumentException("Character cannot be null");
        if (vitality == null)
            throw new IllegalArgumentException("Vitality cannot be null");

        characterVitalityMap.put(character, vitality);
    }

    /**
     * Reset all the temporary swaps.
     */
    protected void resetTemporarySwaps() {
        temporaryUserCharacterMap.clear();
        temporaryCharacterUserMap.clear();
    }

    // ******************************
    // Helper Methods
    // ******************************

    /**
     * Check if the given {@link User}
     * has been temporarily swapped.
     *
     * @param user the {@link User} to check
     * @return {@literal true} if the {@link User} has been temporarily swapped, {@literal false} otherwise
     */
    public boolean isUserTemporarilySwapped(User user) {
        return temporaryUserCharacterMap.containsKey(user);
    }

    /**
     * Check if the given {@link Character}
     * has been temporarily swapped.
     *
     * @param character the {@link Character} to check
     * @return {@literal true} if the {@link Character} has been temporarily swapped, {@literal false} otherwise
     */
    public boolean isCharacterTemporarilySwapped(Character character) {
        return temporaryCharacterUserMap.containsKey(character);
    }

}
