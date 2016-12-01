package net.anomalyxii.werewolves.router.request;

import net.anomalyxii.werewolves.router.RouterRequest;
import net.anomalyxii.werewolves.router.XmlOrJsonSupport;
import org.apache.http.Header;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Anomaly on 22/11/2016.
 */
public class AbstractRequest<T> implements RouterRequest<T>, XmlOrJsonSupport {

    // ******************************
    // Members
    // ******************************

    private final URI uri;
    private final T content;
    private final List<Header> headers = new ArrayList<>();

    // ******************************
    // Constructors
    // ******************************

    public AbstractRequest(URI host, String path, T content) {
        this(host.resolve(path), content);
    }

    private AbstractRequest(URI uri, T content) {
        this.uri = uri;
        this.content = content;
    }

    // ******************************
    // RouterRequest Methods
    // ******************************

    @Override
    public URI getURI() {
        return uri;
    }

    @Override
    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    @Override
    public Optional<T> getContent() {
        return content != null
               ? Optional.of(content)
               : Optional.empty();
    }

    // ******************************
    // Header Methods
    // ******************************

    @Override
    public void addHeader(Header header) {
        this.headers.add(header);
    }

    @Override
    public void removeHeader(Header header) {
        this.headers.remove(header);
    }

}
