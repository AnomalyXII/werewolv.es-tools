package net.anomalyxii.werewolves.wwesbot.spring.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.Router;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import net.anomalyxii.werewolves.wwesbot.spring.domain.GameStatistics;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiException;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

/**
 * An implementation of the {@link ApiService} that wraps a
 * {@link Router} in order to provide caching and other useful
 * features not provided by default.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
@Cacheable
@CacheConfig(cacheNames = "api")
public class ApiServiceImpl implements ApiService {

    // *********************************
    // Attributes
    // *********************************

    private final Router liveRouter;
    private final Router archiveRouter;

    // *********************************
    // Constructors
    // *********************************

    public ApiServiceImpl(Router liveRouter, Router archiveRouter) {
        this.liveRouter = liveRouter;
        this.archiveRouter = archiveRouter;
    }

    // *********************************
    // Service Methods
    // *********************************

    @Override
    public GamesList getGameIDs() throws ApiException {
        try {
            GamesList liveGameList = liveRouter.games();
            GamesList archiveGameList = archiveRouter.games();

            return new GamesList(liveGameList.getActiveGameIDs(), // Live,
                                 liveGameList.getPendingGameIDs(), // Live,
                                 archiveGameList.getCompletedGameIDs()); // Archived
        } catch (RouterException e) {
            throw new ApiException("Failed to retrieve games: " + e.getMessage(), e);
        }
    }

    @Override
    public Game getGame(String id) throws ApiException {
        boolean isArchivedGame;
        try {
            // Todo: this call won't be cached - is that okay??
            GamesList archivedGameList = archiveRouter.games();
            isArchivedGame = archivedGameList.getCompletedGameIDs().contains(id);
        } catch (Exception e) {
            throw new ApiException("Failed to determine active games: " + e.getMessage(), e);
        }

        try {
            return !isArchivedGame ? liveRouter.game(id) : archiveRouter.game(id);
        } catch (RouterException e) {
            throw new ApiException("Failed to retrieve game '" + id + "': " + e.getMessage(), e);
        }
    }

    @Override
    public GameStatistics getGameStatistics(String id) throws ApiException {
        // We do this so that we can cache them ;D
        return new GameStatistics(getGame(id));
    }

}
