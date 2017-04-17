package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.parser.LiveGameParser;
import net.anomalyxii.werewolves.router.exceptions.RouterException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A {@link Router} implementation that attempts to load
 * archived {@link Game} files from within the project.
 * <p>
 * Created by Anomaly on 05/01/2017.
 */
public class LocalRouter implements Router {

    // ******************************
    // Members
    // ******************************

    private final Path liveRoot;
    private final Path archiveRoot;
    //
    private final LiveGameParser parser = new LiveGameParser();

    // ******************************
    // Constructors
    // ******************************


    public LocalRouter() {
        this(getPackagedLiveGamesRootPath(), getPackagedArchivedGamesRootPath());
    }

    public LocalRouter(Path root) {
        this(root.resolve("live"), root.resolve("archive"));
    }

    public LocalRouter(Path liveRoot, Path archiveRoot) {
        this.liveRoot = liveRoot;
        this.archiveRoot = archiveRoot;
    }

    // ******************************
    // Router Methods
    // ******************************

    @Override
    public GamesList games() throws RouterException {
        try {
            List<String> pending = Collections.emptyList();
            List<String> active = extractGameIDsFromDirectory(liveRoot);
            List<String> complete = extractGameIDsFromDirectory(archiveRoot);

            return new GamesList(active, pending, complete);
        } catch (IOException e) {
            throw new RouterException(e);
        }
    }

    @Override
    public Game game(String id) throws RouterException {

        String filename = String.format("%s.json", id);
        Path file = liveRoot.resolve(filename);
        if (!Files.exists(file))
            file = archiveRoot.resolve(filename);

        if (!Files.exists(file))
            throw new RouterException("Could not find local copy of game '" + id + "'");

        try (InputStream input = Files.newInputStream(file)) {
            return parser.parse(id, input);
        } catch (IOException e) {
            throw new RouterException(e);
        }

    }

    // ******************************
    // Static Helper Methods
    // ******************************

    private static Path getPackagedLiveGamesRootPath() {
        URL url = LocalRouter.class.getClassLoader().getResource("live/");
        if (Objects.isNull(url))
            throw new IllegalArgumentException("Could not find default 'live/' archived games folder");

        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not find default 'live/' archived games folder");
        }
    }

    private static Path getPackagedArchivedGamesRootPath() {
        URL url = LocalRouter.class.getClassLoader().getResource("archive/");
        if (Objects.isNull(url))
            throw new IllegalArgumentException("Could not find default 'archive/' archived games folder");

        try {
            return Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Could not find default 'archive/' archived games folder");
        }
    }

    private static List<String> extractGameIDsFromDirectory(Path path) throws IOException {
        return Files.list(path).map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.endsWith(".json"))
                .map(name -> name.substring(0, name.length() - 5))
                .collect(Collectors.toList());
    }

}
