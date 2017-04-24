package net.anomalyxii.werewolves.router.http.request;

import net.anomalyxii.werewolves.router.Auth;
import net.anomalyxii.werewolves.router.RouterRequest;
import net.anomalyxii.werewolves.router.http.HttpRouter;

import java.net.URI;
import java.util.Optional;

/**
 * An abstract {@link RouterRequest} for a {@link HttpRouter}.
 *
 * Created by Anomaly on 22/11/2016.
 */
public abstract class HttpRouterRequest<T> implements RouterRequest<T> {

    // ******************************
    // Members
    // ******************************

    private final URI uri;
    private final Auth auth;

    // ******************************
    // Constructors
    // ******************************

    protected HttpRouterRequest(URI uri, Auth auth) {
        this.uri = uri;
        this.auth = auth;
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public Optional<Auth> getAuth() {
        return Optional.ofNullable(auth);
    }

    @Override
    public abstract Optional<T> getContent();

}
