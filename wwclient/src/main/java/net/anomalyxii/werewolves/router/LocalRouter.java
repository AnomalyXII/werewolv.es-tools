package net.anomalyxii.werewolves.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.parser.LiveGameParser;
import net.anomalyxii.werewolves.router.exceptions.RouterException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class LocalRouter implements Router {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Router Methods
    // ******************************


    @Override
    public boolean login(String username, String password) throws RouterException {
        return true; // No authentication needed!
    }

    @Override
    public boolean oauth(String username, String password) throws RouterException {
        return true; // No authentication needed!
    }

    @Override
    public Games games() throws RouterException {
        throw new UnsupportedOperationException("Not implemented yet :(");
    }

    @Override
    public Game game(String id) throws RouterException {

        String gameResourcePath = "live/" + id + ".json";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(gameResourcePath)) {
            if (input == null)
                throw new RouterException("Game " + gameResourcePath + " was not found :(");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> events = objectMapper.readValue(input, List.class);
            return new LiveGameParser().parse(events);
        } catch (IOException e) {
            throw new RouterException(e);
        }

    }

}
