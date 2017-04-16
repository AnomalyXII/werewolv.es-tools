package net.anomalyxii.werewolves.router;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.Repository;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class SaltMineRouterTest {

    // ******************************
    // Test Methods
    // ******************************

    // login

    // token

    // games

    @Test
    public void games_should_return_archived_games() throws Exception {
        // arrange
        URL archiveDirectory = getClass().getClassLoader().getResource("archive/");
        assertNotNull(archiveDirectory);

        Git git = mock(Git.class);
        Repository repository = mock(Repository.class);
        when(git.getRepository()).thenReturn(repository);
        when(git.pull()).thenThrow(new AssertionError("Should not have tried to update!"));
        when(repository.getWorkTree()).thenReturn(new File(archiveDirectory.toURI()));

        SaltMineRouter router = new SaltMineRouter(git);

        // act
        Games games = router.games();

        // assert
        assertNotNull(games);
        assertEquals(games.getActiveGameIDs(), Collections.emptyList());
        assertEquals(games.getPendingGameIDs(), Collections.emptyList());
        assertEquals(games.getCompletedGameIDs(), Arrays.asList("ext-035", "ext-036", "ext-037"));
    }

    // game

    @Test
    public void game_should_return_archive_game() throws Exception {
        // arrange
        URL archiveDirectory = getClass().getClassLoader().getResource("archive/");
        assertNotNull(archiveDirectory);

        Git git = mock(Git.class);
        Repository repository = mock(Repository.class);
        when(git.getRepository()).thenReturn(repository);
        when(git.pull()).thenThrow(new AssertionError("Should not have tried to update!"));
        when(repository.getWorkTree()).thenReturn(new File(archiveDirectory.toURI()));

        SaltMineRouter router = new SaltMineRouter(git);

        // act
        Game game = router.game("ext-036");

        // assert
        assertNotNull(game);
    }

    // ******************************
    // Helper Methods
    // ******************************

}