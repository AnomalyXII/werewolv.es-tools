package net.anomalyxii.werewolves.domain;

/**
 * Each of the available roles.
 *
 * Created by Anomaly on 27/11/2016.
 */
public enum Role {

    // Village
    LYCAN(Alignment.VILLAGE),
    SEER(Alignment.VILLAGE),
    PROTECTOR(Alignment.VILLAGE),
    HUNTSMAN(Alignment.VILLAGE),
    GRAVEDIGGER(Alignment.VILLAGE),
    GRAVEROBBER(Alignment.VILLAGE),
    STALKER(Alignment.VILLAGE),
    HARLOT(Alignment.VILLAGE),
    MILITIA(Alignment.VILLAGE),
    REVIVER(Alignment.VILLAGE),
    MESSIAH(Alignment.VILLAGE),

    // Werewolves
    WEREWOLF(Alignment.WEREWOLVES),
    SHAPESHIFTER(Alignment.WEREWOLVES),
    ALPHA_WOLF(Alignment.WEREWOLVES),
    BLOODHOUND(Alignment.WEREWOLVES),

    // Coven
    DJINN(Alignment.COVEN),
    SUCCUBUS(Alignment.COVEN),
    WITCH(Alignment.COVEN),
    SHAMAN(Alignment.COVEN),
    PUPPET_MASTER(Alignment.COVEN),
    PUPPET(Alignment.COVEN),

    // End of constants
    ;

    private final Alignment alignment;

    // Constructors

    Role(Alignment alignment) {
        this.alignment = alignment;
    }

    // Getters

    public Alignment getAlignment() {
        return alignment;
    }

}
