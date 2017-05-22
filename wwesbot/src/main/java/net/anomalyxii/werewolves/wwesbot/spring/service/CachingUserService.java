package net.anomalyxii.werewolves.wwesbot.spring.service;

import net.anomalyxii.werewolves.domain.UserStatistics;
import net.anomalyxii.werewolves.services.ServiceException;
import net.anomalyxii.werewolves.services.UserService;
import org.springframework.cache.annotation.Cacheable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CacheResult;

/**
 * A {@link UserService} with caching enabled.
 * <p>
 * Created by Anomaly on 22/05/2017.
 */
@CacheResult
@CacheDefaults(cacheName = "users")
public class CachingUserService implements UserService {

    // ******************************
    // Members
    // ******************************

    private final UserService delegate;

    // ******************************
    // Constructors
    // ******************************

    public CachingUserService(UserService delegate) {
        this.delegate = delegate;
    }

    // ******************************
    // UserService Methods
    // ******************************

    @Override
    public UserStatistics getUserStatistics(String id) throws ServiceException {
        return delegate.getUserStatistics(id);
    }

}
