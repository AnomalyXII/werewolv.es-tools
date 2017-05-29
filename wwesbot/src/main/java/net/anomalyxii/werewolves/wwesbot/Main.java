package net.anomalyxii.werewolves.wwesbot;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.DispatchingMessageReceivedListener;
import net.anomalyxii.bot.api.scheduler.BotScheduler;
import net.anomalyxii.bot.api.scheduler.ScheduledActionSpecification;
import net.anomalyxii.bot.discord.server.DiscordServer;
import net.anomalyxii.bot.impl.scheduler.QuartzScheduledActionSpecification;
import net.anomalyxii.bot.irc.server.IrcServer;
import net.anomalyxii.botmanager.api.BotManager;
import net.anomalyxii.botmanager.api.services.BotService;
import net.anomalyxii.botmanager.spring.SpringWebManager;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.UserService;
import net.anomalyxii.werewolves.wwesbot.handlers.*;
import net.anomalyxii.werewolves.wwesbot.spring.BotConfiguration;
import net.anomalyxii.werewolves.wwesbot.spring.BotSchedulerConfiguration;
import net.anomalyxii.werewolves.wwesbot.spring.BotServiceConfiguration;
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
                .sources(SpringWebManager.class, BotConfiguration.class, BotServiceConfiguration.class, BotSchedulerConfiguration.class)
                .main(SpringWebManager.class);

        SpringApplication application = builder.build();
        ConfigurableApplicationContext context = application.run(args);

        BotService service = context.getBean(BotService.class);
        Bot bot = context.getBean(Bot.class);

        BotManager manager = service.registerBot(bot);

        DispatchingMessageReceivedListener dispatchListener = new DispatchingMessageReceivedListener();

        GameService gameService = context.getBean(GameService.class);
        UserService userService = context.getBean(UserService.class);
        dispatchListener.registerCommandHandler(new ListActiveGamesHandler(gameService));
        dispatchListener.registerCommandHandler(new ListPendingGamesHandler(gameService));
        dispatchListener.registerCommandHandler(new ListArchivedGamesHandler(gameService));
        dispatchListener.registerCommandHandler(new StatsHandler(gameService));
        dispatchListener.registerCommandHandler(new UserStatsHandler(userService));

        // service.registerBotEventListener(manager, dispatchListener);
        bot.asEventSubscriber().registerBotEventListener(dispatchListener);

        BotScheduler scheduler = context.getBean(BotScheduler.class);
        scheduler.start();

        ScheduledActionSpecification specification = new QuartzScheduledActionSpecification("0 */10 * * * ?")
        scheduler.registerBot(bot);

        IDiscordClient discordClient = context.getBean(IDiscordClient.class);
        manager.connect(new IrcServer("wwes-bot", URI.create("irc://irc.quakenet.org:6667")));
        manager.connect(new DiscordServer(discordClient));

    }

}
