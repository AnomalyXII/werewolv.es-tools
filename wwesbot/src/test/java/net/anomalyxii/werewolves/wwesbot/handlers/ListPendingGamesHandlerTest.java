package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.Command;
import net.anomalyxii.bot.api.server.Messagable;
import net.anomalyxii.bot.api.server.Server;
import net.anomalyxii.bot.api.server.events.MessageEvent;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class ListPendingGamesHandlerTest {

    // ******************************
    // Test Methods
    // ******************************

    @Test
    public void handle_should_send_a_list_of_pending_games_to_the_sender() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!pending");

        GameService service = mock(GameService.class);
        when(service.getGameIds())
                .thenReturn(new GamesList(Collections.emptyList(), Arrays.asList("tst-003", "tst-004", "tst-001")));
        ListPendingGamesHandler handler = new ListPendingGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "(pending) Pending games are: tst-001, tst-003, tst-004");
    }

    @Test
    public void handle_should_send_an_appropriate_message_if_no_games_are_pending() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!pending");

        GameService service = mock(GameService.class);
        when(service.getGameIds()).thenReturn(new GamesList(Collections.emptyList(), Collections.emptyList()));
        ListPendingGamesHandler handler = new ListPendingGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "(pending) There are no pending games...");
    }

    @Test
    public void handle_should_send_an_appropriate_message_if_pending_games_could_not_be_fetched() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!pending");

        GameService service = mock(GameService.class);
        when(service.getGameIds()).thenThrow(new ServiceException("You're in a unit test!"));
        ListPendingGamesHandler handler = new ListPendingGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "Error: Could not retrieve pending games: You're in a unit test!");
    }

}