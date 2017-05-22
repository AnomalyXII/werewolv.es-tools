package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.Command;
import net.anomalyxii.bot.api.handlers.CommandHandler;
import net.anomalyxii.bot.api.server.Messagable;
import net.anomalyxii.bot.api.server.Server;
import net.anomalyxii.bot.api.server.events.MessageEvent;
import net.anomalyxii.bot.impl.handlers.AbstractCommandHandler;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.UserStatistics;
import net.anomalyxii.werewolves.domain.players.User;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
import net.anomalyxii.werewolves.services.UserService;

import java.util.List;

/**
 * A {@link CommandHandler} that retrieves statistics about
 * a {@code werewolv.es} {@link User}.
 * <p>
 * Created by Anomaly on 15/04/17.
 */
public class UserStatsHandler extends AbstractCommandHandler {

    private static final String COMMAND = "!ustats";

    // *********************************
    // Attributes
    // *********************************

    private final UserService service;

    // *********************************
    // Constructors
    // *********************************

    public UserStatsHandler(UserService service) {
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
        List<String> usernames = command.getArguments();

        // Todo: allow parsing from non-option arguments
        // Todo: limit the number of games we'll parse??
        if (usernames.isEmpty() && usernames.isEmpty())
            throw new IllegalArgumentException("(stats) No games or users were specified");

        Messagable recipient = message.isPrivate() ? message.getSender() : message.getTarget();

        for (String username : usernames)
            handleUserStats(bot, server, recipient, username);

    }

    /**
     * Report the statistics of a specified
     * {@link Game werewolv.es Game}.
     *
     * @param bot       the {@link Bot} that received the {@link MessageEvent}
     * @param recipient the {@link Messagable} to send the report to
     * @param username    the {@link User User's} username
     * @throws ServiceException if the {@link Game} cannot be fetched from the API
     */
    protected void handleUserStats(Bot bot, Server server, Messagable recipient, String username) throws ServiceException {
        UserStatistics stats = service.getUserStatistics(username);
        server.say(recipient, "(ustats) " + stats.toFormattedString());
    }

}
