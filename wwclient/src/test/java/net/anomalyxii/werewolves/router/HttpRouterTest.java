package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mockserver.MockServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

/**
 * Test the {@link HttpRouter} class
 * <p>
 * Created by Anomaly on 27/11/2016.
 */
public class HttpRouterTest {

    private int port = PortFactory.findFreePort();
    private ClientAndServer mockServer;

    // ******************************
    // Set-up & Tear-down Methods
    // ******************************

    @BeforeClass
    public void beforeClass() {
        mockServer = ClientAndServer.startClientAndServer(port);
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        mockServer.reset();
    }

    @AfterClass
    public void afterClass() {
        mockServer.stop();
    }

    // ******************************
    // Test Methods
    // ******************************

    // login

    @Test
    public void login_should_authenticate_the_user() throws RouterException {

        // arrange
        mockServer // Mock the HTTP request
                .when(HttpRequest.request()
                        .withPath("/api/Account/Login")
                        .withMethod("POST")
                        .withBody("{\"userName\":\"username\",\"password\":\"password\"}"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"accessToken\":\"x12345\",\"tokenType\":\"bearer\",\"expiresIn\":360000}"));
        Router router = new HttpRouter(URI.create("http://localhost:" + port + "/"));

        // act
        boolean success = router.login("username", "password");

        // assert
        assertTrue(success);

    }

    @Test
    public void login_should_handle_authentication_failures() throws RouterException {

        // arrange
        mockServer // Mock the HTTP request
                .when(HttpRequest.request()
                        .withPath("/api/Account/Login")
                        .withMethod("POST")
                        .withBody("{\"userName\":\"username\",\"password\":\"password\"}"))
                .respond(HttpResponse.response()
                        .withStatusCode(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n" +
                                  "    \"Message\": \"The request is invalid.\",\n" +
                                  "    \"ModelState\": {\n" +
                                  "        \"Credentials\": [\n" +
                                  "            \"The user name or password provided is incorrect.\"\n" +
                                  "        ]\n" +
                                  "    }\n" +
                                  "}"));
        Router router = new HttpRouter(URI.create("http://localhost:" + port + "/"));

        // act
        boolean success = router.login("username", "password");

        // assert
        assertFalse(success);

    }

    // oauth



    // games

    @Test
    public void games_should_return_a_list_of_active_and_pending_games() throws RouterException {

        // arrange
        mockServer // Mock the HTTP request
                .when(HttpRequest.request()
                        .withPath("/api/Game")
                        .withMethod("GET")
                        .withHeader("Authorization", "Bearer x12345z"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"active\":[\"ext-001\", \"ext-002\"],\"pending\":[\"ext-003\"]}"));

        Router router = new HttpRouter(URI.create("http://localhost:" + port + "/"), "x12345z");

        // act
        Games games = router.games();

        // assert
        assertNotNull(games);
        assertEquals(games.getActiveGameIDs(), Arrays.asList("ext-001", "ext-002"));
        assertEquals(games.getPendingGameIDs(), Collections.singletonList("ext-003"));

    }

    @Test(expectedExceptions = RouterException.class)
    public void games_should_handle_authentication_failures() throws RouterException {

        // arrange
        Router router = new HttpRouter(URI.create("http://localhost:" + port + "/"));

        // act
        router.games();

        // assert
        fail("Should have thrown an exception");

    }

    // game

    @Test
    public void game_should_correctly_parse_a_game() throws Exception {

        // arrange
        InputStream resource = getClass().getClassLoader().getResourceAsStream("live/ext-090.json");
        assertNotNull(resource);

        String response;
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resource))) {
             response = buffer.lines().collect(Collectors.joining("\n"));
        }

        mockServer // Mock the HTTP request
                .when(HttpRequest.request()
                              .withPath("/api/Game/ext-090")
                              .withMethod("GET")
                              .withHeader("Authorization", "Bearer x12345z"))
                .respond(HttpResponse.response()
                                 .withStatusCode(200)
                                 .withHeader("Content-Type", "application/json;charset=UTF-8")
                                 .withBody(response));

        Router router = new HttpRouter(URI.create("http://localhost:" + port + "/"), "x12345z");

        // act
        Game game = router.game("ext-090");

        // assert
        assertNotNull(game);
        assertEquals(game.getUsers().size(), 20);
        assertEquals(game.getCharacters().size(), 13);
        assertEquals(game.getDays().size(), 4);
        assertEquals(game.getWinningAlignment(), Alignment.WEREWOLVES);

        // Pre- and Post-Game
        assertEquals(game.getPreGameEvents().size(), 118);
        assertEquals(game.getPostGameEvents().size(), 96);
        // Day 1 and Night 1
        assertEquals(game.getDay(0).getDayPhase().getEvents().size(), 30);
        assertTrue(game.getDay(0).getDayPhase().isComplete());
        assertEquals(game.getDay(0).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(0).getNightPhase().isComplete());
        // Day 1 and Night 2
        assertEquals(game.getDay(1).getDayPhase().getEvents().size(), 333);
        assertTrue(game.getDay(1).getDayPhase().isComplete());
        assertEquals(game.getDay(1).getNightPhase().getEvents().size(), 3);
        assertTrue(game.getDay(1).getNightPhase().isComplete());
        // Day 1 and Night 3
        assertEquals(game.getDay(2).getDayPhase().getEvents().size(), 354);
        assertTrue(game.getDay(2).getDayPhase().isComplete());
        assertEquals(game.getDay(2).getNightPhase().getEvents().size(), 13);
        assertTrue(game.getDay(2).getNightPhase().isComplete());
        // Day 1 and Night 4
        assertEquals(game.getDay(3).getDayPhase().getEvents().size(), 288);
        assertTrue(game.getDay(3).getDayPhase().isComplete());
        assertEquals(game.getDay(3).getNightPhase().getEvents().size(), 1);
        assertTrue(game.getDay(3).getNightPhase().isComplete());

    }


}