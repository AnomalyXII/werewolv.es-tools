package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.RouterRequest;
import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.router.http.response.StandardHttpRouterResponse;
import net.anomalyxii.werewolves.router.http.response.account.LoginResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameListResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameResponseBean;
import net.anomalyxii.werewolves.services.GameService;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.*;

public class CompositeGameServiceTest {

    // ******************************
    // Test Methods
    // ******************************

    // getGameIDs

    @Test
    public void games_should_return_GameList_from_all_services() throws Exception {
        // arrange
        GameService primary = mock(GameService.class);
        when(primary.getGameIDs()).thenReturn(new GamesList(singletonList("tst-010"), emptyList()));
        GameService secondary = mock(GameService.class);
        when(secondary.getGameIDs()).thenReturn(new GamesList(asList("tst-010", "tst-011"), asList("tst-012", "tst-013")));
        GameService tertiary = mock(GameService.class);
        when(tertiary.getGameIDs()).thenReturn(new GamesList(emptyList(), emptyList(), asList("tst-005", "tst-006", "tst-007", "tst-008")));

        CompositeGameService service = new CompositeGameService(primary, secondary, tertiary);

        // act
        GamesList gameList = service.getGameIDs();

        // assert
        assertNotNull(gameList);
        assertEquals(gameList.getActiveGameIDs(), asList("tst-010", "tst-011"));
        assertEquals(gameList.getPendingGameIDs(), asList("tst-012", "tst-013"));
        assertEquals(gameList.getCompletedGameIDs(), asList("tst-005", "tst-006", "tst-007", "tst-008"));
    }

    // getGame

    @Test
    public void game_should_return_Game_from_primary_service_if_found() throws Exception {
        // arrange
        Game expected = new Game("tst-010");
        GameService primary = mock(GameService.class);
        when(primary.doesGameExist("tst-010")).thenReturn(true);
        when(primary.getGame("tst-010")).thenReturn(expected);
        GameService secondary = mock(GameService.class);
        when(secondary.doesGameExist("tst-010")).thenReturn(true);
        when(secondary.getGame("tst-010")).thenThrow(new AssertionError("Should not have been called"));
        GameService tertiary = mock(GameService.class);
        when(tertiary.doesGameExist("tst-010")).thenReturn(true);
        when(tertiary.getGame("tst-010")).thenThrow(new AssertionError("Should not have been called"));

        CompositeGameService service = new CompositeGameService(primary, secondary, tertiary);

        // act
        Game game = service.getGame("tst-010");

        // assert
        assertEquals(game, expected);
    }

}