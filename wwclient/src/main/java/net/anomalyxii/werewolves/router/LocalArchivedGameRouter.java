package net.anomalyxii.werewolves.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.parser.ArchivedGameParser;
import net.anomalyxii.werewolves.router.exceptions.RouterException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class LocalArchivedGameRouter extends LocalRouter {

    // ******************************
    // Members
    // ******************************

    private final String username;

    // ******************************
    // Constructors
    // ******************************

    public LocalArchivedGameRouter(String username) {
        this.username = username;
    }

    // ******************************
    // Router Methods
    // ******************************

    @Override
    public Game game(String id) throws RouterException {

        String gameResourcePath = "archive/" + id + ".json";
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(gameResourcePath)) {
            if (input == null)
                throw new RouterException("Game " + gameResourcePath + " was not found :(");

            return new ArchivedGameParser().parse(id, input);
        } catch (IOException e) {
            throw new RouterException(e);
        }

    }

}
