package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.parser.ArchivedGameParser;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A {@link Router} that fetches archived {@link GamesList} from
 * the "salt mine" (a {@link Git} repository).
 * <p>
 * Created by Anomaly on 16/04/2017.
 */
public class SaltMineRouter implements Router {

    // ******************************
    // Members
    // ******************************

    private final Git git;
    //
    private boolean periodicUpdate;
    private boolean updateBeforeRequest;

    // ******************************
    // Constructors
    // ******************************


    public SaltMineRouter(Path localRepositoryPath) throws GitAPIException {
        this(cloneRepository(localRepositoryPath, "https://github.com/Kirschstein/salt-mine.git"));
    }


    public SaltMineRouter(Path localRepositoryPath, String remoteRepositoryURI) throws GitAPIException {
        this(cloneRepository(localRepositoryPath, remoteRepositoryURI));
    }

    SaltMineRouter(Git git) {
        this.git = git;
    }


    // ******************************
    // Getters & Setters
    // ******************************

    public boolean isPeriodicUpdate() {
        return periodicUpdate;
    }

    public void setPeriodicUpdate(boolean periodicUpdate) {
        this.periodicUpdate = periodicUpdate;
    }

    public boolean isUpdateBeforeRequest() {
        return updateBeforeRequest;
    }

    public void setUpdateBeforeRequest(boolean updateBeforeRequest) {
        this.updateBeforeRequest = updateBeforeRequest;
    }

    // ******************************
    // Router Methods
    // ******************************

    @Override
    public GamesList games() throws RouterException {
        if (updateBeforeRequest)
            update(git);

        List<String> active = Collections.emptyList();
        List<String> pending = Collections.emptyList();

        File workTree = git.getRepository().getWorkTree();
        if (Objects.isNull(workTree))
            return new GamesList(active, pending);

        File[] filesInRepository = workTree.listFiles();
        if (Objects.isNull(filesInRepository))
            return new GamesList(active, pending);

        List<String> completed = Arrays.stream(filesInRepository)
                .filter(Objects::nonNull)
                .map(File::toPath)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.endsWith(".json"))
                .map(name -> name.substring(0, name.length() - 5))
                .collect(Collectors.toList());

        return new GamesList(active, pending, completed);
    }

    @Override
    public Game game(String id) throws RouterException {
        if (updateBeforeRequest)
            update(git);

        // Todo: do we really need to do this via Git, or can we go straight to the File System?
        File workTree = git.getRepository().getWorkTree();
        if (Objects.isNull(workTree))
            throw new RouterException("Could not find game '" + id + "'");

        File[] filesInRepository = workTree.listFiles();
        if (Objects.isNull(filesInRepository))
            throw new RouterException("Could not find game '" + id + "'");

        String filename = String.format("%s.json", id);
        Optional<Path> firstMatch = Arrays.stream(filesInRepository)
                .filter(Objects::nonNull)
                .map(File::toPath)
                .filter(file -> file.getFileName().toString().equals(filename))
                .findFirst();

        Path path = firstMatch.orElseThrow(() -> new RouterException("Could not find game '" + id + "'"));
        ArchivedGameParser parser = new ArchivedGameParser();

        try {
            return parser.parse(id, path);
        } catch (IOException e) {
            throw new RouterException("Failed to retrieve game: " + e.getMessage(), e);
        }
    }

    // ******************************
    // AutoCloseable Methods
    // ******************************

    @Override
    public void close() {
        git.close();
    }


    // ******************************
    // Helper Methods
    // ******************************

    protected static Git cloneRepository(Path localRepositoryPath, String remoteRepositoryURI) throws GitAPIException {
        return Git.cloneRepository().setURI(remoteRepositoryURI).setDirectory(localRepositoryPath.toFile()).call();
    }

    /**
     * Update a {@link Git} {@link Repository}.
     *
     * @param git the {@link Git} instance to update
     */
    protected static void update(Git git) throws RouterException {
        PullResult pullResult;
        try {
            pullResult = git.pull().call();
            // Todo: log some stuff??
        } catch (GitAPIException e) {
            throw new RouterException("Failed to update Git repository: " + e.getMessage(), e);
        }
    }


}
