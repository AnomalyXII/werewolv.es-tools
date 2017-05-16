package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.Command;
import net.anomalyxii.bot.api.handlers.CommandHandler;
import net.anomalyxii.bot.api.server.Messagable;
import net.anomalyxii.bot.api.server.Server;
import net.anomalyxii.bot.api.server.events.MessageEvent;
import net.anomalyxii.bot.impl.handlers.AbstractCommandHandler;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
import net.anomalyxii.werewolves.wwesbot.spring.domain.GameStatistics;

import java.util.Collections;
import java.util.List;

/**
 * A {@link CommandHandler} that retrieves statistics about
 * a {@code werewolv.es} user or game.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
public class StatsHandler extends AbstractCommandHandler {

    private static final String COMMAND = "!stats";

    // *********************************
    // Attributes
    // *********************************

    private final GameService service;

    // *********************************
    // Constructors
    // *********************************

    public StatsHandler(GameService service) {
        this.service = service;
    }

    // *********************************
    // CommandHandler Methods
    // *********************************

    @Override
    protected String getCommand() {
        return COMMAND;
    }

    @Override
    public void handleCommand(Command command) throws Exception {
        Bot bot = command.getBot();
        Server server = command.getServer();
        MessageEvent message = command.getMessageEvent();
        List<String> arguments = command.getArguments();

        List<String> gameIds = arguments;
        List<String> userIds = Collections.emptyList();
        //if (arguments.containsOption("user"))
        //    userIds = arguments.getOptionValues("user");

        // Todo: allow parsing from non-option arguments
        // Todo: limit the number of games we'll parse??
        if (gameIds.isEmpty() && userIds.isEmpty())
            throw new IllegalArgumentException("(stats) No games or users were specified");

        Messagable recipient = message.isPrivate() ? message.getSender() : message.getTarget();

        for (String gameId : gameIds)
            handleGameStats(bot, server, recipient, gameId);

        for (String id : userIds)
            handleUserStats(bot, server, recipient, id);

    }

    /**
     * Report the statistics of a specified
     * {@link Game werewolv.es Game}.
     *
     * @param bot       the {@link Bot} that received the {@link MessageEvent}
     * @param recipient the {@link Messagable} to send the report to
     * @param gameId    the {@code game ID}
     * @throws ServiceException if the {@link Game} cannot be fetched from the API
     */
    protected void handleGameStats(Bot bot, Server server, Messagable recipient, String gameId) throws ServiceException {
        Game game = service.getGame(gameId);
        GameStatistics stats = new GameStatistics(game);
        server.say(recipient, "(stats) " + stats.toFormattedString());
    }

    /**
     * Report the statistics of a specified
     * {@code werewolv.es User}
     *
     * @param bot       the {@link Bot} that received the {@link MessageEvent}
     * @param recipient the {@link Messagable} to send the report to
     * @param userId    the {@code user ID}
     * @throws ServiceException if the {@link Game} cannot be fetched from the API
     */
    protected void handleUserStats(Bot bot, Server server, Messagable recipient, String userId) throws ServiceException {

    }

}
