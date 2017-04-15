package net.anomalyxii.werewolves.wwesbot.handlers;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.handlers.Command;
import net.anomalyxii.bot.api.server.Messagable;
import net.anomalyxii.bot.api.server.Server;
import net.anomalyxii.bot.api.server.events.MessageEvent;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiException;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiService;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;

public class ListActiveGamesHandlerTest {

    // ******************************
    // Test Methods
    // ******************************

    @Test
    public void handle_should_send_a_list_of_active_games_to_the_sender() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!active");

        ApiService service = mock(ApiService.class);
        when(service.getGameIDs())
                .thenReturn(new GamesList(Arrays.asList("tst-003", "tst-004", "tst-001"), Collections.emptyList()));
        ListActiveGamesHandler handler = new ListActiveGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "(active) Active games are: tst-001, tst-003, tst-004");
    }

    @Test
    public void handle_should_send_an_appropriate_message_if_no_games_are_active() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!active");

        ApiService service = mock(ApiService.class);
        when(service.getGameIDs()).thenReturn(new GamesList(Collections.emptyList(), Collections.emptyList()));
        ListActiveGamesHandler handler = new ListActiveGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "(active) There are no active games...");
    }

    @Test
    public void handle_should_send_an_appropriate_message_if_active_games_could_not_be_fetched() throws Exception {
        // arrange
        Bot bot = mock(Bot.class);
        Server server = mock(Server.class);
        Messagable sender = mock(Messagable.class);
        MessageEvent command = mock(MessageEvent.class);
        when(command.getSender()).thenReturn(sender);
        when(command.isPrivate()).thenReturn(true);
        when(command.getMessageText()).thenReturn("!active");

        ApiService service = mock(ApiService.class);
        when(service.getGameIDs()).thenThrow(new ApiException("You're in a unit test!"));
        ListActiveGamesHandler handler = new ListActiveGamesHandler(service);

        // act
        handler.handle(Command.parse(bot, server, command));

        // assert
        verify(server).say(sender, "Error: Could not retrieve active games: You're in a unit test!");
    }

}