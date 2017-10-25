package net.anomalyxii.werewolves.domain;

/**
 * Each of the available roles.
 *
 * Created by Anomaly on 27/11/2016.
 */
public enum Role {

    // Village
    VILLAGER("Villager", Alignment.VILLAGE),
    LYCAN("Lycan", Alignment.VILLAGE),
    SEER("Seer", Alignment.VILLAGE),
    PROTECTOR("Protector", Alignment.VILLAGE),
    HUNTSMAN("Huntsman", Alignment.VILLAGE),
    GRAVEDIGGER("Gravedigger", Alignment.VILLAGE),
    GRAVEROBBER("Graverobber", Alignment.VILLAGE),
    STALKER("Stalker", Alignment.VILLAGE),
    HARLOT("Harlot", Alignment.VILLAGE),
    MILITIA("Militia", Alignment.VILLAGE),
    REVIVER("Reviver", Alignment.VILLAGE),
    MESSIAH("Messiah", Alignment.VILLAGE),
    MASONLEADER("Mason Leader", Alignment.VILLAGE),
    TAROTREADER("Tarot Reader", Alignment.VILLAGE),
    WITCHHUNTER("Witch Hunter", Alignment.VILLAGE),
    SLEEPWALKER("Sleepwalker", Alignment.VILLAGE),
    INSOMNIAC("Insomniac", Alignment.VILLAGE),
    BEHOLDER("Beholder", Alignment.VILLAGE),
    ZEALOT("Zealot", Alignment.VILLAGE),

    NAIVE_SEER("Naive Seer", Alignment.VILLAGE),
    PARANOID_SEER("Paranoid Seer", Alignment.VILLAGE),
    INSANE_SEER("Insane Seer", Alignment.VILLAGE),
    CHAOTIC_SEER("Chaotic Seer", Alignment.VILLAGE),

    // Werewolves
    WEREWOLF("Werewolf", Alignment.WEREWOLVES),
    SHAPESHIFTER("Shapeshifter", Alignment.WEREWOLVES),
    ALPHAWOLF("Alphawolf", Alignment.WEREWOLVES),
    BLOODHOUND("Bloodhound", Alignment.WEREWOLVES),
    BLOODLETTER("Bloodletter", Alignment.WEREWOLVES),
    DIREWOLF("Direwolf", Alignment.WEREWOLVES),

    // Coven
    ALCHEMIST("Alchemist", Alignment.COVEN),
    DJINN("Djinn", Alignment.COVEN),
    SUCCUBUS("Succubus", Alignment.COVEN),
    WITCH("Witch", Alignment.COVEN),
    SHAMAN("Shaman", Alignment.COVEN),
    PUPPETMASTER("Puppet Master", Alignment.COVEN),
    PUPPET("Puppet", Alignment.COVEN),

    // "Neutral"
    VAMPIRE("Vampire", Alignment.VAMPIRES),
    VAMPIREMASTER("Vampire Master", Alignment.VAMPIRES),
    FAMILIAR("Familiar", Alignment.VAMPIRES),
    FAMILIARSTALKER("Familiar (Stalker)", Alignment.VAMPIRES),
    FAMILIARGRAVEDIGGER("Familiar (Gravedigger)", Alignment.VAMPIRES),
    FAMILIARMILITIA("Familiar (Militia)", Alignment.VAMPIRES),
    DEMONLORD("Demonlord", Alignment.DEMONS),
    HELLHOUND("Hellhound", Alignment.DEMONS),

    // End of constants
    ;

    private final String name;
    private final Alignment alignment;

    // Constructors

    Role(String name, Alignment alignment) {
        this.name = name;
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

    // To String

    @Override
    public String toString() {
        return name;
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
