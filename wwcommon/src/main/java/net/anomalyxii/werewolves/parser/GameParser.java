package net.anomalyxii.werewolves.parser;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.events.Event;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * A parser for turning a {@link List} of "events" into a
 * {@link Game}.
 *
 * Created by Anomaly on 05/01/2017.
 */
public interface GameParser<T> {

    // ******************************
    // Interface Methods
    // ******************************

    /**
     * Parse a {@link Game}.
     *
     * @param id   the {@link Game} ID
     * @param file the {@link Path} containing the {@link Game} {@link Event Events}
     * @return the parsed {@link Game}
     */
    Game parse(String id, Path file) throws IOException;

    /**
     * Parse a {@link Game}.
     *
     * @param id the {@link Game} ID
     * @param in the {@link InputStream} containing the {@link Game} {@link Event Events}
     * @return the parsed {@link Game}
     */
    Game parse(String id, InputStream in) throws IOException;

    /**
     * Parse a {@link Game}.
     *
     * @param id     the {@link Game} ID
     * @param events the {@link Game} {@link Event Events}
     * @return the parsed {@link Game}
     */
    Game parse(String id, List<? extends T> events);

}
