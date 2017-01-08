package net.anomalyxii.werewolves.utils;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.events.PlayerNominationEvent;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.AbstractPlayer;
import net.anomalyxii.werewolves.domain.players.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class GameDumper {

    private static final String BLANK_PREFIX = "         ";
    private static final String META_PREFIX  = "    META | ";

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public static void dump(Game game) {

        game.getPreGameEvents().forEach(System.out::println);

        System.out.printf("%s ======= Game Started ======= %n", BLANK_PREFIX);
        game.getUsers().stream()
                .filter(User::isJoinedGame)
                .forEach(user -> System.out.printf("%s%s is participating in this game %n",
                                                   META_PREFIX,
                                                   user.getName()));

        game.getDays().forEach(day -> {
            DayPhase dp = day.getDayPhase();
            NightPhase np = day.getNightPhase();

            System.out.printf("%s ========= New Day  ========= %n", BLANK_PREFIX);
            dp.getEvents().forEach(System.out::println);
            if (dp.isComplete()) {
                // Print the end-of-day votes:
                dp.getEvents().stream()
                        .filter(event -> event.getType() == Event.EventType.NOMINATION)
                        .map(event -> (PlayerNominationEvent) event)
                        .collect(Collectors.groupingBy(Event::getPlayer))
                        .values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toMap(
                                PlayerNominationEvent::getActualPlayer,
                                PlayerNominationEvent::getTarget,
                                (oldTarget, newTarget) -> newTarget))
                        .entrySet().stream()
                        .sorted(Comparator.comparing(a -> a.getKey().getName()))
                        .forEach((entry) -> System.out.printf(
                                "%s Final vote: %s -> %s%n",
                                META_PREFIX,
                                entry.getKey().getName(),
                                entry.getValue().getName()));

                System.out.printf("%s ======= Night Falls  ======= %n", BLANK_PREFIX);
                np.getEvents().forEach(System.out::println);
            }
            System.out.printf("%s ========= End Day  ========= %n", BLANK_PREFIX);
        });

        // Game over?
        if (game.getWinningAlignment() != null) {
            System.out.println();
            System.out.printf("Game Over! Congratulations to the %s! %n",
                              game.getWinningAlignment().toString().toLowerCase());
            System.out.println();

            System.out.printf("%s ======- Role Reveal  ======= %n", BLANK_PREFIX);
            game.getCharacters().stream()
                    .sorted(Comparator.comparing(AbstractPlayer::getName))
                    .forEach(character -> System.out.printf("%s <%s> %s was a %s%s %n",
                                                            META_PREFIX,
                                                            character.getName(),
                                                            character.getUser().getName(),
                                                            character.getRole(),
                                                            character.isPossiblyIncorrectlyLinked() ? " (probably)"
                                                                                                    : ""));

            game.getPostGameEvents().forEach(System.out::println);
        }

    }

}
