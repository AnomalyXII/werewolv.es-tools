package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import org.testng.annotations.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

import static org.testng.Assert.*;

public class LocalRouterTest {

    // ******************************
    // Test Methods
    // ******************************

    // games

    @Test
    public void games_should_throw_UnsupportedOperationException() throws Exception {
        // arrange
        URL liveDirectoryURL = getClass().getClassLoader().getResource("live");
        URL archiveDirectoryURL = getClass().getClassLoader().getResource("archive");
        assertNotNull(liveDirectoryURL);
        assertNotNull(archiveDirectoryURL);

        LocalRouter router = new LocalRouter(Paths.get(liveDirectoryURL.toURI()), Paths.get(archiveDirectoryURL.toURI()));

        // act
        GamesList gameList = router.games();

        // assert
        assertNotNull(gameList);
        assertEquals(gameList.getPendingGameIDs(), Collections.emptyList());
        assertEquals(gameList.getActiveGameIDs(), Collections.singletonList("ext-090"));
        assertEquals(gameList.getCompletedGameIDs(), Arrays.asList("ext-035", "ext-036", "ext-037"));
    }

    // game

    @Test
    public void game_should_return_game_from_resources() throws Exception {
        // arrange
        URL liveDirectoryURL = getClass().getClassLoader().getResource("live");
        URL archiveDirectoryURL = getClass().getClassLoader().getResource("archive");
        assertNotNull(liveDirectoryURL);
        assertNotNull(archiveDirectoryURL);

        LocalRouter router = new LocalRouter(Paths.get(liveDirectoryURL.toURI()), Paths.get(archiveDirectoryURL.toURI()));

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
        assertEquals(game.getDay(0).getDayPhase().getEvents().size(), 30);
        assertTrue(game.getDay(0).getDayPhase().isComplete());
        assertEquals(game.getDay(0).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(0).getNightPhase().isComplete());
        // Day 1 and Night 2
        assertEquals(game.getDay(1).getDayPhase().getEvents().size(), 333);
        assertTrue(game.getDay(1).getDayPhase().isComplete());
        assertEquals(game.getDay(1).getNightPhase().getEvents().size(), 3);
        assertTrue(game.getDay(1).getNightPhase().isComplete());
        // Day 1 and Night 3
        assertEquals(game.getDay(2).getDayPhase().getEvents().size(), 354);
        assertTrue(game.getDay(2).getDayPhase().isComplete());
        assertEquals(game.getDay(2).getNightPhase().getEvents().size(), 13);
        assertTrue(game.getDay(2).getNightPhase().isComplete());
        // Day 1 and Night 4
        assertEquals(game.getDay(3).getDayPhase().getEvents().size(), 288);
        assertTrue(game.getDay(3).getDayPhase().isComplete());
        assertEquals(game.getDay(3).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(3).getNightPhase().isComplete());
    }

}