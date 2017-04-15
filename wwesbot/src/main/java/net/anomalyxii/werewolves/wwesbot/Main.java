package net.anomalyxii.werewolves.wwesbot;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.DispatchingMessageReceivedListener;
import net.anomalyxii.bot.discord.server.DiscordServer;
import net.anomalyxii.bot.irc.server.IrcServer;
import net.anomalyxii.botmanager.api.BotManager;
import net.anomalyxii.botmanager.api.services.BotService;
import net.anomalyxii.botmanager.spring.SpringWebManager;
import net.anomalyxii.werewolves.wwesbot.handlers.ListActiveGamesHandler;
import net.anomalyxii.werewolves.wwesbot.handlers.ListPendingGamesHandler;
import net.anomalyxii.werewolves.wwesbot.handlers.StatsHandler;
import net.anomalyxii.werewolves.wwesbot.spring.BotConfiguration;
import net.anomalyxii.werewolves.wwesbot.spring.BotServiceConfiguration;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiService;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import sx.blah.discord.api.IDiscordClient;

import java.net.URI;

/**
 * The main class for starting the {@link BotManager}
 * <p>
 * Created by Anomaly on 29/03/2017.
 */
@SpringBootApplication
public class Main {

    // ******************************
    // Constructors
    // ******************************

    public static void main(String... args) throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(SpringWebManager.class, BotConfiguration.class, BotServiceConfiguration.ApiServiceConfiguration.class)
                .main(SpringWebManager.class);

        SpringApplication application = builder.build();
        ConfigurableApplicationContext context = application.run(args);

        BotService service = context.getBean(BotService.class);
        Bot bot = context.getBean(Bot.class);

        BotManager manager = service.registerBot(bot);

        DispatchingMessageReceivedListener dispatchListener = new DispatchingMessageReceivedListener();

        ApiService apiService = context.getBean(ApiService.class);
        dispatchListener.registerCommandHandler(new ListActiveGamesHandler(apiService));
        dispatchListener.registerCommandHandler(new ListPendingGamesHandler(apiService));
        dispatchListener.registerCommandHandler(new StatsHandler(apiService));
        bot.asEventSubscriber().registerBotEventListener(dispatchListener);

        IDiscordClient discordClient = context.getBean(IDiscordClient.class);
        manager.connect(new IrcServer(URI.create("irc://irc.quakenet.org:6667")));
        manager.connect(new DiscordServer(discordClient));

    }

}
