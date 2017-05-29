package net.anomalyxii.werewolves.wwesbot.spring.service;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResult;

/**
 * A {@link GameService} with caching enabled.
 * <p>
 * Created by Anomaly on 22/05/2017.
 */
@CacheResult
@CacheDefaults(cacheName = "games")
public class CachingGameService implements GameService {

    // ******************************
    // Members
    // ******************************

    private final GameService delegate;

    // ******************************
    // Constructors
    // ******************************

    public CachingGameService(GameService delegate) {
        this.delegate = delegate;
    }

    // ******************************
    // GameService Methods
    // ******************************

    @Override
    public boolean doesGameExist(String id) {
        return delegate.doesGameExist(id);
    }

    @Override
    public GamesList getGameIds() throws ServiceException {
        return delegate.getGameIds();
    }

    @Override
    public Game getGame(String id) throws ServiceException {
        return delegate.getGame(id);
    }

    @Override
    public GameStatistics getGameStatistics(String id) throws ServiceException {
        return delegate.getGameStatistics(id);
    }

}
