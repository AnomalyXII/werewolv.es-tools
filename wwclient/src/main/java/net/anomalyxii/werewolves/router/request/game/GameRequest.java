package net.anomalyxii.werewolves.router.request.game;

import net.anomalyxii.werewolves.router.request.EmptyRequest;

import java.net.URI;

/**
 * Created by Anomaly on 29/11/2016.
 */
public class GameRequest extends EmptyRequest {

    // ******************************
    // Constructors
    // ******************************

    public GameRequest(URI host, String id) {
        super(host, "/api/Game/" + id);
    }

}
