package net.anomalyxii.werewolves.router.http.response.game;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Response from the {@code /api/Game/[id]} endpoint.
 * <p>
 * Created by Anomaly on 22/11/2016.
 */
public class GameResponseBean extends ArrayList<GameResponseBean.Event> {

    // ******************************
    // Static Helper Classes
    // ******************************

    public static class Event extends HashMap<String, Object> {

    }

}
