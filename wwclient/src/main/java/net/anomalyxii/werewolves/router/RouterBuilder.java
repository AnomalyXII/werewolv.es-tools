package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.router.exceptions.RouterException;

/**
 * Created by Anomaly on 27/11/2016.
 */
public interface RouterBuilder {

    // ******************************
    // Interface Methods
    // ******************************

    Router forToken(String token) throws RouterException;

    Router forCredentials(String username, String password) throws RouterException;

}
