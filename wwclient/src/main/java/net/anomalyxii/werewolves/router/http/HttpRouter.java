package net.anomalyxii.werewolves.router.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.router.*;
import net.anomalyxii.werewolves.router.RouterException;
import net.anomalyxii.werewolves.router.http.response.EmptyHttpRouterResponse;
import net.anomalyxii.werewolves.router.http.response.HttpRouterResponse;
import net.anomalyxii.werewolves.router.http.response.ResponseBean;
import net.anomalyxii.werewolves.router.http.response.StandardHttpRouterResponse;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Objects;

/**
 * A {@link Router} implementation that accesses the
 * {@code werewolv.es API} via standard {@code HTTP} requests.
 * <p>
 * Created by Anomaly on 20/11/2016.
 */
public class HttpRouter implements Router {

    private static final Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    // ******************************
    // Members
    // ******************************

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ******************************
    // Router Methods
    // ******************************

    @Override
    public HttpRouterResponse<Void> submit(RouterRequest<?> request) throws RouterException {
        try {
            return doSubmit(request, null);
        } catch (UncheckedIOException e) {
            throw new RouterException(e.getMessage(), e.getCause());
        } catch (RouterException e) {
            throw e;
        } catch (Exception e) {
            throw new RouterException("Failed to submit request: " + e.getMessage(), e);
        }
    }

    @Override
    public <T> HttpRouterResponse<T> submit(RouterRequest<?> request, Class<T> responseClass) throws RouterException {
        try {
            return doSubmit(request, responseClass);
        } catch (UncheckedIOException e) {
            throw new RouterException(e.getMessage(), e.getCause());
        } catch (RouterException e) {
            throw e;
        } catch (Exception e) {
            throw new RouterException("Failed to submit request: " + e.getMessage(), e);
        }
    }

    // ******************************
    // Request Dispatch Methods
    // ******************************

    /**
     * Submit a request to the provided {@link URI} and handle the
     * response that is returned.
     *
     * @param request       the {@link URI} endpoint
     * @param responseClass the target {@link Class} to deserialize the response body into
     * @return the deserialized response body
     */
    protected <T> HttpRouterResponse<T> doSubmit(RouterRequest<?> request, Class<T> responseClass) throws Exception {

        URI endpoint = request.getURI();
        logger.debug("Submitting HTTP GET to '{}'", endpoint);
        Request httpRequest = isUsePost(request) ? Request.Post(endpoint) : Request.Get(endpoint);

        httpRequest.addHeader("Accept", "application/json");
        httpRequest.addHeader("Content-Type", "application/json");

        request.getAuth().ifPresent(auth -> httpRequest.addHeader(new BasicHeader("Authorization", auth.getAuthorizationString())));
        request.getContent().ifPresent(requestContent -> httpRequest.bodyString(serializeRequest(requestContent, objectMapper), ContentType.APPLICATION_JSON));

        // Validate
        Response response = httpRequest.execute();
        logger.debug("Successfully retrieved result from '{}'", endpoint);

        HttpResponse httpResponse = response.returnResponse();

        int statusCode = httpResponse.getStatusLine().getStatusCode();
        String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
        logger.debug("Response -> [{} -> {}]", statusCode, reasonPhrase);

        if (statusCode != 200) {
            InputStream responseBody = httpResponse.getEntity().getContent();
            ResponseBean deserializedResponse = deserializeResponse(responseBody, objectMapper, ResponseBean.class);
            throw new UnhappyHttpResponseException(statusCode, deserializedResponse.getMessage(), deserializedResponse.getModelState());
        }

        if (Objects.isNull(responseClass))
            return new EmptyHttpRouterResponse<>(statusCode, reasonPhrase);

        InputStream responseBody = httpResponse.getEntity().getContent();
        T deserializedResponse = deserializeResponse(responseBody, objectMapper, responseClass);
        return new StandardHttpRouterResponse<>(statusCode, reasonPhrase, deserializedResponse);

    }

    // ******************************
    // Serialisation & Deserialization
    // ******************************

    /**
     * Serialise the given {@link Object} to be transmitted as the
     * body of the {@link Request}.
     * <p>
     * Todo: support more than JSON?
     *
     * @param body         the {@link Object} to serialize
     * @param objectMapper the {@link ObjectMapper} to use for serialisation
     * @return the serialized {@link Object}
     */
    protected static String serializeRequest(Object body, ObjectMapper objectMapper) {
        try {
            return objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            logger.debug("Could not serialize body", e);
            throw new UncheckedIOException("Could not serialize body", e);
        }
    }

    /**
     * Deserialize the given {@link String} received in the body
     * of the {@link Response}.
     * <p>
     * Todo: support more than JSON?
     *
     * @param body         the {@link String} to deserialize
     * @param objectMapper the {@link ObjectMapper} to use for serialisation
     * @param target       the target {@link Class} to deserialize into
     * @return the deserialized {@link Object}
     * @throws RouterException if anything goes wrong during deserialisation
     */
    protected static <T> T deserializeResponse(InputStream body, ObjectMapper objectMapper, Class<T> target) throws RouterException {
        try {
            return objectMapper.readValue(body, target);
        } catch (IOException e) {
            logger.debug("Could not deserialize body", e);
            throw new ResponseDeserializationException(e);
        }
    }

    // ******************************
    // Helper Methods
    // ******************************

    /*
     * Check if this RouterRequest should be submitted via
     * GET or POST.
     *
     * POST should be used for any requests that contain a
     * content body.
     */
    private static boolean isUsePost(RouterRequest<?> request) {
        return request.getContent().isPresent();
    }

}
