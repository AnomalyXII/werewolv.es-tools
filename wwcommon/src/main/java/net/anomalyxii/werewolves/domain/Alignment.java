package net.anomalyxii.werewolves.domain;

import java.util.Objects;

/**
 * An alignment.
 * <p>
 * Created by Anomaly on 27/11/2016.
 */
public enum Alignment {

    NEUTRAL,
    VILLAGE,
    WEREWOLVES,
    COVEN,
    VAMPIRES,
    DEMONS,

    // Special alignments:
    MASONS(VILLAGE),
    PUPPETS(COVEN),

    // End of constants
    ;

    // ******************************
    // Members
    // ******************************

    private final Alignment treatAs;

    // ******************************
    // Constructors
    // ******************************

    Alignment() {
        this(null);
    }

    Alignment(Alignment treatAs) {
        this.treatAs = treatAs;
    }

    // ******************************
    // Helper Methods
    // ******************************

    /**
     * Some of the {@code Alignments} are not,
     * technically, alignments, but are actually
     * specialisations of another. This method
     * will return the base alignment, to
     * simplify checking if the given alignment
     * is compatible with an {@code Event} etc.
     *
     * @return the base {@code Alignment}
     */
    public Alignment baseAlignment() {
        if(Objects.nonNull(treatAs))
            return treatAs.baseAlignment();
        return this;
    }

    // ******************************
    // Static Helper Methods
    // ******************************

    /**
     * Parse the result of a {@link Role#SEER} check
     * into an {@code Alignment}.
     *
     * @param msg the message
     * @return the {@code Alignment}
     */
    public static Alignment forMessageString(String msg) {
        if ("member of the village".equalsIgnoreCase(msg) || "Villager".equalsIgnoreCase(msg))
            return VILLAGE;
        if ("member of the wolfpack".equalsIgnoreCase(msg) || "Werewolf".equalsIgnoreCase(msg))
            return WEREWOLVES;
        if ("member of the coven".equalsIgnoreCase(msg))
            return COVEN;
        if ("puppet".equalsIgnoreCase(msg))
            return PUPPETS;
        if ("vampire".equalsIgnoreCase(msg))
            return VAMPIRES;
        if ("vampire familiar".equalsIgnoreCase(msg))
            return VAMPIRES;
        if ("demon".equalsIgnoreCase(msg))
            return DEMONS;

        throw new IllegalArgumentException("Invalid message string: " + msg);
    }

}
