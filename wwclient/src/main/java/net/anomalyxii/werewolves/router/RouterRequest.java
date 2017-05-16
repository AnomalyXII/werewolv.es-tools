package net.anomalyxii.werewolves.router;

import java.net.URI;
import java.util.Optional;

/**
 * Contains details of a request being made via a
 * {@link Router}.
 * <p>
 * Created by Anomaly on 22/11/2016.
 */
public interface RouterRequest<T> {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Get the {@link URI} of the endpoint that this
     * {@code RouterRequest} is being made to.
     *
     * @return the endpoint {@link URI}
     */
    URI getURI();

    /**
     * Get the {@link Auth} to use for this {@code RouterRequest}.
     * @return
     */
    Optional<Auth> getAuth();

    /**
     * Get the ({@link Optional}) content that should be sent in
     * the body of this {@code RouterRequest}.
     *
     * @return the {@link Optional} request body
     */
    Optional<T> getContent();

}
