package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Role;
import net.anomalyxii.werewolves.domain.Vitality;

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
    private Vitality currentVitality = Vitality.ALIVE;
    private Alignment currentAlignment;
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

    /**
     * Get the {@link Vitality} of this {@code Character} at the
     * current point of the {@code Game}.
     *
     * @return the {@code Character's} current {@link Vitality}
     */
    public Vitality getCurrentVitality() {
        return currentVitality;
    }

    /**
     * Set the {@link Vitality} of this {@code Character} at the
     * current point of the {@code Game}.
     *
     * @param currentVitality  the {@code Character's} current {@link Vitality}
     */
    public void setCurrentVitality(Vitality currentVitality) {
        this.currentVitality = currentVitality;
    }

    /**
     * Get the {@link Alignment} of this {@code Character} at the
     * current point of the {@code Game}.
     *
     * @return the {@code Character's} current {@link Alignment}
     */
    public Alignment getCurrentAlignment() {
        // Hack hack hack???
        if(currentAlignment == null)
            return role.getAlignment();

        return currentAlignment;
    }

    /**
     * Set the {@link Alignment} of this {@code Character} at the
     * current point of the {@code Game}.
     *
     * @param currentAlignment  the {@code Character's} current {@link Alignment}
     */
    public void setCurrentAlignment(Alignment currentAlignment) {
        this.currentAlignment = currentAlignment;
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
