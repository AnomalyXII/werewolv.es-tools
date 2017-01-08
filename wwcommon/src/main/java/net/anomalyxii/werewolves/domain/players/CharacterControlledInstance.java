package net.anomalyxii.werewolves.domain.players;

import net.anomalyxii.werewolves.domain.Vitality;

/**
 * Created by Anomaly on 07/01/2017.
 */
public class CharacterControlledInstance extends CharacterInstance {

    // ******************************
    // Members
    // ******************************

    private final User originalUser;

    // ******************************
    // Constructors
    // ******************************

    public CharacterControlledInstance(Character character, User user, User originalUser, Vitality vitality) {
        super(character, user, vitality);
        this.originalUser = originalUser;
    }

    // ******************************
    // Getters
    // ******************************

    public User getOriginalUser() {
        return originalUser;
    }

    // ******************************
    // PlayerInstance Methods
    // ******************************

    @Override // Todo: remove this when testing is finished!!
    public String getName() {
        if(originalUser == null)
            return super.getName();
        return String.format("%s (%s as %s)", getCharacter().getName(), getUser().getName(), originalUser.getName());
    }

}
