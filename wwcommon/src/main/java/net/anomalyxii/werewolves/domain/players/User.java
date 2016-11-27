package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Player;

import java.net.URI;

/**
 * A <code>werewolv.es</code> user. This is
 * a player's real identity, i.e.
 *
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
