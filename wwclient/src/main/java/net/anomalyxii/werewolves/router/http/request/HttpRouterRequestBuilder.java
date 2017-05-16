package net.anomalyxii.werewolves.router.http.request;

import net.anomalyxii.werewolves.router.Auth;

import java.net.URI;
import java.util.Objects;

/**
 * A builder for {@link HttpRouterRequest HttpRouterRequests}.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class HttpRouterRequestBuilder<T> {

    // ******************************
    // Members
    // ******************************

    private URI host;
    private URI endpoint;
    private Auth auth;
    private T content;

    // ******************************
    // Constructors
    // ******************************

    private HttpRouterRequestBuilder(URI host, URI endpoint, Auth auth, T content) {
        this.host = host;
        this.endpoint = endpoint;
        this.auth = auth;
        this.content = content;
    }

    // ******************************
    // Builder Methods
    // ******************************

    /**
     * Set the target host.
     *
     * @param host the host
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public HttpRouterRequestBuilder<T> withHost(String host) {
        return withHost(URI.create(host));
    }

    /**
     * Set the target host.
     *
     * @param host the host
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public HttpRouterRequestBuilder<T> withHost(URI host) {
        this.host = host;
        return this;
    }

    /**
     * Set the target endpoint.
     *
     * @param endpoint the endpoint
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public HttpRouterRequestBuilder<T> withEndpoint(String endpoint) {
        return withEndpoint(URI.create(endpoint));
    }

    /**
     * Set the target endpoint.
     *
     * @param endpoint the endpoint
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public HttpRouterRequestBuilder<T> withEndpoint(URI endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    /**
     * Set the {@link Auth} type.
     *
     * @param auth the {@link Auth}
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public HttpRouterRequestBuilder<T> withAuth(Auth auth) {
        this.auth = auth;
        return this;
    }

    /**
     * Set the request content.
     *
     * @param content the content
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public <X> HttpRouterRequestBuilder<X> withContent(X content) {
        return new HttpRouterRequestBuilder<>(host, endpoint, auth, content);
    }

    /**
     * Remove the request content.
     *
     * @return a {@link HttpRouterRequestBuilder} for chaining
     */
    public HttpRouterRequestBuilder<Void> withoutContent() {
        return new HttpRouterRequestBuilder<>(host, endpoint, auth, null);
    }

    /**
     * Clone this {@link HttpRouterRequest}.
     *
     * @return the cloned {@link HttpRouterRequest}
     */
    public HttpRouterRequestBuilder<T> copy() {
        return new HttpRouterRequestBuilder<>(host, endpoint, auth, content);
    }

    /**
     * Build the {@link HttpRouterRequest}.
     *
     * @return the new {@link HttpRouterRequest}
     */
    public HttpRouterRequest<T> build() {
        URI target = Objects.nonNull(host) ? host : URI.create("http://werewolv.es/"); // Todo: customizable default
        target = target.resolve(endpoint);

        if (Objects.isNull(content))
            return new EmptyHttpRouterRequest<>(target, auth);

        return new StandardHttpRouterRequest<>(target, auth, content);
    }

    // ******************************
    // Public Static Helper Methods
    // ******************************

    public static HttpRouterRequestBuilder<Void> newBuilder() {
        return new HttpRouterRequestBuilder<>(null, null, null, null);
    }

}
