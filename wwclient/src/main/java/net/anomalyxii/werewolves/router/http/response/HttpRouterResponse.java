package net.anomalyxii.werewolves.router.http.response;

import net.anomalyxii.werewolves.router.RouterResponse;
import net.anomalyxii.werewolves.router.http.HttpRouter;

/**
 * An abstract {@link RouterResponse} for a {@link HttpRouter}.
 *
 * Created by Anomaly on 15/05/2017.
 */
public abstract class HttpRouterResponse<T> implements RouterResponse<T> {

    // ******************************
    // Members
    // ******************************

    private final int code;
    private final String reason;

    // ******************************
    // Constructors
    // ******************************

    public HttpRouterResponse(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    // ******************************
    // Getters
    // ******************************

    /**
     * Get the {@code HTTP Status Code} that was returned with this
     * {@link RouterResponse}.
     *
     * @return the {@code HTTP Status Code}
     */
    public int getCode() {
        return code;
    }

    /**
     * Get the {@code HTTP Status Code Reason} that was returned
     * with this {@link RouterResponse}.
     *
     * @return the {@code HTTP Status Code}
     */
    public String getReason() {
        return reason;
    }

}
