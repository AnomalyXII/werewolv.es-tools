package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.router.http.UnhappyHttpResponseException;
import net.anomalyxii.werewolves.router.http.auth.BearerAuth;
import net.anomalyxii.werewolves.router.http.request.EmptyHttpRouterRequest;
import net.anomalyxii.werewolves.router.http.request.HttpRouterRequest;
import net.anomalyxii.werewolves.router.http.request.StandardHttpRouterRequest;
import net.anomalyxii.werewolves.router.http.request.account.LoginRequestBean;
import net.anomalyxii.werewolves.router.http.response.HttpRouterResponse;
import net.anomalyxii.werewolves.router.http.response.account.LoginResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameListResponseBean;
import org.mockserver.integration.ClientAndServer;
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

        LoginRequestBean requestBean = new LoginRequestBean("username", "password");
        HttpRouterRequest<?> request = new StandardHttpRouterRequest<>(URI.create("http://localhost:" + port + "/api/Account/Login"), null, requestBean);


        HttpRouter router = new HttpRouter();

        // act
        HttpRouterResponse<LoginResponseBean> response = router.submit(request, LoginResponseBean.class);

        // assert
        assertNotNull(response);
        assertEquals(response.getCode(), 200);
        assertEquals(response.getContent().get().getAccessToken(), "x12345");

    }

    @Test(expectedExceptions = UnhappyHttpResponseException.class)
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
                                  "    \"message\": \"The request is invalid.\",\n" +
                                  "    \"modelState\": {\n" +
                                  "        \"model.Password\": [\n" +
                                  "            \"The Password field is required.\"\n" +
                                  "        ]\n" +
                                  "    }\n" +
                                  "}"));
        LoginRequestBean requestBean = new LoginRequestBean("username", "password");
        HttpRouterRequest<?> request = new StandardHttpRouterRequest<>(URI.create("http://localhost:" + port + "/api/Account/Login"), null, requestBean);


        HttpRouter router = new HttpRouter();

        // act
        router.submit(request, LoginResponseBean.class);

        // assert
        fail("Should have thrown an exception");
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
                        .withBody("{\"active\":[\"ext-001\", \"ext-002\"],\"pending\":[\"ext-003\"]}"));

        HttpRouterRequest<?> request = new EmptyHttpRouterRequest<>(URI.create("http://localhost:" + port + "/api/Game"), new BearerAuth("x12345z"));
        HttpRouter router = new HttpRouter();

        // act
        HttpRouterResponse<GameListResponseBean> response = router.submit(request, GameListResponseBean.class);

        // assert
        assertNotNull(response);
        assertEquals(response.getCode(), 200);
        assertEquals(response.getContent().get().getActive(), Arrays.asList("ext-001", "ext-002"));
        assertEquals(response.getContent().get().getPending(), Collections.singletonList("ext-003"));

    }

    @Test(expectedExceptions = UnhappyHttpResponseException.class)
    public void games_should_handle_authentication_failures() throws RouterException {

        // arrange
        mockServer // Mock the HTTP request
                .when(HttpRequest.request()
                              .withPath("/api/Game")
                              .withMethod("GET")
                              .withHeader("Authorization", "Bearer x12345z"))
                .respond(HttpResponse.response()
                                 .withStatusCode(401)
                                 .withHeader("Content-Type", "application/json")
                                 .withBody("{\"message\": \"Authorization has been denied for this request.\"}"));

        HttpRouterRequest<?> request = new EmptyHttpRouterRequest<>(URI.create("http://localhost:" + port + "/api/Game"), new BearerAuth("x12345z"));
        HttpRouter router = new HttpRouter();

        // act
        router.submit(request, GameListResponseBean.class);

        // assert
        fail("Should have thrown an exception");
    }

}