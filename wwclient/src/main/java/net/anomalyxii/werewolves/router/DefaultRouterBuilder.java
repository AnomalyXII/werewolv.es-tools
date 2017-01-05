package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.router.exceptions.RouterException;

/**
 * Created by Anomaly on 27/11/2016.
 */
public class DefaultRouterBuilder implements RouterBuilder {

    // ******************************
    // RouterBuilder Methods
    // ******************************

    @Override
    public Router forToken(String token) throws RouterException {
        return new HttpRouter(token);
    }

    @Override
    public Router forCredentials(String username, String password) throws RouterException {
        Router router = new HttpRouter();
        if (!router.oauth(username, password))
            throw new RouterException("Failed to log-in!");

        return router;
    }

    @Override
    public Router forLocalGame() throws RouterException {
        return new LocalRouter();
    }

    @Override
    public Router forArchivedGame(String username) throws RouterException {
        return new LocalArchivedGameRouter(username);
    }

}
