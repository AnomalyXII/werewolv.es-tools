package net.anomalyxii.werewolves.router.http.request;

import net.anomalyxii.werewolves.router.Auth;

import java.net.URI;
import java.util.Optional;

/**
 * A {@link HttpRouterRequest} that has no request body.
 * <p>
 * Created by Anomaly on 23/11/2016.
 */
public class EmptyHttpRouterRequest<T> extends HttpRouterRequest<T> {

    // ******************************
    // Constructors
    // ******************************

    public EmptyHttpRouterRequest(URI uri, Auth auth) {
        super(uri, auth);
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    @Override
    public Optional<T> getContent() {
        return Optional.empty();
    }

}
