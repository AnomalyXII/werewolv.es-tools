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

import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link CommandHandler} that fetches a list of
 * {@link GamesList#getCompletedGameIDs() archived game IDs}
 * from the {@code werewolv.es API}.
 * <p>
 * Created by Anomaly on 09/04/2017.
 */
public class ListArchivedGamesHandler extends AbstractCommandHandler {

    private static final String COMMAND = "!archived";

    // ******************************
    // Members
    // ******************************

    private final GameService service;

    // ******************************
    // Constructors
    // ******************************

    public ListArchivedGamesHandler(GameService service) {
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
        List<String> archivedGameIDs;
        try {
            archivedGameIDs = service.getGameIDs().getCompletedGameIDs();
        } catch (ServiceException e) {
            // We'll send an error message instead...
            throw new Exception("Could not retrieve archived games: " + e.getMessage(), e);
        }

        if (archivedGameIDs.isEmpty()) {
            server.say(target, "(archived) There are no archived games...");
            return;
        }


        Deque<Deque<String>> groupings = new ArrayDeque<>();
        groupings.push(new ArrayDeque<>());

        archivedGameIDs.stream()
                .sorted()
                .distinct()
                .forEach(id -> {
                    Deque<String> ids = groupings.peek();

                    String previousId = ids.peek();
                    if(Objects.isNull(previousId)) {
                        ids.push(id);
                        return;
                    }

                    String[] parts = id.split("-", 2);
                    if(parts.length != 2)
                        return; // Ignore??

                    String[] previousParts = previousId.split("-", 2);

                    String type = parts[0];
                    String previousType = previousParts[0];

                    if(type.equals(previousType)) {
                        int number = Integer.parseInt(parts[1]);
                        int previousNumber = Integer.parseInt(previousParts[1]);
                        if(number == previousNumber + 1) {
                            ids.push(id);
                            return;
                        }
                    }

                    ids = new ArrayDeque<>();
                    ids.addFirst(id);
                    groupings.push(ids);

                });



        StringBuilder games = new StringBuilder();

        Deque<String> run;
        while(Objects.nonNull((run = groupings.poll()))) {
            if(games.length() > 0)
                games.append(", ");

            if(run.size() < 5)
                games.append(run.stream().collect(Collectors.joining(", ")));
            else
                games.append(run.peekFirst()).append("...").append(run.peekLast());
        }

        server.say(target, "(archived) Archived games are: " + games);
    }

}
