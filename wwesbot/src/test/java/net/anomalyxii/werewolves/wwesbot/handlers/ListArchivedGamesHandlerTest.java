package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.Command;
import net.anomalyxii.bot.api.server.Messagable;
import net.anomalyxii.bot.api.server.Server;
import net.anomalyxii.bot.api.server.events.MessageEvent;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.services.GameService;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class ListArchivedGamesHandlerTest {

    // ******************************
    // Test Methods
    // ******************************

    @Test
    public void handle_should_send_a_list_of_archived_games_to_the_sender() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!archived");

        List<String> completed = Arrays.asList("tst-003",
                                               "tst-004",
                                               "tst-005",
                                               "tst-006",
                                               "tst-007",
                                               "tst-008",
                                               "tst-009",
                                               "tst-010",
                                               "tst-015",
                                               "tst-016",
                                               "tst-017",
                                               "tst-018",
                                               "exp-001",
                                               "exp-002",
                                               "exp-003",
                                               "exp-004",
                                               "exp-005");

        GameService service = mock(GameService.class);
        when(service.getGameIDs())
                .thenReturn(new GamesList(Collections.emptyList(), Collections.emptyList(), completed));
        ListArchivedGamesHandler handler = new ListArchivedGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "(archived) Archived games are: exp-001...exp-005, tst-003...tst-010, tst-015, tst-016, tst-017, tst-018");
    }

    @Test
    public void handle_should_send_an_appropriate_message_if_no_games_are_archived() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!archived");

        GameService service = mock(GameService.class);
        when(service.getGameIDs()).thenReturn(new GamesList(Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
        ListArchivedGamesHandler handler = new ListArchivedGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "(archived) There are no archived games...");
    }

}