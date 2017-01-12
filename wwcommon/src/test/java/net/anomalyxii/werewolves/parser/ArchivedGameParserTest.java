package net.anomalyxii.werewolves.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Game;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class ArchivedGameParserTest {

    // ******************************
    // Test Methods
    // ******************************

    @Test
    @SuppressWarnings("unchecked")
    public void parse_should_correctly_parse_an_archived_game() throws Exception {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("archive/ext-090.json")) {

            // arrange
            assertNotNull(in); // Sanity check

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> events = objectMapper.readValue(in, List.class);

            AbstractGameParser parser = new ArchivedGameParser();

            // act
            Game game = parser.parse(events);

            // assert
            assertNotNull(game);
            assertEquals(game.getUsers().size(), 20);
            //assertEquals(game.getCharacters().size(), 13);
            //assertEquals(game.getDays().size(), 4);
            assertEquals(game.getWinningAlignment(), Alignment.WEREWOLVES);

            // Pre- and Post-Game Events
            assertEquals(game.getPreGameEvents().size(), 95);
            assertEquals(game.getPostGameEvents().size(), 96);

            // In Game Events
            assertEquals(game.getDays().size(), 4);
            // Day 1 and Night 1
            assertEquals(game.getDay(0).getDayPhase().getEvents().size(), 44);
            assertTrue(game.getDay(0).getDayPhase().isComplete());
            assertEquals(game.getDay(0).getNightPhase().getEvents().size(), 45);
            assertTrue(game.getDay(0).getNightPhase().isComplete());
            // Day 1 and Night 2
            assertEquals(game.getDay(1).getDayPhase().getEvents().size(), 335);
            assertTrue(game.getDay(1).getDayPhase().isComplete());
            assertEquals(game.getDay(1).getNightPhase().getEvents().size(), 35);
            assertTrue(game.getDay(1).getNightPhase().isComplete());
            // Day 1 and Night 3
            assertEquals(game.getDay(2).getDayPhase().getEvents().size(), 354);
            assertTrue(game.getDay(2).getDayPhase().isComplete());
            assertEquals(game.getDay(2).getNightPhase().getEvents().size(), 155);
            assertTrue(game.getDay(2).getNightPhase().isComplete());
            // Day 1 and Night 4
            assertEquals(game.getDay(3).getDayPhase().getEvents().size(), 288);
            assertTrue(game.getDay(3).getDayPhase().isComplete());
            assertEquals(game.getDay(3).getNightPhase().getEvents().size(), 4);
            assertTrue(game.getDay(3).getNightPhase().isComplete());
        }
    }

}