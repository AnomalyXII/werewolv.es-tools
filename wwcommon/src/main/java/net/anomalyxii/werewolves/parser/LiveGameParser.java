package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.*;
import net.anomalyxii.werewolves.domain.events.message.*;
import net.anomalyxii.werewolves.domain.events.role.*;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

        // Constructors

        public GameCreationContext(String id) {
            super(id);
        }

        // GameContext Methods

        @Override
        public Event parseEvent(PlayerInstance player, OffsetDateTime timestamp, Map<String, Object> event) {

            PlayerContext playerContext = getPlayerContext();

            String type = parseType(event);
            switch (type) {

                // Identity Events

                case "NewIdentityAssigned":
                    Character newCharacter = getCharacter(event, "newPlayerName");
                    playerContext.assignCharacterToUser(getUser(event, "originalName"), newCharacter);
                    return new IdentityAssignedEvent(playerContext.instanceForCharacter(newCharacter), timestamp);
                case "IdentitySwapped":
                    Character newCharacterIdentity = getCharacter(event, "playerName");
                    playerContext.swapUserIntoCharacter(getUser(event, "originalName"), newCharacterIdentity);
                    return new IdentityAssignedEvent(playerContext.instanceForCharacter(newCharacterIdentity),
                                                     timestamp);

                // Message Events

                case "ModeratorMessage": // Word of God
                    return new ModeratorMessageEvent(timestamp, parseMessage(event));
                case "GhostMessage": // Deadchat
                    return new GraveyardMessageEvent(player, timestamp, parseMessage(event));
                case "CovenNightMessage": // Covenchat
                    return new CovenMessageEvent(player, timestamp, parseMessage(event));
                case "WerewolfNightMessage": // Wolfchat
                    return new WerewolfMessageEvent(player, timestamp, parseMessage(event));
                case "MasonNightMessage": // Masonchat
                    return new MasonMessageEvent(player, timestamp, parseMessage(event));
                case "VampireNightMessage": // Vampirechat
                    return new VampireMessageEvent(player, timestamp, parseMessage(event));
                case "VillageMessage": // Normalchat
                    return new VillageMessageEvent(player, timestamp, parseMessage(event));
                case "SpectatorMessage": // Spectatorchat
                    return new SpectatorMessageEvent(player, timestamp, parseMessage(event));

                // Role Events

                case "RoleAssigned":
                    Role role = getRole((String) event.get("role"));
                    return new RoleAssignedEvent(player, timestamp, role);

                case "NightTargetChosen":
                    PlayerInstance playerWithRole = getInstanceForUser(event, "playerWithRole");
                    PlayerInstance nightTarget = getInstanceForCharacter(event, "target");
                    Role playerRole = getRole((String) event.get("role"));
                    switch (playerRole) {
                        case HARLOT:
                            return new HarlotTargetChosenEvent(playerWithRole, timestamp, nightTarget);
                        case MILITIA:
                            return new MilitiaTargetChosenEvent(playerWithRole, timestamp, nightTarget);
                        case SEER:
                            return new SeerTargetChosenEvent(playerWithRole, timestamp, nightTarget);
                        case STALKER:
                            return new StalkerTargetChosenEvent(playerWithRole, timestamp, nightTarget);
                    }
                    return null;

                case "WerewolfVote":
                    return new WerewolfVoteEvent(getInstanceForCharacter(event, "werewolf"),
                                                 timestamp,
                                                 getInstanceForCharacter(event, "target"));

                case "AlphawolfEnraged":
                    return new AlphawolfEnragedEvent(player, timestamp);
                case "AlphawolfTargetChosen":
                    return new AlphawolfTargetChosenEvent(player, timestamp, getInstanceForCharacter(event, "target"));
                case "ShapeshifterAbilityActivated":
                case "BloodhoundtargetChosen":
                case "GravediggerTargetChosen":
                case "ProtectorTargetChosen":
                case "PuppetmasterSwapSelected":
                case "PuppetmasterSwapped":
                case "ReviverTargetChosen":
                case "SeerTargetMade": // Old?

                    // Role Outcome Events

                case "GraverobberFoundNoRole":
                    return null;
                case "HarlotSawNoVisitors":
                    return new HarlotSawVisitEvent(getInstanceForUser(event, "harlot"),
                                                   timestamp,
                                                   getInstanceForCharacter(event, "visitor"),
                                                   null);
                case "HarlotSawVisit":
                    return new HarlotSawVisitEvent(getInstanceForUser(event, "harlot"),
                                                   timestamp,
                                                   getInstanceForCharacter(event, "visitor"),
                                                   getInstanceForCharacter(event, "target"));
                case "MilitiaUsedKill":
                case "RoleRevealedToBloodhound":
                case "RoleRevealedToGravedigger":
                    return null;
                case "RoleRevealedToSeer":
                    return new SeerSawAlignmentEvent(getInstanceForUser(event, "seerName"),
                                                     timestamp,
                                                     getInstanceForCharacter(event, "target"),
                                                     Alignment.forMessageString((String) event.get("role")));
                case "StalkerSawNoVisit":
                    return new StalkerSawVisitEvent(getInstanceForUser(event, "stalker"),
                                                    timestamp,
                                                    getInstanceForCharacter(event, "visitor"),
                                                    null);
                case "StalkerSawVisit":
                    return new StalkerSawVisitEvent(getInstanceForUser(event, "stalker"),
                                                    timestamp,
                                                    getInstanceForCharacter(event, "visitor"),
                                                    getInstanceForCharacter(event, "target"));
                case "UndeterminedRoleReaveledToGraveDigger":
                    return null;

                case "AlphawolfUsedEnrage":
                case "HuntsmanGuarded":
                case "MessiahResurrected":
                case "MessiahUsedSacrifice":
                case "ProtectorProtected":
                    return null;

                // Other Special Role-related Events

                case "BloodhoundRevertedToWerewolf":

                case "VampireSacrificeMarked":
                    return new VampireSacrificeMarkedEvent(PlayerInstance.MODERATOR,
                                                           timestamp,
                                                           getInstanceForCharacter(event, "target"));

                    // Game Phase Events

                case "PlayerJoined":
                    if (player instanceof User)
                        ((User) player).setJoinedGame(true);

                    return new PlayerJoinedEvent(player, timestamp);
                case "PlayerLeft":
                    if (player instanceof User)
                        ((User) player).setJoinedGame(false);

                    return new PlayerLeftEvent(player, timestamp);

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
                    return new PlayerNominationEvent(player, timestamp, getInstanceForCharacter(event, "target"));
                case "VillageNominationRetracted":
                    return null; // Is this silent?

                case "PlayerKilled":
                    playerContext.assignVitalityToCharacter(player.getCharacter(), Vitality.DEAD);
                    return new PlayerKilledEvent(player, timestamp);
                case "PlayerLynched":
                    playerContext.assignVitalityToCharacter(player.getCharacter(), Vitality.DEAD);
                    return new PlayerLynchedEvent(player, timestamp);
                case "PlayerRevived":
                    playerContext.assignVitalityToCharacter(player.getCharacter(), Vitality.ALIVE);
                    return new PlayerRevivedEvent(player, timestamp);
                case "PlayerSmited":
                    playerContext.assignVitalityToCharacter(player.getCharacter(), Vitality.DEAD);
                    return new PlayerSmitedEvent(player, timestamp);


                case "PendingGameMessage":
                    // This should always be in the post-game phases?
                    return new VillageMessageEvent(player, timestamp, parseMessage(event));

                case "PostGameMessage":
                    // This should always be in the post-game phases?
                    return new VillageMessageEvent(player, timestamp, parseMessage(event));

                case "CovenVictory":
                    finishGame(Alignment.COVEN, event);
                    return null;
                case "VampireVictory":
                    finishGame(Alignment.VAMPIRES, event);
                    return null;
                case "VillageVictory":
                    finishGame(Alignment.VILLAGE, event);
                    return null;
                case "WerewolfVictory":
                    finishGame(Alignment.WEREWOLVES, event);
                    return null;

                case "GameSpyJoined":
                case "HostRightsGranted":
                    return null;
                case "WarnedForInactivity":
                    return new WarnedForInactivityEvent(player, timestamp);
                case "PlayerRoleRevealed":
                case "PlayerActiveDuringLastDay":
                    return null;

                case "JoinGame": // Not needed?
                    return null;

                default:
                    System.err.println(type);
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

            Player player;
            PlayerContext playerContext = getPlayerContext();
            LiveGameParser.PlayerLookupMode lookupMode = getLookupModeForEvent(type);
            switch (lookupMode) {

                case PREGAME:
                    player = playerContext.findOrCreateUserOrSpecialPlayer(name, avatarUrl);
                    break;

                case INGAME_STRICT:
                    player = playerContext.getCharacterOrSpecialPlayer(name);
                    break;

                case INGAME_LAX:
                    player = playerContext.findCharacterOrSpecialPlayer(name);
                    if (player != null)
                        break;

                    player = playerContext.findUser(name); // Probably shouldn't happen?
                    if (player != null)
                        break;

                    player = playerContext.findOrCreateCharacter(name, avatarUrl);

                case POSTGAME:
                    // Todo: make the below work properly for Lore
                    //     - although it probably won't; thanks Kirschstein :|

                    // If this is a post-game event, it's probably the Character
                    // identity of one of the players, but it could be an observer
                    // who will show using their User name instead
                    player = playerContext.findCharacterOrSpecialPlayer(name);
                    if (player != null)
                        break;

                    player = playerContext.findOrCreateUser(name, avatarUrl);
                    break;

                default:
                    throw new AssertionError("Should not happen");
            }

            return playerContext.instanceFor(player);

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
                case "GameSpyJoined":
                case "Spy":
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
            List<Map<String, Object>> demons = (List<Map<String, Object>>) event.get("demons");
            List<Map<String, Object>> other = (List<Map<String, Object>>) event.get("neutrals");
            assignCharactersAndRolesToPlayers(wolves, coven, villagers, demons, other);
        }

        private void assignCharactersAndRolesToPlayers(List<Map<String, Object>> werewolves,
                                                       List<Map<String, Object>> coven,
                                                       List<Map<String, Object>> villagers,
                                                       List<Map<String, Object>> demons,
                                                       List<Map<String, Object>> neutrals) {

            PlayerContext playerContext = getPlayerContext();

            // This is a hack because apparently sometimes the API gets confused
            // Todo: try and persuade Kirschstein to fix the API so this can be removed
            List<Map<String, Object>> spareAssignments = new ArrayList<>();

            List<Map<String, Object>> all = new ArrayList<>();

            if(!Objects.isNull(villagers))
                all.addAll(villagers);

            if(!Objects.isNull(werewolves))
                all.addAll(werewolves);

            if(!Objects.isNull(coven))
                all.addAll(coven);

            if(!Objects.isNull(demons))
                all.addAll(demons);

            if(!Objects.isNull(neutrals))
                all.addAll(neutrals);

            all.forEach(player -> {
                String characterName = (String) player.get("playerName");
                String userName = (String) player.get("originalName");
                String roleName = (String) player.get("role");

                Character character = playerContext.getCharacter(characterName);
                if (character.getUser() != null) {
                    // Character has already been assigned - add to "spare assignments"
                    spareAssignments.add(player);
                    return;
                }

                User user = playerContext.getUser(userName);
                Role role = getRole(roleName);
                playerContext.assignCharacterToUser(user, character);
                playerContext.assignRoleToUser(user, role);

                character.setUser(user);
                character.setRole(role);
            });

            playerContext.allCharacters()
                    .stream()
                    .filter(character -> character.getUser() == null)
                    .forEach(character -> {
                        if(spareAssignments.isEmpty())
                            return;

                        Map<String, Object> player = spareAssignments.remove(0);

                        String userName = (String) player.get("originalName");
                        String roleName = (String) player.get("role");

                        User user = playerContext.getUser(userName); // Todo: do this directly!
                        Role role = getRole(roleName);

                        character.setUserPossiblyIncorrectly(user);
                        character.setRolePossiblyIncorrectly(role);
                    });

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
