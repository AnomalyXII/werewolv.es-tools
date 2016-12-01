package net.anomalyxii.werewolves.router.request.game;

import net.anomalyxii.werewolves.router.request.EmptyRequest;

import java.net.URI;

/**
 * Created by Anomaly on 29/11/2016.
 */
public class GameListRequest extends EmptyRequest {

    // ******************************
    // Constructors
    // ******************************

    public GameListRequest(URI host) {
        super(host, "/api/Game");
    }

}
