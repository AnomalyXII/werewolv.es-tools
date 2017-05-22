package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.Player;
import net.anomalyxii.werewolves.domain.PlayerInstance;
import net.anomalyxii.werewolves.domain.Vitality;

/**
 * Created by Anomaly on 05/01/2017.
 */
public class CharacterInstance implements PlayerInstance {

    // ******************************
    // Members
    // ******************************

    private final Character character;
    private final User user;
    private final Alignment alignment;
    private final Vitality vitality;

    // ******************************
    // Constructors
    // ******************************

    public CharacterInstance(Character character, User user, Alignment alignment, Vitality vitality) {
        this.character = character;
        this.user = user;
        this.alignment = alignment;
        this.vitality = vitality;
    }

    // ******************************
    // PlayerInstance Methods
    // ******************************

    @Override
    public Player getPlayer() {
        return character;
    }

    @Override
    public Character getCharacter() {
        return character;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean isPlayerInGame() {
        return true;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public Vitality getVitality() {
        return vitality;
    }

    @Override // Todo: remove this when testing is finished!!
    public String getName() {
        if(user == null)
            return character.getName();
        return String.format("%s (%s)", character.getName(), user.getName());
    }

}
