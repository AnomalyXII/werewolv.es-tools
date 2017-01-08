package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Role;
import net.anomalyxii.werewolves.domain.Vitality;
import net.anomalyxii.werewolves.domain.players.*;
import net.anomalyxii.werewolves.domain.players.Character;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Created by Anomaly on 07/01/2017.
 */
public class PlayerContextTest {

    // ******************************
    // Test Methods
    // ******************************

    // Get all Players, Users and Characters

    @Test
    public void allPlayers_should_return_users_and_characters() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        playerContext.findOrCreateUser("user1", "user1.png");
        playerContext.findOrCreateUser("user2", "user2.png");
        playerContext.findOrCreateUser("user3", "user3.png");
        playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.findOrCreateCharacter("character3", "character3.png");

        // act
        Collection<Player> players = playerContext.allPlayers();

        // assert
        assertNotNull(players);
        assertEquals(players.size(), 6);
    }

    @Test
    public void allUsers_should_return_only_users() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        playerContext.findOrCreateUser("user1", "user1.png");
        playerContext.findOrCreateUser("user2", "user2.png");
        playerContext.findOrCreateUser("user3", "user3.png");
        playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.findOrCreateCharacter("character3", "character3.png");

        // act
        Collection<User> users = playerContext.allUsers();

        // assert
        assertNotNull(users);
        assertEquals(users.size(), 3);
    }

    @Test
    public void allCharacters_should_return_only_characters() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        playerContext.findOrCreateUser("user1", "user1.png");
        playerContext.findOrCreateUser("user2", "user2.png");
        playerContext.findOrCreateUser("user3", "user3.png");
        playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.findOrCreateCharacter("character3", "character3.png");

        // act
        Collection<Character> characters = playerContext.allCharacters();

        // assert
        assertNotNull(characters);
        assertEquals(characters.size(), 3);
    }

    @Test
    public void allUsersWithCharacter_should_correctly_link_user_to_character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        User u3 = playerContext.findOrCreateUser("user3", "user3.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        Character c3 = playerContext.findOrCreateCharacter("character3", "character3.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUser(u3, c3);

        // act
        Map<User, Character> map = playerContext.allUsersWithCharacter();

        // assert
        assertNotNull(map);
        assertEquals(map.size(), 3);
        assertEquals(map.get(u1), c1);
        assertEquals(map.get(u2), c2);
        assertEquals(map.get(u3), c3);
    }

    // InstanceFor Methods

    @Test
    public void instanceFor_should_return_a_UserInstance_for_a_user_with_no_character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        PlayerInstance instance = playerContext.instanceFor(u1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof UserInstance);
        assertEquals(instance.getPlayer(), u1);
        assertEquals(instance.getUser(), u1);
        assertNull(instance.getCharacter());
    }

    @Test
    public void instanceFor_should_return_a_UserInstance_for_a_user_with_a_character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        PlayerInstance instance = playerContext.instanceFor(u1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getUser(), u1);
        assertEquals(instance.getCharacter(), c1);
    }

    @Test
    public void instanceFor_should_return_a_CharacterInstance_for_a_character_with_no_user() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        PlayerInstance instance = playerContext.instanceFor(c1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getCharacter(), c1);
        assertNull(instance.getUser());
    }

    @Test
    public void instanceFor_should_return_a_CharacterInstance_for_a_character_with_user() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        PlayerInstance instance = playerContext.instanceFor(c1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getCharacter(), c1);
        assertEquals(instance.getUser(), u1);
    }

    @Test
    public void instanceFor_should_return_a_CharacterControlledInstance_for_a_character_with_swapped_user() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.swapUserCharactersTemporarily(u1, u2);

        // act
        PlayerInstance instance = playerContext.instanceFor(c1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterControlledInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getCharacter(), c1);
        assertEquals(instance.getUser(), u2);
        assertEquals(((CharacterControlledInstance) instance).getOriginalUser(), u1);
    }

    @Test
    public void instanceForUser_should_return_a_UserInstance_for_a_user_with_no_character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        PlayerInstance instance = playerContext.instanceForUser(u1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof UserInstance);
        assertEquals(instance.getPlayer(), u1);
        assertEquals(instance.getUser(), u1);
        assertNull(instance.getCharacter());
    }

    @Test
    public void instanceForUser_should_return_a_UserInstance_for_a_user_with_a_character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        PlayerInstance instance = playerContext.instanceForUser(u1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getUser(), u1);
        assertEquals(instance.getCharacter(), c1);
    }

    @Test
    public void instanceForCharacter_should_return_a_CharacterInstance_for_a_character_with_no_user() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        PlayerInstance instance = playerContext.instanceForCharacter(c1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getCharacter(), c1);
        assertNull(instance.getUser());
    }

    @Test
    public void instanceForCharacter_should_return_a_CharacterInstance_for_a_character_with_user() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        PlayerInstance instance = playerContext.instanceForCharacter(c1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getCharacter(), c1);
        assertEquals(instance.getUser(), u1);
    }

    @Test
    public void instanceForCharacter_should_return_a_CharacterControlledInstance_for_a_character_with_swapped_user() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.swapUserCharactersTemporarily(u1, u2);

        // act
        PlayerInstance instance = playerContext.instanceForCharacter(c1);

        // assert
        assertNotNull(instance);
        assertTrue(instance instanceof CharacterControlledInstance);
        assertEquals(instance.getPlayer(), c1);
        assertEquals(instance.getCharacter(), c1);
        assertEquals(instance.getUser(), u2);
        assertEquals(((CharacterControlledInstance) instance).getOriginalUser(), u1);
    }

    // Get, Find and FindOrCreate Methods

    @Test
    public void getSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player sp1 = playerContext.getSpecialPlayer("Moderator");

        // assert
        assertNotNull(sp1);
        assertEquals(sp1, Player.MODERATOR);
    }

    @Test
    public void getSpecialPlayer_should_return_Anonymous_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player sp1 = playerContext.getSpecialPlayer(null);

        // assert
        assertNotNull(sp1);
        assertEquals(sp1, Player.ANONYMOUS);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getSpecialPlayer_should_throw_IllegalArgumentException_for_unknown() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        playerContext.getSpecialPlayer("FakeSpecialPlayerName");

        // assert
        fail("Should have thrown an exception");
    }

    @Test
    public void findSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player sp1 = playerContext.findSpecialPlayer("Moderator");

        // assert
        assertNotNull(sp1);
        assertEquals(sp1, Player.MODERATOR);
    }

    @Test
    public void findSpecialPlayer_should_return_Anonymous_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player sp1 = playerContext.findSpecialPlayer(null);

        // assert
        assertNotNull(sp1);
        assertEquals(sp1, Player.ANONYMOUS);
    }

    @Test
    public void findSpecialPlayer_should_return_null_for_unknown() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player sp1 = playerContext.findSpecialPlayer("FakeSpecialPlayerName");

        // assert
        assertNull(sp1);
    }

    @Test
    public void getUser_should_return_existing_User_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        User u1$ = playerContext.getUser("user1");

        // assert
        assertNotNull(u1$);
        assertEquals(u1$, u1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getUser_should_throw_IllegalArgumentException_for_unknown_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        playerContext.getUser("user1");

        // assert
        fail("Should have thrown an exception");
    }

    @Test
    public void findUser_should_return_existing_User_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        User u1$ = playerContext.findUser("user1");

        // assert
        assertNotNull(u1$);
        assertEquals(u1$, u1);
    }

    @Test
    public void findUser_should_return_null_for_unknown_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        User nullUser = playerContext.findUser("user1");

        // assert
        assertNull(nullUser);
    }

    @Test
    public void findOrCreateUser_should_return_create_new_User_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        User u1$ = playerContext.findOrCreateUser("user1", "user1.png");

        // assert
        assertNotNull(u1$);
        assertEquals(u1$.getName(), "user1");
        assertEquals(u1$.getAvatarURI(), URI.create("user1.png"));
    }

    @Test
    public void findOrCreateUser_should_return_existing_User_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        User u1$ = playerContext.findOrCreateUser("user1", "user1.png");

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == u1); // Should be the same instance!
    }

    @Test
    public void getUserOrSpecialPlayer_should_return_an_existing_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        Player u1$ = playerContext.getUserOrSpecialPlayer("user1");

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == u1); // Should be the same instance!
    }

    @Test
    public void getUserOrSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player u1$ = playerContext.getUserOrSpecialPlayer("Moderator");

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == Player.MODERATOR); // Should be the same instance!
    }

    @Test
    public void findUserOrSpecialPlayer_should_return_an_existing_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        Player u1$ = playerContext.findUserOrSpecialPlayer("user1");

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == u1); // Should be the same instance!
    }

    @Test
    public void findUserOrSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player u1$ = playerContext.findUserOrSpecialPlayer("Moderator");

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == Player.MODERATOR); // Should be the same instance!
    }

    @Test
    public void findOrCreateUserOrSpecialPlayer_should_return_an_existing_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        Player u1$ = playerContext.findOrCreateUserOrSpecialPlayer("user1", "user1.png");

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == u1); // Should be the same instance!
    }

    @Test
    public void findOrCreateUserOrSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player u1$ = playerContext.findOrCreateUserOrSpecialPlayer("Moderator", null);

        // assert
        assertNotNull(u1$);
        assertTrue(u1$ == Player.MODERATOR); // Should be the same instance!
    }

    @Test
    public void findOrCreateUserOrSpecialPlayer_should_return_create_new_User_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player u1$ = playerContext.findOrCreateUserOrSpecialPlayer("user1", "user1.png");

        // assert
        assertNotNull(u1$);
        assertEquals(u1$.getName(), "user1");
        assertEquals(u1$.getAvatarURI(), URI.create("user1.png"));
    }

    @Test
    public void getCharacter_should_return_existing_Character_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        Character c1$ = playerContext.getCharacter("character1");

        // assert
        assertNotNull(c1$);
        assertEquals(c1$, c1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void getCharacter_should_throw_IllegalArgumentException_for_unknown_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        playerContext.getCharacter("character1");

        // assert
        fail("Should have thrown an exception");
    }

    @Test
    public void findCharacter_should_return_existing_Character_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        Character c1$ = playerContext.findCharacter("character1");

        // assert
        assertNotNull(c1$);
        assertEquals(c1$, c1);
    }

    @Test
    public void findCharacter_should_return_null_for_unknown_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Character nullCharacter = playerContext.findCharacter("character1");

        // assert
        assertNull(nullCharacter);
    }

    @Test
    public void findOrCreateCharacter_should_return_create_new_Character_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Character c1$ = playerContext.findOrCreateCharacter("character1", "character1.png");

        // assert
        assertNotNull(c1$);
        assertEquals(c1$.getName(), "character1");
        assertEquals(c1$.getAvatarURI(), URI.create("character1.png"));
    }

    @Test
    public void findOrCreateCharacter_should_return_existing_Character_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        Character c1$ = playerContext.findOrCreateCharacter("character1", "character1.png");

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == c1); // Should be the same instance!
    }

    @Test
    public void getCharacterOrSpecialPlayer_should_return_an_existing_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        Player c1$ = playerContext.getCharacterOrSpecialPlayer("character1");

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == c1); // Should be the same instance!
    }

    @Test
    public void getCharacterOrSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player c1$ = playerContext.getCharacterOrSpecialPlayer("Moderator");

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == Player.MODERATOR); // Should be the same instance!
    }

    @Test
    public void findCharacterOrSpecialPlayer_should_return_an_existing_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        Player c1$ = playerContext.findCharacterOrSpecialPlayer("character1");

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == c1); // Should be the same instance!
    }

    @Test
    public void findCharacterOrSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player c1$ = playerContext.findCharacterOrSpecialPlayer("Moderator");

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == Player.MODERATOR); // Should be the same instance!
    }

    @Test
    public void findOrCreateCharacterOrSpecialPlayer_should_return_an_existing_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        Player c1$ = playerContext.findOrCreateCharacterOrSpecialPlayer("character1", "character1.png");

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == c1); // Should be the same instance!
    }

    @Test
    public void findOrCreateCharacterOrSpecialPlayer_should_return_Moderator_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player c1$ = playerContext.findOrCreateCharacterOrSpecialPlayer("Moderator", null);

        // assert
        assertNotNull(c1$);
        assertTrue(c1$ == Player.MODERATOR); // Should be the same instance!
    }

    @Test
    public void findOrCreateCharacterOrSpecialPlayer_should_return_create_new_Character_correctly() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        Player c1$ = playerContext.findOrCreateCharacterOrSpecialPlayer("character1", "character1.png");

        // assert
        assertNotNull(c1$);
        assertEquals(c1$.getName(), "character1");
        assertEquals(c1$.getAvatarURI(), URI.create("character1.png"));
    }

    // Character and User Linking Methods

    @Test
    public void getCharacterFor_should_return_an_assigned_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        Character character = playerContext.getCharacterFor(u1);

        // assert
        assertNotNull(character);
        assertTrue(character == c1);
    }

    @Test
    public void getCharacterFor_should_return_a_temporarily_assigned_Character_if_set() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUserTemporarily(u1, c2);

        // act
        Character character = playerContext.getCharacterFor(u1);

        // assert
        assertNotNull(character);
        assertTrue(character == c2);
    }

    @Test
    public void getUserFromCharacter_should_return_an_assigned_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        User user = playerContext.getUserFromCharacter(c1);

        // assert
        assertNotNull(user);
        assertTrue(user == u1);
    }

    @Test
    public void getUserFromCharacter_should_return_a_temporarily_assigned_User_if_Set() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUserTemporarily(u1, c2);

        // act
        User user = playerContext.getUserFromCharacter(c2);

        // assert
        assertNotNull(user);
        assertTrue(user == u1);
    }

    @Test
    public void getRoleForUser_should_return_correct_Role() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        playerContext.assignRoleToUser(u1, Role.SEER);

        // act
        Role role = playerContext.getRoleForUser(u1);

        // assert
        assertNotNull(role);
        assertEquals(role, Role.SEER);
    }

    @Test
    public void getVitalityForUser_should_return_correct_Vitality() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignVitalityToCharacter(c1, Vitality.DEAD);

        // act
        Vitality vitality = playerContext.getVitalityForUser(u1);

        // assert
        assertNotNull(vitality);
        assertEquals(vitality, Vitality.DEAD);
    }

    @Test
    public void getVitalityForCharacter_should_return_correct_Vitality() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignVitalityToCharacter(c1, Vitality.DEAD);

        // act
        Vitality vitality = playerContext.getVitalityForCharacter(c1);

        // assert
        assertNotNull(vitality);
        assertEquals(vitality, Vitality.DEAD);
    }

    @Test
    public void assignUserToCharacter_should_succeed_if_both_User_and_Character_are_set() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        playerContext.assignCharacterToUser(u1, c1);

        // assert
        assertNotNull(playerContext.getCharacterFor(u1)); // Tested in more rigor above!
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignUserToCharacter_should_throw_IllegalArgumentException_if_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        playerContext.assignCharacterToUser(null, c1);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignUserToCharacter_should_throw_IllegalArgumentException_if_Character_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.assignCharacterToUser(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void assignUserToCharacterTemporarily_should_succeed_if_both_User_and_Character_are_set() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);

        // act
        playerContext.assignCharacterToUserTemporarily(u1, c2);

        // assert
        assertNotNull(playerContext.getCharacterFor(u1)); // Tested in more rigor above!
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignUserToCharacterTemporarily_should_throw_IllegalArgumentException_if_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        playerContext.assignCharacterToUserTemporarily(null, c2);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignUserToCharacterTemporarily_should_throw_IllegalArgumentException_if_Character_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        playerContext.assignCharacterToUserTemporarily(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void swapUserCharacters_should_correctly_swap_two_Users() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);

        // act
        playerContext.swapUserCharacters(u1, u2);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c2);
        assertTrue(playerContext.getCharacterFor(u2) == c1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserCharacters_should_throw_IllegalArgumentException_when_first_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");

        // act
        playerContext.swapUserCharacters(null, u2);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserCharacters_should_throw_IllegalArgumentException_when_second_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.swapUserCharacters(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void swapUserIntoCharacter_should_correctly_swap_User_into_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);

        // act
        playerContext.swapUserIntoCharacter(u1, c2);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c2);
        assertTrue(playerContext.getCharacterFor(u2) == c1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserIntoCharacter_should_throw_IllegalArgumentException_when_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");

        // act
        playerContext.swapUserIntoCharacter(null, c2);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserIntoCharacter_should_throw_IllegalArgumentException_when_Character_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.swapUserCharacters(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void swapUserCharactersTemporarily_should_correctly_swap_two_Users() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);

        // act
        playerContext.swapUserCharactersTemporarily(u1, u2);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c2);
        assertTrue(playerContext.getCharacterFor(u2) == c1);
    }

    @Test
    public void swapUserCharactersTemporarily_should_reset_User_if_they_return_to_the_original_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        User u3 = playerContext.findOrCreateUser("user3", "user3.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        Character c3 = playerContext.findOrCreateCharacter("character3", "character3.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUser(u3, c3);

        // act
        playerContext.swapUserCharactersTemporarily(u1, u2);
        playerContext.swapUserCharactersTemporarily(u2, u3);
        playerContext.swapUserCharactersTemporarily(u3, u1);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c1);
        assertTrue(playerContext.getCharacterFor(u2) == c3);
        assertTrue(playerContext.getCharacterFor(u3) == c2);
        assertFalse(playerContext.isUserTemporarilySwapped(u1));
        assertTrue(playerContext.isUserTemporarilySwapped(u2));
        assertTrue(playerContext.isUserTemporarilySwapped(u3));
    }

    @Test
    public void swapUserCharactersTemporarily_should_reset_all_affected_Users_if_Users_are_the_same() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        User u3 = playerContext.findOrCreateUser("user3", "user3.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        Character c3 = playerContext.findOrCreateCharacter("character3", "character3.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUser(u3, c3);

        // act
        playerContext.swapUserCharactersTemporarily(u1, u2);
        playerContext.swapUserCharactersTemporarily(u1, u3);
        playerContext.swapUserCharactersTemporarily(u1, u1);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c1);
        assertTrue(playerContext.getCharacterFor(u2) == c2);
        assertTrue(playerContext.getCharacterFor(u3) == c3);
        assertFalse(playerContext.isUserTemporarilySwapped(u1));
        assertFalse(playerContext.isUserTemporarilySwapped(u2));
        assertFalse(playerContext.isUserTemporarilySwapped(u3));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserCharactersTemporarily_should_throw_IllegalArgumentException_when_first_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");

        // act
        playerContext.swapUserCharactersTemporarily(null, u2);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserCharactersTemporarily_should_throw_IllegalArgumentException_when_second_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.swapUserCharactersTemporarily(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void swapUserIntoCharacterTemporarily_should_correctly_swap_User_into_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);

        // act
        playerContext.swapUserIntoCharacterTemporarily(u1, c2);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c2);
        assertTrue(playerContext.getCharacterFor(u2) == c1);
    }


    @Test
    public void swapUserIntoCharacterTemporarily_should_reset_User_if_Character_is_original() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);

        // act
        playerContext.swapUserIntoCharacterTemporarily(u1, c2);
        playerContext.swapUserIntoCharacterTemporarily(u1, c1);

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c1);
        assertTrue(playerContext.getCharacterFor(u2) == c2);
        assertFalse(playerContext.isUserTemporarilySwapped(u1));
        assertFalse(playerContext.isUserTemporarilySwapped(u2));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserIntoCharacterTemporarily_should_throw_IllegalArgumentException_when_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");

        // act
        playerContext.swapUserIntoCharacterTemporarily(null, c2);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void swapUserIntoCharacterTemporarily_should_throw_IllegalArgumentException_when_Character_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.swapUserIntoCharacterTemporarily(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void assignRoleToUser_should_correctly_assign_Role_to_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.assignRoleToUser(u1, Role.SEER);

        // assert
        assertEquals(playerContext.getRoleForUser(u1), Role.SEER);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignRoleToUser_should_throw_IllegalArgumentException_if_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        playerContext.assignRoleToUser(null, Role.SEER);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignRoleToUser_should_throw_IllegalArgumentException_if_Role_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.assignRoleToUser(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void assignVitalityToUser_should_correctly_assign_Vitality_to_User() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        playerContext.assignCharacterToUser(u1, c1);

        // act
        playerContext.assignVitalityToUser(u1, Vitality.DEAD);

        // assert
        assertEquals(playerContext.getVitalityForUser(u1), Vitality.DEAD);
        assertEquals(playerContext.getVitalityForCharacter(c1), Vitality.DEAD);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignVitalityToUser_should_throw_IllegalArgumentException_if_User_is_not_linked_to_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.assignVitalityToUser(u1, Vitality.DEAD);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignVitalityToUser_should_throw_IllegalArgumentException_if_User_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        playerContext.assignVitalityToUser(null, Vitality.DEAD);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignRoleToUser_should_throw_IllegalArgumentException_if_Vitality_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");

        // act
        playerContext.assignVitalityToUser(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void assignVitalityToCharacter_should_correctly_assign_Vitality_to_Character() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character u1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        playerContext.assignVitalityToCharacter(u1, Vitality.DEAD);

        // assert
        assertEquals(playerContext.getVitalityForCharacter(u1), Vitality.DEAD);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignVitalityToCharacter_should_throw_IllegalArgumentException_if_Character_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();

        // act
        playerContext.assignVitalityToCharacter(null, Vitality.DEAD);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void assignRoleToCharacter_should_throw_IllegalArgumentException_if_Vitality_is_null() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        Character u1 = playerContext.findOrCreateCharacter("character1", "character1.png");

        // act
        playerContext.assignVitalityToCharacter(u1, null);

        // assert
        fail("Should have thrown an exception!");
    }

    @Test
    public void resetTemporarySwaps_should_reset_temporary_Character_assignments() {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.swapUserIntoCharacterTemporarily(u1, c2);
        assertTrue(playerContext.getCharacterFor(u1) == c2);
        assertTrue(playerContext.getCharacterFor(u2) == c1);

        // act
        playerContext.resetTemporarySwaps();

        // assert
        assertTrue(playerContext.getCharacterFor(u1) == c1);
        assertTrue(playerContext.getCharacterFor(u2) == c2);
    }

    // Miscellaneous Helper Methods

    @Test
    public void isUserTemporarilySwapped_should_correctly_identify_temporarily_swapped_users() throws Exception {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        User u3 = playerContext.findOrCreateUser("user3", "user3.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        Character c3 = playerContext.findOrCreateCharacter("character3", "character3.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUser(u3, c3);
        playerContext.swapUserCharactersTemporarily(u1, u2);

        // act & assert
        assertTrue(playerContext.isUserTemporarilySwapped(u1));
        assertTrue(playerContext.isUserTemporarilySwapped(u2));
        assertFalse(playerContext.isUserTemporarilySwapped(u3));
    }

    @Test
    public void isCharacterTemporarilySwapped_should_pass() throws Exception {
        // arrange
        PlayerContext playerContext = new PlayerContext();
        User u1 = playerContext.findOrCreateUser("user1", "user1.png");
        User u2 = playerContext.findOrCreateUser("user2", "user2.png");
        User u3 = playerContext.findOrCreateUser("user3", "user3.png");
        Character c1 = playerContext.findOrCreateCharacter("character1", "character1.png");
        Character c2 = playerContext.findOrCreateCharacter("character2", "character2.png");
        Character c3 = playerContext.findOrCreateCharacter("character3", "character3.png");
        playerContext.assignCharacterToUser(u1, c1);
        playerContext.assignCharacterToUser(u2, c2);
        playerContext.assignCharacterToUser(u3, c3);
        playerContext.swapUserCharactersTemporarily(u1, u2);

        // act & assert
        assertTrue(playerContext.isCharacterTemporarilySwapped(c1));
        assertTrue(playerContext.isCharacterTemporarilySwapped(c2));
        assertFalse(playerContext.isCharacterTemporarilySwapped(c3));
    }

    // ******************************
    // Helper Methods
    // ******************************

}