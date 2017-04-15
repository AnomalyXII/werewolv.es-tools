package net.anomalyxii.werewolves.wwesbot.spring;

import net.anomalyxii.werewolves.router.DefaultRouterBuilder;
import net.anomalyxii.werewolves.router.Router;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure the {@link Router}
 * <p>
 * Created by Anomaly on 04/04/2017.
 */
@Configuration
public class RouterConfiguration {

    // ******************************
    // Beans
    // ******************************

    @Bean
    @ConditionalOnProperty({"router.token"})
    public static Router routerWithToken(@Value("${router.token}") String token) throws RouterException {
        return new DefaultRouterBuilder().forToken(token);
    }

    @Bean
    @ConditionalOnMissingBean
    public static Router routerWithCredentials(@Value("${router.username:test01}") String username,
                                               @Value("${router.password:test01}") String password) throws RouterException {
        return new DefaultRouterBuilder().forCredentials(username, password);
    }

}
