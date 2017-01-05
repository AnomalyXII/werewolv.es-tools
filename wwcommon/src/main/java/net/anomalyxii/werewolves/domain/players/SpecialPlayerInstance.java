package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class SpecialPlayerInstance implements PlayerInstance {

    // ******************************
    // Members
    // ******************************

    private final Player player;

    // ******************************
    // Constructors
    // ******************************

    public SpecialPlayerInstance(Player player) {
        this.player = player;
    }

    // ******************************
    // PlayerInstance Methods
    // ******************************


    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Character getCharacter() {
        return null;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public boolean isPlayerInGame() {
        return false;
    }

    @Override
    public Vitality getVitality() {
        return null;
    }

}
