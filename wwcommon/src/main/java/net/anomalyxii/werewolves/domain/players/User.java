package net.anomalyxii.werewolves.domain.players;

import java.net.URI;

/**
 * Created by Anomaly on 26/11/2016.
 */
public class User extends AbstractPlayer {

    // ******************************
    // Constructors
    // ******************************

    public User(String name, URI avatarURI) {
        super(name, avatarURI);
    }

}
