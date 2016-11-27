package net.anomalyxii.werewolves.domain.players;

import java.net.URI;

/**
 * Any player that is neither a user
 * nor a character. This includes "players"
 * such as the <b>Moderator</b>
 *
 * Created by Anomaly on 26/11/2016.
 */
public class SpecialPlayer extends AbstractPlayer {

    // ******************************
    // Members
    // ******************************

    // ******************************
    // Constructors
    // ******************************

    public SpecialPlayer(String name, URI avatarURI) {
        super(name, avatarURI);
    }

}
