package net.anomalyxii.werewolves.wwesbot.spring.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.Router;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import net.anomalyxii.werewolves.wwesbot.spring.services.ApiService;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GameServiceImplTest {

    // ******************************
    // Test Methods
    // ******************************

    // getGameIDs

    @Test
    public void getGameIDs_should_return_live_and_archived_results() throws Exception {
        // arrange
        Router liveRouter = mock(Router.class);
        when(liveRouter.games()).thenReturn(new GamesList(Collections.singletonList("ext-123"),
                                                          Collections.singletonList("ext-142")));
        Router archiveRouter = mock(Router.class);
        when(archiveRouter.games()).thenReturn(new GamesList(Collections.emptyList(),
                                                             Collections.emptyList(),
                                                             Arrays.asList("ext-133", "ext-134")));

        ApiService service = new ApiServiceImpl(liveRouter, archiveRouter);

        // act
        GamesList gameList = service.getGameIDs();

        // assert
        assertNotNull(gameList);
        assertEquals(gameList.getActiveGameIDs(), Collections.singletonList("ext-123"));
        assertEquals(gameList.getPendingGameIDs(), Collections.singletonList("ext-142"));
        assertEquals(gameList.getCompletedGameIDs(), Arrays.asList("ext-133", "ext-134"));
    }

    // getGame

    @Test
    public void getGame_should_use_archived_router_if_game_is_completed() throws Exception {
        Router liveRouter = mock(Router.class);
        when(liveRouter.game("ext-133")).thenThrow(new RouterException("Game not found!"));
        Router archiveRouter = mock(Router.class);
        when(archiveRouter.games()).thenReturn(new GamesList(Collections.emptyList(),
                                                             Collections.emptyList(),
                                                             Arrays.asList("ext-133", "ext-134")));
        when(archiveRouter.game("ext-133")).thenReturn(new Game("ext-133"));

        ApiService service = new ApiServiceImpl(liveRouter, archiveRouter);

        // act
        Game game = service.getGame("ext-133");

        // assert
        assertNotNull(game);
        assertEquals(game.getId(), "ext-133");

        verify(archiveRouter).games();
        verify(archiveRouter).game("ext-133");
    }

    @Test
    public void getGame_should_use_live_router_if_game_is_not_completed() throws Exception {
        Router liveRouter = mock(Router.class);
        when(liveRouter.game("ext-123")).thenReturn(new Game("ext-123"));
        Router archiveRouter = mock(Router.class);
        when(archiveRouter.games()).thenReturn(new GamesList(Collections.emptyList(),
                                                             Collections.emptyList(),
                                                             Arrays.asList("ext-133", "ext-134")));
        when(archiveRouter.game("ext-123")).thenThrow(new RouterException("Game not found!"));

        ApiService service = new ApiServiceImpl(liveRouter, archiveRouter);

        // act
        Game game = service.getGame("ext-123");

        // assert
        assertNotNull(game);
        assertEquals(game.getId(), "ext-123");

        verify(archiveRouter).games();
        verify(liveRouter).game("ext-123");
    }


    // ******************************
    // Helper Methods
    // ******************************

}