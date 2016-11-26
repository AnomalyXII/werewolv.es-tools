package net.anomalyxii.werewolves;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.RouterRequest;
import net.anomalyxii.werewolves.router.RouterResponse;
import net.anomalyxii.werewolves.router.exceptions.RequestSerialisationException;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import net.anomalyxii.werewolves.router.exceptions.UnsupportedContentTypeException;
import net.anomalyxii.werewolves.router.request.EmptyRequest;
import net.anomalyxii.werewolves.router.request.LoginRequest;
import net.anomalyxii.werewolves.router.response.GameResponse;
import net.anomalyxii.werewolves.router.response.GamesResponse;
import net.anomalyxii.werewolves.router.response.LoginResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;

/**
 * Created by Anomaly on 20/11/2016.
 */
public class Router {

    private static final Logger logger = LoggerFactory.getLogger(Router.class);

    // ******************************
    // Members
    // ******************************

    // Server-related Members

    private final URI host;
    private String auth;

    // Connection-related Members

    // ******************************
    // Constructors
    // ******************************

    public Router() {
        this(URI.create("http://werewolv.es"));
    }

    public Router(String auth) {
        this(URI.create("http://werewolv.es"), auth);
    }

    public Router(URI host) {
        this(host, null);
    }

    public Router(URI host, String auth) {
        this.host = host;
        this.auth = auth;
    }

    // ******************************
    // Stuff
    // ******************************

    public boolean login(String username, String password) throws RouterException {

        logger.debug("Request /api/Account/Login - started");
        LoginResponse loginResponse = routeAndParse(
                new LoginRequest(host, "/api/Account/Login", username, password),
                LoginResponse.deserialisation());

        auth = loginResponse.getContent()
                .map(LoginResponse.Body::getToken)
                .orElseThrow(() -> new RouterException("Login Failed")); // Todo: make this a better exception
        System.out.println(auth);

        logger.debug("Request /api/Account/Login - succeeded");
        return true;

    }

    public Games games() throws RouterException {

        // Fetch the Game
        logger.debug("Request /api/Game - started");
        if (auth == null)
            throw new RouterException("Not logged in!"); // Todo: make this a better exception

        RouterRequest<?> request = new EmptyRequest(host, "/api/Game");
        request.addHeader(new BasicHeader("Authorization", "Bearer " + auth));
        GamesResponse gameResponse = routeAndParse(request, GamesResponse.deserialisation());

        logger.debug("Request /api/Game - succeeded");

        return gameResponse.getContent()
                .map(content -> new Games(content.getActive(), content.getPending()))
                .orElseThrow(() -> new RouterException("Games not retrieved")); // Todo: make this a better exception

    }

    public Game game(String id) throws RouterException {

        // Fetch the Game
        logger.debug("Request /api/Game/{} - started", id);
        if (auth == null)
            throw new RouterException("Not logged in!"); // Todo: make this a better exception

        RouterRequest<?> request = new EmptyRequest(host, "/api/Game/" + id);
        request.addHeader(new BasicHeader("Authorization", "Bearer " + auth));
        GameResponse gameResponse = routeAndParse(request, GameResponse.deserialisation());

        logger.debug("Request /api/Game/{} - succeeded", id);

        return gameResponse.getContent()
                .map(GameResponse.Body::toGame)
                .orElseThrow(() -> new RouterException("Game not retrieved")); // Todo: make this a better exception

    }

    // ******************************
    // Router Methods
    // ******************************

    protected <T extends RouterResponse<?>> T routeAndParse(RouterRequest request, DeserialisationCallback<T> callback)
            throws RouterException {
        ObjectMapper objectMapper = new ObjectMapper();

        // Transmit
        Response response = transmit(request, objectMapper);

        // Validate
        HttpResponse httpResponse = validate(request, response);

        return callback.deserialise(httpResponse, objectMapper);
    }

    protected Response transmit(String path, RouterRequest request) throws RouterException {
        return transmit(request, new ObjectMapper());
    }

    protected Response transmit(RouterRequest<?> request, ObjectMapper objectMapper) throws RouterException {
        try {
            // Create the request
            Request httpRequest = request.usePost()
                                  ? Request.Post(request.getURI())
                                  : Request.Get(request.getURI());

            // Set the HTTP Headers (if any)
            request.getHeaders().forEach(httpRequest::addHeader);

            // Todo: we need to make the content type configurable for when we can support multiple serialisations
            if (request.getContent().isPresent())
                httpRequest.bodyString(serialiseRequest(request, objectMapper), ContentType.APPLICATION_JSON);

            // Go go gooooooo!
            return httpRequest.execute();
        } catch (IOException e) {
            throw new RouterException(e); // Todo: make this a better exception
        }
    }

    protected HttpResponse validate(Response response) throws RouterException {

        HttpResponse httpResponse;
        try {
            httpResponse = response.returnResponse();
        } catch (IOException e) {
            logger.debug("Request failed", e);
            throw new RouterException(e); // Todo: improve this exception
        }

        // Todo: handle "error" codes
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode != 200)
            // Todo: handle this gracefully
            throw new RouterException("Unexpected status code: " + statusCode);

        return httpResponse;

    }

    protected HttpResponse validate(RouterRequest request, Response response) throws RouterException {

        HttpResponse httpResponse;
        try {
            httpResponse = response.returnResponse();
        } catch (IOException e) {
            logger.debug("Request failed", e);
            throw new RouterException(e); // Todo: improve this exception
        }

        // Todo: handle "error" codes
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (!request.getAcceptedStatusCodes().contains(statusCode))
            // Todo: handle this gracefully
            throw new RouterException("Unexpected status code: " + statusCode);

        return httpResponse;

    }

    // ******************************
    // Serialisation Methods
    // ******************************

    /**
     * Serialise the given {@link RouterRequest}
     * <p>
     * Todo: support more than JSON!!
     *
     * @param request the {@link RouterRequest} to serialise
     * @return the serialised {@link RouterRequest}
     * @throws RouterException if anything goes wrong during serialisation
     */
    protected String serialiseRequest(RouterRequest request) throws RouterException {
        return serialiseRequest(request, new ObjectMapper());
    }

    /**
     * Serialise the given {@link RouterRequest} using an
     * existing {@link ObjectMapper}
     * <p>
     * Todo: support more than JSON!!
     *
     * @param request      the {@link RouterRequest} to serialise
     * @param objectMapper the {@link ObjectMapper} to use for serialisation
     * @return the serialised {@link RouterRequest}
     * @throws RouterException if anything goes wrong during serialisation
     */
    protected String serialiseRequest(RouterRequest request, ObjectMapper objectMapper) throws RouterException {
        // Fail (for now) if we can't serialise into JSON
        if (!request.getSupportedContentTypes().contains(ContentType.APPLICATION_JSON))
            throw new UnsupportedContentTypeException(
                    Collections.singletonList(ContentType.APPLICATION_JSON),
                    request.getSupportedContentTypes()
            );

        try {
            return objectMapper.writeValueAsString(request.getContent().get());
        } catch (JsonProcessingException e) {
            logger.debug("Could not serialise request", e);
            throw new RequestSerialisationException(e, request);
        }
    }

    // ******************************
    // Helper Methods
    // ******************************

    // Anything?

}
