package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Game;
import org.testng.annotations.Test;

import java.net.URL;
import java.nio.file.Paths;

import static org.testng.Assert.*;

public class ArchivedGameParserTest {

    // ******************************
    // Test Methods
    // ******************************

    @Test
    @SuppressWarnings("unchecked")
    public void parse_should_correctly_parse_an_archived_game() throws Exception {
        // arrange
        URL resource = getClass().getClassLoader().getResource("archive/ext-035.json");
        assertNotNull(resource); // Sanity check

        AbstractGameParser parser = new ArchivedGameParser();

        // act
        Game game = parser.parse("ext-090", Paths.get(resource.toURI()));

        // assert
        assertNotNull(game);
        assertEquals(game.getId(), "ext-090");
        assertEquals(game.getUsers().size(), 17);
        assertEquals(game.getCharacters().size(), 15);
        assertEquals(game.getWinningAlignment(), Alignment.VILLAGE);

        // Pre- and Post-Game Events
        assertEquals(game.getPreGameEvents().size(), 243);
        assertEquals(game.getPostGameEvents().size(), 50);

        // In Game Events
        assertEquals(game.getDays().size(), 6);
        // Day 1 and Night 1
        assertEquals(game.getDay(0).getDayPhase().getEvents().size(), 54);
        assertTrue(game.getDay(0).getDayPhase().isComplete());
        assertEquals(game.getDay(0).getNightPhase().getEvents().size(), 18);
        assertTrue(game.getDay(0).getNightPhase().isComplete());
        // Day 2 and Night 2
        assertEquals(game.getDay(1).getDayPhase().getEvents().size(), 563);
        assertTrue(game.getDay(1).getDayPhase().isComplete());
        assertEquals(game.getDay(1).getNightPhase().getEvents().size(), 38);
        assertTrue(game.getDay(1).getNightPhase().isComplete());
        // Day 3 and Night 3
        assertEquals(game.getDay(2).getDayPhase().getEvents().size(), 467);
        assertTrue(game.getDay(2).getDayPhase().isComplete());
        assertEquals(game.getDay(2).getNightPhase().getEvents().size(), 31);
        assertTrue(game.getDay(2).getNightPhase().isComplete());
        // Day 4 and Night 4
        assertEquals(game.getDay(3).getDayPhase().getEvents().size(), 449);
        assertTrue(game.getDay(3).getDayPhase().isComplete());
        assertEquals(game.getDay(3).getNightPhase().getEvents().size(), 19);
        assertTrue(game.getDay(3).getNightPhase().isComplete());
        // Day 5 and Night 5
        assertEquals(game.getDay(4).getDayPhase().getEvents().size(), 387);
        assertTrue(game.getDay(4).getDayPhase().isComplete());
        assertEquals(game.getDay(4).getNightPhase().getEvents().size(), 79);
        assertTrue(game.getDay(4).getNightPhase().isComplete());
        // Day 6 and Night 6
        assertEquals(game.getDay(5).getDayPhase().getEvents().size(), 75);
        assertTrue(game.getDay(5).getDayPhase().isComplete());
        assertEquals(game.getDay(5).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(5).getNightPhase().isComplete());
    }

}