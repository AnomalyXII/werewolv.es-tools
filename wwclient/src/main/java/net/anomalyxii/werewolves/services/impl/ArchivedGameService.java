package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.parser.ArchivedGameParser;
import net.anomalyxii.werewolves.router.*;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
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
 * A basic implementation of the {@link GameService} that takes
 * information from the "salt mine" of archived games.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class ArchivedGameService extends AbstractGameService {

    // ******************************
    // Members
    // ******************************

    private final Git git;

    // ******************************
    // Constructors
    // ******************************

    public ArchivedGameService(Git git) {
        this.git = git;
    }

    // ******************************
    // Constructors
    // ******************************

    @Override
    public boolean doesGameExist(String id) {
        try {
            return getGameIds().getCompletedGameIds().contains(id);
        } catch (ServiceException e) {
            return false;
        }
    }

    @Override
    public GamesList getGameIds() throws ServiceException {
        List<String> active = Collections.emptyList();
        List<String> pending = Collections.emptyList();

        File workTree = git.getRepository().getWorkTree();
        if (Objects.isNull(workTree))
            return new GamesList(active, pending);

        File[] filesInRepository = workTree.listFiles();
        if (Objects.isNull(filesInRepository))
            return new GamesList(active, pending);

        // Todo: do we really need to do this via Git, or can we go straight to the File System?
        List<String> completed = Arrays.stream(filesInRepository)
                .filter(Objects::nonNull)
                .map(File::toPath)
                .map(Path::getFileName)
                .map(Path::toString)
                .filter(name -> name.endsWith(".json"))
                .filter(name -> !"player-stats.json".equalsIgnoreCase(name))
                .map(name -> name.substring(0, name.length() - 5))
                .collect(Collectors.toList());

        return new GamesList(active, pending, completed);
    }

    @Override
    public Game getGame(String id) throws ServiceException {
        // Todo: do we really need to do this via Git, or can we go straight to the File System?
        File workTree = git.getRepository().getWorkTree();
        if (Objects.isNull(workTree))
            throw new ServiceException("Could not find game '" + id + "'");

        if("player-stats".equalsIgnoreCase(id))
            throw new ServiceException("Invalid Game ID: '" + id + "'");

        File[] filesInRepository = workTree.listFiles();
        if (Objects.isNull(filesInRepository))
            throw new ServiceException("Could not find game '" + id + "'");

        String filename = String.format("%s.json", id);
        Optional<Path> firstMatch = Arrays.stream(filesInRepository)
                .filter(Objects::nonNull)
                .map(File::toPath)
                .filter(file -> file.getFileName().toString().equals(filename))
                .findFirst();

        Path path = firstMatch.orElseThrow(() -> new ServiceException("Could not find game '" + id + "'"));
        ArchivedGameParser parser = new ArchivedGameParser();

        try {
            return parser.parse(id, path);
        } catch (IOException e) {
            throw new ServiceException("Failed to retrieve game: " + e.getMessage(), e);
        }
    }

    // ******************************
    // Helper Methods
    // ******************************

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
