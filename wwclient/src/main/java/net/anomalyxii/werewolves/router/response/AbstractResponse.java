package net.anomalyxii.werewolves.router.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.router.DeserialisationCallback;
import net.anomalyxii.werewolves.router.RouterResponse;
import net.anomalyxii.werewolves.router.XmlOrJsonSupport;
import net.anomalyxii.werewolves.router.exceptions.ResponseDeserialisationException;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Anomaly on 22/11/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class AbstractResponse<T> implements RouterResponse<T>, XmlOrJsonSupport {

    // ******************************
    // Members
    // ******************************

    private final int statusCode;
    private final T content;
    private final List<Header> headers = new ArrayList<>();

    // ******************************
    // Constructors
    // ******************************

    public AbstractResponse(int statusCode, T content) {
        this.statusCode = statusCode;
        this.content = content;
    }

    // ******************************
    // RouterResponse Methods
    // ******************************

    @Override
    public List<Header> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Optional<T> getContent() {
        return content != null
               ? Optional.of(content)
               : Optional.empty();
    }

    // ******************************
    // Content Class
    // ******************************

    protected interface Body {
    }

    protected static class DefaultBody implements Body {

        // Members

        private String message;
        private Map<String, Object> modelState;

        // Constructors

        protected DefaultBody() {
        }

        protected DefaultBody(String message, Map<String, Object> modelState) {
            this.message = message;
            this.modelState = modelState;
        }

        // Getters & Setters

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Map<String, Object> getModelState() {
            return modelState;
        }

        public void setModelState(Map<String, Object> modelState) {
            this.modelState = modelState;
        }

    }

    // ******************************
    // Content Class
    // ******************************

    protected static int getStatusCode(HttpResponse response) throws ResponseDeserialisationException {
        return response.getStatusLine().getStatusCode();
    }

    protected static <T> T deserialise(HttpResponse response, ObjectMapper objectMapper, Class<T> clazz) throws ResponseDeserialisationException {
        try {
            return objectMapper.readValue(getInputStream(response), clazz);
        } catch (IOException e) {
            throw new ResponseDeserialisationException(e, null);
        }
    }

    protected static InputStream getInputStream(HttpResponse response) throws ResponseDeserialisationException {
        try {
            return response.getEntity().getContent();
        } catch (IOException e) {
            throw new ResponseDeserialisationException(e, null);
        }
    }

}
