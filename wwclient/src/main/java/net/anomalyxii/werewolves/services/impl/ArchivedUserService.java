package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.*;
import net.anomalyxii.werewolves.domain.players.User;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
import net.anomalyxii.werewolves.services.UserService;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A basic implementation of the {@link UserService} that takes
 * information from the "salt mine" of archived games.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class ArchivedUserService implements UserService {

    // ******************************
    // Members
    // ******************************

    private final GameService gameService;

    // ******************************
    // Constructors
    // ******************************

    public ArchivedUserService(GameService gameService) {
        this.gameService = gameService;
    }

    // ******************************
    // UserService Methods
    // ******************************

    @Override
    public UserStatistics getUserStatistics(String id) throws ServiceException {
        GamesList games = gameService.getGameIDs();

        // Create the user object...
        User user = new User(id, null);

        CumulativeUserStatistics stats = new CumulativeUserStatistics(user);
        games.getCompletedGameIDs().forEach(gameid -> {

            Game game;
            try {
                game = gameService.getGame(gameid);
            } catch (ServiceException e) {
                // Ignore??
                return;
            }

            game.getCharacters().forEach(character -> {
                User u = character.getUser();
                if (!user.equals(u))
                    return;

                stats.incrementNumberGamesPlayed();

                Alignment alignment = character.getCurrentAlignment();
                if(Objects.isNull(alignment))
                    alignment = character.getRole().getAlignment();

                stats.incrementNumberGamesPlayednForAlignment(alignment);

                if(game.getWinningAlignment() == alignment) {
                    stats.incrementNumberGamesWon();
                    stats.incrementNumberGamesWonForAlignment(alignment);
                }
            });


        });

        return stats;
    }

}
