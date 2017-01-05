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

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public static void dump(Game game) {

        game.getPreGameEvents().forEach(System.out::println);

        System.out.println("        ======= Game Started =======");
        game.getUsers().stream()
                .filter(User::isJoinedGame)
                .forEach(user -> System.out.printf(" META | %s is participating in this game %n", user.getName()));

        game.getDays().forEach(day -> {
            DayPhase dp = day.getDayPhase();
            NightPhase np = day.getNightPhase();

            System.out.println("        ======= New Day =======");
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
                                PlayerNominationEvent::getPlayer,
                                PlayerNominationEvent::getTarget,
                                (oldTarget, newTarget) -> newTarget))
                        .entrySet().stream()
                        .sorted(Comparator.comparing(a -> a.getKey().getName()))
                        .forEach((entry) -> System.out.printf(
                                " META | Final vote: %s -> %s%n",
                                entry.getKey().getName(),
                                entry.getValue().getName()));


                System.out.println("        ======= Night Falls =======");
                np.getEvents().forEach(System.out::println);
            }
            System.out.println("        ======= End Day =======");
        });

        // Game over?
        if (game.getWinningAlignment() != null) {
            System.out.println();
            System.out.printf("Game Over! Congratulations to the %s! %n",
                              game.getWinningAlignment().toString().toLowerCase());
            System.out.println();

            System.out.println("        ======= Role Reveals =======");
            game.getCharacters().stream()
                    .sorted(Comparator.comparing(AbstractPlayer::getName))
                    .forEach(character -> System.out.printf(" META | <%s> %s was a %s%s %n",
                                                            character.getName(),
                                                            character.getUser().getName(),
                                                            character.getRole(),
                                                            character.isPossiblyIncorrectlyLinked() ? " (probably)"
                                                                                                    : ""));

            game.getPostGameEvents().forEach(System.out::println);
        }

    }

}
