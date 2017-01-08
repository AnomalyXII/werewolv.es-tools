package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Player;

import java.net.URI;

/**
 * Base class for all {@link Player} implementations
 *
 * Created by Anomaly on 26/11/2016.
 */
public class AbstractPlayer implements Player {

    // ******************************
    // Members
    // ******************************

    private final String name;
    private final URI avatarURI;

    // ******************************
    // Constructors
    // ******************************

    public AbstractPlayer(String name, URI avatarURI) {
        this.name = name;
        this.avatarURI = avatarURI;
    }

    // ******************************
    // Getters
    // ******************************

    public String getName() {
        return name;
    }

    public URI getAvatarURI() {
        return avatarURI;
    }

    // ******************************
    // Equals & HashCode
    // ******************************

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPlayer that = (AbstractPlayer) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
