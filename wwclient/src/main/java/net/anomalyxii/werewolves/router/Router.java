package net.anomalyxii.werewolves.router;

/**
 * A gateway for interacting with the {@code werewolv.es API}.
 * <p>
 * Created by Anomaly on 05/01/2017.
 */
public interface Router extends AutoCloseable {

    // ******************************
    // Interface Methods
    // ******************************

    RouterResponse<Void> submit(RouterRequest<?> request) throws RouterException;

    <T> RouterResponse<T> submit(RouterRequest<?> request, Class<T> responseClass) throws RouterException;

    // ******************************
    // Default Methods
    // ******************************

    @Override
    default void close() throws Exception {
        // Default: no-op
    }

}
