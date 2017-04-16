package net.anomalyxii.werewolves.router.response.game;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.parser.LiveGameParser;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.response.AbstractResponse;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Anomaly on 22/11/2016.
 */
public class GameResponse extends AbstractResponse<GameResponse.Body> {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public GameResponse(int statusCode, Body content) {
        super(statusCode, content);
    }

    // ******************************
    // Content Class
    // ******************************

    public static final class Body extends ArrayList<Map<String, Object>> implements AbstractResponse.Body {

        // Members

        // Constructors

        public Body() {
        }

        // Converters

        public Game toGame(String id) {
            return new LiveGameParser().parse(id, this);
        }

    }

    // ******************************
    // Static Helper Methods
    // ******************************

    public static DeserialisationCallback<GameResponse> deserialisation() {
        return (response, objectMapper) -> {
            // Construct the response object
            return new GameResponse(getStatusCode(response), deserialise(response, objectMapper, Body.class));
        };
    }

}
