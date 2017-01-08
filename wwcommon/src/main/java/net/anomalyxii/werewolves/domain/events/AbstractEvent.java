package net.anomalyxii.werewolves.domain.events;

import net.anomalyxii.werewolves.domain.Alignment;
import net.anomalyxii.werewolves.domain.PlayerInstance;

import java.time.OffsetDateTime;

/**
 * Created by Anomaly on 20/11/2016.
 */
public abstract class AbstractEvent implements Event {

    // ******************************
    // Members
    // ******************************

    private final PlayerInstance player;
    private final OffsetDateTime timestamp;
    //
    private final EventType type;

    // ******************************
    // Constructors
    // ******************************

    public AbstractEvent(PlayerInstance player, OffsetDateTime timestamp, EventType type) {
        this.player = player;
        this.timestamp = timestamp;
        this.type = type;
    }

    // ******************************
    // Getters & Setters
    // ******************************

    @Override
    public PlayerInstance getPlayer() {
        return player;
    }

    @Override
    public OffsetDateTime getTime() {
        return timestamp;
    }

    @Override
    public EventType getType() {
        return type;
    }

    // ******************************
    // To String Helpers
    // ******************************

    /**
     * Format the message in a consistent way.
     *
     * @param player  the {@link PlayerInstance} to fmt
     * @param message the message to fmt
     * @return the formatted message
     */
    protected String fmt(PlayerInstance player, String message) {
        return String.format("%s [%tH:%<tM] <%s> %s", getStatusString(), timestamp, player.getName(), message);
    }

    /**
     * Format the message in a consistent way.
     *
     * @param message the message to fmt
     * @return the formatted message
     */
    protected String fmt(String message) {
        return String.format("%s [%tH:%<tM] %s", getStatusString(), timestamp, message);
    }

    /**
     * Format the message in a consistent way.
     *
     * @param player  the {@link PlayerInstance} to fmt
     * @param message the message to fmt
     * @param args    arguments referenced by the format specifiers in the format string
     * @return the formatted message
     */
    protected String fmt(PlayerInstance player, String message, Object... args) {
        return fmt(player, String.format(message, args));
    }

    /**
     * Format the message in a consistent way.
     *
     * @param message the message to fmt
     * @param args    arguments referenced by the format specifiers in the format string
     * @return the formatted message
     */
    protected String fmt(String message, Object... args) {
        return fmt(String.format(message, args));
    }

    /**
     * Build a list of status modifiers
     * that represent the context of
     * the current event.
     * <p>
     * The status string consists of:
     * <ul>
     * <li>
     * Vitality:
     * <ol>
     * <li><code>~</code> if neither alive
     * nor dead (e.g. not in the game)</li>
     * <li><code>X</code> if dead</li>
     * <li><i>otherwise blank</i></li>
     * </ol>
     * </li>
     * <li>
     * Chat:
     * <ol>
     * <li>blank for village chat</li>
     * <li><code>W</code> for Werewolf</li>
     * <li><code>C</code> for Coven</li>
     * <li><code>V</code> for Vampire</li>
     * <li><code>M</code> for Mason</li>
     * <li><code>G</code> for Graveyard</li>
     * </ol>
     * </li>
     * </ul>
     */
    protected String getStatusString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getVitalityStatus());
        builder.append(getChatStatus());
        return builder.toString();
    }

    /**
     * Return the vitality status
     * code for the event.
     *
     * @return a vitality identifier
     */
    protected char getVitalityStatus() {
        return player.isAlive()
               ? ' '
               : player.isDead()
                 ? 'X'
                 : '~';
    }

    /**
     * Return the chat code
     * for the event.
     *
     * @return a chat identifier
     */
    protected char getChatStatus() {
        Alignment alignment = getAlignmentVisibility();
        if (alignment == null)
            return ' ';

        switch (alignment) {
            case WEREWOLVES:
                return 'W';
            case COVEN:
                return 'C';
            case VAMPIRE:
                return 'V';
        }

        return ' ';
    }

}
