package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ArchivedGameServiceTest {

    // ******************************
    // Test Methods
    // ******************************

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

        ArchivedGameService service = new ArchivedGameService(git);

        // act
        GamesList gameList = service.getGameIDs();

        // assert
        assertNotNull(gameList);
        assertEquals(gameList.getActiveGameIDs(), Collections.emptyList());
        assertEquals(gameList.getPendingGameIDs(), Collections.emptyList());
        assertEquals(gameList.getCompletedGameIDs(), Arrays.asList("ext-035", "ext-036", "ext-037"));
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

        ArchivedGameService service = new ArchivedGameService(git);

        // act
        Game game = service.getGame("ext-036");

        // assert
        assertNotNull(game);
    }


}
