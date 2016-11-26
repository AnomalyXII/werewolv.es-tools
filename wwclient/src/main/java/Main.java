import net.anomalyxii.werewolves.Router;
import net.anomalyxii.werewolves.domain.Game;
import net.anomalyxii.werewolves.domain.Games;
import net.anomalyxii.werewolves.router.exceptions.RouterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Anomaly on 10/07/2016.
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Main Method
    // ******************************

    public static void main(String... args) throws Exception {

        // Create a Router
        Router router = getRouter(args);

        // Create a HTTP client
        Games games = router.games();
        games.getActiveGameIDs().forEach(id ->
                System.out.println("Active: " + id)
        );
        games.getPendingGameIDs().forEach(id ->
                System.out.println("Pending: " + id)
        );

        Game game = router.game("ext-085");
        game.getPreGameEvents().forEach(System.out::println);
        game.getDays().forEach(day -> {
            System.out.println("======= New Day =======");
            day.getEvents().forEach(System.out::println);
            System.out.println("======= End Day =======");
        });
        game.getPostGameEvents().forEach(System.out::println);
}

    private static final Router getRouter(String... args) throws RouterException {

        logger.debug("Creating Router");

        // Default, use Username & Password
        Router router = new Router();

        // Authorisation
        String auth = args[0];
        if(args.length == 1)
            return new Router(auth);

        String password = args[1];

        if (!router.login(auth, password))
            throw new IllegalStateException("Failed to log-in!");

        return router;

    }

}
