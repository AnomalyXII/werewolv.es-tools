package net.anomalyxii.werewolves.wwesbot.spring;

import net.anomalyxii.werewolves.router.DefaultRouterBuilder;
import net.anomalyxii.werewolves.router.Router;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiService;
import net.anomalyxii.werewolves.wwesbot.spring.services.impl.ApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.EternalExpiryPolicy;

/**
 * Spring {@link Configuration} for services.
 * <p>
 * Created by Anomaly on 15/04/2017.
 */
@Configuration
@EnableCaching
public class BotServiceConfiguration {

    @Component
    public static class CachingSetup implements JCacheManagerCustomizer {
        @Override
        public void customize(CacheManager cacheManager) {
            cacheManager.createCache("api", new MutableConfiguration<>()
                    .setExpiryPolicyFactory(EternalExpiryPolicy.factoryOf())
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true));
        }
    }

    @Configuration
    @Import(RouterConfiguration.class)
    public static class ApiServiceConfiguration {

        @Bean
        @Autowired
        public static ApiService apiService(Router liveRouter) throws RouterException {
            Router archiveRouter = new DefaultRouterBuilder().forArchivedGame("test01");
            return new ApiServiceImpl(liveRouter, archiveRouter);
        }

    }

}
