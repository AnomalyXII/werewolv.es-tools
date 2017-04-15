package net.anomalyxii.werewolves.wwesbot.spring;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.impl.GenericBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

/**
 * Configure the {@link Bot Bots}
 */
@Configuration
public class BotConfiguration {

    // ******************************
    // Beans
    // ******************************

    // Bot

    @Configuration
    public static class GenericBotConfiguration {

        @Bean
        public static Bot bot() {
            return new GenericBot();
        }

    }

    // Discord Client

    @Configuration
    public static class DIscordClientConfiguration {

        @Bean
        public static IDiscordClient discordClient(@Value("${bot.discord.token}") String token) throws DiscordException {
            ClientBuilder builder = new ClientBuilder();
            builder.withToken(token);
            return builder.build();
        }

    }

}
