package net.anomalyxii.werewolves.router.http.response.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.anomalyxii.werewolves.router.http.response.ResponseBean;

import java.util.List;

/**
 * Response from the {@code /api/Game/} endpoint.
 * <p>
 * Retrieve and parse a list of {@code active} and
 * {@code pending} games. {@code Active} games are those that
 * have started are no longer accepting new players, whilst
 * {@code pending} games are those that are still in the
 * sign-up phase and thus can still be joined.
 * <p>
 * Created by Anomaly on 22/11/2016.
 */
public class GameListResponseBean {

    // ******************************
    // Members
    // ******************************

    private List<String> active;
    private List<String> pending;

    // ******************************
    // Constructors
    // ******************************

    public GameListResponseBean() {
    }

    public GameListResponseBean(List<String> active, List<String> pending) {
        this.active = active;
        this.pending = pending;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @JsonProperty("active")
    public List<String> getActive() {
        return active;
    }

    public void setActive(List<String> active) {
        this.active = active;
    }

    @JsonProperty("pending")
    public List<String> getPending() {
        return pending;
    }

    public void setPending(List<String> pending) {
        this.pending = pending;
    }

}
