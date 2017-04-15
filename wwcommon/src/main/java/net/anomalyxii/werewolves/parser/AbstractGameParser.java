package net.anomalyxii.werewolves.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.events.Event;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.function.Function;

/**
 * An abstract {@link GameParser} that deals with most of the
 * common bits and pieces (like deserialization, etc) but
 * defers the actual {@link Event} parsing to the
 * implementations.
 * <p>
 * Created by Anomaly on 05/01/2017.
 */
public abstract class AbstractGameParser implements GameParser<Map<String, Object>> {

    private static final ObjectMapper GLOBAL_OBJECT_MAPPER = new ObjectMapper();

    // ******************************
    // Members
    // ******************************

    private final Function<String, GameContext> factory;
    private ObjectMapper objectMapper;

    // ******************************
    // Constructors
    // ******************************

    public AbstractGameParser(Function<String, GameContext> gameContextSupplier) {
        this.factory = gameContextSupplier;
    }

    // ******************************
    // GameParser Methods
    // ******************************

    @Override
    public Game parse(String id, Path file) throws IOException {
        try (InputStream in = Files.newInputStream(file)) {
            return parse(id, in);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Game parse(String id, InputStream in) throws IOException {
        List<Map<String, Object>> events = getObjectMapper().readValue(in, List.class);
        return parse(id, events);
    }

    @Override
    public Game parse(String id, List<? extends Map<String, Object>> events) {
        GameContext context = factory.apply(id);
        events.forEach(context::process);
        return context.build();
    }

    // ******************************
    // Helper Methods
    // ******************************

    /**
     * Get an {@link ObjectMapper} that can be used by this
     * {@link GameParser} to deserialize {@link Event} data.
     *
     * @return an {@link ObjectMapper}
     */
    protected ObjectMapper getObjectMapper() {
        if(Objects.nonNull(objectMapper))
            return objectMapper;
        return GLOBAL_OBJECT_MAPPER;
    }

    /**
     * Set an {@link ObjectMapper} that can be used by this
     * {@link GameParser} to deserialize {@link Event} data.
     *
     * @param objectMapper an {@link ObjectMapper}
     */
    protected void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
