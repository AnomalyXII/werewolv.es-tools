package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.*;
import net.anomalyxii.werewolves.domain.events.message.*;
import net.anomalyxii.werewolves.domain.events.role.*;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse a {@link Game} taken from the archived {@code .json}.
 * <p>
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

        // Constructors

        public GameCreationContext(String id) {
            super(id);
        }


        // GameContext Methods

        @Override
        public Event parseEvent(PlayerInstance player, OffsetDateTime timestamp, Map<String, Object> event) {

            PlayerContext playerContext = getPlayerContext();

            String type = (String) event.get("__type");
            type = type.split(",")[0];

            switch (type) {

                // Identity Events
                case "Werewolf.GameEngine.Core.NewIdentityAssignedEvent":
                    User originalUser = playerContext.getUser((String) event.get("OriginalName"));
                    Character newCharacter = playerContext.findOrCreateCharacter((String) event.get("NewName"),
                                                                                 (String) event.get("NewAvatarUrl"));
                    playerContext.assignCharacterToUser(originalUser, newCharacter);
                    return new IdentityAssignedEvent(playerContext.instanceForCharacter(newCharacter), timestamp);

                case "Werewolf.GameEngine.Roles.Werewolves.Shapeshifter.ShapeshifterSwappedPlayerIdentities":
                case "Werewolf.GameEngine.Roles.Coven.Djinn.DjinnSwappedPlayerIdentities":
                    User firstPlayer = playerContext.getUser((String) event.get("FirstPlayer"));
                    User secondPlayer = playerContext.getUser((String) event.get("SecondPlayer"));
                    playerContext.swapUserCharacters(firstPlayer, secondPlayer);
                    return null;
                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetmasterSwapSelected":
                    return null;
                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetmasterSwapped":
                    User puppetMasterUser = playerContext.getUser((String) event.get("Puppetmaster"));
                    User puppetUser = playerContext.getUser((String) event.get("PlayerName"));
                    PlayerInstance puppetMasterInstance = playerContext.instanceFor(puppetMasterUser);
                    PlayerInstance puppetInstance = playerContext.instanceFor(puppetUser);
                    playerContext.assignControlOfUserToUser(puppetMasterUser, puppetUser);
                    return new IdentitySwappedIntoEvent(puppetMasterInstance, timestamp, puppetInstance);

                // Message Events
                case "Werewolf.GameEngine.Core.ModeratorMessageEvent":
                    return new ModeratorMessageEvent(timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Chatting.PendingGameMessage":
                case "Werewolf.GameEngine.Chatting.PostGameMessageEvent":
                    return new VillageMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Chatting.SpectatorMessageEvent":
                    return new VillageMessageEvent(player, timestamp, parseMessage(event)); // Todo?

                case "Werewolf.GameEngine.Chatting.GhostMessageEvent":
                    return new GraveyardMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Chatting.VillageMessageEvent":
                    return new VillageMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Roles.Village.Masons.MasonNightMessageEvent":
                    return new MasonMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Chatting.WerewolfNightMessageEvent":
                    return new WerewolfMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Chatting.CovenNightMessageEvent":
                    return new CovenMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Roles.Vampires.VampireNightMessageEvent":
                    return new VampireMessageEvent(player, timestamp, parseMessage(event));

                case "Werewolf.GameEngine.Roles.Demons.DemonNightMessageEvent":
                    return new DemonMessageEvent(player, timestamp, parseMessage(event));

                // Role Events

                case "Werewolf.GameEngine.Roles.AlphawolfAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.ALPHAWOLF);
                    return new RoleAssignedEvent(player, timestamp, Role.ALPHAWOLF);
                case "Werewolf.GameEngine.Roles.BloodhoundAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.BLOODHOUND);
                    return new RoleAssignedEvent(player, timestamp, Role.BLOODHOUND);
                case "Werewolf.GameEngine.Roles.BloodletterAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.BLOODLETTER);
                    return new RoleAssignedEvent(player, timestamp, Role.BLOODLETTER);
                case "Werewolf.GameEngine.Roles.DirewolfAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.DIREWOLF);
                    return new RoleAssignedEvent(player, timestamp, Role.DIREWOLF);
                case "Werewolf.GameEngine.Roles.ShapeshifterAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.SHAPESHIFTER);
                    return new RoleAssignedEvent(player, timestamp, Role.SHAPESHIFTER);
                case "Werewolf.GameEngine.Roles.WerewolfAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.WEREWOLF);
                    return new RoleAssignedEvent(player, timestamp, Role.WEREWOLF);
                case "Werewolf.GameEngine.Roles.AllWerewolvesAssignedEvent":
                    return null;

                case "Werewolf.GameEngine.Roles.AlchemistAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.ALCHEMIST);
                    return new RoleAssignedEvent(player, timestamp, Role.ALCHEMIST);
                case "Werewolf.GameEngine.Roles.DjinnAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.DJINN);
                    return new RoleAssignedEvent(player, timestamp, Role.DJINN);
                case "Werewolf.GameEngine.Roles.PuppetAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.PUPPET);
                    return new RoleAssignedEvent(player, timestamp, Role.PUPPET);
                case "Werewolf.GameEngine.Roles.PuppetmasterAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.PUPPETMASTER);
                    return new RoleAssignedEvent(player, timestamp, Role.PUPPETMASTER);
                case "Werewolf.GameEngine.Roles.ShamanAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.SHAMAN);
                    return new RoleAssignedEvent(player, timestamp, Role.SHAMAN);
                case "Werewolf.GameEngine.Roles.SuccubusAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.SUCCUBUS);
                    return new RoleAssignedEvent(player, timestamp, Role.SUCCUBUS);
                case "Werewolf.GameEngine.Roles.WitchAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.WITCH);
                    return new RoleAssignedEvent(player, timestamp, Role.WITCH);

                case "Werewolf.GameEngine.Roles.DemonlordAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.DEMONLORD);
                    return new RoleAssignedEvent(player, timestamp, Role.DEMONLORD);
                case "Werewolf.GameEngine.Roles.HellhoundAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.HELLHOUND);
                    return new RoleAssignedEvent(player, timestamp, Role.HELLHOUND);

                case "Werewolf.GameEngine.Roles.VampireAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.VAMPIRE);
                    return new RoleAssignedEvent(player, timestamp, Role.VAMPIRE);
                case "Werewolf.GameEngine.Roles.FamiliarStalkerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.FAMILIARSTALKER);
                    return new RoleAssignedEvent(player, timestamp, Role.FAMILIARSTALKER);

                case "Werewolf.GameEngine.Roles.VampireMasterAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.VAMPIREMASTER);
                    return new RoleAssignedEvent(player, timestamp, Role.VAMPIREMASTER);
                case "Werewolf.GameEngine.Roles.FamiliarGravediggerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.FAMILIARGRAVEDIGGER);
                    return new RoleAssignedEvent(player, timestamp, Role.FAMILIARGRAVEDIGGER);
                case "Werewolf.GameEngine.Roles.FamiliarMilitiaAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.FAMILIARMILITIA);
                    return new RoleAssignedEvent(player, timestamp, Role.FAMILIARMILITIA);

                case "Werewolf.GameEngine.Roles.BeholderAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.BEHOLDER);
                    return new RoleAssignedEvent(player, timestamp, Role.BEHOLDER);
                case "Werewolf.GameEngine.Roles.GravediggerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.GRAVEDIGGER);
                    return new RoleAssignedEvent(player, timestamp, Role.GRAVEDIGGER);
                case "Werewolf.GameEngine.Roles.GraverobberAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.GRAVEROBBER);
                    return new RoleAssignedEvent(player, timestamp, Role.GRAVEROBBER);
                case "Werewolf.GameEngine.Roles.HarlotAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.HARLOT);
                    return new RoleAssignedEvent(player, timestamp, Role.HARLOT);
                case "Werewolf.GameEngine.Roles.HuntsmanAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.HUNTSMAN);
                    return new RoleAssignedEvent(player, timestamp, Role.HUNTSMAN);
                case "Werewolf.GameEngine.Roles.InsomniacAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.INSOMNIAC);
                    return new RoleAssignedEvent(player, timestamp, Role.INSOMNIAC);
                case "Werewolf.GameEngine.Roles.LycanAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.LYCAN);
                    return new RoleAssignedEvent(player, timestamp, Role.LYCAN);
                case "Werewolf.GameEngine.Roles.MasonLeaderAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.MASONLEADER);
                    return new RoleAssignedEvent(player, timestamp, Role.MASONLEADER);
                case "Werewolf.GameEngine.Roles.MessiahAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.MESSIAH);
                    return new RoleAssignedEvent(player, timestamp, Role.MESSIAH);
                case "Werewolf.GameEngine.Roles.MilitiaAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.MILITIA);
                    return new RoleAssignedEvent(player, timestamp, Role.MILITIA);
                case "Werewolf.GameEngine.Roles.ProtectorAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.PROTECTOR);
                    return new RoleAssignedEvent(player, timestamp, Role.PROTECTOR);
                case "Werewolf.GameEngine.Roles.ReviverAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.REVIVER);
                    return new RoleAssignedEvent(player, timestamp, Role.REVIVER);
                case "Werewolf.GameEngine.Roles.SeerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.SEER);
                    return new RoleAssignedEvent(player, timestamp, Role.SEER);
                case "Werewolf.GameEngine.Roles.SleepwalkerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.SLEEPWALKER);
                    return new RoleAssignedEvent(player, timestamp, Role.SLEEPWALKER);
                case "Werewolf.GameEngine.Roles.StalkerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.STALKER);
                    return new RoleAssignedEvent(player, timestamp, Role.STALKER);
                case "Werewolf.GameEngine.Roles.TarotReaderAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.TAROTREADER);
                    return new RoleAssignedEvent(player, timestamp, Role.TAROTREADER);
                case "Werewolf.GameEngine.Roles.VillagerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.VILLAGER);
                    return new RoleAssignedEvent(player, timestamp, Role.VILLAGER);
                case "Werewolf.GameEngine.Roles.WitchHunterAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.WITCHHUNTER);
                    return new RoleAssignedEvent(player, timestamp, Role.WITCHHUNTER);
                case "Werewolf.GameEngine.Roles.ZealotAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.ZEALOT);
                    return new RoleAssignedEvent(player, timestamp, Role.ZEALOT);

                case "Werewolf.GameEngine.Roles.NaiveSeerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.NAIVE_SEER);
                    return new RoleAssignedEvent(player, timestamp, Role.NAIVE_SEER);
                case "Werewolf.GameEngine.Roles.ParanoidSeerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.PARANOID_SEER);
                    return new RoleAssignedEvent(player, timestamp, Role.PARANOID_SEER);
                case "Werewolf.GameEngine.Roles.InsaneSeerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.INSANE_SEER);
                    return new RoleAssignedEvent(player, timestamp, Role.INSANE_SEER);
                case "Werewolf.GameEngine.Roles.ChaoticSeerAssignedEvent":
                    playerContext.assignRoleToUser(player.getUser(), Role.CHAOTIC_SEER);
                    return new RoleAssignedEvent(player, timestamp, Role.CHAOTIC_SEER);

                case "Werewolf.GameEngine.Roles.Werewolves.Bloodhound.BloodhoundRevertedToWerewolfEvent":
                    playerContext.assignRoleToUser(playerContext.getUser((String) event.get("BloodhoundName")), Role.WEREWOLF);
                    return new RoleAssignedEvent(player, timestamp, Role.WEREWOLF);


                // Miscellaneous Role-based Voting Events

                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetVoted":
                    User puppetWhoVoted = playerContext.getUser((String) event.get("Puppet"));
                    User puppetVoteTarget = playerContext.getUser((String) event.get("Target"));
                    Character puppetVoteTargetCharacter = playerContext.getCharacterFor(puppetVoteTarget);
                    return new PlayerNominationEvent(playerContext.instanceForUser(puppetWhoVoted),
                                                     timestamp,
                                                     playerContext.instanceFor(puppetVoteTargetCharacter));
                case "Werewolf.GameEngine.Roles.Coven.Puppetmaster.PuppetVoteRetracted":
                    return null;

                case "Werewolf.GameEngine.Phases.Night.WerewolfVoteEvent": // Old?
                    return new WerewolfVoteEvent(getInstanceForUser(event, "Werewolf"),
                                                 timestamp,
                                                 getInstanceForUser(event, "Target"));

                // Use Ability Events

                case "Werewolf.GameEngine.Roles.NightTargetChosenEvent":
                    PlayerInstance playerWithRole = getInstanceForUser(event, "PlayerWithRole");
                    PlayerInstance target = getInstanceForUser(event, "Target");

                    Role roleForPlayer = playerContext.getRoleForUser(playerWithRole.getUser());
                    switch (roleForPlayer) {

                        // Vampire
                        case FAMILIARSTALKER:
                        case VAMPIRE:
                            return null;

                        // Village
                        case GRAVEDIGGER:
                            return new GraveDiggerTargetChosenEvent(playerWithRole, timestamp, target);
                        case GRAVEROBBER:
                            return new GraveRobberTargetChosenEvent(playerWithRole, timestamp, target);
                        case HARLOT:
                            return new HarlotTargetChosenEvent(playerWithRole, timestamp, target);
                        case HUNTSMAN:
                            return new HunstmanTargetChosenEvent(playerWithRole, timestamp, target);
                        case MESSIAH:
                            return new MessiahTargetChosenEvent(playerWithRole, timestamp, target);
                        case MILITIA:
                            return new MilitiaTargetChosenEvent(playerWithRole, timestamp, target);
                        case PROTECTOR:
                            return new ProtectorTargetChosenEvent(playerWithRole, timestamp, target);
                        case REVIVER:
                            return new ReviverTargetChosenEvent(playerWithRole, timestamp, target);
                        case SEER:
                            return new SeerTargetChosenEvent(playerWithRole, timestamp, target);
                        case STALKER:
                            return new StalkerTargetChosenEvent(playerWithRole, timestamp, target);

                        // Werewolves
                        case ALPHAWOLF:
                            return new AlphawolfTargetChosenEvent(player, timestamp, target);
                        case BLOODHOUND:
                            return new BloodhoundTargetChosenEvent(player, timestamp, target);

                    }
                    return null;

                case "Werewolf.GameEngine.Roles.PrimaryNightTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.SecondaryNightTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.Coven.Djinn.PrimaryDjinnTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Djinn.SecondaryDjinnTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.CovenMembersShownToShaman":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanLureTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanProtectionTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Succubus.PrimarySuccubusTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Succubus.SecondarySuccubusTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Witch.WitchReviveTargetChosen":
                case "Werewolf.GameEngine.Roles.Coven.Witch.WitchKillTargetChosen":
                case "Werewolf.GameEngine.Roles.Vampires.VampireSwitchedToKill":
                case "Werewolf.GameEngine.Roles.Vampires.VampireSwitchedToRecruit":
                    return null;
                case "Werewolf.GameEngine.Roles.Werewolves.Alphawolf.AlphawolfEnraged":
                    return new AlphawolfEnragedEvent(player, timestamp);
                case "Werewolf.GameEngine.Phases.Night.ShapeshifterAbilityActivated": // Old?
                    return null;

                // Old events - ignore?
                case "Werewolf.GameEngine.Roles.Village.Gravedigger.GravediggerTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.Village.Huntsman.HuntsmanTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.Village.Protector.ProtectorTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.Village.Reviver.ReviverTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.Village.Seer.SeerTargetChosenEvent":
                case "Werewolf.GameEngine.Roles.Werewolves.Alphawolf.AlphawolfTargetChosen":
                case "Werewolf.GameEngine.Roles.Werewolves.Bloodhound.BloodhoundTargetChosenEvent":
                    return null;

                // Ability Report Events

                case "Werewolf.GameEngine.Roles.Village.Beholder.BeholderSawNothing":
                case "Werewolf.GameEngine.Roles.Village.Gravedigger.RoleRevealedToGravediggerEvent":
                case "Werewolf.GameEngine.Roles.Village.Gravedigger.UndeterminedRoleReaveledToGraveDigger":
                case "Werewolf.GameEngine.Roles.Village.Graverobber.GraverobberFoundNoRoleEvent":
                    return null;
                case "Werewolf.GameEngine.Roles.Village.Harlot.HarlotSawNoVisitors":
                    return new HarlotSawVisitEvent(getInstanceForUser(event, "Harlot"),
                                                   timestamp,
                                                   getInstanceForUser(event, "Target"),
                                                   null);
                case "Werewolf.GameEngine.Roles.Village.Harlot.HarlotSawVisit":
                    return new HarlotSawVisitEvent(getInstanceForUser(event, "Harlot"),
                                                   timestamp,
                                                   getInstanceForUser(event, "Visitor"),
                                                   getInstanceForUser(event, "Target"));
                case "Werewolf.GameEngine.Roles.Village.Insomniac.InsomniacSawVisit":
                    return new InsomniacSawVisitEvent(getInstanceForUser(event, "Insomniac"),
                                                      timestamp,
                                                      getInstanceForUser(event, "Visitor"),
                                                      getInstanceForUser(event, "Target"));
                case "Werewolf.GameEngine.Roles.Village.Reviver.PlayerRevivedEvent":
                    playerContext.assignVitalityToUser(player.getUser(), Vitality.ALIVE);
                    return new PlayerRevivedEvent(player, timestamp);
                case "Werewolf.GameEngine.Roles.Village.Seer.RoleRevealedToSeerEvent":
                    return new SeerSawAlignmentEvent(getInstanceForUser(event, "SeerName"),
                                                     timestamp,
                                                     getInstanceForUser(event, "Target"),
                                                     Alignment.forMessageString((String) event.get("Role")));
                case "Werewolf.GameEngine.Roles.Village.Stalker.StalkerSawNoVisit":
                    return new StalkerSawVisitEvent(getInstanceForUser(event, "Stalker"),
                                                    timestamp,
                                                    getInstanceForUser(event, "Target"),
                                                    null);
                case "Werewolf.GameEngine.Roles.Village.Stalker.StalkerSawVisit":
                    return new StalkerSawVisitEvent(getInstanceForUser(event, "Stalker"),
                                                    timestamp,
                                                    getInstanceForUser(event, "Visitor"),
                                                    getInstanceForUser(event, "Target"));
                case "Werewolf.GameEngine.Roles.Village.TarotReader.RolesRevealedToTarotReader":
                case "Werewolf.GameEngine.Roles.Village.WitchHunter.MagicUserRevealedToWitchHunter":


                case "Werewolf.GameEngine.Roles.Werewolves.Bloodhound.RoleRevealedToBloodhoundEvent": // Todo

                case "Werewolf.GameEngine.Roles.Demons.Demonlord.BlightedPlayersShownToDemonLord": // Todo
                case "Werewolf.GameEngine.Roles.Village.Masons.MasonRecruited":
                case "Werewolf.GameEngine.Roles.Village.WitchHunter.MagicUsersShown":
                case "Werewolf.GameEngine.Roles.Village.WitchHunter.NonMagicUserRevealedToWitchHunter":
                    return null;
                case "Werewolf.GameEngine.Roles.Vampires.VampireSacrificeMarked":
                    return new VampireSacrificeMarkedEvent(PlayerInstance.MODERATOR,
                                                           timestamp,
                                                           getInstanceForUser(event, "Target"));

                // These are silent?
                case "Werewolf.GameEngine.Roles.Coven.Alchemist.AlchemistTransmutePotionUsed":
                case "Werewolf.GameEngine.Roles.Coven.Alchemist.AlchemistPurgePotionUsed":
                case "Werewolf.GameEngine.Roles.Coven.Alchemist.CovenMembersShownToAlchemist":
                case "Werewolf.GameEngine.Roles.Coven.Djinn.IdentitySwappedByDjinn":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanLureTotemUsed":
                case "Werewolf.GameEngine.Roles.Coven.Shaman.ShamanProtectionTotemBroken":
                case "Werewolf.GameEngine.Roles.Coven.Witch.WitchUsedKill":
                case "Werewolf.GameEngine.Roles.Coven.Witch.WitchUsedRevive":
                case "Werewolf.GameEngine.Roles.Demons.Demonlord.PlayerBlighted":
                case "Werewolf.GameEngine.Roles.Vampires.VampireRevived":
                case "Werewolf.GameEngine.Roles.Vampires.VampireMaster.FamiliarRecruitsAwardedToVampire":
                case "Werewolf.GameEngine.Roles.Village.Huntsman.HuntsmanGuardedEvent":
                case "Werewolf.GameEngine.Roles.Village.Masons.RemovedFromMasons":
                case "Werewolf.GameEngine.Roles.Village.Messiah.MessiahResurrected":
                case "Werewolf.GameEngine.Roles.Village.Messiah.MessiahUsedSacrifice":
                case "Werewolf.GameEngine.Roles.Village.Militia.MilitiaUsedKill":
                case "Werewolf.GameEngine.Roles.Village.Protector.ProtectorProtected":
                case "Werewolf.GameEngine.Roles.Village.Reviver.ReviverUsedAbility":
                case "Werewolf.GameEngine.Roles.Werewolves.Alphawolf.AlphawolfUsedEnrage":
                case "Werewolf.GameEngine.Roles.Werewolves.Shapeshifter.IdentitySwappedByShapeshifterEvent":
                case "Werewolf.GameEngine.Roles.RoleBlocked":
                case "Werewolf.GameEngine.Roles.RoleRevealedToPlayer":
                    // Todo: add an event with EventType.ROLE_ABILITY_USED
                    return null;

                // Game Phase Events

                case "Werewolf.GameEngine.Creation.PlayerJoinedEvent":
                    player.getUser().setJoinedGame(true);
                    if (isGameStarted()) {
                        // A user has joined the game whilst it's in progress...
                        // This is almost certainly due to Kirschstein messing about,
                        //   but it could cause problems later on so we need to fix it
                        User user = player.getUser();
                        Character character = playerContext.getCharacterFor(user);
                        if (Objects.isNull(character)) {
                            character = playerContext.findOrCreateCharacter(user.getName(), user.getAvatarURI().toString());
                            playerContext.assignCharacterToUser(user, character);
                        }
                    }
                    return null;
                case "Werewolf.GameEngine.Creation.PlayerLeftEvent":
                    player.getUser().setJoinedGame(false);
                    return null;

                case "Werewolf.GameEngine.Creation.GameStartedEvent":
                    setGameStarted(true);
                    // Fall through to DayStartedEvent!

                case "Werewolf.GameEngine.Phases.Day.DayStartedEvent":
                    Integer dayNumber = (Integer) event.getOrDefault("DayNumber", 1);
                    startDayPhase(dayNumber, timestamp);
                    return null;

                case "Werewolf.GameEngine.Phases.Night.NightStartedEvent":
                    startNightPhase(timestamp);
                    return null;

                case "Werewolf.GameEngine.Core.AnonymisedGameStartedEvent":
                case "Werewolf.GameEngine.Creation.GameSetToFixedLengthDayCycleEvent":
                case "Werewolf.GameEngine.Creation.GameSetToFourOneCycle":
                case "Werewolf.GameEngine.Creation.GameSetToEightTwoCycle":
                case "Werewolf.GameEngine.Creation.GameSetToTenFiveCycle":
                case "Werewolf.GameEngine.Creation.GameSetToThirtyThirtyLengthCycle":
                case "Werewolf.GameEngine.Creation.SpectatorLeftEvent":
                case "Werewolf.GameEngine.Creation.GameSpyLeftEvent":
                    return null; // Set something in the context?

                case "Werewolf.GameEngine.Phases.Day.VillageNominationsOpenedEvent":
                    return null;

                case "Werewolf.GameEngine.Phases.Day.VillageNominationEvent":
                    User user = playerContext.getUser((String) event.get("Target"));
                    Character targetCharacter = playerContext.getCharacterFor(user);
                    return new PlayerNominationEvent(player, timestamp, playerContext.instanceFor(targetCharacter));
                case "Werewolf.GameEngine.Phases.Day.VillageNominationRetractedEvent":
                    return null; // Is this silent?

                case "Werewolf.GameEngine.Phases.Night.PlayerKilledEvent":
                    playerContext.assignVitalityToUser(player.getUser(), Vitality.DEAD);
                    return new PlayerKilledEvent(player, timestamp);
                case "Werewolf.GameEngine.Phases.Day.PlayerLynchedEvent":
                    playerContext.assignVitalityToUser(player.getUser(), Vitality.DEAD);
                    return new PlayerLynchedEvent(player, timestamp);
                case "Werewolf.GameEngine.Phases.Day.PlayerSmitedEvent":
                    playerContext.assignVitalityToUser(player.getUser(), Vitality.DEAD);
                    return new PlayerSmitedEvent(player, timestamp, (String) event.get("Reason"));

                case "Werewolf.GameEngine.Phases.After.CovenVictoryEvent":
                    finishGame(Alignment.COVEN);
                    return null;
                case "Werewolf.GameEngine.Phases.After.VampireVictoryEvent":
                    finishGame(Alignment.VAMPIRES);
                    return null;
                case "Werewolf.GameEngine.Phases.After.VillageVictoryEvent":
                    finishGame(Alignment.VILLAGE);
                    return null;
                case "Werewolf.GameEngine.Phases.After.WerewolfVictoryEvent":
                    finishGame(Alignment.WEREWOLVES);
                    return null;

                // Other Events
                case "Werewolf.GameEngine.Core.PlayerRoleRevealedEvent":
                case "Werewolf.GameEngine.Creation.GameCreatedEvent":
                case "Werewolf.GameEngine.Creation.GameSpyJoinedEvent":
                case "Werewolf.GameEngine.Creation.SpectatorJoinedEvent":
                case "Werewolf.GameEngine.PlayerActivity.InactivitySmitingEnabled":
                    return null;
                case "Werewolf.GameEngine.PlayerActivity.WarnedForInactivity":
                    return new WarnedForInactivityEvent(player, timestamp);
                case "Werewolf.GameEngine.PlayerActivity.PlayerCheckedGame":
                case "Werewolf.GameEngine.PlayerActivity.PlayerActiveDuringLastDay":
                    return null;

                default:
                    System.err.println(type);
                    return null;

            }

        }

        // Parse Methods

        @Override
        protected PlayerInstance parsePlayerInstance(Map<String, Object> event) {
            PlayerContext playerContext = getPlayerContext();

            String playerName = (String) event.get("PlayerName");
            String avatarUrl = (String) event.get("PlayerName");
            Player player = playerContext.findOrCreateUserOrSpecialPlayer(playerName, avatarUrl);
            return playerContext.instanceFor(player);
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