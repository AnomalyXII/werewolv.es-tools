package net.anomalyxii.werewolves.domain;

import net.anomalyxii.werewolves.domain.Day;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Vitality;
import net.anomalyxii.werewolves.domain.events.Event;

import java.util.*;

/**
 * Contains computed statistics about a {@link Game}.
 * <p>
 * Created by Anomaly on 17/04/2017.
 */
public class GameStatistics {

    private static final Set<Event.EventType> MESSAGE_EVENT_TYPES = new HashSet<>(Arrays.asList(
            Event.EventType.COVEN_MESSAGE,
            Event.EventType.WEREWOLF_MESSAGE,
            Event.EventType.VAMPIRE_MESSAGE,
            Event.EventType.MASON_MESSAGE,
            Event.EventType.VILLAGE_MESSAGE));

    // ******************************
    // Members
    // ******************************

    private final Game game;
    //
    private final boolean isStarted;
    private final boolean isComplete;
    private final String status;

    private final int numberPlayersTotal;
    private final int numberPlayersAlive;
    private final int numberDays;
    private final int mostActiveDay;
    private final int numberLinesSpokenVillage;
    private final int numberLinesSpokenTotal;


    // ******************************
    // Constructors
    // ******************************

    public GameStatistics(Game game) {
        this.game = game;

        // Calculated statistics:
        // Game Status
        this.isStarted = !game.getDays().isEmpty();
        this.isComplete = !Objects.isNull(game.getWinningAlignment());
        this.status = isStarted ? isComplete ? "Complete" : "in Progress" : "in Sign-up";

        // Player Count
        this.numberPlayersTotal = game.getCharacters().size();
        this.numberPlayersAlive = (int) game.getCharacters().stream()
                .filter(character -> character.getCurrentVitality() == Vitality.ALIVE)
                .count();

        // Day Stats
        this.numberDays = game.getDays().size();

        List<Event> allMessageEvents = new ArrayList<>();
        List<Event> villageMessageEvents = new ArrayList<>();
        Map<Day, List<Event>> villageMessageEventsByDay = new HashMap<>();

        game.getDays().forEach(day -> day.getDayPhase().getEvents().forEach(event -> {
            if (!MESSAGE_EVENT_TYPES.contains(event.getType()))
                return;

            allMessageEvents.add(event);
            if (Event.EventType.VILLAGE_MESSAGE == event.getType()) {
                villageMessageEvents.add(event);
                villageMessageEventsByDay.computeIfAbsent(day, key -> new ArrayList<>()).add(event);
            }
        }));

        this.numberLinesSpokenTotal = allMessageEvents.size();
        this.numberLinesSpokenVillage = villageMessageEvents.size();
        this.mostActiveDay = villageMessageEventsByDay.entrySet().stream()
                .max(Comparator.comparingInt(a -> a.getValue().size()))
                .map(Map.Entry::getKey)
                .map(Day::getDayNumber)
                .orElse(-1);

    }

    // ******************************
    // Getters
    // ******************************

    public Game getGame() {
        return game;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public String getStatus() {
        return status;
    }

    public int getNumberPlayersTotal() {
        return numberPlayersTotal;
    }

    public int getNumberPlayersAlive() {
        return numberPlayersAlive;
    }

    public int getNumberDays() {
        return numberDays;
    }

    public int getMostActiveDay() {
        return mostActiveDay;
    }

    public int getNumberLinesSpokenVillage() {
        return numberLinesSpokenVillage;
    }

    public int getNumberLinesSpokenTotal() {
        return numberLinesSpokenTotal;
    }

    // ******************************
    // To String
    // ******************************

    public String toFormattedString() {
        StringBuilder builder = new StringBuilder();
        List<Object> arguments = new ArrayList<>();

        // Game ID:
        builder.append("[%s]");
        arguments.add(game.getId());

        // Game Status:
        builder.append(" :: ");
        builder.append("Game %s");
        arguments.add(status);
        if(isComplete) {
            builder.append(" :: ");
            builder.append("The %s Won");
            arguments.add(game.getWinningAlignment());
        }

        // Players Alive:
        builder.append(" :: ");
        builder.append("%d / %d Players Alive");
        arguments.add(numberPlayersAlive);
        arguments.add(numberPlayersTotal);

        // Days:
        builder.append(" :: ");
        builder.append("%d Days");
        arguments.add(numberDays);

        // Lines Spoken:
        builder.append(" :: ");
        builder.append("%d / %d Lines Spoken to the Village");
        arguments.add(numberLinesSpokenVillage);
        arguments.add(numberLinesSpokenTotal);


        return String.format(builder.toString(), arguments.toArray(new Object[arguments.size()]));
    }


}
