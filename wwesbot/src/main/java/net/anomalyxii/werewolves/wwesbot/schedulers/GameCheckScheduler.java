package net.anomalyxii.werewolves.wwesbot.schedulers;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.scheduler.BotSchedulerContext;
import net.anomalyxii.bot.api.scheduler.ScheduledAction;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A {@link ScheduledAction} that checks if any new games have
 * entered sign-up or if a previously pending game has started.
 *
 * Created by Anomaly on 29/05/2017.
 */
public class GameCheckScheduler implements ScheduledAction {

    private static final Logger logger = LoggerFactory.getLogger(GameCheckScheduler.class);
    //
    private static final String ACTIVE_GAMES = "ACTIVE_GAMES";
    private static final String PENDING_GAMES = "PENDING_GAMES";

    // ******************************
    // Members
    // ******************************

    private final GameService service;

    // ******************************
    // Constructors
    // ******************************

    public GameCheckScheduler(GameService service) {
        this.service = service;
    }

    // ******************************
    // ScheduledAction Methods
    // ******************************

    @Override
    public void execute(BotSchedulerContext context) {

        List<Bot> bots = context.getBots();
        if(bots.isEmpty()) {
            logger.warn("No Bots to notify - aborting check");
            return;
        }

        GamesList games;
        try {
            games = service.getGameIds();
        } catch (ServiceException e) {
            logger.warn("Was unable to refresh current games - aborting check");
            return;
        }

        List<String> previouslyActiveGameIds = context.getOrDefault(ACTIVE_GAMES, Collections.emptyList());
        List<String> currentlyActiveGameIds = games.getActiveGameIds();
        List<String> noLongerActiveGameIds = new ArrayList<>();
        List<String> notPreviouslyActiveGameIds = new ArrayList<>();
        filterIds(previouslyActiveGameIds, currentlyActiveGameIds, noLongerActiveGameIds, notPreviouslyActiveGameIds);


        List<String> previouslyPendingGameIds = context.getOrDefault(PENDING_GAMES, Collections.emptyList());
        List<String> currentlyPendingGameIds = games.getPendingGameIds();
        List<String> noLongerPendingGameIds = new ArrayList<>();
        List<String> notPreviouslyPendingGameIds = new ArrayList<>();
        filterIds(previouslyPendingGameIds, currentlyPendingGameIds, noLongerPendingGameIds, notPreviouslyPendingGameIds);


        for (String id : noLongerActiveGameIds) {
            String msg = "Game '" + id + "' has finished!";
            bots.forEach();
        }


    }

    // ******************************
    // Helper Methods
    // ******************************

    /*
     * Extract the IDs that are in one list but not the other
     */
    private void filterIds(List<String> previous, List<String> current, List<String> noLonger, List<String> notPreviously) {
        List<String> allActiveGameIds = new ArrayList<>();
        allActiveGameIds.addAll(previous);
        allActiveGameIds.addAll(current);

        for (String id : allActiveGameIds) {
            boolean inPrevious = previous.contains(id);
            boolean inCurrent = current.contains(id);

            if(inCurrent && !inPrevious)
                notPreviously.add(id);
            else if(inPrevious && !inCurrent)
                noLonger.add(id);
        }
    }


}
