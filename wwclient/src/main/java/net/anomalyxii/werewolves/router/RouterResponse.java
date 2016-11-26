package net.anomalyxii.werewolves.router;

import org.apache.http.Header;

import java.util.List;
import java.util.Optional;

/**
 * Created by Anomaly on 22/11/2016.
 */
public interface RouterResponse<T> extends ContentTypeSupport {

    // ******************************
    // Interface Methods
    // ******************************

    List<Header> getHeaders();

    Optional<T> getContent();

    int getStatusCode();

    // ******************************
    // Default Methods
    // ******************************

    default boolean isFailure() {
        return getStatusCode() != 200;
    }

}
