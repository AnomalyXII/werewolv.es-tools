package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.handlers.Command;
import net.anomalyxii.bot.api.handlers.CommandHandler;
import net.anomalyxii.bot.api.server.Messagable;
import net.anomalyxii.bot.api.server.Server;
import net.anomalyxii.bot.api.server.events.MessageEvent;
import net.anomalyxii.bot.impl.handlers.AbstractCommandHandler;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link CommandHandler} that fetches a list of
 * {@link GamesList#getPendingGameIds() pending game IDs}
 * from the {@code werewolv.es API}.
 * <p>
 * Created by Anomaly on 09/04/2017.
 */
public class ListPendingGamesHandler extends AbstractCommandHandler {

    private static final String COMMAND = "!pending";

    // ******************************
    // Members
    // ******************************

    private final GameService service;

    // ******************************
    // Constructors
    // ******************************

    public ListPendingGamesHandler(GameService service) {
        this.service = service;
    }

    // ******************************
    // CommandHandler Methods
    // ******************************

    @Override
    protected String getCommand() {
        return COMMAND;
    }

    @Override
    public void handleCommand(Command command) throws Exception {
        Server server = command.getServer();
        MessageEvent message = command.getMessageEvent();
        Messagable target = message.isPrivate() ? message.getSender() : message.getTarget();
        List<String> pendingGameIDs;
        try {
            pendingGameIDs = service.getGameIds().getPendingGameIds();
        } catch (ServiceException e) {
            // We'll send an error message instead...
            throw new Exception("Could not retrieve pending games: " + e.getMessage(), e);
        }

        if (pendingGameIDs.isEmpty()) {
            server.say(target, "(pending) There are no pending games...");
            return;
        }

        String games = pendingGameIDs.stream()
                .sorted()
                .distinct()
                .collect(Collectors.joining(", "));
        server.say(target, "(pending) Pending games are: " + games);
    }

}
