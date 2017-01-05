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
    //
    private boolean possiblyIncorrectUser = false; // Todo: remove the hack and remove this
    private boolean possiblyIncorrectRole = false; // Todo: remove the hack and remove this

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

    public void setUserPossiblyIncorrectly(User user) {
        this.user = user;
        this.possiblyIncorrectUser = true;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRolePossiblyIncorrectly(Role role) {
        this.role = role;
        this.possiblyIncorrectRole = true;
    }

    // ******************************
    // Helper Methods
    // ******************************

    public boolean isLinkedToUser() {
        return user != null;
    }

    public boolean isPossiblyIncorrectlyLinked() {
        return possiblyIncorrectUser || possiblyIncorrectRole;
    }

}
