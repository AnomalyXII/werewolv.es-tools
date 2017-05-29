package net.anomalyxii.werewolves.wwesbot.spring;

import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.UserService;
import net.anomalyxii.werewolves.services.impl.ArchivedGameService;
import net.anomalyxii.werewolves.services.impl.ArchivedUserService;
import net.anomalyxii.werewolves.services.impl.CompositeGameService;
import net.anomalyxii.werewolves.services.impl.LiveGameService;
import net.anomalyxii.werewolves.wwesbot.spring.service.CachingGameService;
import net.anomalyxii.werewolves.wwesbot.spring.service.CachingUserService;
import org.eclipse.jgit.api.Git;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;
import javax.cache.expiry.EternalExpiryPolicy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

/**
 * Spring {@link Configuration} for services.
 * <p>
 * Created by Anomaly on 15/04/2017.
 */
@Configuration
@EnableCaching
public class BotServiceConfiguration {

    // ******************************
    // Beans
    // ******************************

    @Component
    public static class CachingSetup implements JCacheManagerCustomizer {
        @Override
        public void customize(CacheManager cacheManager) {
            cacheManager.createCache("games", new MutableConfiguration<>()
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 3600)))
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true));
            cacheManager.createCache("users", new MutableConfiguration<>()
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.SECONDS, 3600)))
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true));
        }
    }

    // ******************************
    // Nested Configuration Classes
    // ******************************

    @Configuration
    public static class RouterConfiguration {

        @Bean
        public static HttpRouter router() {
            return new HttpRouter();
        }

    }

    @Configuration
    public static class ApiServiceConfiguration {

        @Bean
        public static Git saltmineRepository() throws Exception {

            String tmpDir = System.getProperty("java.io.tmpdir");
            Path tmp = Paths.get(tmpDir);
            Path tmpRepo = tmp.resolve("wwes-salt-mine");

            String remote = "https://github.com/Kirschstein/salt-mine.git";

            if (Files.exists(tmpRepo))
                return Git.open(tmpRepo.toFile());


            return Git.cloneRepository()
                    .setURI(remote)
                    .setDirectory(tmpRepo.toFile())
                    .call();
        }

        @Bean(name = "archivedGameService")
        @Autowired
        public static GameService archivedGameService(Git git) {
            return new ArchivedGameService(git);
        }

        @Bean
        @Primary
        @Autowired
        public static GameService cachingGameService(@Qualifier("archivedGameService") GameService archivedGameService,
                                                     @Value("${bot.router.username:test01}") String username,
                                                     @Value("${bot.router.password:test01}") String password,
                                                     HttpRouter router) {
            GameService liveGameService = new LiveGameService(username, password, router);
            GameService compositeGameService = new CompositeGameService(archivedGameService, liveGameService);
            return new CachingGameService(compositeGameService);
        }

        @Bean
        @Autowired
        public static UserService cachingUserService(@Qualifier("archivedGameService") GameService archivedGameService) {
            return new CachingUserService(new ArchivedUserService(archivedGameService));
        }

    }

}
