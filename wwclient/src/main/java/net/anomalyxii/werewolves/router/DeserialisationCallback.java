package net.anomalyxii.werewolves.router;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Response;

import java.io.IOException;

/**
 * Created by Anomaly on 24/11/2016.
 */
@FunctionalInterface
public interface DeserialisationCallback<T extends RouterResponse<?>> {

    // ******************************
    // Interface Methods
    // ******************************

    T deserialise(HttpResponse response, ObjectMapper objectMapper) throws RouterException;

    // ******************************
    // Default Methods
    // ******************************

    default T deserialise(Response response) throws RouterException {
        return deserialise(response, new ObjectMapper());
    }

    default T deserialise(Response response, ObjectMapper objectMapper) throws RouterException {
        try {
            return deserialise(response.returnResponse(), objectMapper);
        } catch(IOException e) {
            // Todo: improve this exception
            throw new RouterException(e);
        }
    }

    default T deserialise(HttpResponse response) throws RouterException {
        return deserialise(response, new ObjectMapper());
    }

}
