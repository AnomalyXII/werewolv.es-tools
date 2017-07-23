package net.anomalyxii.werewolves;

import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.GamesList;
import net.anomalyxii.werewolves.router.http.HttpRouter;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.services.impl.CompositeGameService;
import net.anomalyxii.werewolves.services.impl.LiveGameService;
import net.anomalyxii.werewolves.utils.GameDumper;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    // ******************************
    // Constructors
    // ******************************

    // ******************************
    // Run
    // ******************************

    public void run(String... args) throws Exception {

        CommandLine arguments = getCommandLine(args);

        // Create a HttpRouter
        try(HttpRouter router = new HttpRouter()) {

            GameService liveService = new LiveGameService("test01", "test01", router);
            GameService gameService = new CompositeGameService(liveService);

            GamesList games = gameService.getGameIDs();
            games.getActiveGameIDs().forEach(id -> System.out.println("Active: " + id));
            games.getPendingGameIDs().forEach(id -> System.out.println("Pending: " + id));

            // Fetch a game
            Game game = gameService.getGame(arguments.getOptionValue("g"));
            GameDumper.dump(game);
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
        Option local = new Option("L", "local", false, "Whether to fetch built-in game details");

        Option password = new Option("P", "password", true, "The werewolv.es password to use to authenticate");
        password.setRequired(false);

        Option game = new Option("g", "game-id", true, "The werewolv.es Game ID to fetch");
        game.setRequired(true);

        Option archived = new Option("A", "archived", false, "Whether to fetch archived game details");
        archived.setRequired(false);

        OptionGroup authTypeOptionGroup = new OptionGroup();
        authTypeOptionGroup.addOption(username);
        authTypeOptionGroup.addOption(token);
        authTypeOptionGroup.addOption(local);
        authTypeOptionGroup.setRequired(true);

        Options options = new Options();
        options.addOptionGroup(authTypeOptionGroup);
        options.addOption(password);
        options.addOption(game);
        options.addOption(archived);

        CommandLine arguments = parser.parse(options, args);

        // Extra check because we can't enforce this in the above logic!
        if(arguments.hasOption("A") && !arguments.hasOption("U"))
            throw new ParseException("If -A is specified, -U must also be provided");

        return arguments;

    }

    // ******************************
    // Main Method
    // ******************************

    public static void main(String... args) throws Exception {
        Main main = new Main();
        main.run(args);
    }

}
