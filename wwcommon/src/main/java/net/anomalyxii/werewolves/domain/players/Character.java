package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Role;

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
    private Role role;

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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // ******************************
    // Helper Methods
    // ******************************


    @Override
    public String getFormattedName() {
        return isLinkedToUser() ? String.format("%s (%s)", getName(), getUser().getName()) : getName();
    }

    public boolean isLinkedToUser() {
        return user != null;
    }

}
