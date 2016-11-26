package net.anomalyxii.werewolves.router.response;

import net.anomalyxii.werewolves.router.DeserialisationCallback;

/**
 * Created by Anomaly on 24/11/2016.
 */
public class EmptyResponse extends AbstractResponse<Void> {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public EmptyResponse(int statusCode) {
        super(statusCode, null);
    }

    // ******************************
    // Static Helper Methods
    // ******************************

    public static DeserialisationCallback<EmptyResponse> deserialisation() {
        return (response, objectMapper) -> {
            // Construct the response object
            return new EmptyResponse(getStatusCode(response));
        };
    }

}
