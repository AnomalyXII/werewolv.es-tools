package net.anomalyxii.werewolves.router.response.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.exceptions.ResponseDeserialisationException;
import net.anomalyxii.werewolves.router.response.AbstractResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Anomaly on 22/11/2016.
 */
public class GameListResponse extends AbstractResponse<GameListResponse.Body> {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public GameListResponse(int statusCode, Body content) {
        super(statusCode, content);
    }

    // ******************************
    // Content Class
    // ******************************

    public static final class Body extends AbstractResponse.DefaultBody {

        // Members

        private List<String> active;
        private List<String> pending;

        // Constructors

        public Body() {
        }

        public Body(List<String> active, List<String> pending) {
            this.active = active;
            this.pending = pending;
        }

        // Constructors

        @JsonProperty("Active")
        public List<String> getActive() {
            return active;
        }

        public void setActive(List<String> active) {
            this.active = active;
        }

        @JsonProperty("Pending")
        public List<String> getPending() {
            return pending;
        }

        public void setPending(List<String> pending) {
            this.pending = pending;
        }

    }

    // ******************************
    // Static Helper Methods
    // ******************************

    public static DeserialisationCallback<GameListResponse> deserialisation() {
        return (response, objectMapper) -> {
            int statusCode = response.getStatusLine().getStatusCode();
            InputStream in = null;
            try {
                in = response.getEntity().getContent();
            } catch (IOException e) {
                throw new ResponseDeserialisationException(e, null);
            }

            // Deserialise the body
            Body body;
            try {
                body = objectMapper.readValue(in, Body.class);
            } catch (IOException e) {
                throw new ResponseDeserialisationException(e, null);
            }

            // Construct the response object
            return new GameListResponse(statusCode, body);
        };
    }

}
