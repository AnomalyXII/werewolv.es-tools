package net.anomalyxii.werewolves;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.events.Event;
import net.anomalyxii.werewolves.domain.events.PlayerNominationEvent;
import net.anomalyxii.werewolves.domain.phases.DayPhase;
import net.anomalyxii.werewolves.domain.phases.NightPhase;
import net.anomalyxii.werewolves.domain.players.AbstractPlayer;
import net.anomalyxii.werewolves.domain.players.Character;
import net.anomalyxii.werewolves.domain.players.User;
import net.anomalyxii.werewolves.router.DefaultRouterBuilder;
import net.anomalyxii.werewolves.router.Router;
import net.anomalyxii.werewolves.router.RouterBuilder;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Fetch
 * <p>
 * Created by Anomaly on 10/07/2016.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // ******************************
    // Members
    // ******************************

    private final RouterBuilder builder;

    // ******************************
    // Constructors
    // ******************************

    public Main() {
        this(new DefaultRouterBuilder());
    }

    public Main(RouterBuilder builder) {
        this.builder = builder;
    }

    // ******************************
    // Run
    // ******************************

    public void run(String... args) throws Exception {

        CommandLine arguments = getCommandLine(args);

        // Create a Router
        Router router = getRouter(arguments);

        // Games games = router.games();
        // games.getActiveGameIDs().forEach(id -> System.out.println("Active: " + id));
        // games.getPendingGameIDs().forEach(id -> System.out.println("Pending: " + id));

        // Fetch a game
        Game game = router.game(arguments.getOptionValue("g"));
        game.getPreGameEvents().forEach(System.out::println);
        game.getDays().forEach(day -> {
            DayPhase dp = day.getDayPhase();
            NightPhase np = day.getNightPhase();

            System.out.println("======= New Day =======");
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
                                "Final vote: %s -> %s%n",
                                entry.getKey().getName(),
                                entry.getValue().getName()));


                System.out.println("======= Night Falls =======");
                np.getEvents().forEach(System.out::println);
            }
            System.out.println("======= End Day =======");
        });

        // Game over?
        if (game.getWinningAlignment() != null) {
            System.out.println();
            System.out.printf("Game Over! Congratulations to the %s! %n",
                              game.getWinningAlignment().toString().toLowerCase());
            System.out.println();

            System.out.println("======= Role Reveals =======");
            game.getCharacters().stream()
                    .sorted(Comparator.comparing(AbstractPlayer::getName))
                    .forEach(character -> System.out.printf("[xx:xx] <%s> %s was a %s%s %n",
                                                            character.getName(),
                                                            character.getUser().getName(),
                                                            character.getRole(),
                                                            character.isPossiblyIncorrectlyLinked() ? " (probably)"
                                                                                                    : ""));

            game.getPostGameEvents().forEach(System.out::println);
        }

    }

    // ******************************
    // Helper Methods
    // ******************************

    /**
     * Parse the command line arguments that
     * were supplied to the program.
     *
     * @param args the command line arguments to parse
     * @return a {@link CommandLine} containing the parsed arguments
     * @throws ParseException if anything is wrong during parsing
     */
    private CommandLine getCommandLine(String... args) throws ParseException {

        CommandLineParser parser = new DefaultParser();

        Option username = new Option("U", "username", true, "The werewolv.es username to use to authenticate");
        Option token = new Option("T", "token", true, "The werewolv.es API token to use to authenticate");

        Option password = new Option("P", "password", true, "The werewolv.es password to use to authenticate");
        password.setRequired(false);

        Option game = new Option("g", "game-id", true, "The werewolv.es Game ID to fetch");
        game.setRequired(true);

        OptionGroup authTypeOptionGroup = new OptionGroup();
        authTypeOptionGroup.addOption(username);
        authTypeOptionGroup.addOption(token);
        authTypeOptionGroup.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(authTypeOptionGroup);
        options.addOption(password);
        options.addOption(game);
        return parser.parse(options, args);

    }

    private Router getRouter(CommandLine arguments) throws RouterException {

        logger.debug("Creating Router");

        // Try using a pre-auth'd token
        if (arguments.hasOption("T"))
            return builder.forToken(arguments.getOptionValue("T"));

        // Try using a username and password
        String username = arguments.getOptionValue("U");

        // See if the password was set on the cmdline, or ask for it if not
        String password;
        if (arguments.hasOption("P"))
            password = arguments.getOptionValue("P");
        else
            password = new String(System.console().readPassword());

        return builder.forCredentials(username, password);

    }

    // ******************************
    // Main Method
    // ******************************

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
    }

}
