package net.anomalyxii.werewolves.router.response.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.*;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.response.AbstractResponse;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Anomaly on 22/11/2016.
 */
public class GameResponse extends AbstractResponse<GameResponse.Body> {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public GameResponse(int statusCode, Body content) {
        super(statusCode, content);
    }

    // ******************************
    // Content Class
    // ******************************

    public static final class Body extends ArrayList<EventBody> implements AbstractResponse.Body {

        // Members

        // Constructors

        public Body() {
        }

        // Converters

        public Game toGame() {
            GameCreationContext context = new GameCreationContext();
            this.forEach(context::process);
            return context.build();
        }

    }

    public static final class EventBody {

        // Members

        private final Map<String, Object> data;

        // Constructors

        public EventBody(Map<String, Object> data) {
            this.data = data;
        }

        // Getters

        public String getPlayerName() {
            return (String) get("playerName");
        }

        public String getAvatarURL() {
            return (String) get("avatarUrl");
        }

        public String getType() {
            return (String) get("__type");
        }

        public String getTimestamp() {
            return (String) get("timeStamp");
        }

        public OffsetDateTime getTime() {
            String timestamp = getTimestamp();
            if (timestamp == null)
                return null;

            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime parsedDate = OffsetDateTime.parse(timestamp, formatter);
            return parsedDate;
        }

        @SuppressWarnings("unchecked")
        public <T> T get(String key) {
            return (T) data.get(key);
        }

        // Factory Methods

        @JsonCreator
        public static EventBody factory(Map<String, Object> map) {
            return new EventBody(map);
        }

    }

    public static class GameCreationContext {

        // Members

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

        // Process Methods

