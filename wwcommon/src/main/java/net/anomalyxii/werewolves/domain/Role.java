package net.anomalyxii.werewolves.domain;

/**
 * Each of the available roles.
 *
 * Created by Anomaly on 27/11/2016.
 */
public enum Role {

    // Village
    VILLAGER(Alignment.VILLAGE),
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
    MASONLEADER(Alignment.VILLAGE),
    TAROTREADER(Alignment.VILLAGE),
    WITCHHUNTER(Alignment.VILLAGE),
    SLEEPWALKER(Alignment.VILLAGE),
    INSOMNIAC(Alignment.VILLAGE),
    BEHOLDER(Alignment.VILLAGE),
    ZEALOT(Alignment.VILLAGE),

    NAIVE_SEER(Alignment.VILLAGE),
    PARANOID_SEER(Alignment.VILLAGE),
    INSANE_SEER(Alignment.VILLAGE),
    CHAOTIC_SEER(Alignment.VILLAGE),

    // Werewolves
    WEREWOLF(Alignment.WEREWOLVES),
    SHAPESHIFTER(Alignment.WEREWOLVES),
    ALPHAWOLF(Alignment.WEREWOLVES),
    BLOODHOUND(Alignment.WEREWOLVES),
    BLOODLETTER(Alignment.WEREWOLVES),
    DIREWOLF(Alignment.WEREWOLVES),

    // Coven
    ALCHEMIST(Alignment.COVEN),
    DJINN(Alignment.COVEN),
    SUCCUBUS(Alignment.COVEN),
    WITCH(Alignment.COVEN),
    SHAMAN(Alignment.COVEN),
    PUPPETMASTER(Alignment.COVEN),
    PUPPET(Alignment.COVEN),

    // "Neutral"
    VAMPIRE(Alignment.VAMPIRES),
    VAMPIREMASTER(Alignment.VAMPIRES),
    FAMILIAR(Alignment.VAMPIRES),
    FAMILIARSTALKER(Alignment.VAMPIRES),
    FAMILIARGRAVEDIGGER(Alignment.VAMPIRES),
    FAMILIARMILITIA(Alignment.VAMPIRES),
    DEMONLORD(Alignment.DEMONS),
    HELLHOUND(Alignment.DEMONS),

    // End of constants
    ;

    private final Alignment alignment;

    // Constructors

    Role(Alignment alignment) {
        this.alignment = alignment;
    }

    // Getters

    /**
     * Get the {@link Alignment} that
     * this role belongs to.
     *
     * @return the {@link Alignment}
     */
    public Alignment getAlignment() {
        return alignment;
    }

    // Helper Methods

    /**
     * Convert the name of a {@link Role}
     * into the appropriate constant.
     *
     * @param role the role identifier
     * @return the {@link Role}
     */
    public static Role forString(String role) {
        String sanitisedRole = role.toUpperCase().replaceAll("[ ()]", "");
        if("FAMILIARMILTIA".equalsIgnoreCase(sanitisedRole))
            return FAMILIARMILITIA;
        return Role.valueOf(sanitisedRole);
    }

}
