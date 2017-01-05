package net.anomalyxii.werewolves.domain.players;

import java.net.URI;

/**
 * A <code>werewolv.es</code> user. This is
 * a player's real identity, i.e.
 *
 * Created by Anomaly on 26/11/2016.
 */
public class User extends AbstractPlayer {

    // ******************************
    // Members
    // ******************************

    private boolean joinedGame = false;

    // ******************************
    // Constructors
    // ******************************

    public User(String name, URI avatarURI) {
        super(name, avatarURI);
    }

    // ******************************
    // Getters & Setters
    // ******************************

    public boolean isJoinedGame() {
        return joinedGame;
    }

    public void setJoinedGame(boolean joinedGame) {
        this.joinedGame = joinedGame;
    }

}
