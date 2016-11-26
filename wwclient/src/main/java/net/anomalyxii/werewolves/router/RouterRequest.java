package net.anomalyxii.werewolves.router;

import org.apache.http.Header;
import org.apache.http.entity.ContentType;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Anomaly on 22/11/2016.
 */
public interface RouterRequest<T> extends ContentTypeSupport {

    // ******************************
    // Interface Methods
    // ******************************

    URI getURI();

    List<Header> getHeaders();

    void addHeader(Header header);

    void removeHeader(Header header);

    Optional<T> getContent();

    // ******************************
    // Default Methods
    // ******************************

    default boolean usePost() {
        return false;
    }

    default List<Integer> getAcceptedStatusCodes() {
        return Collections.singletonList(200);
    }

    default List<Integer> getRejectedStatusCodes() {
        return Collections.singletonList(400);
    }

}
