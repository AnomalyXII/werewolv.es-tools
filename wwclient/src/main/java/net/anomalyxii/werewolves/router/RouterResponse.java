package net.anomalyxii.werewolves.router;

import java.util.Optional;

/**
 * Contains details of a response returned via a
 * {@link Router}.
 * <p>
 * Created by Anomaly on 22/11/2016.
 */
public interface RouterResponse<T> {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Get the {@link Optional} response content
     * @return
     */
    Optional<T> getContent();

}
