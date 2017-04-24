package net.anomalyxii.werewolves.router.http.response;

import java.util.Optional;

/**
 * A {@link HttpRouterResponse} that has no response body.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class EmptyHttpRouterResponse<T> extends HttpRouterResponse<T> {

    // ******************************
    // Constructors
    // ******************************

    public EmptyHttpRouterResponse(int statusCode, String reasonPhrase) {
        super(statusCode, reasonPhrase);
    }

    // ******************************
    // RouterResponse Methods
    // ******************************

    @Override
    public Optional<T> getContent() {
        return Optional.empty();
    }

}
