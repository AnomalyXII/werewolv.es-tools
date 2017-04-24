package net.anomalyxii.werewolves.router.http.request;

import net.anomalyxii.werewolves.router.Auth;

import java.net.URI;
import java.util.Objects;
import java.util.Optional;

/**
 * A standard, JSON-based {@link HttpRouterRequest}.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class StandardHttpRouterRequest<T> extends HttpRouterRequest<T> {

    // ******************************
    // Members
    // ******************************

    private final T content;

    // ******************************
    // Constructors
    // ******************************

    public StandardHttpRouterRequest(URI uri, Auth auth, T content) {
        super(uri, auth);
        this.content = Objects.requireNonNull(content);
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    @Override
    public Optional<T> getContent() {
        return Optional.of(content);
    }

}
