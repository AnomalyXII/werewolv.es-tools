package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Game;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class LocalRouterTest {

    // ******************************
    // Test Methods
    // ******************************

    @Test
    public void login_should_always_return_true() throws Exception {
        // arrange
        LocalRouter router = new LocalRouter();

        // act & assert
        assertTrue(router.login("test", "test"));
    }

    @Test
    public void oauth_should_always_return_true() throws Exception {
        // arrange
        LocalRouter router = new LocalRouter();

        // act & assert
        assertTrue(router.login("test", "test"));
    }

    @Test(expectedExceptions = UnsupportedOperationException.class)
    public void games_should_throw_UnsupportedOperationException() throws Exception {
        // arrange
        LocalRouter router = new LocalRouter();

        // act
        router.games();

        // assert
        fail("Should have thrown an exception");
    }

    @Test
    public void login_should_return_game_from_resources() throws Exception {
        // arrange
        LocalRouter router = new LocalRouter();

        // act
        Game game = router.game("ext-090");

        // assert
        assertNotNull(game);
        assertEquals(game.getUsers().size(), 20);
        assertEquals(game.getCharacters().size(), 13);
        assertEquals(game.getDays().size(), 4);
        assertEquals(game.getWinningAlignment(), Alignment.WEREWOLVES);

        // Pre- and Post-Game
        assertEquals(game.getPreGameEvents().size(), 118);
        assertEquals(game.getPostGameEvents().size(), 96);
        // Day 1 and Night 1
        assertEquals(game.getDay(0).getDayPhase().getEvents().size(), 29);
        assertTrue(game.getDay(0).getDayPhase().isComplete());
        assertEquals(game.getDay(0).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(0).getNightPhase().isComplete());
        // Day 1 and Night 2
        assertEquals(game.getDay(1).getDayPhase().getEvents().size(), 333);
        assertTrue(game.getDay(1).getDayPhase().isComplete());
        assertEquals(game.getDay(1).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(1).getNightPhase().isComplete());
        // Day 1 and Night 3
        assertEquals(game.getDay(2).getDayPhase().getEvents().size(), 354);
        assertTrue(game.getDay(2).getDayPhase().isComplete());
        assertEquals(game.getDay(2).getNightPhase().getEvents().size(), 12);
        assertTrue(game.getDay(2).getNightPhase().isComplete());
        // Day 1 and Night 4
        assertEquals(game.getDay(3).getDayPhase().getEvents().size(), 288);
        assertTrue(game.getDay(3).getDayPhase().isComplete());
        assertEquals(game.getDay(3).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(3).getNightPhase().isComplete());
    }

}