        public void process(EventBody eventBody) {

            // Lookup the player
            Player player = findOrCreatePlayer(eventBody.getPlayerName(),
                                               eventBody.getAvatarURL(),
                                               eventBody.getType());
            players.add(player);

            Day currentDay = days.peekLast();
            List<Event> currentPhase = gameStarted
                                       ? gameFinished
                                         ? postGameEvents
                                         : dayPhase
                                           ? dayGameEvents.get(currentDay)
                                           : nightGameEvents.get(currentDay)
                                       : preGameEvents;

            switch (eventBody.getType()) {

                // Message Events

                case "ModeratorMessage": // Word of God
                    currentPhase.add(new ModeratorMessage(eventBody.getTime(), eventBody.get("message")));
                    break;
                case "GhostMessage": // Deadchat
                    currentPhase.add(new GraveyardMessageEvent(player, eventBody.getTime(), eventBody.get("message")));
                    break;
                case "CovenNightMessage": // Covenchat
                    currentPhase.add(new CovenMessageEvent(player, eventBody.getTime(), eventBody.get("message")));
                    break;
                case "WerewolfNightMessage": // Wolfchat
                    currentPhase.add(new WerewolfMessageEvent(player, eventBody.getTime(), eventBody.get("message")));
                    break;
                case "VillageMessage": // Normalchat
                    currentPhase.add(new VillageMessageEvent(player, eventBody.getTime(), eventBody.get("message")));
                    break;

                // Role Events

                case "RoleAssigned":
                    Role role = getRole(eventBody.get("role"));
                    currentPhase.add(new RoleAssignedEvent(player, eventBody.getTime(), role));
                    break;

                case "AlphawolfEnraged":
                case "AlphawolfTargetChosen":
                case "AlphawolfUsedEnrage":
                case "BloodhoundRevertedToWerewolf":
                case "BloodhoundtargetChosen":
                case "GravediggerTargetChosen":
                case "GraverobberFoundNoRole":
                case "HarlotSawNoVisitors":
                case "HarlotSawVisit":
                case "HuntsmanGuarded":
                case "MessiahResurrected":
                case "MessiahUsedSacrifice":
                case "MilitiaUsedKill":
                case "ProtectorProtected":
                case "ProtectorTargetChosen":
                case "ReviverTargetChosen":
                case "RoleRevealedToBloodhound":
                case "RoleRevealedToGravedigger":
                case "RoleRevealedToSeer":
                case "SeerTargetMade":
                case "ShapeshifterAbilityActivated":
                case "StalkerSawNoVisit":
                case "StalkerSawVisit":
                case "UndeterminedRoleReaveledToGraveDigger":
                    break;


                // Game Phase Events

                case "PlayerJoined":
                    currentPhase.add(new PlayerJoinedEvent(player, eventBody.getTime()));
                    break;
                case "PlayerLeft":
                    currentPhase.add(new PlayerLeftEvent(player, eventBody.getTime()));
                    break;

                case "GameStarted":
                    // Todo: does this need to be an event??
                    //currentPhase.add(new GameStartedEvent(player, calendar));
                    gameStarted = true;
                    // Fall through!

                case "DayStarted":
                    if (!dayPhase) {
                        Day lastDay = days.peekLast();
                        if (lastDay != null)
                            lastDay.getNightPhase().setComplete(true);

                        dayPhase = true;
                        List<Event> newDayPhase = new ArrayList<>();
                        List<Event> newNightPhase = new ArrayList<>();
                        currentDay = new Day(new DayPhase(newDayPhase), new NightPhase(newNightPhase));
                        dayGameEvents.put(currentDay, newDayPhase);
                        nightGameEvents.put(currentDay, newNightPhase);
                        days.addLast(currentDay);

                        dayPhase = true;
                    }
                    break;

                case "NightStarted":
                    days.peekLast().getDayPhase().setComplete(true);
                    dayPhase = false;
                    break;

                case "AnonymisedGameStarted":
                case "GameSetToFixedLengthDayCycle":
                    break; // Set something in the context?

                case "VillageNominationsOpened":
                    break;

                case "VillageNomination":
                    Character targetCharacter = getCharacter(eventBody.get("target"));
                    currentPhase.add(new PlayerNominationEvent(player, eventBody.getTime(), targetCharacter));
                case "VillageNominationRetracted":
                    break; // Is this silent?

                case "PlayerKilled":
                    currentPhase.add(new PlayerKilledEvent(player, eventBody.getTime()));
                    break;
                case "PlayerLynched":
                    currentPhase.add(new PlayerLynchedEvent(player, eventBody.getTime()));
                    break;
                case "PlayerRevived":
                    currentPhase.add(new PlayerRevivedEvent(player, eventBody.getTime()));
                    break;
                case "PlayerSmited":
                    currentPhase.add(new PlayerSmitedEvent(player, eventBody.getTime()));
                    break;


                case "PendingGameMessage":
                    // This should always be in the post-game phases?
                    preGameEvents.add(new VillageMessageEvent(player, eventBody.getTime(), eventBody.get("message")));
                    break;

                case "PostGameMessage":
                    // This should always be in the post-game phases?
                    postGameEvents.add(new VillageMessageEvent(player, eventBody.getTime(), eventBody.get("message")));
                    break;

                case "CovenVictory":
                    winner = Alignment.COVEN;
                    assignCharactersAndRolesToPlayers(eventBody.get("werewolves"),
                                                      eventBody.get("coven"),
                                                      eventBody.get("villagers"),
                                                      eventBody.get("neutrals"));
                    gameFinished = true;
                    break;
                case "WerewolfVictory":
                    winner = Alignment.WEREWOLVES;
                    assignCharactersAndRolesToPlayers(eventBody.get("werewolves"),
                                                      eventBody.get("coven"),
                                                      eventBody.get("villagers"),
                                                      eventBody.get("neutrals"));
                    gameFinished = true;
                    break;
                case "VillageVictory":
                    winner = Alignment.VILLAGE;
                    assignCharactersAndRolesToPlayers(eventBody.get("werewolves"),
                                                      eventBody.get("coven"),
                                                      eventBody.get("villagers"),
                                                      eventBody.get("neutrals"));
                    gameFinished = true;
                    break;
                case "VampireVictory":
                    winner = Alignment.VAMPIRE;
                    assignCharactersAndRolesToPlayers(eventBody.get("werewolves"),
                                                      eventBody.get("coven"),
                                                      eventBody.get("villagers"),
                                                      eventBody.get("neutrals"));
                    gameFinished = true;
                    break;

                case "GameSpyJoined":
                case "HostRightsGranted":
                case "WarnedForInactivity":
                case "PlayerRoleRevealed":
                case "IdentitySwapped":
                case "NewIdentityAssigned":
                case "PlayerActiveDuringLastDay":
                case "WerewolfVote":
                case "NightTargetChosen":
                    break;

                case "JoinGame": // Not needed?
                    break;

                default:
                    return;

            }

        }

        // Build Methods

        public Game build() {
            Game game = new Game();
            players.forEach(game::addPlayer);

            preGameEvents.forEach(game::addPreGameEvent);
            days.forEach(game::addDay);
            postGameEvents.forEach(game::addPostGameEvent);

            game.setWinningAlignment(winner);
            return game;
        }

        // Helper Methods

