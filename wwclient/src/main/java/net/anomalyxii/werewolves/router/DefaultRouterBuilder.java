package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Build a {@link Router}.
 * <p>
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
        HttpRouter router = new HttpRouter();
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
        // Todo: improve
        try {
            Path tmpdir = Files.createTempDirectory("wwes-saltmine-git");
            return new SaltMineRouter(tmpdir);
        } catch(IOException e) {
            throw new RouterException("Failed to create temporary salt-mine directory", e);
        } catch (GitAPIException e) {
            throw new RouterException("Failed to clone salt-mine", e);
        }
    }

}
