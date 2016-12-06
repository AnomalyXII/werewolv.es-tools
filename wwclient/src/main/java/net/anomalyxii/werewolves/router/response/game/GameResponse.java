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

        public Calendar getCalendar() {
            String timestamp = getTimestamp();
            if (timestamp == null)
                return null;

            Calendar calendar = Calendar.getInstance();

            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            OffsetDateTime parsedDate = OffsetDateTime.parse(timestamp, formatter);
            Date date = Date.from(parsedDate.toInstant());
            calendar.setTime(date);

            return calendar;
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
            Player player = findOrCreatePlayer(eventBody.getPlayerName(), eventBody.getAvatarURL());
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
                    currentPhase.add(new ModeratorMessage(eventBody.getCalendar(), eventBody.get("message")));
                    break;
                case "GhostMessage": // Deadchat
                    currentPhase.add(
                            new GraveyardMessageEvent(player, eventBody.getCalendar(), eventBody.get("message")));
                    break;
                case "CovenNightMessage": // Covenchat
                    currentPhase.add(new CovenMessageEvent(player, eventBody.getCalendar(), eventBody.get("message")));
                    break;
                case "WerewolfNightMessage": // Wolfchat
                    currentPhase.add(
                            new WerewolfMessageEvent(player, eventBody.getCalendar(), eventBody.get("message")));
                    break;
                case "VillageMessage": // Normalchat
                    currentPhase.add(
                            new VillageMessageEvent(player, eventBody.getCalendar(), eventBody.get("message")));
                    break;

                // Role Events

                case "RoleAssigned":
                    Role role = Role.forString(eventBody.get("role"));
                    currentPhase.add(new RoleAssignedEvent(player, eventBody.getCalendar(), role));
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
                    currentPhase.add(new PlayerJoinedEvent(player, eventBody.getCalendar()));
                    break;
                case "PlayerLeft":
                    currentPhase.add(new PlayerLeftEvent(player, eventBody.getCalendar()));
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
                    currentPhase.add(new PlayerNominationEvent(player, eventBody.getCalendar(), targetCharacter));
                case "VillageNominationRetracted":
                    break; // Is this silent?

                case "PlayerKilled":
                    currentPhase.add(new PlayerKilledEvent(player, eventBody.getCalendar()));
                    break;
                case "PlayerLynched":
                    currentPhase.add(new PlayerLynchedEvent(player, eventBody.getCalendar()));
                    break;
                case "PlayerRevived":
                    currentPhase.add(new PlayerRevivedEvent(player, eventBody.getCalendar()));
                    break;
                case "PlayerSmited":
                    currentPhase.add(new PlayerSmitedEvent(player, eventBody.getCalendar()));
                    break;


                case "PendingGameMessage":
                    // This should always be in the post-game phases?
                    preGameEvents.add(
                            new VillageMessageEvent(player, eventBody.getCalendar(), eventBody.get("message")));
                    break;

                case "PostGameMessage":
                    // This should always be in the post-game phases?
                    postGameEvents.add(
                            new VillageMessageEvent(player, eventBody.getCalendar(), eventBody.get("message")));
                    break;

                case "CovenVictory":
                    winner = Alignment.COVEN;
                    gameFinished = true;
                    break;
                case "WerewolfVictory":
                    winner = Alignment.WEREWOLVES;
                    gameFinished = true;
                    break;
                case "VillageVictory":
                    winner = Alignment.VILLAGE;
                    gameFinished = true;
                    break;
                case "VampireVictory":
                    // Lol like this'll ever happen...
                    winner = Alignment.VAMPIRE;
                    gameFinished = true;
                    break;

                case "GameSpyJoined":
                case "HostRightsGranted":
                case "WarnedForInactivity":
                case "PlayerRoleRevealed":
                case "IdentitySwapped":
                case "NewIdentityAssigned":
                case "PlayerActiveDuringLastDay":
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

        private Player findOrCreatePlayer(String name, String avatarUrl) {

            if (Player.MODERATOR.getName().equals(name))
                return Player.MODERATOR;

            if (!gameStarted)
                return findOrCreateUser(name, avatarUrl);
            else
                return findOrCreateCharacter(name, avatarUrl);

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
