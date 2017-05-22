package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class UserInstance implements PlayerInstance {

    // ******************************
    // Members
    // ******************************

    private final User user;

    // ******************************
    // Constructors
    // ******************************

    public UserInstance(User user) {
        this.user = user;
    }

    // ******************************
    // PlayerInstance
    // ******************************

    @Override
    public Player getPlayer() {
        return user;
    }

    @Override
    public Character getCharacter() {
        return null;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean isPlayerInGame() {
        return false;
    }

    @Override
    public Alignment getAlignment() {
        return null;
    }

    @Override
    public Vitality getVitality() {
        return null;
    }

}
