package net.anomalyxii.werewolves.wwesbot.spring;

import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.impl.ArchivedGameService;
import net.anomalyxii.werewolves.services.impl.CompositeGameService;
import net.anomalyxii.werewolves.services.impl.LiveGameService;
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
import javax.cache.expiry.EternalExpiryPolicy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public static class RouterConfiguration {

        // ******************************
        // Beans
        // ******************************

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

        @Bean(name = "liveGameService")
        @Autowired
        public static LiveGameService liveGameService(@Value("${bot.router.username:test01}") String username,
                                                      @Value("${bot.router.password:test01}") String password,
                                                      HttpRouter router) {
            return new LiveGameService(username, password, router);
        }

        @Bean(name = "archivedGameService")
        @Autowired
        public static ArchivedGameService archivedGameService(Git git) {
            return new ArchivedGameService(git);
        }

        @Bean
        @Primary
        @Autowired
        public static GameService apiService(@Qualifier("liveGameService") GameService liveGameService,
                                             @Qualifier("archivedGameService") ArchivedGameService archivedGameService) {
            return new CompositeGameService(liveGameService, archivedGameService);
        }

    }

}
