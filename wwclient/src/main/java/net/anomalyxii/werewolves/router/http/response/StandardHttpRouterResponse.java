package net.anomalyxii.werewolves.router.http.response;

import net.anomalyxii.werewolves.router.http.request.HttpRouterRequest;

import java.util.Objects;
import java.util.Optional;

/**
 * A standard, JSON-based {@link HttpRouterResponse}.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class StandardHttpRouterResponse<T> extends HttpRouterResponse<T> {

    // ******************************
    // Members
    // ******************************

    private final T content;

    // ******************************
    // Constructors
    // ******************************

    public StandardHttpRouterResponse(int code, String reason, T content) {
        super(code, reason);
        this.content = Objects.requireNonNull(content);
    }

    // ******************************
    // RouterResponse Methods
    // ******************************

    @Override
    public Optional<T> getContent() {
        return Optional.of(content);
    }

}