        private Player findOrCreatePlayer(String name, String avatarUrl, String type) {

            if (name == null)
                return Player.MODERATOR;

            if (Player.MODERATOR.getName().equals(name))
                return Player.MODERATOR;

            PlayerLookupMode lookupMode = getLookupModeForEvent(type);
            switch (lookupMode) {

                case PREGAME:
                    return findOrCreateUser(name, avatarUrl);

                case INGAME_STRICT:
                    return getCharacter(name);

                case INGAME_LAX: {
                    Character character = findCharacter(name);
                    if (character != null)
                        return character;

                    User user = findUser(name); // Probably shouldn't happen?
                    if (user != null)
                        return user;

                    return findOrCreateCharacter(name, avatarUrl);
                }

                case POSTGAME: {
                    // Todo: make the below work properly for Lore
                    //     - although it probably won't; thanks Kirschstein :|

                    // If this is a post-game event, it's probably the Character
                    // identity of one of the players, but it could be an observer
                    // who will show using their User name instead
                    Character character = findCharacter(name);
                    if (character != null)
                        return character;

                    return findOrCreateUser(name, avatarUrl);
                }

                default:
                    throw new AssertionError("Should not happen");
            }

        }

        private User getUser(String name) {
            User user = findUser(name);
            if (user != null)
                return user;
            throw new IllegalArgumentException("User '" + name + "' was not found");
        }

        private User findUser(String name) {
            return users.get(name);
        }

        private User findOrCreateUser(String name, String avatarUrl) {
            User user = findUser(name);
            if (user != null)
                return user;

            URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

            user = new User(name, uri);
            users.put(name, user);
            return user;
        }

        private Character getCharacter(String name) {
            Character character = findCharacter(name);
            if (character != null)
                return character;
            throw new IllegalArgumentException("Character '" + name + "' was not found");
        }

        private Character findCharacter(String name) {
            return characters.get(name);
        }

        private Character findOrCreateCharacter(String name, String avatarUrl) {
            Character character = findCharacter(name);
            if (character != null)
                return character;

            URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

            character = new Character(name, uri);
            characters.put(name, character);
            return character;
        }

        private PlayerLookupMode getLookupModeForEvent(String eventType) {
            switch (eventType) {

                case "PendingGameMessage":
                case "RoleAssigned":
                    return PlayerLookupMode.PREGAME;

                case "PostGameMessage":
                    return PlayerLookupMode.POSTGAME;

                case "JoinGame":
                case "PlayerJoined":
                case "PlayerLeft":
                    return !gameStarted
                           ? PlayerLookupMode.PREGAME
                           : PlayerLookupMode.INGAME_LAX;

                default:
                    return PlayerLookupMode.INGAME_STRICT;

            }
        }

        private Role getRole(String role) {
            return Role.forString(role);
        }

        private void assignCharactersAndRolesToPlayers(List<Map<String, Object>> werewolves,
                                                       List<Map<String, Object>> coven,
                                                       List<Map<String, Object>> villagers,
                                                       List<Map<String, Object>> neutrals) {

            // This is a hack because apparently sometimes the API gets confused
            // Todo: try and persuade Kirschstein to fix the API so this can be removed
            List<Map<String, Object>> spareAssignments = new ArrayList<>();

            List<Map<String, Object>> all = new ArrayList<>();
            all.addAll(werewolves);
            all.addAll(coven);
            all.addAll(villagers);
            all.addAll(neutrals);

            all.forEach(player -> {
                String characterName = (String) player.get("playerName");
                String userName = (String) player.get("originalName");
                String roleName = (String) player.get("role");

                Character character = getCharacter(characterName);
                if (character.getUser() != null) {
                    // Character has already been assigned - add to "spare assignments"
                    spareAssignments.add(player);
                    return;
                }

                User user = getUser(userName);
                Role role = getRole(roleName);

                character.setUser(user);
                character.setRole(role);
            });

            characters.values()
                    .stream()
                    .filter(character -> character.getUser() == null)
                    .forEach(character -> {
                        Map<String, Object> player = spareAssignments.remove(0);

                        String userName = (String) player.get("originalName");
                        String roleName = (String) player.get("role");

                        User user = getUser(userName);
                        Role role = getRole(roleName);

                        character.setUserPossiblyIncorrectly(user);
                        character.setRolePossiblyIncorrectly(role);
                    });

        }

    }

    // ******************************
    // Static Helper Methods
    // ******************************

    public static DeserialisationCallback<GameResponse> deserialisation() {
        return (response, objectMapper) -> {
            // Construct the response object
            return new GameResponse(getStatusCode(response), deserialise(response, objectMapper, Body.class));
        };
    }

    // ******************************
    // Hacky McHackface Constants
    // ******************************

    private enum PlayerLookupMode {

        PREGAME,
        INGAME_STRICT,
        INGAME_LAX,
        POSTGAME,

        // End of constants
        ;

    }

}
