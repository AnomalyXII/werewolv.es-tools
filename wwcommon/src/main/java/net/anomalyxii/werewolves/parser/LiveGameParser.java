package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Role;
import net.anomalyxii.werewolves.domain.events.*;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.CharacterInstance;
import net.anomalyxii.werewolves.domain.players.User;
import net.anomalyxii.werewolves.domain.players.UserInstance;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class LiveGameParser extends AbstractGameParser {

    // ******************************
    // Constructors
    // ******************************

    public LiveGameParser() {
        super(GameCreationContext::new);
    }

    // ******************************
    // Private Helper Methods
    // ******************************

    public static class GameCreationContext extends GameContext {

        // Process Methods

        @Override
        public Event parseEvent(PlayerInstance player, Map<String, Object> event) {

            String type = parseType(event);
            switch (type) {

                // Identity Events

                case "NewIdentityAssigned":
                    // Todo: add an event for this!
                    User originalUser = getUser((String) event.get("originalName")).getUser();
                    Character newCharacter = getCharacter((String) event.get("newPlayerName")).getCharacter();
                    assignUserToCharacter(originalUser, newCharacter);
                    return null;
                case "IdentitySwapped":
                    return null;

                // Message Events

                case "ModeratorMessageEvent": // Word of God
                    return new ModeratorMessageEvent(parseTime(event), parseMessage(event));
                case "GhostMessage": // Deadchat
                    return new GraveyardMessageEvent(player, parseTime(event), parseMessage(event));
                case "CovenNightMessage": // Covenchat
                    return new CovenMessageEvent(player, parseTime(event), parseMessage(event));
                case "WerewolfNightMessage": // Wolfchat
                    return new WerewolfMessageEvent(player, parseTime(event), parseMessage(event));
                case "VillageMessage": // Normalchat
                    return new VillageMessageEvent(player, parseTime(event), parseMessage(event));

                // Role Events

                case "RoleAssigned":
                    Role role = getRole((String) event.get("role"));
                    return new RoleAssignedEvent(player, parseTime(event), role);

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
                    return null;

                // Game Phase Events

                case "PlayerJoined":
                    if (player instanceof User)
                        ((User) player).setJoinedGame(true);

                    return new PlayerJoinedEvent(player, parseTime(event));
                case "PlayerLeft":
                    if (player instanceof User)
                        ((User) player).setJoinedGame(false);

                    return new PlayerLeftEvent(player, parseTime(event));

                case "GameStarted":
                    // Todo: does this need to be an event??
                    //currentPhase.add(new GameStartedEvent(player, calendar));
                    setGameStarted(true);
                    // Fall through!

                case "DayStarted":
                    if (!isDayPhase())
                        startDayPhase();
                    return null;

                case "NightStarted":
                    startNightPhase();
                    return null;

                case "AnonymisedGameStarted":
                case "GameSetToFixedLengthDayCycle":
                    return null; // Set something in the context?

                case "VillageNominationsOpened":
                    return null;

                case "VillageNomination":
                    // Todo: should this use an instance too??
                    Character targetCharacter = getCharacter((String) event.get("target")).getCharacter();
                    return new PlayerNominationEvent(player, parseTime(event), targetCharacter);
                case "VillageNominationRetracted":
                    return null; // Is this silent?

                case "PlayerKilled":
                    return new PlayerKilledEvent(player, parseTime(event));
                case "PlayerLynched":
                    return new PlayerLynchedEvent(player, parseTime(event));
                case "PlayerRevived":
                    return new PlayerRevivedEvent(player, parseTime(event));
                case "PlayerSmited":
                    return new PlayerSmitedEvent(player, parseTime(event));


                case "PendingGameMessage":
                    // This should always be in the post-game phases?
                    return new VillageMessageEvent(player, parseTime(event), parseMessage(event));

                case "PostGameMessage":
                    // This should always be in the post-game phases?
                    return new VillageMessageEvent(player, parseTime(event), parseMessage(event));

                case "CovenVictory":
                    finishGame(Alignment.COVEN, event);
                    return null;
                case "VampireVictory":
                    finishGame(Alignment.VAMPIRE, event);
                    return null;
                case "VillageVictory":
                    finishGame(Alignment.VILLAGE, event);
                    return null;
                case "WerewolfVictory":
                    finishGame(Alignment.WEREWOLVES, event);
                    return null;

                case "GameSpyJoined":
                case "HostRightsGranted":
                case "WarnedForInactivity":
                case "PlayerRoleRevealed":
                case "PlayerActiveDuringLastDay":
                case "WerewolfVote":
                case "NightTargetChosen":
                    return null;

                case "JoinGame": // Not needed?
                    return null;

                default:
                    return null;

            }

        }

        @Override
        protected PlayerInstance parsePlayerInstance(Map<String, Object> event) {
            String playerName = (String) event.get("playerName");
            String avatarUrl = (String) event.get("avatarUrl");
            String type = parseType(event);

            return findOrCreatePlayer(playerName, avatarUrl, type);
        }

        @Override
        protected String parseType(Map<String, Object> event) {
            return (String) event.get("__type");
        }

        @Override
        protected OffsetDateTime parseTime(Map<String, Object> event) {
            String timestamp = (String) event.get("timeStamp");
            if (timestamp == null)
                return null;

            DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            return OffsetDateTime.parse(timestamp, formatter);
        }

        @Override
        protected String parseMessage(Map<String, Object> event) {
            return (String) event.get("message");
        }

        // Helper Methods

        protected PlayerInstance findOrCreatePlayer(String name, String avatarUrl, String type) {

            LiveGameParser.PlayerLookupMode lookupMode = getLookupModeForEvent(type);
            switch (lookupMode) {

                case PREGAME:
                    return findOrCreateUserOrSpecialPlayer(name, avatarUrl);

                case INGAME_STRICT:
                    return getCharacterOrSpecialPlayer(name);

                case INGAME_LAX: {
                    PlayerInstance player = findCharacterOrSpecialPlayer(name);
                    if (player != null)
                        return player;

                    UserInstance user = findUser(name); // Probably shouldn't happen?
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
                    PlayerInstance player = findCharacterOrSpecialPlayer(name);
                    if (player != null)
                        return player;

                    return findOrCreateUser(name, avatarUrl);
                }

                default:
                    throw new AssertionError("Should not happen");
            }

        }

        private LiveGameParser.PlayerLookupMode getLookupModeForEvent(String eventType) {
            switch (eventType) {

                case "PendingGameMessage":
                case "RoleAssigned":
                    return LiveGameParser.PlayerLookupMode.PREGAME;

                case "PostGameMessage":
                    return LiveGameParser.PlayerLookupMode.POSTGAME;

                case "JoinGame":
                case "PlayerJoined":
                case "PlayerLeft":
                    return !isGameStarted()
                           ? LiveGameParser.PlayerLookupMode.PREGAME
                           : LiveGameParser.PlayerLookupMode.INGAME_LAX;

                default:
                    return LiveGameParser.PlayerLookupMode.INGAME_STRICT;

            }
        }

        private void finishGame(Alignment winner, Map<String, Object> event) {
            finishGame(winner);

            List<Map<String, Object>> wolves = (List<Map<String, Object>>) event.get("werewolves");
            List<Map<String, Object>> coven = (List<Map<String, Object>>) event.get("coven");
            List<Map<String, Object>> villagers = (List<Map<String, Object>>) event.get("villagers");
            List<Map<String, Object>> other = (List<Map<String, Object>>) event.get("neutrals");
            assignCharactersAndRolesToPlayers(wolves, coven, villagers, other);
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

                Character character = getCharacter(characterName).getCharacter(); // Todo: do this directly!
                if (character.getUser() != null) {
                    // Character has already been assigned - add to "spare assignments"
                    spareAssignments.add(player);
                    return;
                }

                User user = getUser(userName).getUser(); // Todo: do this directly!
                Role role = getRole(roleName);

                character.setUser(user);
                character.setRole(role);
            });

            getCharacters().values()
                    .stream()
                    .filter(character -> character.getUser() == null)
                    .forEach(character -> {
                        Map<String, Object> player = spareAssignments.remove(0);

                        String userName = (String) player.get("originalName");
                        String roleName = (String) player.get("role");

                        User user = getUser(userName).getUser(); // Todo: do this directly!
                        Role role = getRole(roleName);

                        character.setUserPossiblyIncorrectly(user);
                        character.setRolePossiblyIncorrectly(role);
                    });

        }

    }

    public static final class EventBody extends HashMap<String, Object> {

        // Constructors

        public EventBody(Map<String, Object> data) {
            super(data);
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
            return (T) super.get(key);
        }

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
