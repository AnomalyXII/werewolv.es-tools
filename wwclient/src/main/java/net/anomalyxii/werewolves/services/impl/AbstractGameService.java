package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Day;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.Vitality;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;

import java.util.*;

/**
 * An abstract base for {@link GameService GameServices}.
 *
 * Created by Anomaly on 22/05/2017.
 */
public abstract class AbstractGameService implements GameService {

    private static final Set<Event.EventType> MESSAGE_EVENT_TYPES = new HashSet<>(Arrays.asList(
            Event.EventType.COVEN_MESSAGE,
            Event.EventType.WEREWOLF_MESSAGE,
            Event.EventType.VAMPIRE_MESSAGE,
            Event.EventType.MASON_MESSAGE,
            Event.EventType.VILLAGE_MESSAGE));

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    @Override
    public GameStatistics getGameStatistics(String id) throws ServiceException {
        return createGameStatisticsForGame(getGame(id));
    }

    // ******************************
    // Protected Helper Methods
    // ******************************

    /**
     * Create a new {@link GameStatistics} instance based on the
     * specified {@link Game}.
     *
     * @param game the {@link Game}
     * @return the {@link GameStatistics}
     */
    protected GameStatistics createGameStatisticsForGame(Game game) {

        CumulativeGameStatistics stats = new CumulativeGameStatistics(game);

        // Calculated statistics:
        // Game Status
        if(!game.getDays().isEmpty())
            stats.start();

        if(!Objects.isNull(game.getWinningAlignment()))
            stats.complete();

        // Player Count
        for (Character ignored : game.getCharacters())
            stats.incrementNumberPlayersTotal();

        game.getCharacters().stream()
                .filter(character -> character.getCurrentVitality() == Vitality.ALIVE)
                .forEach(character -> stats.incrementNumberPlayersAlive());

        // Day Stats
        game.getDays().forEach(day -> {
            stats.incrementNumberDays();

            day.getDayPhase().getEvents().forEach(event -> {

                if (!MESSAGE_EVENT_TYPES.contains(event.getType()))
                    return;

                stats.incrementNumberLinesSpokenTotal();
                if (Event.EventType.VILLAGE_MESSAGE == event.getType())
                    stats.incrementNumberLinesSpokenVillage();
            });
        });

        // Todo: set most active day...

        return stats;

    }

}
