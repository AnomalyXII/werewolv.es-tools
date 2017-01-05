package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;
import net.anomalyxii.werewolves.domain.events.*;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.CharacterInstance;
import net.anomalyxii.werewolves.domain.players.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class ArchivedGameParser extends AbstractGameParser {

    private static final Pattern TIMESTAMP_OFFSET_REGEXP = Pattern.compile("/Date\\(([0-9]+)([+][0-9]+)\\)/");

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public ArchivedGameParser() {
        super(GameCreationContext::new);
    }

    // ******************************
    // Context Class
    // ******************************

    private static class GameCreationContext extends GameContext {

        @Override
        public Event parseEvent(PlayerInstance player, Map<String, Object> event) {

            String type = (String) event.get("__type");
            type = type.split(",")[0];

            switch (type) {

                // Identity Events
                case "Werewolf.GameEngine.Core.NewIdentityAssignedEvent":
                    // Todo: add an event for this!
                    User originalUser = getUser((String) event.get("OriginalName")).getUser();
                    Character newCharacter = findOrCreateCharacter((String) event.get("NewName"),
                                                                   (String) event.get("NewAvatarUrl")).getCharacter();
                    assignUserToCharacter(originalUser, newCharacter);
                    return null;

                // Message Events
                case "Werewolf.GameEngine.Core.ModeratorMessageEvent":
                    return new ModeratorMessageEvent(parseTime(event), parseMessage(event));

                case "Werewolf.GameEngine.Chatting.PendingGameMessage":
                case "Werewolf.GameEngine.Chatting.PostGameMessageEvent":
                    return new VillageMessageEvent(parsePlayerInstance(event), parseTime(event), parseMessage(event));

                case "Werewolf.GameEngine.Chatting.GhostMessageEvent":
                    return new GraveyardMessageEvent(parsePlayerInstance(event), parseTime(event), parseMessage(event));

                case "Werewolf.GameEngine.Chatting.VillageMessageEvent":
                    return new VillageMessageEvent(parsePlayerInstance(event), parseTime(event), parseMessage(event));

                case "Werewolf.GameEngine.Chatting.WerewolfNightMessageEvent":
                    return new WerewolfMessageEvent(parsePlayerInstance(event), parseTime(event), parseMessage(event));

                case "Werewolf.GameEngine.Chatting.CovenNightMessageEvent":
                    return new CovenMessageEvent(parsePlayerInstance(event), parseTime(event), parseMessage(event));

                case "Werewolf.GameEngine.Roles.Vampires.VampireNightMessageEvent":
                    return new VampireMessageEvent(parsePlayerInstance(event), parseTime(event), parseMessage(event));

                // Role Events
                case "Werewolf.GameEngine.Roles.AllWerewolvesAssignedEvent":
                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetVoted":
                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetmasterSwapSelected":
                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetmasterSwapped":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.CovenMembersShownToShaman":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanLureTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanLureTotemUsed":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanProtectionTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Succubus.PrimarySuccubusTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Succubus.SecondarySuccubusTargetChosen":
                case "Werewolf.GameEngine.Roles.DjinnAssignedEvent":
                case "Werewolf.GameEngine.Roles.FamiliarStalkerAssignedEvent":
                case "Werewolf.GameEngine.Roles.GravediggerAssignedEvent":
                case "Werewolf.GameEngine.Roles.HarlotAssignedEvent":
                case "Werewolf.GameEngine.Roles.HuntsmanAssignedEvent":
                case "Werewolf.GameEngine.Roles.MessiahAssignedEvent":
                case "Werewolf.GameEngine.Roles.MilitiaAssignedEvent":
                case "Werewolf.GameEngine.Roles.NightTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.ProtectorAssignedEvent":
                case "Werewolf.GameEngine.Roles.PuppetAssignedEvent":
                case "Werewolf.GameEngine.Roles.PuppetmasterAssignedEvent":
                case "Werewolf.GameEngine.Roles.ReviverAssignedEvent":
                case "Werewolf.GameEngine.Roles.SeerAssignedEvent":
                case "Werewolf.GameEngine.Roles.ShamanAssignedEvent":
                case "Werewolf.GameEngine.Roles.ShapeshifterAssignedEvent":
                case "Werewolf.GameEngine.Roles.StalkerAssignedEvent":
                case "Werewolf.GameEngine.Roles.SuccubusAssignedEvent":
                case "Werewolf.GameEngine.Roles.VampireAssignedEvent":
                case "Werewolf.GameEngine.Roles.Vampires.VampireSwitchedToRecruit":
                case "Werewolf.GameEngine.Roles.Village.Gravedigger.RoleRevealedToGravediggerEvent":
                case "Werewolf.GameEngine.Roles.Village.Gravedigger.UndeterminedRoleReaveledToGraveDigger":
                case "Werewolf.GameEngine.Roles.Village.Harlot.HarlotSawNoVisitors":
                case "Werewolf.GameEngine.Roles.Village.Harlot.HarlotSawVisit":
                case "Werewolf.GameEngine.Roles.Village.Huntsman.HuntsmanGuardedEvent":
                case "Werewolf.GameEngine.Roles.Village.Messiah.MessiahResurrected":
                case "Werewolf.GameEngine.Roles.Village.Messiah.MessiahUsedSacrifice":
                case "Werewolf.GameEngine.Roles.Village.Militia.MilitiaUsedKill":
                case "Werewolf.GameEngine.Roles.Village.Protector.ProtectorProtected":
                case "Werewolf.GameEngine.Roles.Village.Reviver.PlayerRevivedEvent":
                case "Werewolf.GameEngine.Roles.Village.Seer.RoleRevealedToSeerEvent":
                case "Werewolf.GameEngine.Roles.Village.Stalker.StalkerSawNoVisit":
                case "Werewolf.GameEngine.Roles.Village.Stalker.StalkerSawVisit":
                case "Werewolf.GameEngine.Roles.WerewolfAssignedEvent":
                case "Werewolf.GameEngine.Roles.Werewolves.Shapeshifter.ShapeshifterSwappedPlayerIdentities":
                    return null;

                // Game Phase Events

                case "Werewolf.GameEngine.Creation.PlayerJoinedEvent":
                case "Werewolf.GameEngine.Creation.PlayerLeftEvent":
                    return null;

                case "Werewolf.GameEngine.Creation.GameStartedEvent":
                    setGameStarted(true);
                    // Fall through to DayStartedEvent!

                case "Werewolf.GameEngine.Phases.Day.DayStartedEvent":
                    startDayPhase();
                    return null;

                case "Werewolf.GameEngine.Phases.Night.NightStartedEvent":
                    startNightPhase();
                    return null;

                case "Werewolf.GameEngine.Phases.After.CovenVictoryEvent":
                    finishGame(Alignment.COVEN);
                    assignFinalUsersToCharacters();
                    return null;
                case "Werewolf.GameEngine.Phases.After.VillageVictoryEvent":
                    finishGame(Alignment.VILLAGE);
                    assignFinalUsersToCharacters();
                    return null;
                case "Werewolf.GameEngine.Phases.After.VampireVictoryEvent":
                    finishGame(Alignment.VAMPIRE);
                    assignFinalUsersToCharacters();
                    return null;
                case "Werewolf.GameEngine.Phases.After.WerewolfVictoryEvent":
                    finishGame(Alignment.WEREWOLVES);
                    assignFinalUsersToCharacters();
                    return null;

                // Other Events
                case "Werewolf.GameEngine.Core.AnonymisedGameStartedEvent":
                case "Werewolf.GameEngine.Creation.GameCreatedEvent":
                case "Werewolf.GameEngine.Creation.GameSetToFixedLengthDayCycleEvent":
                case "Werewolf.GameEngine.Creation.GameSpyJoinedEvent":
                case "Werewolf.GameEngine.Phases.Day.PlayerLynchedEvent":
                case "Werewolf.GameEngine.Phases.Day.PlayerSmitedEvent":
                case "Werewolf.GameEngine.Phases.Day.VillageNominationEvent":
                case "Werewolf.GameEngine.Phases.Day.VillageNominationRetractedEvent":
                case "Werewolf.GameEngine.Phases.Day.VillageNominationsOpenedEvent":
                case "Werewolf.GameEngine.Phases.Night.PlayerKilledEvent":
                case "Werewolf.GameEngine.Phases.Night.ShapeshifterAbilityActivated":
                case "Werewolf.GameEngine.Phases.Night.WerewolfVoteEvent":
                case "Werewolf.GameEngine.PlayerActivity.InactivitySmitingEnabled":
                case "Werewolf.GameEngine.PlayerActivity.WarnedForInactivity":

            }

            return null;

        }

        // Parse Methods

        @Override
        protected PlayerInstance parsePlayerInstance(Map<String, Object> event) {
            String playerName = (String) event.get("PlayerName");
            String avatarUrl = (String) event.get("PlayerName");
            PlayerInstance instance = findOrCreateUserOrSpecialPlayer(playerName, avatarUrl);
            if(instance.getUser() == null)
                return instance; // Probably a "Special Player"

            User user = instance.getUser();
            Character character = getCharacterFor(user);
            if(character == null)
                return instance; // Probably a user who isn't joined to the game?

            return new CharacterInstance(character, user, Vitality.ALIVE); // Todo: look this up!
        }

        @Override
        protected String parseType(Map<String, Object> event) {
            return (String) event.get("__type");
        }

        @Override
        protected OffsetDateTime parseTime(Map<String, Object> event) {
            String timestamp = (String) event.get("TimeStamp");
            if (timestamp == null)
                return null;

            Matcher matcher = TIMESTAMP_OFFSET_REGEXP.matcher(timestamp);
            if (!matcher.find())
                return null;

            long extractedTimestamp = Long.parseLong(matcher.group(1));
            Date date = new Date(extractedTimestamp);

            return date.toInstant().atOffset(ZoneOffset.of(matcher.group(2)));
        }

        @Override
        protected String parseMessage(Map<String, Object> event) {
            return (String) event.get("Message");
        }

    }

}