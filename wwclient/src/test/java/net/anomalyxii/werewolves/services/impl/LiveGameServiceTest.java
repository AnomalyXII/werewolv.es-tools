package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.RouterRequest;
import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.router.http.response.StandardHttpRouterResponse;
import net.anomalyxii.werewolves.router.http.response.account.LoginResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameListResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameResponseBean;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class LiveGameServiceTest {

    // ******************************
    // Test Methods
    // ******************************

    // getGameIDs

    @Test
    public void games_should_return_live_games() throws Exception {
        // arrange
        LoginResponseBean loginBean = new LoginResponseBean("x123456", "bearer", -1);
        StandardHttpRouterResponse<LoginResponseBean> login = new StandardHttpRouterResponse<>(200, "SUCCESS", loginBean);

        GameListResponseBean responseBean = new GameListResponseBean(Collections.singletonList("tst-001"), Collections.emptyList());
        StandardHttpRouterResponse<GameListResponseBean> response = new StandardHttpRouterResponse<>(200, "SUCCESS", responseBean);

        HttpRouter router = mock(HttpRouter.class);
        when(router.submit(any(RouterRequest.class), eq(LoginResponseBean.class))).thenReturn(login);
        when(router.submit(any(RouterRequest.class), eq(GameListResponseBean.class))).thenReturn(response);
        LiveGameService service = new LiveGameService("username", "password", router);

        // act
        GamesList gameList = service.getGameIDs();

        // assert
        assertNotNull(gameList);
        assertEquals(gameList.getActiveGameIDs(), Collections.singletonList("tst-001"));
        assertEquals(gameList.getPendingGameIDs(), Collections.emptyList());
        assertEquals(gameList.getCompletedGameIDs(), Collections.emptyList());
    }

    // getGame

    @Test
    public void game_should_return_live_game() throws Exception {
        // arrange
        LoginResponseBean loginBean = new LoginResponseBean("x123456", "bearer", -1);
        StandardHttpRouterResponse<LoginResponseBean> login = new StandardHttpRouterResponse<>(200, "SUCCESS", loginBean);

        GameResponseBean bean = new GameResponseBean();

        GameResponseBean.Event event1 = new GameResponseBean.Event();
        event1.put("playerName", "aheadofyourtime");
        event1.put("avatarUrl", "http://www.gravatar.com/avatar/5c3ddd99a84e03f8dc07a502773a0c54?s=200&d=wavatar");
        event1.put("gameId", "ext-090");
        event1.put("timeStamp", "2016-11-28T15:43:28.464+00:00");
        event1.put("formattedTimeStamp", "Mon 15:43:28");
        event1.put("__type", "PlayerJoined");

        GameResponseBean.Event event2 = new GameResponseBean.Event();
        event2.put("playerName", "aheadofyourtime");
        event2.put("message", "90 !!!!!");
        event2.put("gameId", "ext-090");
        event2.put("timeStamp", "2016-11-28T15:43:31.620+00:00");
        event2.put("formattedTimeStamp", "Mon 15:43:31");
        event2.put("__type", "PendingGameMessage");

        GameResponseBean.Event event3 = new GameResponseBean.Event();
        event3.put("gameId", "ext-090");
        event3.put("timeStamp", "2016-12-01T20:05:15.396+00:00");
        event3.put("formattedTimeStamp", "Thu 20:05:15");
        event3.put("__type", "GameStarted");

        bean.add(event1);
        bean.add(event2);
        bean.add(event3);

        StandardHttpRouterResponse<GameResponseBean> response = new StandardHttpRouterResponse<>(200, "SUCCESS", bean);

        HttpRouter router = mock(HttpRouter.class);
        when(router.submit(any(RouterRequest.class), eq(LoginResponseBean.class))).thenReturn(login);
        when(router.submit(any(RouterRequest.class), eq(GameResponseBean.class))).thenReturn(response);
        LiveGameService service = new LiveGameService("username", "password", router);

        // act
        Game game = service.getGame("tst-001");

        // assert
        assertNotNull(game);
        assertEquals(game.getId(), "tst-001");
        assertEquals(game.getPreGameEvents().size(), 2);
        assertEquals(game.getDays().size(), 1);
    }

}