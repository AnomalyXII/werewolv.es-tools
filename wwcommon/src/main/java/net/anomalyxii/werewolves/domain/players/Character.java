package net.anomalyxii.werewolves.domain.players;

import java.net.URI;

/**
 * A character in
 *
 * Created by Anomaly on 26/11/2016.
 */
public class Character extends AbstractPlayer {

    // ******************************
    // Members
    // ******************************

    private User user;

    // ******************************
    // Constructors
    // ******************************

    public Character(String name, URI avatarURI) {
        super(name, avatarURI);
    }

    // ******************************
    // Getters & Setters
    // ******************************

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
