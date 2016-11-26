package net.anomalyxii.werewolves.router.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import net.anomalyxii.werewolves.domain.Day;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.events.*;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.SpecialPlayer;
import net.anomalyxii.werewolves.domain.players.User;
import net.anomalyxii.werewolves.router.DeserialisationCallback;

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

        private final String playerName;
        private final String avatarURL;
        private final String gameId;
        private final String timestamp;
        private final String type;
        private final String message;
        private final String target;
        //
        private final List<Event> nestedEvents;

        // Constructors

        public EventBody(String playerName, String avatarURL, String gameId,
                         String timestamp, String type, String message, String target) {
            this.playerName = playerName;
            this.avatarURL = avatarURL;
            this.gameId = gameId;
            this.timestamp = timestamp;
            this.type = type;
            this.message = message;
            this.target = target;

            this.nestedEvents = null;
        }

        public EventBody(List<Event> nestedEvents) {
            this.nestedEvents = nestedEvents;

            this.playerName = null;
            this.avatarURL = null;
            this.gameId = null;
            this.timestamp = null;
            this.type = null;
            this.message = null;
            this.target = null;
        }

        // Factory Methods

        @JsonCreator
        public static EventBody factory(Map<String, Object> map) {
            String playerName = (String) map.get("PlayerName");
            String avatarUrl = (String) map.get("AvatarUrl");
            String gameId = (String) map.get("GameId");
            String timestamp = (String) map.get("TimeStamp");
            String type = (String) map.get("__type");
            String message = (String) map.get("Message");
            String target = (String) map.get("Target");

            return new EventBody(playerName, avatarUrl, gameId, timestamp, type, message, target);
        }

    }

    public static class GameCreationContext {

        private static final Player MODERATOR = new SpecialPlayer("Moderator", null);

        // Members

        private boolean gameStarted = false;
        private boolean gameFinished = false;
        private boolean dayPhase = false;

        private final Set<Player> players = new LinkedHashSet<>();
        private final Map<String, User> users = new HashMap<>();
        private final Map<String, Character> characters = new HashMap<>();

        private final List<Event> preGameEvents = new ArrayList<>();
        private final List<Event> postGameEvents = new ArrayList<>();
        private final Deque<List<Event>> days = new ArrayDeque<>();

        // Process Methods

        public void process(EventBody eventBody) {

            Calendar calendar = Calendar.getInstance();

            // Todo: improve this!
            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime parsedDate = OffsetDateTime.parse(eventBody.timestamp, formatter);
            Date date = Date.from(parsedDate.toInstant());
            calendar.setTime(date);

            Player player = findOrCreatePlayer(eventBody.playerName, eventBody.avatarURL);
            players.add(player);

            List<Event> currentPhase = gameStarted
                                       ? gameFinished
                                         ? postGameEvents
                                         : days.peekLast()
                                       : preGameEvents;

            switch (eventBody.type) {

                case "VillageMessage":
                    currentPhase.add(new PlayerMessageEvent(player, calendar, eventBody.message));
                    break;

                case "PlayerJoined":
                    currentPhase.add(new PlayerJoinedEvent(player, calendar));
                    break;
                case "PlayerLeft":
                    currentPhase.add(new PlayerLeftEvent(player, calendar));
                    break;

                case "GameStarted":
                    currentPhase.add(new GameStartedEvent(player, calendar));
                    gameStarted = true;
                    dayPhase = true;
                    days.addLast(new ArrayList<>());
                    break;

                case "AnonymisedGameStarted":
                case "GameSetToFixedLengthDayCycle":
                    return; // Set something in the context?

                case "DayStarted":
                    if (!dayPhase) {
                        days.addLast(new ArrayList<>());
                        dayPhase = true;
                    }
                    return;
                case "NightStarted":
                    dayPhase = false;
                    return;

                case "VillageNominationsOpened":
                    return;

                case "VillageNomination":
                    Character targetCharacter = getCharacter(eventBody.target);
                    currentPhase.add(new PlayerNominationEvent(player, calendar, targetCharacter));
                case "VillageNominationRetracted":
                    break; // Is this silent?

                case "PlayerKilled":
                    currentPhase.add(new PlayerKilledEvent(player, calendar));
                    break;
                case "PlayerLynched":
                    currentPhase.add(new PlayerLynchedEvent(player, calendar));
                    break;

                case "PostGameMessage":
                    // This should always be in the post-game phase?
                    postGameEvents.add(new PlayerMessageEvent(player, calendar, eventBody.message));
                    break;

                case "GameSpyJoined":
                case "HostRightsGranted":
                case "ModeratorMessage":
                case "WarnedForInactivity":
                case "PlayerSmited":
                case "PlayerRoleRevealed":
                case "CovenVictory":
                    return;

                case "JoinGame": // Not needed?
                default:
                    return;

            }

        }

        // Build Methods

        public Game build() {
            Game game = new Game();
            players.forEach(game::addPlayer);
            preGameEvents.forEach(game::addPreGameEvent);
            postGameEvents.forEach(game::addPostGameEvent);
            days.stream().map(Day::new).forEach(game::addDay);
            return game;
        }

        // Helper Methods

        public Player findOrCreatePlayer(String name, String avatarUrl) {

            if (MODERATOR.getName().equals(name))
                return MODERATOR;

            if (!gameStarted)
                return findOrCreateUser(name, avatarUrl);
            else
                return findOrCreateCharacter(name, avatarUrl);

        }

        private User getUser(String name) {
            User user = findUser(name);
            if(user != null)
                return user;
            throw new IllegalArgumentException("User '" + name + "' was not found");
        }

        private User findUser(String name) {
            return users.get(name);
        }

        private User findOrCreateUser(String name, String avatarUrl) {
            User user = findUser(name);
            if(user != null)
                return user;

            URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

            user = new User(name, uri);
            users.put(name, user);
            return user;
        }

        private Character getCharacter(String name) {
            Character character = findCharacter(name);
            if(character != null)
                return character;
            throw new IllegalArgumentException("Character '" + name + "' was not found");
        }

        private Character findCharacter(String name) {
            return characters.get(name);
        }

        private Character findOrCreateCharacter(String name, String avatarUrl) {
            Character character = findCharacter(name);
            if(character != null)
                return character;

            URI uri = avatarUrl != null ? URI.create(avatarUrl) : null;

            character = new Character(name, uri);
            characters.put(name, character);
            return character;
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

}
