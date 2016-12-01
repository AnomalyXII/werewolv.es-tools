package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.mockserver.integration.ClientAndProxy;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;

import static org.testng.Assert.*;

/**
 * Test the {@link Router} class
 * <p>
 * Created by Anomaly on 27/11/2016.
 */
public class RouterTest {

    int port = PortFactory.findFreePort();
    private ClientAndServer mockServer;
    private ClientAndProxy proxy;

    // ******************************
    // Set-up & Tear-down Methods
    // ******************************

    @BeforeClass
    public void beforeClass() {
        mockServer = ClientAndServer.startClientAndServer(port);
    }

    @AfterClass
    public void afterClass() {
        mockServer.stop();
        //proxy.stop();
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
                        .withBody("{\"UserName\":\"username\",\"Password\":\"password\"}"))
                .respond(HttpResponse.response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"Token\":\"x12345\"}"));
        Router router = new Router(URI.create("http://localhost:" + port + "/"));

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
                        .withBody("{\"UserName\":\"username\",\"Password\":\"passowrd\"}"))
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
        Router router = new Router(URI.create("http://localhost:" + port + "/"));

        // act
        boolean success = router.login("username", "password");

        // assert
        assertFalse(success);

    }

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
                        .withBody("{\"Active\":[\"ext-001\", \"ext-002\"],\"Pending\":[\"ext-003\"]}"));

        Router router = new Router(URI.create("http://localhost:" + port + "/"), "x12345z");

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
        Router router = new Router(URI.create("http://localhost:" + port + "/"));

        // act
        router.games();

        // assert
        fail("Should have thrown an exception");

    }

    // game


}