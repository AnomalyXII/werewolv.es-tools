package net.anomalyxii.werewolves;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.router.Router;
import net.anomalyxii.werewolves.router.RouterBuilder;
import org.apache.commons.cli.ParseException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.fail;

/**
 * Test the {@link Main} class
 *
 * Created by Anomaly on 27/11/2016.
 */
public class MainTest {

    // ******************************
    // DataProvider Methods
    // ******************************

    @DataProvider(name = "invalid")
    public Object[][] invalidData() {
        return new Object[][]{
                { new String[0] },
                {"-U", "-g", "ext-001"},
                {"-U", "username", "-P", "-g", "ext-001"},
                {"-U", "username", "-P", "password", "-T", "x12345z", "-g", "ext-001"},
                {"-T", "-g", "ext-001"},
                {"-T", "x12345z",},
                {"-T", "x12345z", "-g"},
                {"-T", "x12345z", "-g"},
        };
    }

    // ******************************
    // Test Methods
    // ******************************

    @Test
    public void main_should_accept_valid_UsernamePassword_arguments() throws Exception {

        // arrange
        // Mock the router so that we don't need to do actual HTTP requests
        Router router = mock(Router.class);
        when(router.games()).thenReturn(new Games(Collections.singletonList("ext-001"), Collections.emptyList()));
        when(router.game(anyString())).thenReturn(new Game("ext-001"));

        RouterBuilder builder = mock(RouterBuilder.class);
        when(builder.forCredentials(anyString(), anyString())).thenReturn(router);

        Main main = new Main(builder);

        // act
        main.run("-U", "username", "-P", "password", "-g", "ext-001");

        // assert
        verify(builder).forCredentials("username", "password");
        verify(router).game("ext-001");

    }

    // Todo: test -U without -P (mock System.console()?)

    @Test
    public void main_should_accept_valid_Token_arguments() throws Exception {

        // arrange
        // Mock the router so that we don't need to do actual HTTP requests
        Router router = mock(Router.class);
        when(router.games()).thenReturn(new Games(Collections.singletonList("ext-001"), Collections.emptyList()));
        when(router.game(anyString())).thenReturn(new Game("ext-001"));

        RouterBuilder builder = mock(RouterBuilder.class);
        when(builder.forToken(anyString())).thenReturn(router);

        Main main = new Main(builder);

        // act
        main.run("-T", "x12345z", "-g", "ext-001");

        // assert
        verify(builder).forToken("x12345z");
        verify(router).game("ext-001");

    }

    @Test
    public void main_should_accept_valid_Local_arguments() throws Exception {

        // arrange
        // Mock the router so that we don't need to do actual HTTP requests
        Router router = mock(Router.class);
        when(router.games()).thenReturn(new Games(Collections.singletonList("ext-001"), Collections.emptyList()));
        when(router.game(anyString())).thenReturn(new Game("ext-001"));

        RouterBuilder builder = mock(RouterBuilder.class);
        when(builder.forLocalGame()).thenReturn(router);

        Main main = new Main(builder);

        // act
        main.run("-L", "-g", "ext-001");

        // assert
        verify(builder).forLocalGame();
        verify(router).game("ext-001");

    }

    @Test
    public void main_should_accept_valid_Archived_arguments() throws Exception {

        // arrange
        // Mock the router so that we don't need to do actual HTTP requests
        Router router = mock(Router.class);
        when(router.games()).thenReturn(new Games(Collections.singletonList("ext-001"), Collections.emptyList()));
        when(router.game(anyString())).thenReturn(new Game("ext-001"));

        RouterBuilder builder = mock(RouterBuilder.class);
        when(builder.forArchivedGame(anyString())).thenReturn(router);

        Main main = new Main(builder);

        // act
        main.run("-A", "-U", "username", "-g", "ext-001");

        // assert
        verify(builder).forArchivedGame("username");
        verify(router).game("ext-001");

    }

    @Test(dataProvider = "invalid", expectedExceptions = ParseException.class)
    public void main_should_reject_invalid_arguments(String... args) throws Exception {

        // arrange
        // Mock the router so that we don't need to do actual HTTP requests
        Router router = mock(Router.class);
        when(router.games()).thenReturn(new Games(Collections.singletonList("ext-001"), Collections.emptyList()));
        when(router.game(anyString())).thenReturn(new Game("ext-001"));

        RouterBuilder builder = mock(RouterBuilder.class);
        when(builder.forToken(anyString())).thenReturn(router);
        when(builder.forCredentials(anyString(), anyString())).thenReturn(router);
        when(builder.forLocalGame()).thenReturn(router);
        when(builder.forArchivedGame(anyString())).thenReturn(router);

        Main main = new Main(builder);

        // act
        main.run(args);

        // assert
        fail("Should have thrown an exception");

    }

}