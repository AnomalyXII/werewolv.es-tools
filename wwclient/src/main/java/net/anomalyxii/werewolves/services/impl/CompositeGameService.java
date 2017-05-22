package net.anomalyxii.werewolves.services.impl;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GameStatistics;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.ServiceException;
import net.anomalyxii.werewolves.services.UncheckedServiceException;

import java.util.*;
import java.util.stream.Stream;

/**
 * A {@link GameService} that combines multiple other
 * {@link GameService GameServices} to provide a single,
 * unified view over all of them.
 * <p>
 * Created by Anomaly on 15/05/2017.
 */
public class CompositeGameService implements GameService {

    // ******************************
    // Members
    // ******************************

    private final GameService primaryDelegate;
    private final Set<GameService> otherDelegates;

    // ******************************
    // Constructors
    // ******************************

    public CompositeGameService(GameService primaryDelegate, GameService... otherDelegates) {
        this.primaryDelegate = Objects.requireNonNull(primaryDelegate);
        this.otherDelegates = new HashSet<>(Arrays.asList(otherDelegates));
    }

    // ******************************
    // GameService Methods
    // ******************************

    @Override
    public boolean doesGameExist(String id) {
        return stream(primaryDelegate, otherDelegates).anyMatch(delegate -> delegate.doesGameExist(id));
    }

    @Override
    public GamesList getGameIDs() throws ServiceException {
        Set<String> activeGameIDs = new TreeSet<>();
        Set<String> pendingGameIDs = new TreeSet<>();
        Set<String> completedGameIDs = new TreeSet<>();

        try {
            stream(primaryDelegate, otherDelegates).map(this::delegate).forEach(list -> {
                activeGameIDs.addAll(list.getActiveGameIDs());
                pendingGameIDs.addAll(list.getPendingGameIDs());
                completedGameIDs.addAll(list.getCompletedGameIDs());
            });


            return new GamesList(asList(activeGameIDs), asList(pendingGameIDs), asList(completedGameIDs));
        } catch (UncheckedServiceException e) {
            throw e.getCause();
        }
    }

    @Override
    public Game getGame(String id) throws ServiceException {
        return stream(primaryDelegate, otherDelegates)
                .filter(delegate -> delegate.doesGameExist(id))
                .findFirst()
                .map(delegate -> delegate(delegate, id))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .orElseThrow(() -> new ServiceException("Could not find Game with ID '" + id + "'"));
    }

    @Override
    public GameStatistics getGameStatistics(String id) throws ServiceException {
        return new GameStatistics(getGame(id));
    }


    // ******************************
    // Private Helper Methods
    // ******************************

    /*
     * Delegate a call to GameService#getGameIDs safely
     */
    private GamesList delegate(GameService delegate) {
        try {
            return delegate.getGameIDs();
        } catch (ServiceException e) {
            throw new UncheckedServiceException(e);
        }
    }


    /*
     * Delegate a call to GameService#getGame safely
     */
    private Optional<Game> delegate(GameService delegate, String id) {
        try {
            return Optional.ofNullable(delegate.getGame(id));
        } catch (ServiceException e) {
            return Optional.empty();
        }
    }


    /*
     * Create a Stream containing all the items
     */
    private <T> Stream<T> stream(T primary, Collection<T> others) {
        return Stream.concat(Stream.of(primary), others.stream());
    }

    /*
     * Turn a Collection into a List
     */
    private <T> List<T> asList(Collection<T> collection) {
        return collection instanceof List ? (List) collection : new ArrayList<>(collection);
    }

}
