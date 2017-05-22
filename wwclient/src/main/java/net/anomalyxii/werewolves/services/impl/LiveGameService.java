package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.parser.LiveGameParser;
import net.anomalyxii.werewolves.router.Auth;
import net.anomalyxii.werewolves.router.RouterException;
import net.anomalyxii.werewolves.router.RouterRequest;
import net.anomalyxii.werewolves.router.RouterResponse;
import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.router.http.UnhappyHttpResponseException;
import net.anomalyxii.werewolves.router.http.auth.BearerAuth;
import net.anomalyxii.werewolves.router.http.request.HttpRouterRequest;
import net.anomalyxii.werewolves.router.http.request.HttpRouterRequestBuilder;
import net.anomalyxii.werewolves.router.http.request.account.LoginRequestBean;
import net.anomalyxii.werewolves.router.http.response.HttpRouterResponse;
import net.anomalyxii.werewolves.router.http.response.ResponseBean;
import net.anomalyxii.werewolves.router.http.response.account.LoginResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameListResponseBean;
import net.anomalyxii.werewolves.router.http.response.game.GameResponseBean;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * A basic implementation of the {@link GameService}.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class LiveGameService implements GameService {

    // ******************************
    // Members
    // ******************************

    private final String username;
    private final String password;
    private final HttpRouter router;
    //
    private Auth auth;

    // ******************************
    // Constructors
    // ******************************

    public LiveGameService(String username, String password, HttpRouter router) {
        this.username = username;
        this.password = password;
        this.router = router;
    }

    // ******************************
    // Constructors
    // ******************************

    @Override
    public boolean doesGameExist(String id) {
        return true; // Technically, every game exists via this service... :(
    }

    @Override
    public GamesList getGameIDs() throws ServiceException {
        HttpRouterRequestBuilder<Void> request = HttpRouterRequestBuilder.newBuilder().withEndpoint("/api/Game");
        RouterResponse<GameListResponseBean> response = routeWithAuthenticationRetry(request, GameListResponseBean.class);
        GameListResponseBean responseBean = response.getContent().orElseThrow(() -> new ServiceException("Failed to retrieve GameList"));
        return new GamesList(responseBean.getActive(), responseBean.getPending());
    }

    @Override
    public Game getGame(String id) throws ServiceException {
        HttpRouterRequestBuilder<Void> request = HttpRouterRequestBuilder.newBuilder().withEndpoint("/api/Game/" + id);
        RouterResponse<GameResponseBean> response = routeWithAuthenticationRetry(request, GameResponseBean.class);
        GameResponseBean responseBean = response.getContent().orElseThrow(() -> new ServiceException("Failed to retrieve Game '" + id + "'"));

        LiveGameParser parser = new LiveGameParser();
        return parser.parse(id, responseBean);
    }

    @Override
    public GameStatistics getGameStatistics(String id) throws ServiceException {
        return new GameStatistics(getGame(id));
    }

    // ******************************
    // Authentication Methods
    // ******************************

    /**
     * Determine whether authorization is required before
     * attempting to access a given resource. By default, it is
     * assumed that authorization is always required if there is
     * no existing {@link Auth} token.
     *
     * @param builder the {@link RouterRequest} being made
     * @return {@literal true} if the {@code HttpRouter} needs to request authorization before making the current request; {@literal false} otherwise
     */
    protected boolean isAuthRequired(HttpRouterRequestBuilder<?> builder) {
        return Objects.isNull(auth);
    }

    /*
     * Try to dispatch the specified RouterRequest. If the server returns
     * HTTP 401 (Unauthorized) then attempt to log-in and try the request
     * again.
     */
    protected <T> RouterResponse<T> routeWithAuthenticationRetry(HttpRouterRequestBuilder<Void> builder, Class<T> responseClass)
            throws ServiceException {

        boolean authAttempted = false;
        if (isAuthRequired(builder)) {
            login(username, password);
            authAttempted = true;
        }

        HttpRouterRequest<?> request = builder.withAuth(auth).build();

        try {
            HttpRouterResponse<T> response;
            try {
                response = router.submit(request, responseClass);
            } catch (UnhappyHttpResponseException e) {
                if (e.getCode() != 401 || authAttempted)
                    throwAppropriateException("Failed to route request", e);

                login(username, password);

                // Retry...
                try {
                    response = router.submit(builder.copy().withAuth(auth).build(), responseClass);
                } catch (UnhappyHttpResponseException e1) {
                    throwAppropriateException("Failed to route request", e1);
                    return null; // Shouldn't get here!
                }
            }

            return response;
        } catch (RouterException e) {
            throw new ServiceException(e.getCause());
        }

    }

    /*
     * Perform a log-in attempt
     */
    protected void login(String username, String password) throws ServiceException {

        HttpRouterRequest<LoginRequestBean> request = HttpRouterRequestBuilder.newBuilder()
                .withEndpoint("/api/Account/Login")
                .withContent(new LoginRequestBean(username, password))
                .build();

        try {
            HttpRouterResponse<LoginResponseBean> response = router.submit(request, LoginResponseBean.class);
            LoginResponseBean responseBean = response.getContent().orElseThrow(() -> new ServiceException("Failed to log-in"));
            auth = new BearerAuth(responseBean.getAccessToken());
            System.out.println(auth.getAuthorizationString());
        } catch (RouterException e) {
            throw new ServiceException("Failed to log in: " + e.getMessage(), e);
        }

    }

    protected ServiceException constructAppropriateException(String prefix, UnhappyHttpResponseException content) {
        StringBuilder builder = new StringBuilder();
        builder.append(prefix);


        if (Objects.nonNull(content.getMessage())) {
            builder.append(": ").append(content.getMessage());

            Map<String, Object> modelState = content.getModelState();

            boolean canReportModelState = modelState.size() == 1;
            if (canReportModelState) {
                String firstKey = modelState.keySet().iterator().next();
                Object error = modelState.get(firstKey);

                if (error instanceof Collection) {
                    Collection<?> errors = (Collection<?>) error;

                    boolean canReportModelErrors = errors.size() == 1;
                    if (canReportModelErrors) {
                        String firstError = String.valueOf(errors.iterator().next());
                        builder.append(" -> ").append(firstError);
                    }
                } else if (error instanceof String) {
                    builder.append(" -> ").append(error);
                }
            }

        }

        return new ServiceException(builder.toString());
    }

    /*
     * Convert the ResponseBean into an appropriate ServiceException
     * and then throw it!
     */
    protected void throwAppropriateException(String prefix, UnhappyHttpResponseException content) throws ServiceException {
        throw constructAppropriateException(prefix, content);
    }

}
